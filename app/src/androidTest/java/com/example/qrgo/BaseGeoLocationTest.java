package com.example.qrgo;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class BaseGeoLocationTest extends BaseTestManager {
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
        return null;
    }

    @After
    public void cleanup() {
            deleteUser(username);
            deleteQrCode(username,"123456");
            solo.finishOpenedActivities();
        }
}
