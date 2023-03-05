package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qrgo.utilities.BasicCommentArrayAdapter;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.utilities.BasicQrArrayAdapter;
import com.example.qrgo.models.Comment;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.CircleTransform;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

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


        ListView listView = findViewById(R.id.play_qr_listview);


        ListView commentListView = findViewById(R.id.play_comment_listview);

// Create an ArrayAdapter for the list items
        FirebaseConnect firebaseConnect = new FirebaseConnect();
        // GETTING THE PLAYER PROFILE FOR THE USER
        firebaseConnect.getPlayerProfile("testUser", new FirebaseConnect.OnPlayerProfileGetListener(){
            @Override
            public void onPlayerProfileGet(PlayerProfile playerProfile) {
                if (playerProfile != null) {
                    // Do something with the player profile object

                    Log.d("FirebaseConnect", "Player profile has " + playerProfile.getComments() + " as comment profiles");
                    Log.d("FirebaseConnect", "Player profile has " + playerProfile.getFirstName() + " as username");

                    TextView usernameTextView = findViewById(R.id.play_username);
                    usernameTextView.setText(playerProfile.getUsername());

                    TextView totalScoreTextView = findViewById(R.id.play_total_score);
                    totalScoreTextView.setText(String.valueOf(playerProfile.getTotalScore()));

                    TextView highestScoreTextView = findViewById(R.id.play_high_qr);
                    highestScoreTextView.setText(String.valueOf(playerProfile.getHighestScore()));

                    TextView lowestScoreTextView = findViewById(R.id.play_low_qr);
                    lowestScoreTextView.setText(String.valueOf(playerProfile.getLowestScore()));

                    TextView play_qr_head = findViewById(R.id.play_qr_head);
                    play_qr_head.setText("QR Codes (" + playerProfile.getQrCodeBasicProfiles().size() + ")");

                    TextView play_comment_head = findViewById(R.id.play_comment_head);
                    play_comment_head.setText("Comments (" + playerProfile.getComments().size() + ")");

                    TextView emailTextView = findViewById(R.id.play_email);
                    emailTextView.setText(playerProfile.getContactEmail());

                    LinearLayout progressBar = findViewById(R.id.progressBar);
                    RelativeLayout playerProfileLayout = findViewById(R.id.player_profile);

                    TextView playerName = findViewById(R.id.player_name);
                    playerName.setText(playerProfile.getFirstName() + " " + playerProfile.getLastName());

                    progressBar.setVisibility(View.INVISIBLE);
                    playerProfileLayout.setVisibility(View.VISIBLE);

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


                    // Set up the QR code list view
                    List<BasicQRCode> qrCodeList = playerProfile.getQrCodeBasicProfiles();
                    ArrayList<BasicQRCode> qrCodeArrayList = new ArrayList<>(qrCodeList);
                    // Limit the number of QR codes to 3 AND SORT IT IN ( not DONE DESCENDING ORDER)
                    if (qrCodeArrayList.size() >= 3) {
                        qrCodeArrayList = new ArrayList<>(qrCodeArrayList.subList(0, 3));
                    }

                    BasicQrArrayAdapter qrAdapter = new BasicQrArrayAdapter( PlayerActivity.this, qrCodeArrayList);
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


                    // Set up the comment list view
                    List<Comment> commentList = (List<Comment>) playerProfile.getComments();
                    ArrayList<Comment> commentArrayList = new ArrayList<>(commentList);
                    // Limit the number of comments to 3
                    if (commentArrayList.size() >= 3) {
                        commentArrayList = new ArrayList<>(commentArrayList.subList(0, 3));
                    }

                    BasicCommentArrayAdapter commentAdapter = new BasicCommentArrayAdapter(PlayerActivity.this, R.layout.comment_items, commentArrayList);
                    commentListView.setAdapter(commentAdapter);
                    int comment_height = 0;
                    if (commentArrayList.size() == 3) {
                        comment_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 324, getResources().getDisplayMetrics());
                    } else if (commentArrayList.size() == 2) {
                        comment_height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 224, getResources().getDisplayMetrics());
                    } else {
                        comment_height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    }
                    commentListView.getLayoutParams().height = (int) comment_height;






                    // Set up the view all button for QR CODES
                    TextView play_qr_view_all = findViewById(R.id.play_qr_view_all);
                    play_qr_view_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your fragment here
                            QrListview  qrFragment = new QrListview();
                            ArrayList<BasicQRCode> qrCodeArrayList = new ArrayList<>(qrCodeList);
                            // Pass qrCodeList as a parameter to the fragment
                            qrFragment.setQrCodeList(qrCodeArrayList);

                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .add(R.id.fragment_container, qrFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });


                    // Set up the view all button for COMMENTS
                    TextView play_comment_view_all = findViewById(R.id.play_comment_view_all);
                    play_comment_view_all.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            // Call your fragment here
                            CommentListview  commentFragment = new CommentListview();
                            ArrayList<Comment> commentArrayList = new ArrayList<>(commentList);
                            // Pass qrCodeList as a parameter to the fragment
                            commentFragment.setCommentList(commentArrayList);

                            getSupportFragmentManager().beginTransaction()
                                    .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                    .add(R.id.fragment_container, commentFragment)
                                    .addToBackStack(null)
                                    .commit();
                        }
                    });



                } else {
                    // Handle the case where the username is not found in the database
                    Log.d("FirebaseConnect", "Player profile not found for testUser");
                }
            }
        });
//          code to add new user
//        firebaseConnect.scanQRCode("7822", "testUser", "HUHAE", 22.5, 24.5, "yahoo.cad",
//                500, new FirebaseConnect.OnQRCodeScannedListener(){
//            @Override
//            public void onQRScanComplete(boolean success) {
//                if (success) {
//                    // Do something with the player profile object
//                    Log.d("FirebaseConnect", "QR scan complete");
//                } else {
//                    // Handle the case where the username is not found in the database
//                    Log.d("FirebaseConnect", "QR scan failed");
//                }
//            }
//        });

    }
}