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

import com.example.qrgo.BaseGeoLocationTest;
import com.example.qrgo.BaseUserProfileTest;
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

import org.junit.After;
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


public class QRProfileTest extends BaseGeoLocationTest {

    private static GeoPoint testLocation = new GeoPoint(53.562235, -113.54732666666666);
    @Test
    public void testQRProfile() {
        signUpTestUser();
        solo.waitForActivity(HomeActivity.class, 2000);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
        addTestQR("123456", testLocation.getLatitude(), testLocation.getLongitude());
        solo.sleep(2000);


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
            solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        } else {
            solo.clickInList(0);
            solo.waitForActivity(QrProfileActivity.class);
            solo.waitForActivity(QrProfileActivity.class, 2000);
            solo.assertCurrentActivity("Expected QRProfileActivity", QrProfileActivity.class);
        }


    }
}