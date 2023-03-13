
package com.example.qrgo.IntentTests;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertTrue;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.MainActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.QRIntakeActivity;
import com.example.qrgo.QrProfileActivity;
import com.example.qrgo.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.robotium.solo.Solo;


import org.junit.AfterClass;
import org.junit.Before;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;


@RunWith(AndroidJUnit4.class)
public class A_CreateNewUserAddQrTest extends AndroidJUnitRunner {

    private static Solo solo;
    private static String username;
    private static String imei;
    public ActivityTestRule<MainActivity> rule = new ActivityTestRule<>(MainActivity.class);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        // Launch the activity under test
        rule.launchActivity(new Intent());
        // Initialize the solo object
        solo = new Solo(getInstrumentation(), rule.getActivity());
        // Get the shared preferences object
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);

        // Edit the shared preferences using the editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Set the "qrgodb" key to null
        editor.remove("qrgodb").commit();

        // Set the "user" key to null
        editor.remove("imei").commit();

        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.ACCESS_COARSE_LOCATION");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.ACCESS_FINE_LOCATION");
        InstrumentationRegistry.getInstrumentation().getUiAutomation().executeShellCommand(
                "pm grant com.example.qrgo android.permission.CAMERA");
    }
    @Test
    public void testSignUpUser() {
        // Launch the MainActivity
        solo.assertCurrentActivity("Expected MainActivity", MainActivity.class);
        solo.enterText((EditText) solo.getView(R.id.username), "first");
        solo.enterText((EditText) solo.getView(R.id.address), "last");
        solo.enterText((EditText) solo.getView(R.id.phone_number), "testemail@gmail.com");
        solo.enterText((EditText) solo.getView(R.id.phone_number2), "1233456");
        solo.clickOnView(solo.getView(R.id.register));

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        // Copy imei from shared preferences
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        imei = sharedPreferences.getString("imei", "");

        testActionBarIsHidden();

        // Click on the profile picture
        solo.clickOnView(solo.getView(R.id.main_profile_picture));
        solo.waitForActivity(PlayerActivity.class, 2000);
        testActionBarIsHidden();

        assertTrue(solo.waitForText("first last", 1, 2000));
        assertTrue(solo.waitForText("testemail@gmail.com", 1, 2000));

        // copy the text from id play_username and store it in a string
        username = sharedPreferences.getString("user", "");


        // Click on the close_button
        solo.clickOnView(solo.getView(R.id.close_button));
        solo.waitForActivity(HomeActivity.class, 2000);

//        // Try adding a QR code
//        solo.clickOnView(solo.getView(R.id.add_button));
//        solo.waitForActivity(QRIntakeActivity.class, 2000);
//
//        solo.waitForDialogToOpen();
//        Button buttonYes = (Button) solo.getView(android.R.id.button1);
//        solo.clickOnView(buttonYes);
//
//        // Wait for the QrProfileActivity to launch
//        solo.waitForActivity(QrProfileActivity.class, 2000);
//        // Click on @+id/close_button
//        solo.clickOnView(solo.getView(R.id.close_button));
        solo.sleep(2000);

    }

    public void testActionBarIsHidden() {
        Activity activity = rule.getActivity();
        ActionBar actionBar = activity.getActionBar();
        assert (actionBar == null);
    }


    @AfterClass
    public static void cleanup() {
        System.out.println("Cleaning up");
        System.out.println("Username: " + username);
        System.out.println("IMEI: " + imei);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Users").document(imei).delete(); // replace with the actual ID used in the tests
        db.collection("Profiles").document(username).delete(); // replace with the actual username used in the tests
        solo.finishOpenedActivities();

    }

}




