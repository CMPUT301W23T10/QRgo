package com.example.qrgo.UserProfileTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserGlobalTotalRankingTest extends BaseUserProfileTest {
    @Test
    public void testTotalRanking() {
        signUpTestUser();

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);


        // Click on the profile picture
        solo.clickOnView(solo.getView(R.id.main_profile_picture));
        solo.waitForActivity(PlayerActivity.class, 2000);

        solo.assertCurrentActivity("Expected PlayerActivity",PlayerActivity.class);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("Profiles").document(username).update(
                "totalScore",100000000
        );
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.play_total_score_head));
        solo.sleep(2000);
        solo.waitForText("#1",1,2000);

        assertEquals("#1",((TextView) solo.getCurrentActivity().findViewById(R.id.rank)).getText().toString());
    }

}
