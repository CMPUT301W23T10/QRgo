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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderbaordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderbaordFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ArrayList<BasicPlayerProfile> dataList;

    private ListView leaderboardList;

    public LeaderbaordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LeaderbaordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LeaderbaordFragment newInstance(String param1, String param2) {
        LeaderbaordFragment fragment = new LeaderbaordFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        leaderboardList = rootView.findViewById(R.id.all_users_list_view);
        dataList = new ArrayList<>();

        FirebaseConnect fb = new FirebaseConnect();

        FirebaseConnect.OnPlayerListLoadedListener listener = new FirebaseConnect.OnPlayerListLoadedListener() {



            @Override
            public void onPlayerListLoaded(List<BasicPlayerProfile> playerList) {
                for (BasicPlayerProfile user : playerList) {
                    dataList.add(user);
                    // print out the total score of each user and their name
                    Log.d("LeaderboardFragment", user.getFirstName() + " " + user.getTotalScore());
                }

                LeaderboardListAdapter leaderboardListAdapter = new LeaderboardListAdapter(getContext(), dataList);
                leaderboardList.setAdapter(leaderboardListAdapter);
            }

            @Override
            public void onPlayerListLoadFailure(Exception e) {

            }
        };


        fb.getPlayersSortedByTotalScore(listener);




        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderbaord_blank, container, false);
    }
}