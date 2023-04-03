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

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;


public abstract class BaseUserProfileTest extends BaseTestManager {
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
    @After
    public void cleanup() {
        deleteUser(username);
        solo.finishOpenedActivities();
    }
}
