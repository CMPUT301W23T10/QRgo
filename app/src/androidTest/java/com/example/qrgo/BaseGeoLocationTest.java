package com.example.qrgo;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.qrgo.MainActivity;
import com.example.qrgo.R;
import com.example.qrgo.listeners.OnCommentAddListener;
import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class BaseGeoLocationTest extends AndroidJUnitRunner {
    protected static Solo solo;
    protected static String username;
    protected static String imei;
    protected static SharedPreferences sharedPreferences;
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class,true, true);

    @Before
    public void setUp() throws Exception {
        // Launch the activity under test
        rule.launchActivity(new Intent());
        // Initialize the solo object
        solo = new Solo(getInstrumentation(), rule.getActivity());
        sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        imei = sharedPreferences.getString("qrgodb", "");

        // Edit the shared preferences using the editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("imei").commit();
        editor.remove("user").commit();
        editor.remove("qrgodb").commit();

        getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.ACCESS_COARSE_LOCATION");
        getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.ACCESS_FINE_LOCATION");
        getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.CAMERA");
    }

    public void signUpTestUser() {
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.firstNameEntry), "first");
        solo.enterText((EditText) solo.getView(R.id.lastNameEntry), "last");
        solo.enterText((EditText) solo.getView(R.id.emailEntry), "testemail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.phoneEntry), "1234567890");
        solo.clickOnView(solo.getView(R.id.register));
    }

    public void addTestQR(String qrString, double latitude, double longitude) {
        String humanReadableQR = "Human Readable QR";
        String photoUrl = "https://example.com/photo.jpg";
        int points = 10;

        final CountDownLatch latch = new CountDownLatch(1);
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

    public static Marker getMarker(MapView mapView) {
        for (Overlay overlay : mapView.getOverlays()) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                if (marker.getTitle().equals("Place me!") && marker.isDraggable()) {
                    // Found the marker with the matching title
                    // You can now call methods on this marker, such as dragTo()
                    return marker;
                }
            }
        }
        // If no marker with the matching title was found, return null or throw an exception
        return null;

    }

    @After
    public void cleanup() {
        if (imei.equals("") && username.equals("")) {
            Log.d("testSignUpUser", "imei or username is empty");
        } else {
            Log.d("testSignUpUser", "imei: " + imei + " username: " + username);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.size() == 1) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    db.collection("Users").document(document.getId()).delete();
                }
            });

            db.collection("QRCodes").whereEqualTo("qrString", "QRCODE").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.size() == 1) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        DocumentReference qrCodeRef = db.collection("QRCodes").document(document.getId());
                        qrCodeRef.delete();
                    }
                }
            });

            db.collection("Profiles").document(username).delete(); // replace with the actual username used in the tests
            solo.finishOpenedActivities();
        }
    }
}
