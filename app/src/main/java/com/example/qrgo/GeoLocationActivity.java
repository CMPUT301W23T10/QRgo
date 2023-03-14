package com.example.qrgo;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 *
 */
public class GeoLocationActivity extends AppCompatActivity implements LocationListener {

    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private MapView map = null;

    private double latitude;
    private double longitude;

    private GeoPoint startPoint;
    private List<List<Double>> coordinates = new ArrayList<>();

    private LocationManager mLocationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FirebaseConnect database = new FirebaseConnect();
        database.getAllQrCoordinates(new FirebaseConnect.OnCoordinatesListLoadedListener() {
            @Override
            public void onCoordinatesListLoaded(Map<String, List<List<Double>>> mapped_coordinates) {
                for (List<List<Double>> coordinateList : mapped_coordinates.values()) {
                    for (List<Double> coordinate : coordinateList) {
                        coordinates.add(coordinate);
                    }
                }
            }

            @Override
            public void onCoordinatesListLoadFailure(Exception e) {

            }
        });

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        setContentView(R.layout.activity_geo_location);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);


        // Create a new MyLocationNewOverlay object
        MyLocationNewOverlay myLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);

        requestPermissionsIfNecessary(new String[]{(
                // if you need to show the current location, uncomment the line below
                Manifest.permission.ACCESS_FINE_LOCATION
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                //Manifest.permission.WRITE_EXTERNAL_STORAGE
        )});

        map.getOverlays().add(myLocationOverlay);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location location = getLastKnownLocation();

        if (getLastKnownLocation() == null) {
            latitude = 53.5232;
            longitude = -113.5263;
        } else {
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        startPoint = new GeoPoint(latitude, longitude);


        //Log.d("hazarika", "latitude: " + startPoint.getLatitude() + "longitude" + startPoint.getLongitude());
        mapController.setCenter(startPoint);
        mapController.setZoom(15);

        createDraggableMarker(map, new GeoPoint(startPoint.getLatitude(), startPoint.getLongitude()), "Hello world", coordinates);

        // Hide the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        // Set up the close button
        FloatingActionButton goToPlayerActivityButton = findViewById(R.id.close_button);
        goToPlayerActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(GeoLocationActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onLocationChanged(Location location) {
        // Update map view with current location
        map.getController().setCenter(new GeoPoint(location.getLatitude(), location.getLongitude()));
        map.getController().setZoom(15);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * This functions asks the user for the permissions.
     * The prime permission asked is the user location to get a user location marker.
     * @param permissions an array of string of permissions
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Adds a marker to the map provided the longitude and latitude values
     * The marker is a tear drop shaped icon with a pointing finger image
     * @param latitude double value representing the latitude
     * @param longitude double value representing the longitude
     */
    private void addMarker(double latitude, double longitude) {
        //Log.d("hazarika123", "latitude: " + latitude + "longitude" + longitude);
        Marker marker = new Marker(map);
        marker.setPosition(new GeoPoint(latitude, longitude));
        marker.setTitle("Latitude: " + Double.toString(latitude) + " Longitude: " + Double.toString(longitude));
        map.getOverlays().add(marker);
        map.invalidate();
    }

    /**
     * Creates a draggable marker that allows the user to pin it in any location the user wants.
     * All the markers nearby the draggable markers are displayed.
     * @param map the map object which represents the osmdroid map
     * @param position A GeoPoint of where the draggable marker will spawn
     * @param title A text which will be displayed when the marker is clicked
     * @param markerPositions A nested list of doubles containing the latitude and longitude values of all the markers fetched from the firebase
     * @return
     */
    private Marker createDraggableMarker(MapView map, GeoPoint position, String title, List<List<Double>> markerPositions) {
        Marker marker = new Marker(map);
        marker.setIcon(getResources().getDrawable(R.drawable.baseline_accessibility_24));
        marker.setPosition(position);
        marker.setTitle(title);
        marker.setDraggable(true);
        marker.setOnMarkerDragListener(new Marker.OnMarkerDragListener() {
            @Override
            public void onMarkerDrag(Marker marker) {
                // This method is called repeatedly while the marker is being dragged
                for (List coordinate:coordinates) {
                    addMarker((Double) coordinate.get(0), (Double) coordinate.get(1));
                }
                removeMarkersOutOfRange(markerPositions, marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                // This method is called when the marker is released after being dragged
                GeoPoint position = marker.getPosition();
                double latitude = position.getLatitude();
                double longitude = position.getLongitude();
                marker.setSnippet(Double.toString(latitude) + " " + Double.toString(longitude));
                addMarkersInRange(map, markerPositions, position);
            }

            @Override
            public void onMarkerDragStart(Marker marker) {
                // This method is called when the marker is first touched to start dragging
            }
        });
        map.getOverlays().add(marker);
        return marker;
    }

    /**
     * Removes the markers that are outside the set range from the reach of the draggable marker
     * @param markerPositions A nested list of doubles containing the latitude and longitude values of all the markers fetched from the firebase
     * @param center the GeoPoint of the draggable marker
     */
    private void removeMarkersOutOfRange(List<List<Double>> markerPositions, GeoPoint center) {
        double range = 0.05;
        List<Overlay> overlaysToKeep = new ArrayList<>();
        for (Overlay overlay : map.getOverlays()) {
            if (overlay instanceof Marker) {
                Marker marker = (Marker) overlay;
                if (marker.isDraggable()) {
                    overlaysToKeep.add(marker);
                    continue;
                }
                GeoPoint position = marker.getPosition();
                double latitude = position.getLatitude();
                double longitude = position.getLongitude();
                double distance = Math.sqrt(Math.pow((latitude - center.getLatitude()), 2) + Math.pow((longitude - center.getLongitude()), 2));
                if (distance <= range) {
                    overlaysToKeep.add(marker);
                }
            } else {
                overlaysToKeep.add(overlay);
            }
        }
        map.getOverlays().clear();
        map.getOverlays().addAll(overlaysToKeep);
        map.invalidate();
    }

    /**
     * Adds the markers that are within the set range from the reach of the draggable marker
     * @param map the map object which represents the osmdroid map
     * @param markerPositions A nested list of doubles containing the latitude and longitude values of all the markers fetched from the firebase
     * @param center the GeoPoint of the draggable marker
     */
    private void addMarkersInRange(MapView map, List<List<Double>> markerPositions, GeoPoint center) {
        double range = 0.05;
        for (List<Double> markerPosition : markerPositions) {
            double latitude = markerPosition.get(0);
            double longitude = markerPosition.get(1);
            double distance = Math.sqrt(Math.pow((latitude - center.getLatitude()), 2) + Math.pow((longitude - center.getLongitude()), 2));
            if (distance <= range) {
                Marker marker = new Marker(map);
                marker.setPosition(new GeoPoint(latitude, longitude));
                marker.setTitle("Latitude: " + Double.toString(latitude) + " Longitude: " + Double.toString(longitude));
                map.getOverlays().add(marker);

                marker.setOnMarkerClickListener(new Marker.OnMarkerClickListener() {
                    @Override
                    public boolean onMarkerClick(Marker marker, MapView mapView) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(GeoLocationActivity.this);
                        builder.setMessage("Do you want to go to a different activity?");
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Log.d("hazarika123", "latitude: " + latitude + "longitude" + longitude);
                                Intent intent = new Intent(GeoLocationActivity.this, QrProfileActivity.class);
                                startActivity(intent);
                            }
                        });
                        builder.setNegativeButton("No", null);
                        builder.show();
                        return false;
                    }
                });
            }
        }
        map.invalidate();
    }

    /**
     * Gets the current location of the user.
     * @return the current location of the user given the location permission is granted
     */
    private Location getLastKnownLocation() {
        mLocationManager = (LocationManager)getApplicationContext().getSystemService(LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(true);
        Location bestLocation = null;
        for (String provider : providers) {
            @SuppressLint("MissingPermission") Location l = mLocationManager.getLastKnownLocation(provider);
            if (l == null) {
                continue;
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                bestLocation = l;
            }
        }
        return bestLocation;
    }
}