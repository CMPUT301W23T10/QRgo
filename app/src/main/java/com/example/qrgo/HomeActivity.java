package com.example.qrgo;

import static com.example.qrgo.MainActivity.sharedPrefdb;
import static com.example.qrgo.SignupActivity.user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.BasicQrArrayAdapter;
import com.example.qrgo.utilities.CarouselAdapter;
import com.example.qrgo.utilities.CircleTransform;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.UserCarouselAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ImageView imageView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefdb, Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", "");

        setContentView(R.layout.activity_home);

        FloatingActionButton addBtn = findViewById(R.id.add_button);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, QRIntakeActivity.class);
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });

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
        ViewPager viewPager = findViewById(R.id.view_pager);

        // Firebase Connect
        FirebaseConnect firebaseConnect = new FirebaseConnect();
        firebaseConnect.getPlayerProfile(user, new  FirebaseConnect.OnPlayerProfileGetListener(){


            @Override
            public void onPlayerProfileGet(PlayerProfile userProfile) {
                TextView scans = findViewById(R.id.collected);
                scans.setText("Collected "+userProfile.getTotalScans());

                TextView main_total_score = findViewById(R.id.main_total_score);
                main_total_score.setText(userProfile.getTotalScore()+"");

                // Define the carousel items
                List<BasicQRCode> carouselItems = userProfile.getQrCodeBasicProfiles();
                List<BasicQRCode> temp = carouselItems;
                if (temp.size() > 3) {
                    temp = temp.subList(0, 3);
                }
                if (temp.size() == 0) {
                    temp.add(new BasicQRCode(
                            "NaN",
                            "NaN",
                            "#No QR",
                            00
                    ));
                }
                CarouselAdapter carouselAdapter = new CarouselAdapter(HomeActivity.this, temp);

                // Set up the view all button for QR CODES
                TextView user_qr_view_all = findViewById(R.id.user_qr_view_all);
                user_qr_view_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your fragment here
                        QrListview  qrFragment = new QrListview();
                        ArrayList<BasicQRCode> qrCodeArrayList = new ArrayList<>(carouselItems);
                        // Pass qrCodeList as a parameter to the fragment
                        qrFragment.setQrCodeList(qrCodeArrayList);
                        qrFragment.setComeFrom("home");
                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.fragment_container, qrFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                viewPager.setAdapter(carouselAdapter);
            }
        });

        firebaseConnect.getPlayersSortedByTotalScore(
                new FirebaseConnect.OnPlayerListLoadedListener () {
                    @Override
                    public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                        TextView play_user_head = findViewById(R.id.users_head);
                        play_user_head.setText("Users (" + playerList.size() + ")");
                        if (playerList.size() > 3) {
                            playerList = playerList.subList(0, 3);
                        }
                        // Define the user carousel items
                        ViewPager userViewPager = findViewById(R.id.user_view_pager);

                        UserCarouselAdapter userCarouselAdapter = new UserCarouselAdapter(HomeActivity.this, playerList);
                        userViewPager.setAdapter(userCarouselAdapter);

                    }
                    @Override
                    public void onPlayerListLoadFailure(Exception e) {
                        Toast.makeText(HomeActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        firebaseConnect.getQrCodesSortedByPoints(
                new FirebaseConnect.OnQrListLoadedListener() {

                    @Override
                    public void onQrListLoaded(List<BasicQRCode> qrcodes) {
                        ListView listView = findViewById(R.id.home_qr_listview);
                        // Set up the QR code list view
                        List<BasicQRCode> qrCodeList = qrcodes;
                        ArrayList<BasicQRCode> qrCodeArrayList = new ArrayList<>(qrCodeList);
                        // Limit the number of QR codes to 3 AND SORT IT IN ( not DONE DESCENDING ORDER)
                        if (qrCodeArrayList.size() >= 3) {
                            qrCodeArrayList = new ArrayList<>(qrCodeArrayList.subList(0, 3));
                        }
                        TextView play_qr_head = findViewById(R.id.qr_title);
                        play_qr_head.setText("QR Desk (" + qrcodes.size() + ")");

                        BasicQrArrayAdapter qrAdapter = new BasicQrArrayAdapter( HomeActivity.this, qrCodeArrayList, "player");
                        listView.setAdapter(qrAdapter);
                        int height = 0;
                        if (qrCodeArrayList.size() == 3) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 324, getResources().getDisplayMetrics());
                        } else if (qrCodeArrayList.size() == 2) {
                            height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 224, getResources().getDisplayMetrics());
                        } else {
                            height = LinearLayout.LayoutParams.WRAP_CONTENT;
                        }
                        listView.getLayoutParams().height = (int) height;

                        TextView qr_view_all = findViewById(R.id.qr_view_all);
                        qr_view_all.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Call your fragment here
                                QrListview  qrFragment = new QrListview();
                                ArrayList<BasicQRCode> qrCodeArrayList = new ArrayList<>(qrcodes);
                                // Pass qrCodeList as a parameter to the fragment
                                qrFragment.setQrCodeList(qrCodeArrayList);
                                qrFragment.setComeFrom("homeAll");
                                getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                        .add(R.id.fragment_container, qrFragment)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        });

                    }
                    @Override
                    public void onQrListLoadFailure(Exception e) {
                        Toast.makeText(HomeActivity.this, "No internet", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Handle click event on the profile picture and also set the profile picture
        imageView = findViewById(R.id.main_profile_picture);
        Picasso.get()
                .load(R.drawable.demo_picture)
                .transform(new CircleTransform())
                .into(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Handle click event here
                Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
                // Put the username in the intent
                intent.putExtra("username", user);
                startActivity(intent);
            }
        });

        ImageView geolocationButton = findViewById(R.id.map_image);
        geolocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Handle click event here
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startGeoLocationActivity();
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
            }

        });

        LinearLayout searchLayout = findViewById(R.id.call_search_fragment);
        searchLayout.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.search_fragment_container, new SearchFragment(), "searchFragment")
                        .addToBackStack(null)
                        .commit();
            }

        });

        // Change the carousel item when the left or right arrow is clicked
        ImageView leftButton = findViewById(R.id.left_arrow_button);
        ImageView rightButton = findViewById(R.id.right_arrow_button);
        leftButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Handle click event here
                        viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
                    }
                }
        );
        rightButton.setOnClickListener(
                new View.OnClickListener() {
                    public void onClick(View v) {
                        // Handle click event here
                        viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                    }
                }
        );
        firebaseConnect.getPlayerProfile(user, new FirebaseConnect.OnPlayerProfileGetListener(){
                    @Override
                    public void onPlayerProfileGet(PlayerProfile userProfile) {

                    }
                });
    }
    private void startGeoLocationActivity() {
        Intent intent = new Intent(HomeActivity.this, GeoLocationActivity.class);
        startActivity(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startGeoLocationActivity();
            } else {
                startGeoLocationActivity();
                Toast.makeText(this, "Unable to get your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
