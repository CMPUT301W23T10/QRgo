package com.example.qrgo.FirebaseTests;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.MainActivity;
import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.listeners.OnUserAddListener;
import com.example.qrgo.listeners.OnUserProfileAddListener;
import com.example.qrgo.listeners.QRCodeListener;
import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class B_FirebaseQRScanTests {
    private static Solo solo;
    private FirebaseConnect firebaseConnect;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true, true);



    @Before
    public void setup() throws InterruptedException {
        solo =  new Solo(InstrumentationRegistry.getInstrumentation(),rule.getActivity());
        firebaseConnect = new FirebaseConnect();
        String imei = "1234567890";
        String username = "AUTOTESTUSERNAME";
        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch nlatch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);
        final AtomicBoolean nresult = new AtomicBoolean(false);

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

        String firstName = "John";
        String lastName = "Doe";
        String contactPhone = "123-456-7890";
        String contactEmail = "johndoe@example.com";
        int totalScore = 0;
        int totalScans = 0;
        int highestScore = 0;
        int lowestScore = 0;
        // Act
        firebaseConnect.getPlayerProfileManager().addNewPlayerProfile(username, firstName, lastName, contactPhone, contactEmail,
                totalScore, totalScans, highestScore, lowestScore, new OnUserProfileAddListener() {
                    @Override
                    public void onUserProfileAdd(boolean success) {
                        nresult.set(success);
                        nlatch.countDown();
                    }
                });

        nlatch.await(5, TimeUnit.SECONDS);
        assertTrue(nresult.get());
    }

    @Test
    public void testScanQRCode_WithNewUser() throws InterruptedException {
        String qrString = "QRCODE WITH THIS HASH WON'T EXIST";
        String username = "AUTOTESTUSERNAME";
        String humanReadableQR = "Human Readable QR";
        double latitude = 37.7749;
        double longitude = -122.4194;
        String photoUrl = "https://example.com/photo.jpg";
        int points = 10;

        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch nlatch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getQRCodeManager().scanQRCode(qrString, username, humanReadableQR, latitude, longitude, photoUrl, points, new OnQRCodeScannedListener() {
                    @Override
                    public void onQRScanComplete(boolean success) {
                        Log.d("onQRScanComplete", "onQRScanComplete: Complete");
                        System.out.println(success);
                        result.set(success);
                        latch.countDown();
                    }
                });
            }
        });

        latch.await(10, TimeUnit.SECONDS);
        assertTrue(result.get());

        db.collection("QRCodes").whereEqualTo("qrString", qrString).get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                List<String> scanUsers = (List<String>) documentSnapshot.get("scannedUsers");
                assertTrue(scanUsers.contains(username));
                List<String> locationObjectPhotos = (List<String>) documentSnapshot.get("locationObjectPhoto");
                assertTrue(locationObjectPhotos.contains(photoUrl));
                int qrPoints = documentSnapshot.getLong("qrPoints").intValue();
                assertEquals(qrPoints, points);

                // Verify that the user's profile has been updated with the new QR Code scan information
                Query userQuery = db.collection("Profiles").whereEqualTo("username", username);
                userQuery.get().addOnSuccessListener(userQuerySnapshot -> {
                    if (!userQuerySnapshot.isEmpty()) {
                        DocumentSnapshot userDocumentSnapshot = userQuerySnapshot.getDocuments().get(0);
                        List<String> qrScans = (List<String>) userDocumentSnapshot.get("qrScans");
                        assertTrue(qrScans.contains(documentSnapshot.getId()));
                        int totalScore = userDocumentSnapshot.getLong("totalScore").intValue();
                        assertEquals(totalScore, points);
                        int totalScans = userDocumentSnapshot.getLong("totalScans").intValue();
                        assertEquals(totalScans, 1);
                        int highestScore = userDocumentSnapshot.getLong("highestScore").intValue();
                        assertEquals(highestScore, points);
                        int lowestScore = userDocumentSnapshot.getLong("lowestScore").intValue();
                        assertEquals(lowestScore, points);
                    }
                });
            } else {
                fail("QR Code document not found");
            }
        });
        nlatch.await(10, TimeUnit.SECONDS);
    }

    @Test
    public void testGetQRCode() throws InterruptedException {
        // Arrange
        String qrString = "QRCODEID1";
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean qrCodeRetrieved = new AtomicBoolean(false);
        final AtomicBoolean qrCodeNotFound = new AtomicBoolean(false);
        final AtomicBoolean qrCodeRetrievalFailure = new AtomicBoolean(false);

        // Act
        firebaseConnect.getQRCodeManager().getQRCode(qrString, new QRCodeListener() {
            @Override
            public void onQRCodeRetrieved(QRCode qrCode) {
                Log.i("testGetQRCode()", "QR code retrieved");
                qrCodeRetrieved.set(true);
                latch.countDown();
            }

            @Override
            public void onQRCodeNotFound() {
                Log.i("testGetQRCode()", "QR code not found");
                qrCodeNotFound.set(true);
                latch.countDown();
            }

            @Override
            public void onQRCodeRetrievalFailure(Exception e) {
                Log.e("testGetQRCode()", "QR code retrieval failure", e);
                qrCodeRetrievalFailure.set(true);
                latch.countDown();
            }
        });

        latch.await(10, TimeUnit.SECONDS);

        // Assert
        assertTrue(qrCodeRetrieved.get() || qrCodeNotFound.get());
        assertFalse(qrCodeRetrievalFailure.get());
    }
    @AfterClass
    public static void cleanup() {
        String qrString = "QRCODE WITH THIS HASH WON'T EXIST";
        String username = "AUTOTESTUSERNAME";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document("1234567890").delete(); // replace with the actual ID used in the tests
        db.collection("Profiles").document(username).delete(); // replace with the actual username used in the tests
        db.collection("QRCodes").whereEqualTo("qrString", qrString)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        List<String> scanUsers = (List<String>) documentSnapshot.get("scannedUsers");
                        scanUsers.remove(username);
                        if (scanUsers.isEmpty()) {
                            documentSnapshot.getReference().delete();
                        } else {
                            documentSnapshot.getReference().update("scannedUsers", scanUsers);
                        }
                    }
                });
        // close the Solo instance to release any resources
        solo.finishOpenedActivities();

    }
}
