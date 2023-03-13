package com.example.qrgo;

import static com.example.qrgo.MainActivity.sharedPrefdb;
import static com.example.qrgo.SignupActivity.user;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;

import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.QRGenerationController;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * This Activity serves as the basis for the QR scanning process, this activity registers the
 * callback listener for {@link QRScanActivity} and based on the result uses
 * {@link QRGenerationController} and {@link FirebaseConnect} to produce and push a
 * {@link com.example.qrgo.models.QRCode} into the database with all required fields.
 */
public class QRIntakeActivity extends AppCompatActivity {

    private double[] playerLocation = {181, 181};
    private FirebaseConnect db = new FirebaseConnect();
    private QRGenerationController generator;
    private static final int REQUEST_LOCATION_PERMISSION = 1;

    private final ActivityResultLauncher<ScanOptions> QRScanLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Toast.makeText(QRIntakeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(QRIntakeActivity.this, HomeActivity.class);
                        // Clear the activity stack so that becomes the new root activity
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        // Start the HomeActivity and finish the current activity
                        startActivity(intent);
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Toast.makeText(QRIntakeActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    generator = new QRGenerationController(result.getContents());
                    submitQR();
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_intake);



        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }
        // Hide the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        }
        scanQRCode();
    }

    /**
     * sets up the QR scanner by initializing scan options and setting {@link QRScanActivity} as the
     * activity for the actual scanning process, launches the QR scan process.
     */
    public void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setCaptureActivity(QRScanActivity.class);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan A nearby QR code!");
        options.setOrientationLocked(true);
        options.setBeepEnabled(false);
        QRScanLauncher.launch(options);
    }

    /**
     * based on the result of the {@link QRScanActivity} and whether the user has opted to include
     * location information uses {@link FirebaseConnect} to push the scanned QR code into the database
     * upon successful QR scan opens {@link QrProfileActivity} so that a user can view the scanned
     * QR code and have options to add a location photo.
     */
    public void submitQR() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Record location?");
        builder.setMessage("Do you want to record your location?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    // We have permission, so get the user's location
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    if (location != null) {
                        playerLocation[0] = location.getLatitude();
                        playerLocation[1] = location.getLongitude();
                    } else {
                        playerLocation[0] = 181;
                        playerLocation[1] = 181;
                    }
                    SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefdb, Context.MODE_PRIVATE);
                    user = sharedPreferences.getString("user", "");
                    db.scanQRCode(generator.getHash(), user, generator.getHumanReadableName(), playerLocation[0], playerLocation[1], "www.google.ca", generator.getScore(), new FirebaseConnect.OnQRCodeScannedListener() {
                        @Override
                        public void onQRScanComplete(boolean success) {
                            Intent intent = new Intent(QRIntakeActivity.this, QrProfileActivity.class);
                            intent.putExtra("qr_code", generator.getHash());
                            startActivity(intent);
                        }
                    });

                } else {
                    ActivityCompat.requestPermissions(QRIntakeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
                }

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                playerLocation[0] = 181;
                playerLocation[1] = 181;

                db.scanQRCode(generator.getHash(), user, generator.getHumanReadableName(), playerLocation[0], playerLocation[1], "www.google.ca", generator.getScore(), new FirebaseConnect.OnQRCodeScannedListener() {
                    @Override
                    public void onQRScanComplete(boolean success) {
                        Intent intent = new Intent(QRIntakeActivity.this, QrProfileActivity.class);
                        intent.putExtra("qr_code", generator.getHash());
                        startActivity(intent);
                    }
                });

            }
        });
        builder.create().show();
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this, new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,}, 1);
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // We have permission, so get the user's location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                playerLocation[0] = location.getLatitude();
                playerLocation[1] = location.getLongitude();
            } else {
                playerLocation[0] = 181;
                playerLocation[1] = 181;
            }
            db.scanQRCode(generator.getHash(), user, generator.getHumanReadableName(), playerLocation[0], playerLocation[1], "www.google.ca", generator.getScore(), new FirebaseConnect.OnQRCodeScannedListener() {
                @Override
                public void onQRScanComplete(boolean success) {
                    Intent intent = new Intent(QRIntakeActivity.this, QrProfileActivity.class);
                    intent.putExtra("hash", generator.getHash());
                    startActivity(intent);
                }
            });
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
