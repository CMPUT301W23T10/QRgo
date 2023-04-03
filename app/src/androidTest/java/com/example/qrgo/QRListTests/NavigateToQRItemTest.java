package com.example.qrgo.QRListTests;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.R;

import org.junit.Test;

public class NavigateToQRItemTest extends BaseUserProfileTest {
    @Test
    public void testNavigateToQRItem() {
        signUpTestUser();
        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
        addTestQR("123456");
        solo.sleep(2000);


        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.view_pager));
        solo.waitForFragmentByTag("QRItemFragment");


        solo.waitForFragmentByTag("QRItemFragment");


    }
}
