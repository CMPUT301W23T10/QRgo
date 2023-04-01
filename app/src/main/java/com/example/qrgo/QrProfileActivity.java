package com.example.qrgo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.listeners.OnCommentAddListener;
import com.example.qrgo.listeners.QRCodeListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.Comment;
import com.example.qrgo.models.QRCode;
import com.example.qrgo.utilities.BasicCommentArrayAdapter;
import com.example.qrgo.utilities.CircleTransform;
import com.example.qrgo.utilities.FirebaseConnect;
import com.example.qrgo.utilities.ImageViewController;
import com.example.qrgo.utilities.RoundedSquareTransform;
import com.example.qrgo.utilities.UserCarouselAdapter;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class QrProfileActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_profile);
        // find username in the shared preferences with key user
        SharedPreferences sharedPreferences = getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        String user = sharedPreferences.getString("user", "");
        String firstName = sharedPreferences.getString("firstName", "");
        String LastName = sharedPreferences.getString("lastName", "");
        Log.d("QrProfileActivity", "User " + user + " " + firstName + " " + LastName);
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
        ListView commentListView = findViewById(R.id.qr_comment_listview);


        firebaseconnect.getQRCodeManager().getQRCode(qr_code_id, new QRCodeListener() {

            @Override
            public void onQRCodeRetrieved(QRCode qrCode) {
                LinearLayout qr_image_view_container = findViewById(R.id.qr_image_view_container);
                // Set the background color of the qr code based on the rarity
                if (qrCode.getHumanReadableQR().contains("(C)")) {
                    qr_image_view_container.setBackgroundResource(R.drawable.common_rounded_corner);
                }
                else if (qrCode.getHumanReadableQR().contains("(R)")) {
                    qr_image_view_container.setBackgroundResource(R.drawable.rare_rounded_corner);

                } else if (qrCode.getHumanReadableQR().contains("(E)")) {
                    qr_image_view_container.setBackgroundResource(R.drawable.epic_rounded_corner);

                } else {
                    qr_image_view_container.setBackgroundResource(R.drawable.legendary_rounded_corner);
                }
                ImageView imageView = findViewById(R.id.qr_image_view);
                Picasso.get()
                        .load(R.drawable.demo_qr_image)
                        .transform(new RoundedSquareTransform(100))
                        .into(imageView);

                TextView qrCodeName = findViewById(R.id.qr_name);
                qrCodeName.setText(qrCode.getHumanReadableQR());

                List<BasicPlayerProfile> scanned_list = qrCode.getScannedPlayer();
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
                    QRListFragment qrFragment = new QRListFragment();
                    ArrayList<BasicPlayerProfile> qrCodeArrayList = new ArrayList<>(scanned_list);
                    // Pass qrCodeList as a parameter to the fragment
                    qrFragment.setQrCodeList(qrCodeArrayList);
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                            .add(R.id.qr_fragment_container, qrFragment)
                            .addToBackStack(null)
                            .commit();
                });


                TextView qr_score = findViewById(R.id.qr_score);
                qr_score.setText(Integer.toString(qrCode.getQrCodePoints()) + " pts");


//                TextView qr_comment_head = findViewById(R.id.qr_comment_head);
//                qr_comment_head.setText("Comments (" + qrCode.getComments().size() + ")");

                // Set up the comment list view
                List<Comment> commentList = (List<Comment>) qrCode.getComments();
                ArrayList<Comment> commentArrayList = new ArrayList<>(commentList);
                // Limit the number of comments to 3
                if (commentArrayList.size() >= 3) {
                    commentArrayList = new ArrayList<>(commentArrayList.subList(0, 3));
                }

                BasicCommentArrayAdapter commentAdapter = new BasicCommentArrayAdapter(QrProfileActivity.this, R.layout.comment_items, commentArrayList);
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
                TextView qr_comment_head = findViewById(R.id.qr_comment_head);
                qr_comment_head.setText("Comments (" + qrCode.getComments().size() + ")");
                // Set up the view all button for COMMENTS
                TextView qr_comment_view_all = findViewById(R.id.qr_comment_view_all);
                qr_comment_view_all.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Call your fragment here
                        CommentListviewFragment commentFragment = new CommentListviewFragment();
                        ArrayList<Comment> commentArrayList = new ArrayList<>(commentList);
                        // Pass qrCodeList as a parameter to the fragment
                        commentFragment.setCommentList(commentArrayList);

                        getSupportFragmentManager().beginTransaction()
                                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                                .add(R.id.qr_fragment_container, commentFragment)
                                .addToBackStack(null)
                                .commit();
                    }
                });

                // Set current user's name for the heading above comment editText
                TextView qr_user_name = findViewById(R.id.qr_user_name);

                qr_user_name.setText(user + " (You)");

                // Set up the picture for the current user
                ImageView qr_user_profile_picture = findViewById(R.id.qr_user_profile_picture);

                // Load user image into the ImageView
                ImageViewController imageViewController = new ImageViewController();
                imageViewController.setImage(firstName,qr_user_profile_picture);
                ImageView qr_send_comment = findViewById(R.id.qr_send_comment);
                qr_send_comment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        // Get the comment from the editText
                        EditText qr_comment_box = findViewById(R.id.qr_comment_box);
                        String comment = qr_comment_box.getText().toString();
                        firebaseconnect.getCommentManager().addComment(comment, user, qrCode.getQrString(), firstName, LastName, new OnCommentAddListener() {
                            @Override
                            public void onCommentAdd(boolean success) {
                                if (success) {
                                    Toast.makeText(QrProfileActivity.this, "Comment added", Toast.LENGTH_SHORT).show();
                                    // Refresh the activity
                                    Intent intent = getIntent();
                                    finish();
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(QrProfileActivity.this, "Comment not added", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                });
                // Remove the progress bar and show the page
                LinearLayout progressBar = findViewById(R.id.qr_progressBar);
                RelativeLayout qr_profile = findViewById(R.id.qr_profile);

                progressBar.setVisibility(View.INVISIBLE);
                qr_profile.setVisibility(View.VISIBLE);
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

        // GeoLocation button
        ImageView locationButton = findViewById(R.id.location_button);
        locationButton.setOnClickListener(v -> {
            Intent intent1 = new Intent(QrProfileActivity.this, GeoLocationActivity.class);
            intent1.putExtra("qrCode", qr_code_id);
            startActivity(intent1);
        });
    }

}
