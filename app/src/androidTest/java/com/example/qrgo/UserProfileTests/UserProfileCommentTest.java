package com.example.qrgo.UserProfileTests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;

import org.junit.After;
import org.junit.Test;

public class UserProfileCommentTest extends BaseUserProfileTest {
    @Test
    public void testTotalRanking() {
        signUpTestUser();

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);

        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");

        addTestQR("123456");
        addCommentToQr("123456","Hello World!");
        solo.sleep(2000);

        solo.clickOnView(solo.getView(R.id.main_profile_picture));
        solo.waitForActivity(PlayerActivity.class, 2000);

        solo.assertCurrentActivity("Expected PlayerActivity",PlayerActivity.class);


        solo.clickOnView(solo.getView(R.id.play_comment_view_all));
        solo.sleep(2000);
        solo.waitForText("Hello World!",1,2000);
    }

    @Override
    @After
    public void cleanup() {
        deleteUser(username);
        deleteQrCode(username, "123456");
        deleteComment("123456");
        solo.finishOpenedActivities();
    }
}
