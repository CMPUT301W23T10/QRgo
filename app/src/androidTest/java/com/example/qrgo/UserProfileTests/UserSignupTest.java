
package com.example.qrgo.UserProfileTests;

import static org.junit.Assert.assertTrue;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;


import org.junit.Test;


public class UserSignupTest extends BaseUserProfileTest {

    @Test
    public void testSignUpUser() {
        // Launch the MainActivity
        signUpTestUser();

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        testActionBarIsHidden();

        // Click on the profile picture
        solo.clickOnView(solo.getView(R.id.main_profile_picture));
        solo.waitForActivity(PlayerActivity.class, 2000);
        testActionBarIsHidden();

        assertTrue(solo.waitForText("first last", 1, 2000));
        assertTrue(solo.waitForText("testemail@gmail.com", 1, 2000));
        
        // Click on the close_button
        solo.clickOnView(solo.getView(R.id.close_button));
        solo.waitForActivity(HomeActivity.class, 2000);
        solo.sleep(2000);
        // Get the shared preferences object
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
    }
}
