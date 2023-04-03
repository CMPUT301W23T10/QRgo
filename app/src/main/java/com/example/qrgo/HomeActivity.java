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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.listeners.OnPlayerListLoadedListener;
import com.example.qrgo.listeners.OnPlayerProfileGetListener;
import com.example.qrgo.listeners.OnQrListLoadedListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.BasicQrArrayAdapter;
import com.example.qrgo.utilities.CarouselAdapter;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.ImageViewController;
import com.example.qrgo.utilities.UserCarouselAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is the home activity of the app
 * It contains the insights of the user
 */
public class HomeActivity extends AppCompatActivity {
    ImageView imageView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    /**
     * This method is called when the back button is pressed
     */
    @Override
    public void onBackPressed() {
        // Create a new intent to close the app
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        
    }
    /**
     * This method is called when the activity is created
     * {@link FirebaseConnect} is used to get the user's insights
     * {@link CarouselAdapter} is used to display user's scanned QR codes
     * {@link UserCarouselAdapter} is used to display global users insights
     * {@link BasicQrArrayAdapter} is used to display global scanned QR codes
     * {@link ImageViewController} is used to display the user's profile picture
     * {@link BasicPlayerProfile} class that represents the user's insights
     * {@link BasicQRCode} class that represents the QR code
     * {@link PlayerProfile} class that represents more detailed user's insights
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the user from the shared preferences
        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefdb, Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", "");
        setContentView(R.layout.activity_home);
        // Set up the add button to add a new QR code
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

        // Set up leaderboard page
        TextView leaderboardButton = findViewById(R.id.user_view_all);
        leaderboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LeaderboardFragment qrFragment = new LeaderboardFragment();
                qrFragment.setComeFrom("HomeActivity");
                // Pass qrCodeList as a parameter to the fragment
                getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                        .add(R.id.fragment_container, qrFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ViewPager viewPager = findViewById(R.id.view_pager);

        // Firebase Connect
        FirebaseConnect firebaseConnect = new FirebaseConnect();
        firebaseConnect.getPlayerProfileManager().getPlayerProfile(user, new  OnPlayerProfileGetListener(){


            @Override
            public void onPlayerProfileGet(PlayerProfile userProfile) {
                // Load user image into the ImageView
                imageView = findViewById(R.id.main_profile_picture);
                ImageViewController imageViewController = new ImageViewController();
                imageViewController.setImage(userProfile.getFirstName(),imageView);

                TextView scans = findViewById(R.id.collected);
                scans.setText("Collected "+userProfile.getTotalScans());

                TextView mainTotalScore = findViewById(R.id.main_total_score);
                mainTotalScore.setText(userProfile.getTotalScore()+"");

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
                TextView userQrViewAll = findViewById(R.id.user_qr_view_all);
                userQrViewAll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your fragment here
                        QrListviewFragment qrFragment = new QrListviewFragment();
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
                // Remove the progress bar and show the page
                LinearLayout progressBar = findViewById(R.id.home_progressBar);
                LinearLayout homeLoading = findViewById(R.id.home_loading);

                progressBar.setVisibility(View.INVISIBLE);
                homeLoading.setVisibility(View.VISIBLE);
            }
        });

        firebaseConnect.getPlayerProfileManager().getPlayersSortedByTotalScore(
                new OnPlayerListLoadedListener() {
                    @Override
                    public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                        TextView playUserHead = findViewById(R.id.users_head);
                        playUserHead.setText("Users (" + playerList.size() + ")");
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

        firebaseConnect.getQRCodeManager().getQrCodesSortedByPoints(
                new OnQrListLoadedListener() {

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
                                QrListviewFragment qrFragment = new QrListviewFragment();
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
        // Handle click event on the search bar
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
        firebaseConnect.getPlayerProfileManager().getPlayerProfile(user, new OnPlayerProfileGetListener(){
                    @Override
                    public void onPlayerProfileGet(PlayerProfile userProfile) {

                    }
                });
    }

    /**
     * Start the GeoLocationActivity
     */
    private void startGeoLocationActivity() {
        Intent intent = new Intent(HomeActivity.this, GeoLocationActivity.class);
        startActivity(intent);
    }

    /**
     * Handle the result of the permission request
     */
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
