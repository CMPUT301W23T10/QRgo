package com.example.qrgo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationRequest;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.QRGenerationController;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.CancellationTokenSource;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 * 
 */
public class QRIntakeActivity extends AppCompatActivity {

    private String hash;
    private double[] playerLocation = {181, 181};
    private FirebaseConnect db = new FirebaseConnect();
    private FusedLocationProviderClient fusedLocationClient;
    private QRGenerationController generator;


    private final ActivityResultLauncher<ScanOptions> QRScanLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if (result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("QRIntakeActivity", "Cancelled scan");
                        Toast.makeText(QRIntakeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if (originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                        Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(QRIntakeActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("QRIntakeActivity", "Scanned");
                    generator = new QRGenerationController(result.getContents());
                    Log.d("QRIntakeActivity", generator.getHash());
                    //Log.d("QRIntakeActivity", (String) generator.getScore());
                    Log.d("QRIntakeActivity", generator.getHumanReadableName());

                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_intake);

        Switch locationSwitch = findViewById(R.id.switch1);
        String[] permissions = {Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissions(permissions, 1);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();

        locationSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (ActivityCompat.checkSelfPermission(QRIntakeActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(QRIntakeActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    playerLocation[0] = 181;
                    playerLocation[1] = 181;
                    locationSwitch.setChecked(false);
                    locationSwitch.setClickable(false);
                }
                if (locationSwitch.isChecked()) {
                    fusedLocationClient.getCurrentLocation(LocationRequest.QUALITY_HIGH_ACCURACY, cancellationTokenSource.getToken())
                            .addOnSuccessListener(QRIntakeActivity.this, new OnSuccessListener<Location>() {
                                @Override
                                public void onSuccess(Location location) {
                                    playerLocation[0] = location.getLatitude();
                                    playerLocation[1] = location.getLongitude();
                                    Log.d("QRIntakeActivity", "onSuccess: " + playerLocation[0]);
                                }
                            });

                }
            }
        });
    }

    public void scanQRCode(View view) {
        ScanOptions options = new ScanOptions();
        options.setCaptureActivity(QRScanActivity.class);
        options.setDesiredBarcodeFormats(ScanOptions.QR_CODE);
        options.setPrompt("Scan A nearby QR code!");
        options.setOrientationLocked(true);
        options.setBeepEnabled(false);
        QRScanLauncher.launch(options);
    }

    public void submitQR(View view) {
        db.scanQRCode(generator.getHash(), "testUser", generator.getHumanReadableName(), playerLocation[0], playerLocation[1], "www.google.ca", generator.getScore(), new FirebaseConnect.OnQRCodeScannedListener() {
            @Override
            public void onQRScanComplete(boolean success) {
                Log.d("QRIntakeActivity", "scanned QR to database");
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
