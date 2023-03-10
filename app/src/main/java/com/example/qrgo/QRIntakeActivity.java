package com.example.qrgo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.QRIntakeController;
import com.google.zxing.client.android.Intents;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

/**
 *This class serves as the activity that allows a user to scan a QR code and add it to their
 * collection. Scanning of a QR code begins a process in which a user is able to add relevant metadata
 * for either creating or updating a {@link com.example.qrgo.models.QRCode} in the database. Logic
 * is handled by {@link QRIntakeController} which creates, validates, and
 * updates associated database entries with correct fields.
 */
public class QRIntakeActivity extends AppCompatActivity {

    private String hash;
    private QRCode currentQR;
    private PlayerProfile currentPlayer;
    //private FirebaseConnect db = new FirebaseConnect();


    private final ActivityResultLauncher<ScanOptions> QRScanLauncher = registerForActivityResult(new ScanContract(),
            result -> {
                if(result.getContents() == null) {
                    Intent originalIntent = result.getOriginalIntent();
                    if (originalIntent == null) {
                        Log.d("QRIntakeActivity", "Cancelled scan");
                        Toast.makeText(QRIntakeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
                    } else if(originalIntent.hasExtra(Intents.Scan.MISSING_CAMERA_PERMISSION)) {
                         Log.d("MainActivity", "Cancelled scan due to missing camera permission");
                        Toast.makeText(QRIntakeActivity.this, "Cancelled due to missing camera permission", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Log.d("QRIntakeActivity", "Scanned");
                    hash = QRIntakeController.generateHash(result.getContents());

                    QRIntakeController.calculateFields(currentQR);
                    Log.d("QRIntakeActivity", currentQR.getHumanReadableQR());
                    Log.d("QRIntakeActivity",  hash);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_intake);
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

//    public void checkHash(String hash) {
//        db.getQRCode(hash, new FirebaseConnect.QRCodeListener() {
//            @Override
//            public void onQRCodeRetrieved(QRCode qrCode) {
//                currentQR = qrCode;
//            }
//
//            @Override
//            public void onQRCodeNotFound() {
//                //int score = QRIntakeController.calculateScore(hash);
//                //String humanReadableName = QRIntakeController.generateHumanReadableName(hash);
//                Log.d("QRIntakeActivity", "QR not found, creating a new QR code");
//                currentQR = new QRCode(hash);
//                //TODO calculate/generate fields that can be generated from hash
//            }
//        });
//    }
//
//    public void checkOwnership() {
//        if (!currentQR.getScannedPlayerIds().contains(currentPlayer.getUsername())) {
//            currentQR.addScannedPlayer(currentPlayer.getUsername());
//        }
//        else {
//            Toast.makeText(QRIntakeActivity.this, "You have already scanned this QR code", Toast.LENGTH_LONG).show();
//            //TODO kick user back to entry point
//        }
//    }

}
