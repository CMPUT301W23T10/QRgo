package com.example.qrgo;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.qrgo.listeners.OnCommentAddListener;
import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseTestManager extends AndroidJUnitRunner {
    protected static Solo solo;
    protected static String username;
    protected static String imei;
    protected static SharedPreferences sharedPreferences;
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true, true);

    public void signUpTestUser() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.firstNameEntry), "first");
        solo.enterText((EditText) solo.getView(R.id.lastNameEntry), "last");
        solo.enterText((EditText) solo.getView(R.id.emailEntry), "testemail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.phoneEntry), "1234567890");
        solo.clickOnView(solo.getView(R.id.register));
    }

    public void addTestQR(String qrString) {
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
                ArrayList<Integer> featurelist= new ArrayList<Integer>();
                featurelist.add(1);
                featurelist.add(1);
                featurelist.add(1);
                featurelist.add(1);
                featurelist.add(1);
                connect.getQRCodeManager().scanQRCode(qrString, username, humanReadableQR, latitude, longitude, photoUrl, points, featurelist, new OnQRCodeScannedListener() {
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
    }

    public void addCommentToQr(String qrString, String commentText) {
        final CountDownLatch latch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);
        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getCommentManager().addComment(commentText, username, qrString, "first", "last", new OnCommentAddListener() {
                    @Override
                    public void onCommentAdd(boolean success) {
                        result.set(success);
                        latch.countDown();
                    }
                });
            }
        });
    }

    public void testActionBarIsHidden() {
        Activity activity = rule.getActivity();
        ActionBar actionBar = activity.getActionBar();
        assert (actionBar == null);
    }

    public void deleteUser(String username) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
            QuerySnapshot querySnapshot = task.getResult();
            if (querySnapshot.size() == 1) {
                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                db.collection("Users").document(document.getId()).delete();
            }
        });
        db.collection("Profiles").document(username).delete();
    }

    public void deleteQrCode(String username, String qrString) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRCodes").whereEqualTo("qrString", qrString)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        documentSnapshot.getReference().delete();
                    }
                });
    }

    public void deleteComment(String qrString) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Comments").whereEqualTo("commentQRCode", qrString).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.size() == 1) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    DocumentReference qrCodeRef = db.collection("Comments").document(document.getId());
                    qrCodeRef.delete();
                }
            }
        });
    }



}

