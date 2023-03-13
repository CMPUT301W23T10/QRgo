package com.example.qrgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;
import java.util.List;


public class LeaderbaordFragment extends Fragment {

    private ListView leaderboardList;
    private ArrayList<BasicPlayerProfile> dataList;
    private LeaderboardListAdapter leaderboardListAdapter;
    public LeaderbaordFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("LeaderboardFragment", "onCreateView: ");
        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        // initialize the leaderboard ListView and dataList
        leaderboardList = rootView.findViewById(R.id.all_users_list_view);
        dataList = new ArrayList<>();

        // initiate firebase connection for fragment
        FirebaseConnect fb = new FirebaseConnect();
        FirebaseConnect.OnPlayerListLoadedListener listener = new FirebaseConnect.OnPlayerListLoadedListener() {
            @Override
            public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                for (BasicPlayerProfile user : playerList) {
                    dataList.add(user);
                    // print out the total score of each user and their name
                    Log.d("LeaderboardFragment", user.getFirstName() + " " + user.getTotalScore());
                }

                LeaderboardListAdapter leaderboardListAdapter = new LeaderboardListAdapter(getActivity(), dataList);
                leaderboardList.setAdapter(leaderboardListAdapter);
            }

            @Override
            public void onPlayerListLoadFailure(Exception e) {

            }
        };
        // perform query search
        fb.getPlayersSortedByTotalScore(listener);
        // Inflate the layout for this fragment
        return rootView;
    }
}