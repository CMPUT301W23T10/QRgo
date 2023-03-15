package com.example.qrgo;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.utilities.BasicUserArrayAdapter;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardFragment extends Fragment {

    public LeaderboardFragment() {
        // Required empty public constructor
    }
    FloatingActionButton back;
    FloatingActionButton highScoreQR;
    FloatingActionButton mostScannedQR;
    FloatingActionButton totalHighScore;
    FloatingActionButton highScoreLocation;
    TextView users_subtitle;
    ListView all_users_listview;
    FirebaseConnect fb = new FirebaseConnect();


    private void toggleFabs(FloatingActionButton activeFab) {
        FloatingActionButton[] fabs = {highScoreQR, mostScannedQR, totalHighScore, highScoreLocation};
        for (FloatingActionButton fab : fabs) {
            if (fab == activeFab) {

                // Toggle the active fab
                fab.setImageTintList(ColorStateList.valueOf(Color.parseColor("#FF28262C")));
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                users_subtitle.setText(fab.getContentDescription());
                // If the active fab is highScoreQR, load the high score list
                if (activeFab == highScoreQR){
                    fb.getPlayersSortedByHighestScore(
                            new FirebaseConnect.OnPlayerListLoadedListener (){
                                @Override
                                public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                                    // convert the list to an array
                                    ArrayList<BasicPlayerProfile> playerArrayList = new ArrayList<>(playerList);
                                    BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerArrayList, "highScore");
                                    all_users_listview.setAdapter(userAdapter);
                                }
                                @Override
                                public void onPlayerListLoadFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to load players", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                } else if (activeFab == totalHighScore) {
                    fb.getPlayersSortedByTotalScore(
                            new FirebaseConnect.OnPlayerListLoadedListener (){
                                @Override
                                public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                                    // convert the list to an array
                                    ArrayList<BasicPlayerProfile> playerArrayList = new ArrayList<>(playerList);
                                    BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerArrayList, "totalScore");
                                    all_users_listview.setAdapter(userAdapter);
                                }
                                @Override
                                public void onPlayerListLoadFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to load players", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                } else if (activeFab == mostScannedQR) {
                    fb.getPlayersSortedByTotalScans(
                            new FirebaseConnect.OnPlayerListLoadedListener (){
                                @Override
                                public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                                    // convert the list to an array
                                    ArrayList<BasicPlayerProfile> playerArrayList = new ArrayList<>(playerList);
                                    BasicUserArrayAdapter userAdapter = new BasicUserArrayAdapter(requireActivity(), playerArrayList, "totalScore");
                                    all_users_listview.setAdapter(userAdapter);
                                }
                                @Override
                                public void onPlayerListLoadFailure(Exception e) {
                                    Toast.makeText(requireActivity(), "Failed to load players", Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                }
            } else {
                // Untoggle other fabs
                fab.setImageTintList(ColorStateList.valueOf(Color.WHITE));
                fab.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#FF28262C")));
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }
        View rootView = inflater.inflate(R.layout.fragment_leaderboard_highscore, container, false);
         back = rootView.findViewById(R.id.back_button);
         highScoreQR = rootView.findViewById(R.id.high_score_button);
         users_subtitle = rootView.findViewById(R.id.users_subtitle);
         mostScannedQR = rootView.findViewById(R.id.most_scanned_button);
         totalHighScore = rootView.findViewById(R.id.total_score_button);
         highScoreLocation = rootView.findViewById(R.id.location_score_button);
        all_users_listview = rootView.findViewById(R.id.all_users_listview);

        // Set highScoreQR as the default active FAB
        toggleFabs(highScoreQR);


        highScoreQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(highScoreQR);
            }
        });

        mostScannedQR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(mostScannedQR);
            }
        });

        totalHighScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(totalHighScore);
            }
        });

        highScoreLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleFabs(highScoreLocation);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove the topmost fragment from the back stack
                getActivity().getSupportFragmentManager().popBackStack();
                // Create a new intent to navigate to the HomeActivity
                Intent intent = new Intent(getActivity(), HomeActivity.class);
                // Clear the activity stack so that becomes the new root activity
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                // Start the HomeActivity and finish the current activity
                startActivity(intent);
                getActivity().finish();
            }
        });



            return rootView;
    }

}