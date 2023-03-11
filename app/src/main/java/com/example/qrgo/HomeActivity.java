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
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.qrgo.utilities.CarouselAdapter;
import com.example.qrgo.utilities.CircleTransform;
import com.example.qrgo.utilities.CustomCarouselItem;
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
        imageView = findViewById(R.id.main_profile_picture);
        Picasso.get()
                .load(R.drawable.demo_picture)
                .transform(new CircleTransform())
                .into(imageView);


        List<CustomCarouselItem> carouselItems = new ArrayList<>();

        // Add some sample data
        carouselItems.add(new CustomCarouselItem(R.drawable.demo_qr_image, "#1", "My Code 1", "200 PTS"));
        carouselItems.add(new CustomCarouselItem(R.drawable.demo_qr_image, "#2", "My Code 2", "150 PTS"));
        carouselItems.add(new CustomCarouselItem(R.drawable.demo_qr_image, "#3", "My Code 3", "100 PTS"));

        ViewPager viewPager = findViewById(R.id.view_pager);
        CarouselAdapter carouselAdapter = new CarouselAdapter(this, carouselItems);
        viewPager.setAdapter(carouselAdapter);

        // Create an ontop for main_profile_picture
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Handle click event here
                Intent intent = new Intent(HomeActivity.this, PlayerActivity.class);
                // Put the username in the intent
                intent.putExtra("username", "testUser");
                startActivity(intent);
            }

        });

        Button button = findViewById(R.id.geolocation_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Handle click event here
                if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    startGeoLocationActivity();
                } else {
                    ActivityCompat.requestPermissions(HomeActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
                }
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
