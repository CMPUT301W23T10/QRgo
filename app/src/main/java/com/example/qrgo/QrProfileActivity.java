package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.CircleTransform;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.RoundedSquareTransform;
import com.example.qrgo.utilities.UserCarouselAdapter;
import com.squareup.picasso.Picasso;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

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
                ImageView imageView = findViewById(R.id.qr_image_view);
                Picasso.get()
                        .load(R.drawable.demo_qr_image)
                        .transform(new RoundedSquareTransform(100))
                        .into(imageView);

//                Log.d("QrProfileActivity", "QR Code Name " + qrCode.getHumanReadableQR());
                TextView qrCodeName = findViewById(R.id.qr_name);
                qrCodeName.setText(qrCode.getHumanReadableQR());

                List<BasicPlayerProfile> scanned_list = qrCode.getScannedPlayer();
//                Log.d("QrProfileActivity", "QR Code Players " + scanned_list);
                TextView users_head = findViewById(R.id.qr_users_head);
                users_head.setText("Users (" + scanned_list.size() + ")");
                List<BasicPlayerProfile> playerList = scanned_list;
                if (scanned_list.size() > 3) {
                    playerList = playerList.subList(0, 3);
                }
                // Define the user carousel items
                ViewPager userViewPager = findViewById(R.id.qr_view_pager);

                UserCarouselAdapter userCarouselAdapter = new UserCarouselAdapter(QrProfileActivity.this, playerList);
                userViewPager.setAdapter(userCarouselAdapter);

                TextView qr_users_view_all = findViewById(R.id.qr_users_view_all);
                qr_users_view_all.setOnClickListener(v -> {
                    QrAllUsers_listview  qrFragment = new QrAllUsers_listview();
                    ArrayList<BasicPlayerProfile> qrCodeArrayList = new ArrayList<>(scanned_list);
                    // Pass qrCodeList as a parameter to the fragment
                    qrFragment.setQrCodeList(qrCodeArrayList);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.qr_fragment_container, qrFragment)
                            .addToBackStack(null)
                            .commit();
                });


//                Log.d("QrProfileActivity", "QR Code Points " + qrCode.getQrCodePoints());
                TextView qr_score = findViewById(R.id.qr_score);
                qr_score.setText(Integer.toString(qrCode.getQrCodePoints()) + " pts");


//                Log.d("QrProfileActivity", "QR Code Comments " + qrCode.getComments());
//                TextView qr_comment_head = findViewById(R.id.qr_comment_head);
//                qr_comment_head.setText("Comments (" + qrCode.getComments().size() + ")");
            }

            @Override
            public void onQRCodeNotFound() {
                Toast.makeText(QrProfileActivity.this, "No Internet", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onQRCodeRetrievalFailure(Exception e) {

            }
        });

        // Define the back button
        ImageView backButton = findViewById(R.id.close_button);
        backButton.setOnClickListener(v -> {
           Intent intent1 = new Intent(QrProfileActivity.this, MainActivity.class);
              startActivity(intent1);
        });
    }
}