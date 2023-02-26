package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

public class PlayerActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }
        // Hide the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        // Set up the close button
        FloatingActionButton goToPlayerActivityButton = findViewById(R.id.close_button);
        goToPlayerActivityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PlayerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        String imageUrl = "https://avataaars.io/?avatarStyle=Circle&topType=LongHairStraight2&accessoriesType=Prescription02&hairColor=BrownDark&facialHairType=MoustacheMagnum&facialHairColor=Blonde&clotheType=CollarSweater&clotheColor=Gray02&eyeType=Squint&eyebrowType=DefaultNatural&mouthType=Serious&skinColor=Pale";
        ImageView imageView = findViewById(R.id.play_image_view);
        Picasso.get().load("https://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).into(imageView);


    }
}