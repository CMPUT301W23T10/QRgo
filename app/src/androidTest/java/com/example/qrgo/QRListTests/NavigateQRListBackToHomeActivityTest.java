package com.example.qrgo.QRListTests;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.R;

import org.junit.After;
import org.junit.Test;

public class NavigateQRListBackToHomeActivityTest extends BaseUserProfileTest {
    @Test
    public void testNavigateQRListBackToHomeActivity() {
        signUpTestUser();
        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
        addTestQR("123456");
        solo.sleep(2000);


        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");
        solo.clickOnView(solo.getView(R.id.back_button));
    }
    @Override
    @After
    public void cleanup() {
        deleteUser(username);
        deleteQrCode(username,"123456");
        solo.finishOpenedActivities();

    }
}
