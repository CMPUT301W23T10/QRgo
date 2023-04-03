package com.example.qrgo.QRListTests;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.qrgo.BaseUserProfileTest;
import com.example.qrgo.HomeActivity;
import com.example.qrgo.QrProfileActivity;
import com.example.qrgo.R;

import org.junit.After;
import org.junit.Test;

public class NavigateToQRListTest extends BaseUserProfileTest {
    @Test
    public void testNavigateToQRList() {
        signUpTestUser();
        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");
        addTestQR("123456");
        solo.sleep(4000);

        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        solo.clickOnView(solo.getView(R.id.back_button));
        solo.waitForActivity(HomeActivity.class);

        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        solo.clickOnView(solo.getView(R.id.qr_arrow_icon));
        solo.waitForActivity(QrProfileActivity.class);
        solo.assertCurrentActivity("QrProfileActivity", QrProfileActivity.class);
        solo.waitForText("Human Readable QR",1,2000);
    }
    @Override
    @After
    public void cleanup() {
        deleteUser(username);
        deleteQrCode(username,"123456");
        solo.finishOpenedActivities();

    }
}
