package com.example.qrgo.UserProfileTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.TextView;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;
import com.google.firebase.firestore.FirebaseFirestore;

import org.junit.Test;


public class UserGlobalHighestRankingTest extends BaseUserProfileTest {
    @Test
    public void testHighestRanking() {
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
                "highestScore",100000000
        );
        solo.sleep(2000);
        solo.clickOnView(solo.getView(R.id.play_high_qr_head));
        solo.sleep(2000);
        solo.waitForText("#1",1,2000);

        assertEquals("#1",((TextView) solo.getCurrentActivity().findViewById(R.id.rank)).getText().toString());
    }

}
