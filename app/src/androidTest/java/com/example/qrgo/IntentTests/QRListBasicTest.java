package com.example.qrgo.IntentTests;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.MainActivity;
import com.example.qrgo.QrProfileActivity;
import com.example.qrgo.R;
import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@RunWith(AndroidJUnit4.class)
@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
public class QRListBasicTest {


    private static Solo solo;

    private static String imei;

    private static String username;
//    @After
//    public void tearDown() {
//        solo.finishOpenedActivities();
//    }

    @Rule
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void setUp(){
        rule.launchActivity(new Intent());
        solo = new Solo(getInstrumentation(), rule.getActivity());


    }


    @Test
    public void A_setUpSomethingElse() throws Exception{
        // Launch the activity under test
//        rule.launchActivity(new Intent());
//        // Initialize the solo object
//        solo = new Solo(getInstrumentation(), rule.getActivity());
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
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

        // Launch the MainActivity
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.firstNameEntry), "Lukey");
        solo.enterText((EditText) solo.getView(R.id.lastNameEntry), "Razz");
        solo.enterText((EditText) solo.getView(R.id.emailEntry), "testemail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.phoneEntry), "1234567890");
        solo.clickOnView(solo.getView(R.id.register));

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        solo.assertCurrentActivity("Expected HomeActvity", HomeActivity.class);
        username = sharedPreferences.getString("user", "");

        String humanReadableQR = "Human Readable QR";
        double latitude = 37.7749;
        double longitude = -122.4194;
        String photoUrl = "https://example.com/photo.jpg";
        int points = 50;

        final CountDownLatch latch = new CountDownLatch(1);
        final CountDownLatch nlatch = new CountDownLatch(1);
        final AtomicBoolean result = new AtomicBoolean(false);

        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            public void run() {
                FirebaseConnect connect = new FirebaseConnect();
                connect.getQRCodeManager().scanQRCode("qrString", username, humanReadableQR, latitude, longitude, photoUrl, points, new OnQRCodeScannedListener() {
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


    @Test
    public void B_testNavigateToQRList() {
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        ImageView qr_arrow_icon = (ImageView) solo.getView(R.id.qr_arrow_icon);
        solo.clickOnView(qr_arrow_icon);
        Log.d("Test_B", "we got here: ");
        solo.assertCurrentActivity("QrProfileActivity", QrProfileActivity.class);
    }


    @Test
    public void C_testNavigateQRListBackToHomeActivity() {
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        FloatingActionButton back_button = (FloatingActionButton) solo.getView(R.id.back_button);
        solo.clickOnView(back_button);


    }


    @Test
    public void D_testNavigateToQRItem() {
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.view_pager));
        solo.waitForFragmentByTag("QRItemFragment");


        solo.waitForFragmentByTag("QRItemFragment");


    }

    @Test
    public void E_testDeleteQRItem() {
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        ImageView qr_delete_icon = (ImageView) solo.getView(R.id.qr_delete_icon);
        solo.clickOnView(qr_delete_icon);

        solo.waitForFragmentByTag("QRItemFragment");
    }


    @Test
    public void F_testQRListSort(){
        solo.waitForActivity(MainActivity.class);
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        FloatingActionButton sort_button = (FloatingActionButton) solo.getView(R.id.sort_button);
        solo.clickOnView(sort_button);
        solo.waitForFragmentByTag("QrListviewFragment");



    }



    @AfterClass
    public static void cleanup() {
        if (imei.equals("") && username.equals("")) {
            Log.d("testSignUpUser", "imei or username is empty");
        }
        else {
            Log.d("testSignUpUser", "imei: " + imei + " username: " + username);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").document(imei).delete(); // replace with the actual ID used in the tests
            db.collection("Profiles").document(username).delete(); // replace with the actual username used in the tests
            db.collection("QRCodes").whereEqualTo("qrString", "qrString")
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

            solo.finishOpenedActivities();
        }
    }


}
