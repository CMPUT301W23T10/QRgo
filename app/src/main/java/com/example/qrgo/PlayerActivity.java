package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qrgo.utilities.CircleTransform;
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
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
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
        ImageView imageView = findViewById(R.id.play_image_view);
        Picasso.get().load("https://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).into(imageView);

        ////////////////////////////////////ARRAY ADAPTER FOR QR CODES/////////////////////////////////////////////
        // Define your data
        String[] names = { "John", "Mary", "David" };
        int[] scores = { 900, 800, 700 };

        // Get a reference to your ListView
        ListView listView = findViewById(R.id.play_qr_listview);

        // Create an ArrayAdapter for the list items
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.qr_items, R.id.qr_name, names) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from the data array
                String name = getItem(position);
                int score = scores[position];

                // Inflate the list item layout
                View listItemView = super.getView(position, convertView, parent);

                // Get references to the views in the list item layout
                TextView nameTextView = listItemView.findViewById(R.id.qr_name);
                TextView scoreTextView = listItemView.findViewById(R.id.qr_score);
                TextView rankTextView = listItemView.findViewById(R.id.qr_rank);

                // Set the text for the views
                nameTextView.setText(name);
                scoreTextView.setText(score + " pts");
                rankTextView.setText("#" + (position + 1));

                return listItemView;
            }
        };

        // Set the adapter for the ListView
        listView.setAdapter(adapter);

        ////////////////////////////////////ARRAY ADAPTER FOR COMMENTS /////////////////////////////////////////////
        // Define your data
        String[] Hours_ago = { "3 hrs ago", "4 hrs ago", "5 hrs ago"};
        String[] comment_body= {"Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit",
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit"};

        String[] commented_on = {" 'QR 1' ", " 'QR 2' ", " 'QR 3' "};
        // Get a reference to your ListView
        ListView commentListView = findViewById(R.id.play_comment_listview);

        // Create an ArrayAdapter for the list items
        ArrayAdapter<String> comment_adapter = new ArrayAdapter<String>(this, R.layout.comment_items, R.id.commented_time, Hours_ago) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // Get the current item from the data array
                String name = "John";
                String commentBody = comment_body[position];
                String hoursAgo = Hours_ago[position];
                String commentedOn = commented_on[position];

                // Inflate the list item layout
                View listItemView = super.getView(position, convertView, parent);

                // Get references to the views in the list item layout
                TextView commentNameTextView = listItemView.findViewById(R.id.commentor_name);
                TextView commentTimeTextView = listItemView.findViewById(R.id.commented_time);
                TextView commentBodyTimeTextView = listItemView.findViewById(R.id.comment_body);
                TextView commentedOnTextView = listItemView.findViewById(R.id.commented_on);
                ImageView commentsProfilePictureImageView = listItemView.findViewById(R.id.comment_profile_picture);


                // Set the text for the views
                commentNameTextView.setText(name);
                commentTimeTextView.setText(hoursAgo);
                commentBodyTimeTextView.setText(commentBody);
                commentedOnTextView.setText(commentedOn);

                Picasso.get().load("https://i.imgur.com/DvpvklR.png").transform(new CircleTransform()).into(commentsProfilePictureImageView);
                return listItemView;
            }
        };

        // Set the adapter for the ListView
        commentListView.setAdapter(comment_adapter);
    }
}