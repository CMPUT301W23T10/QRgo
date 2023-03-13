package com.example.qrgo.FirebaseTests;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.MainActivity;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class B_FirebaseQRScanTests {
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
    public void testScanQRCode_withExistingQRCodeAndNewUser() throws InterruptedException {
        String qrString = "123";
        String username = "AUTOTESTUSERNAME";
        String humanReadableQR = "Human Readable QR";
        double latitude = 37.7749;
        double longitude = -122.4194;
        String photoUrl = "https://example.com/photo.jpg";
        int points = 10;

        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.scanQRCode(qrString, username, humanReadableQR, latitude, longitude, photoUrl, points, new FirebaseConnect.OnQRCodeScannedListener() {
                    @Override
                    public void onQRScanComplete(boolean success) {
                        result.set(success);
                        latch.countDown();
                    }
                });
            }
        });

        latch.await(10, TimeUnit.SECONDS);
        assertTrue(result.get());

        Query query = db.collection("QRCodes").whereEqualTo("qrString", qrString);
        query.get().addOnSuccessListener(querySnapshot -> {
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
                    } else {
                        fail("User document not found");
                    }
                });
            } else {
                fail("QR Code document not found");
            }
        });
    }
}
