package com.example.qrgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

public class GlobalScoresFragment extends Fragment {
    static String rankHeading = "";
    static String totalScoreHeading = "";
    static String currentScoreHeading = "";
    static String rank = "";
    static String totalScore = "";
    static String currentScore = "";
    public void setRankHeading(String rankHeading) {
        this.rankHeading = rankHeading;
    }
    public void setTotalScoreHeading(String totalScoreHeading) {
        this.totalScoreHeading = totalScoreHeading;
    }
    public void setCurrentScoreHeading(String currentScoreHeading) {
        this.currentScoreHeading = currentScoreHeading;
    }
    public void setRank(String rank) {
        this.rank = rank;
    }
    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }
    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }
    public GlobalScoresFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (container != null) {
            container.removeAllViews();
        }

        View rootView = inflater.inflate(R.layout.fragment_global_scores, container, false);
        // Set headings
        TextView rank_heading = rootView.findViewById(R.id.rank_heading);
        rank_heading.setText(rankHeading);
        TextView total_score_heading = rootView.findViewById(R.id.total_score_heading);
        total_score_heading.setText(totalScoreHeading);
        TextView current_score_heading = rootView.findViewById(R.id.current_score_heading);
        current_score_heading.setText(currentScoreHeading);
        // Set scores

        TextView rank = rootView.findViewById(R.id.rank);
        rank.setText(this.rank);
        TextView total_score = rootView.findViewById(R.id.total_score);
        total_score.setText(this.totalScore);
        TextView current_score = rootView.findViewById(R.id.current_score);
        current_score.setText(this.currentScore);

        // Exit fragment
        FrameLayout exit_fragment = rootView.findViewById(R.id.exit_fragment);
        exit_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove fragment with animation
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right).remove(GlobalScoresFragment.this).commit();
            }
        });
        return rootView;
    }
}