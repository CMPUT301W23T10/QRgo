package com.example.qrgo.UserProfileTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;

public class T_LeaderboardTests extends BaseUserProfileTest {
    @Test
    public void testLeaderboardQRHiScore() {
        signUpTestUser();

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);


        // Click on the leaderboard view
        solo.clickOnView(solo.getView(R.id.user_view_all));
        solo.waitForFragmentByTag("LeaderboardFragment");

        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profiles").document(username).update(
                "totalScore", 100000000
        );


        ListView allUsersListView = (ListView) solo.getView(R.id.all_users_listview);
        assertNotNull(allUsersListView);

        solo.clickOnView(allUsersListView.getChildAt(0));

        solo.waitForActivity(PlayerActivity.class);
        solo.sleep(3000);
        assertEquals("first last",((TextView) solo.getCurrentActivity().findViewById(R.id.player_name)).getText().toString());

        }

        @Test
        public void testLeaderboardTotalHiScore(){
            signUpTestUser();

            // Wait for home activity to launch
            solo.waitForActivity(HomeActivity.class, 2000);


            // Click on the leaderboard view
            solo.clickOnView(solo.getView(R.id.user_view_all));
            solo.waitForFragmentByTag("LeaderboardFragment");

            SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
            username = sharedPreferences.getString("user", "");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Profiles").document(username).update(
                    "totalScore", 100000000
            );

            solo.clickOnView(solo.getView(R.id.most_scanned_button));

            ListView allUsersListView = (ListView) solo.getView(R.id.all_users_listview);
            assertNotNull(allUsersListView);

            solo.clickOnView(allUsersListView.getChildAt(0));

            solo.waitForActivity(PlayerActivity.class);
            solo.sleep(3000);
            assertEquals("first last",((TextView) solo.getCurrentActivity().findViewById(R.id.player_name)).getText().toString());

        }
        @Test
        public void testLeaderboardScanHiScore(){
            signUpTestUser();

            // Wait for home activity to launch
            solo.waitForActivity(HomeActivity.class, 2000);


            // Click on the leaderboard view
            solo.clickOnView(solo.getView(R.id.user_view_all));
            solo.waitForFragmentByTag("LeaderboardFragment");

            SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
            username = sharedPreferences.getString("user", "");
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Profiles").document(username).update(
                    "totalScans", 100000000
            );

            solo.clickOnView(solo.getView(R.id.most_scanned_button));

            ListView allUsersListView = (ListView) solo.getView(R.id.all_users_listview);
            assertNotNull(allUsersListView);

            solo.clickOnView(allUsersListView.getChildAt(0));

            solo.waitForActivity(PlayerActivity.class);
            solo.sleep(3000);
            assertEquals("first last",((TextView) solo.getCurrentActivity().findViewById(R.id.player_name)).getText().toString());

        }
    }
