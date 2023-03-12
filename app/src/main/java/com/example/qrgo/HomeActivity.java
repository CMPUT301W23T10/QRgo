package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.BasicQrArrayAdapter;
import com.example.qrgo.utilities.CarouselAdapter;
import com.example.qrgo.utilities.CircleTransform;
import com.example.qrgo.utilities.CustomCarouselItem;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.UserCarouselAdapter;
import com.example.qrgo.utilities.UserCarouselitem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ImageView imageView;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
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


        // Define the carousel items
        List<CustomCarouselItem> carouselItems = new ArrayList<>();

        // Add some sample data
        carouselItems.add(new CustomCarouselItem(R.drawable.demo_qr_image, "#1", "My Code 1", "200 PTS"));
        carouselItems.add(new CustomCarouselItem(R.drawable.demo_qr_image, "#2", "My Code 2", "150 PTS"));
        carouselItems.add(new CustomCarouselItem(R.drawable.demo_qr_image, "#3", "My Code 3", "100 PTS"));

        ViewPager viewPager = findViewById(R.id.view_pager);

        CarouselAdapter carouselAdapter = new CarouselAdapter(this, carouselItems);
        viewPager.setAdapter(carouselAdapter);

        // Define the user carousel items
        List<UserCarouselitem> userCarouselItems = new ArrayList<>();
        // Add some sample data
        userCarouselItems.add(new UserCarouselitem(R.drawable.demo_picture, "Tim", "testUser","1000", "collected 10"));
        userCarouselItems.add(new UserCarouselitem(R.drawable.demo_picture, "Tim", "testUser", "900","collected 9"));
        userCarouselItems.add(new UserCarouselitem(R.drawable.demo_picture, "Tim", "testUser3", "800","collected 8"));
        ViewPager userViewPager = findViewById(R.id.user_view_pager);

        UserCarouselAdapter userCarouselAdapter = new UserCarouselAdapter(this, userCarouselItems);
        userViewPager.setAdapter(userCarouselAdapter);

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
                intent.putExtra("username", "testUser");
                startActivity(intent);
            }

        });
        ImageView geolocationButton = findViewById(R.id.map_image);
        geolocationButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Handle click event here
//                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    startGeoLocationActivity();
//                } else {
//                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
//                }
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
        FirebaseConnect firebaseConnect = new FirebaseConnect();
        firebaseConnect.getPlayerProfile("testUser", new FirebaseConnect.OnPlayerProfileGetListener(){
                    @Override
                    public void onPlayerProfileGet(PlayerProfile userProfile) {
                        ListView listView = findViewById(R.id.home_qr_listview);
                        // Set up the QR code list view
                        List<BasicQRCode> qrCodeList = userProfile.getQrCodeBasicProfiles();
                        ArrayList<BasicQRCode> qrCodeArrayList = new ArrayList<>(qrCodeList);
                        // Limit the number of QR codes to 3 AND SORT IT IN ( not DONE DESCENDING ORDER)
                        if (qrCodeArrayList.size() >= 3) {
                            qrCodeArrayList = new ArrayList<>(qrCodeArrayList.subList(0, 3));
                        }
                        TextView play_qr_head = findViewById(R.id.qr_title);
                        play_qr_head.setText("QR Desk (" + userProfile.getQrCodeBasicProfiles().size() + ")");

                        BasicQrArrayAdapter qrAdapter = new BasicQrArrayAdapter( HomeActivity.this, qrCodeArrayList);
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
                Toast.makeText(this, "Unable to get your location", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
