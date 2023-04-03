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
import android.view.Display;
import android.widget.EditText;
import android.widget.ListView;

import androidx.core.app.ActivityCompat;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnitRunner;

import com.example.qrgo.BaseGeoLocationTest;
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
import com.google.firebase.firestore.QuerySnapshot;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public class GeoLocationTest extends BaseGeoLocationTest {

    private Marker draggableMarker;

    private static GeoPoint testLocation = new GeoPoint(53.562235, -113.54732666666666);
    double latitude;
    double longitude;


    @Test
    public void testMap() {
        signUpTestUser();
        solo.waitForActivity(HomeActivity.class, 2000);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");

        LocationManager locationManager = (LocationManager) solo.getCurrentActivity().getSystemService(Context.LOCATION_SERVICE);
        Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (lastKnownLocation != null) {
            latitude = lastKnownLocation.getLatitude();
            longitude = lastKnownLocation.getLongitude();
        } else {
            Log.e("Location", "Last known location is null");
        }
        testLocation = new GeoPoint(latitude, longitude);
        addTestQR("123456", testLocation.getLatitude(), testLocation.getLongitude());

        solo.sleep(2000);
        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);
        // Click on the map
        solo.clickOnView(solo.getView(R.id.map_image));
        solo.waitForActivity(GeoLocationActivity.class, 2000);

        solo.assertCurrentActivity("Expected GeoLocationActivity", GeoLocationActivity.class);

        MapView map = (MapView) solo.getView(R.id.map);
        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IGeoPoint startPoint = new org.osmdroid.util.GeoPoint(testLocation.getLatitude(), testLocation.getLongitude());
        solo.getCurrentActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                IMapController mapController = map.getController();
                mapController.setCenter((IGeoPoint) startPoint);
            }
        });

        Marker marker = getMarker(map);
        // Get the screen size
        Display display = solo.getCurrentActivity().getWindowManager().getDefaultDisplay();
        int centerX = display.getWidth() / 2;
        int centerY = display.getHeight() / 2;

        // Long click on the center of the screen
        solo.clickLongOnScreen(centerX, centerY);

        solo.sleep(1000);

        solo.clickOnScreen(centerX, centerY);

        // Wait for the AlertDialog to appear
        solo.waitForDialogToOpen();

        solo.clickOnText("No");
        solo.sleep(2000);
        solo.assertCurrentActivity("Expected GeoLocationActivity", GeoLocationActivity.class);
    }
}