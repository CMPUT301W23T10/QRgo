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
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Window;

import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.listeners.OnQRCodeUploadListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.QRGenerationController;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * This Activity serves as the basis for the QR scanning process, this activity registers the
 * callback listener for {@link QRScanActivity} and based on the result uses
 * {@link QRGenerationController} and {@link FirebaseConnect} to produce and push a
 * {@link com.example.qrgo.models.QRCode} into the database with all required fields.
 */
public class QRIntakeActivity extends AppCompatActivity {

    private double[] playerLocation = {181, 181};
    private File locationPhotoFile;
    private Uri locationPhotoUri;
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
                    askUserForLocation();
                }
            });

    private final ActivityResultLauncher<Uri> captureLocationPhoto = registerForActivityResult( new ActivityResultContracts.TakePicture(), result -> {
        if (result) {
            Log.d("QRIntakeActivity", result.toString());
            try {
                Bitmap uncompressed = MediaStore.Images.Media.getBitmap(this.getContentResolver(), locationPhotoUri);
                if (uncompressed.compress(Bitmap.CompressFormat.JPEG, 50, this.getContentResolver().openOutputStream(locationPhotoUri))) {
                    db.getQRCodeManager().uploadAndRetrieveDownloadUrl(locationPhotoUri, generator.getHash(), new OnQRCodeUploadListener() {
                        @Override
                        public void onQRCodeUploadSuccess(String downloadUrl) {
                            generator.setPhotoUrl(downloadUrl);
                            submitQR();
                        }
                    });
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        else {
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
    // Function to add photo
    public void addLocationPhoto(boolean click) {
        if (click == true) {
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String prefix = "JPEG_" + timeStamp + "_";
            locationPhotoFile = new File(getCacheDir(), prefix + ".jpg");
            if (!locationPhotoFile.exists()) {
                try {
                    locationPhotoFile.createNewFile();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            locationPhotoUri = FileProvider.getUriForFile(this, "com.example.qrgo.fileprovider", locationPhotoFile);
            captureLocationPhoto.launch(locationPhotoUri);
            locationPhotoFile.deleteOnExit();
        } else {
            submitQR();
        }

    }
    public void addLocationData(Location location) {
        if (location != null) {
            playerLocation[0] = location.getLatitude();
            playerLocation[1] = location.getLongitude();
            askUserForPicture();
        } else {
            playerLocation[0] = 181;
            playerLocation[1] = 181;
            askUserForPicture();
        }
    }
    // Function to ask user if they want to click a photo of the location or not
    public void askUserForPicture() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Location Photo?");
        builder.setMessage("Would you like to add a photo of the location?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addLocationPhoto(true);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                addLocationPhoto(false);
            }
        });
        builder.show();
    }

    // Function to add QR to database and go to QrProfileActivity with the scanned QR code
    public void submitQR() {
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefdb, Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", "");
        db.getQRCodeManager().scanQRCode(generator.getHash(), user, generator.getHumanReadableName(), playerLocation[0], playerLocation[1], generator.getPhotoUrl(), generator.getScore(), new OnQRCodeScannedListener() {
            // After scan is done then go to QrProfileActivity with the scanned QR code
            @Override
            public void onQRScanComplete(boolean success) {
                Intent intent = new Intent(QRIntakeActivity.this, QrProfileActivity.class);
                intent.putExtra("qr_code", generator.getHash());
                startActivity(intent);
            }
        });
    }

    /**
     * based on the result of the {@link QRScanActivity} and whether the user has opted to include
     * location information uses {@link FirebaseConnect} to push the scanned QR code into the database
     * upon successful QR scan opens {@link QrProfileActivity} so that a user can view the scanned
     * QR code and have options to add a location photo.
     */
    public void askUserForLocation() {
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
                    if (location == null) {
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        Log.d("Location", "location is null");
                    }
                    addLocationData(location);
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
                // Go to QrProfileActivity with the scanned QR code but no location
                addLocationData(null);
            }
        });
        builder.create().show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // We have permission, so get the user's location
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            addLocationData(location);
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
        }
    }
}
