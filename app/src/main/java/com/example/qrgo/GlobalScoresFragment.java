package com.example.qrgo;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
/**

 A fragment that displays global scores.
 */
public class GlobalScoresFragment extends Fragment {
    static String rankHeading = "";
    static String totalScoreHeading = "";
    static String currentScoreHeading = "";
    static String rank = "";
    static String totalScore = "";
    static String currentScore = "";
    /**

     Sets the rank heading.
     @param rankHeading The rank heading to set.
     */
    public void setRankHeading(String rankHeading) {
        this.rankHeading = rankHeading;
    }
    /**

     Sets the total score heading.
     @param totalScoreHeading The total score heading to set.
     */
    public void setTotalScoreHeading(String totalScoreHeading) {
        this.totalScoreHeading = totalScoreHeading;
    }
    /**

     Sets the current score heading.
     @param currentScoreHeading The current score heading to set.
     */
    public void setCurrentScoreHeading(String currentScoreHeading) {
        this.currentScoreHeading = currentScoreHeading;
    }
    /**

     Sets the rank.
     @param rank The rank to set.
     */
    public void setRank(String rank) {
        this.rank = rank;
    }
    /**

     Sets the total score.
     @param totalScore The total score to set.
     */
    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }
    /**

     Sets the current score.
     @param currentScore The current score to set.
     */
    public void setCurrentScore(String currentScore) {
        this.currentScore = currentScore;
    }

    public GlobalScoresFragment() {
        // Required empty public constructor
    }
    /**

     Creates a new instance of the fragment.
     @return The new instance of the fragment.
     */
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