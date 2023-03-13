package com.example.qrgo.FirebaseTests;

import static junit.framework.TestCase.assertTrue;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.MainActivity;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class C_FirebaseDeleteUserAndProfileTests {
    private Solo solo;
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
    public void testDeleteUser() throws InterruptedException {
        // Arrange
        String imei = "1234567890";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        // Add user to database first
        firebaseConnect.deleteUser(imei, new FirebaseConnect.OnUserDeleteListener() {

            @Override
            public void onUserDelete(boolean success) {
                result.set(success);
                latch.countDown();
            }

        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }

    @Test
    public void testDeletePlayerProfile() throws InterruptedException {
        String username = "AUTOTESTUSERNAME";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        firebaseConnect.deletePlayerProfile(username, new FirebaseConnect.OnUserProfileDeleteListener() {
            @Override
            public void onUserProfileDelete(boolean success) {
                result.set(success);
                latch.countDown();
            }
        });

        latch.await(5, TimeUnit.SECONDS);
        assertTrue(result.get());
    }
}
