package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
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
import com.example.qrgo.utilities.UserCarouselAdapter;
import com.example.qrgo.utilities.UserCarouselitem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    ImageView imageView;

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
        userCarouselItems.add(new UserCarouselitem(R.drawable.demo_picture, "Tim", "1000", "collected 10"));
        userCarouselItems.add(new UserCarouselitem(R.drawable.demo_picture, "Tim", "900", "collected 9"));
        userCarouselItems.add(new UserCarouselitem(R.drawable.demo_picture, "Tim", "800", "collected 8"));
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
    }
}