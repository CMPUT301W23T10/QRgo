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
import android.location.LocationRequest;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.QRGenerationController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;


public class QRIntakeActivity extends AppCompatActivity {

    private String hash;
    private double[] playerLocation = {181, 181};
    private FirebaseConnect db = new FirebaseConnect();
    private FusedLocationProviderClient fusedLocationClient;
    private QRGenerationController generator;
    private static final int REQUEST_LOCATION_PERMISSION = 1;


    private final ActivityResultLauncher<ScanOptions> QRScanLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Toast.makeText(QRIntakeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
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

    public void scanQRCode() {
        ScanOptions options = new ScanOptions();
        options.setCaptureActivity(QRScanActivity.class);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan A nearby QR code!");
        options.setOrientationLocked(true);
        options.setBeepEnabled(false);
        QRScanLauncher.launch(options);
    }

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
                        intent.putExtra("hash", generator.getHash());
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
