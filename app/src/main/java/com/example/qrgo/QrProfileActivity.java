package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.FirebaseConnect;

public class QrProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_profile);
        // Checking intent for qr_code key
        Intent intent = getIntent();
        String qr_code_id = intent.getStringExtra("qr_code");

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

        FirebaseConnect firebaseconnect = new FirebaseConnect();
        firebaseconnect.getQRCode(qr_code_id, new FirebaseConnect.QRCodeListener() {

            @Override
            public void onQRCodeRetrieved(QRCode qrCode) {
                Log.d("QrProfileActivity", "QR Code Name " + qrCode.getHumanReadableQR());
                Log.d("QrProfileActivity", "QR Code Players " + qrCode.getScannedPlayer());
                Log.d("QrProfileActivity", "QR Code Points " + qrCode.getQrCodePoints());
                Log.d("QrProfileActivity", "QR Code Comments " + qrCode.getComments());
            }

            @Override
            public void onQRCodeNotFound() {
                Toast.makeText(QrProfileActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
}