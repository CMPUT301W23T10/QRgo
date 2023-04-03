package com.example.qrgo.FirebaseTests;

import com.example.qrgo.MainActivity;
import com.example.qrgo.listeners.OnBasicPlayerProfileLoadedListener;
import com.example.qrgo.listeners.OnImeiCheckListener;
import com.example.qrgo.listeners.OnPlayerProfileGetListener;
import com.example.qrgo.listeners.OnUserAddListener;
import com.example.qrgo.listeners.OnUserProfileAddListener;
import com.example.qrgo.listeners.OnUsernameCheckListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class FirebaseUserAndProfileTests {
    private static Solo solo;
    private FirebaseConnect firebaseConnect;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true, true);


    @Before
    public void setup() {
        solo =  new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        firebaseConnect = new FirebaseConnect();
    }

    @Test
    public void testCheckImeiExists_withExistingImei() throws InterruptedException {
        String imei = "1234567890";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getUserManager().checkImeiExists(imei, new OnImeiCheckListener() {
                    @Override
                    public void onImeiCheck(boolean exists) {
                        result.set(exists);
                        latch.countDown();
                    }
                });
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }

    @Test
    public void testCheckImeiExists_withNonExistingImei() throws InterruptedException{
        String imei = "IMEI DOES NOT EXIST";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(true);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getUserManager().checkImeiExists(imei, new OnImeiCheckListener() {
                    @Override
                    public void onImeiCheck(boolean exists) {
                        result.set(exists);
                        latch.countDown();
                    }
                });
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertFalse(result.get());
    }

    @Test
    public void testCheckUsernameExists_withExistingUsername() throws InterruptedException{
        // Arrange
        String username = "AUTOTESTUSERNAME";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                // Act
                firebaseConnect.getUserManager().checkUsernameExists(username, new OnUsernameCheckListener() {
                    @Override
                    public void onUsernameCheck(boolean exists) {
                        result.set(exists);
                        latch.countDown();
                    }
                });
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }

    @Test
    public void testCheckUsernameExists_withNonExistingUsername() throws InterruptedException{
        // Arrange
        String username = "This User Does Not Exist";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(true);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                // Act
                firebaseConnect.getUserManager().checkUsernameExists(username, new OnUsernameCheckListener() {
                    @Override
                    public void onUsernameCheck(boolean exists) {
                        result.set(exists);
                        latch.countDown();
                    }
                });
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertFalse(result.get());
    }
    @Test
    public void testAddNewUser() throws InterruptedException {
        // Arrange
        String imei = "1234567890";
        String username = "AUTOTESTUSERNAME";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        // Act
        firebaseConnect.getUserManager().addNewUser(imei, username, new OnUserAddListener() {
            @Override
            public void onUserAdd(boolean success) {
                Log.i("testAddNewUser()", "Success");
                result.set(success);
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }

    @Test
    public void testAddNewPlayerProfile() throws InterruptedException {
        // Arrange
        String username = "AUTOTESTUSERNAME";
        String firstName = "John";
        String lastName = "Doe";
        String contactPhone = "123-456-7890";
        String contactEmail = "johndoe@example.com";
        int totalScore = 0;
        int totalScans = 0;
        int highestScore = 0;
        int lowestScore = 0;
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        // Act
        firebaseConnect.getPlayerProfileManager().addNewPlayerProfile(username, firstName, lastName, contactPhone, contactEmail,
                totalScore, totalScans, highestScore, lowestScore, new OnUserProfileAddListener() {
                    @Override
                    public void onUserProfileAdd(boolean success) {
                        result.set(success);
                        latch.countDown();
                    }
                });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }

    @Test
    public void testGetBasicPlayerProfile() throws InterruptedException {
        // Arrange
        String username = "AUTOTESTUSERNAME";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicReference<BasicPlayerProfile> result = new AtomicReference<>();

        // Act
        firebaseConnect.getPlayerProfileManager().getBasicPlayerProfile(username, new OnBasicPlayerProfileLoadedListener() {
            @Override
            public void onBasicPlayerProfileLoaded(BasicPlayerProfile basicPlayerProfile) {
                result.set(basicPlayerProfile);
                latch.countDown();
            }

            @Override
            public void onBasicPlayerProfileLoadFailure(Exception e) {
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertNotNull(result.get());
        assertEquals(result.get().getUsername(), username);
    }

    @Test
    public void testGetPlayerProfile() throws InterruptedException {
        String username = "AUTOTESTUSERNAME";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getPlayerProfileManager().getPlayerProfile(username, new OnPlayerProfileGetListener() {
                    @Override
                    public void onPlayerProfileGet(PlayerProfile playerProfile) {
                        if (playerProfile != null && playerProfile.getUsername().equals(username)) {
                            result.set(true);
                        }
                        latch.countDown();
                    }

                });
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }

    @AfterClass
    public static void cleanup() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        // delete any users and player profiles created during the tests
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document("1234567890").delete(); // replace with the actual ID used in the tests
        db.collection("Profiles").document("AUTOTESTUSERNAME").delete(); // replace with the actual username used in the tests

        // close the Solo instance to release any resources
        solo.finishOpenedActivities();
        latch.await(5, TimeUnit.SECONDS);
    }

}