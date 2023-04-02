package com.example.qrgo.GeoLocationTests;

import static android.content.Context.LOCATION_SERVICE;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;
import android.widget.EditText;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.qrgo.GeoLocationActivity;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.MainActivity;
import com.example.qrgo.QrProfileActivity;
import com.example.qrgo.R;
import com.example.qrgo.SignupActivity;
import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


public class QRProfileTest extends AndroidJUnitRunner {
    private static Solo solo;
    private static String username;
    private static String imei;

    private static SharedPreferences sharedPreferences;

    private static GeoPoint testLocation;

    private FirebaseConnect db = new FirebaseConnect();
    @ClassRule
    public static ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class, true, true);

    @BeforeClass
    public static void setUpSomethingElse() {
        // Launch the activity under test
        rule.launchActivity(new Intent());

        // Initialize the solo object
        solo = new Solo(getInstrumentation(), rule.getActivity());

        sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);

        imei = sharedPreferences.getString("qrgodb", "");

        // Edit the shared preferences using the editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        // Set the "user" key to null
        editor.remove("imei").commit();
        // Set the "user" key to null
        editor.remove("user").commit();
        // Set the "qrgodb" key to null
        editor.remove("qrgodb").commit();

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.ACCESS_COARSE_LOCATION");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.ACCESS_FINE_LOCATION");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.CAMERA");

        solo.enterText((EditText) solo.getView(R.id.firstNameEntry), "first");
        solo.enterText((EditText) solo.getView(R.id.lastNameEntry), "last");
        solo.enterText((EditText) solo.getView(R.id.emailEntry), "testmail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.phoneEntry), "1234567890");
        solo.clickOnView(solo.getView(R.id.register));

        // assigning location
        testLocation = new GeoPoint(53.562235, -113.54732666666666);

        username = sharedPreferences.getString("user", "");
        addQR();
    }

    @Test
    public void testQRProfile() {
        //click on QR desk
        solo.clickOnView(solo.getView(R.id.qr_view_all));
        //solo.waitForFragmentByTag("QrListviewFragment");
        solo.sleep(2000);

        ListView listView = (ListView) solo.getView(R.id.fragment_qr_listview);
        int count = listView.getCount();
        if (count == 0) {
            // if empty then click on back button
            solo.clickOnView(solo.getView(R.id.back_button));

            // Wait for home activity to launch
            solo.waitForActivity(HomeActivity.class, 2000);

        } else {
            solo.clickInList(0);
            solo.waitForActivity(QrProfileActivity.class);
        }

        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

    }

    public static void addQR() {
        String qrString = "QRCODE";
        String humanReadableQR = "Human Readable QR";
        String photoUrl = "https://example.com/photo.jpg";
        int points = 10;

        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch nlatch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getQRCodeManager().scanQRCode(qrString, username, humanReadableQR, testLocation.getLatitude(), testLocation.getLongitude(), photoUrl, points, new OnQRCodeScannedListener() {
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

    public static void register() {
        // Launch the MainActivity
        solo.assertCurrentActivity("Expected SignUpActivity", SignupActivity.class);
        solo.enterText((EditText) solo.getView(R.id.firstNameEntry), "first");
        solo.enterText((EditText) solo.getView(R.id.lastNameEntry), "last");
        solo.enterText((EditText) solo.getView(R.id.emailEntry), "testmail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.phoneEntry), "1234567890");
        solo.clickOnView(solo.getView(R.id.register));
    }

    @AfterClass
    public static void cleanup() {
        if (imei.equals("") && username.equals("")) {
            Log.d("testSignUpUser", "imei or username is empty");
        } else {
            Log.d("testSignUpUser", "imei: " + imei + " username: " + username);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(imei).delete(); // replace with the actual ID used in the tests
            db.collection("Profiles").document(username).delete(); // replace with the actual username used in the tests
            solo.finishOpenedActivities();
        }
        String qrString = "QRCODE WITH THIS HASH WON'T EXIST";
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("QRCodes").whereEqualTo("qrString", qrString)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        List<String> scanUsers = (List<String>) documentSnapshot.get("scannedUsers");
                        documentSnapshot.getReference().delete();
                    }
                });
        // close the Solo instance to release any resources
        solo.finishOpenedActivities();

    }
}