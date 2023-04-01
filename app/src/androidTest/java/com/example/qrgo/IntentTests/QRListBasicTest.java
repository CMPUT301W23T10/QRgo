package com.example.qrgo.IntentTests;

import static org.junit.Assert.assertTrue;

import android.widget.ImageView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRListBasicTest {

    @Rule
    public ActivityTestRule<HomeActivity> activityTestRule = new ActivityTestRule<>(HomeActivity.class);
    private Solo solo;

    @Before
    public void setUp() {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), activityTestRule.getActivity());
    }

    @After
    public void tearDown() {
        solo.finishOpenedActivities();
    }

    @Test
    public void testNavigateToQRList() {
        solo.waitForActivity(HomeActivity.class);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        ImageView qr_arrow_icon = (ImageView) solo.getView(R.id.qr_arrow_icon);
        solo.clickOnView(qr_arrow_icon);
        //assertTrue(solo.waitForActivity("PlayerActivity"));

    }


    @Test
    public void testNavigateQRListBackToHomeActivity() {
        solo.waitForActivity(HomeActivity.class);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        FloatingActionButton back_button = (FloatingActionButton) solo.getView(R.id.back_button);
        solo.clickOnView(back_button);


    }


    @Test
    public void testNavigateToQRItem() {
        solo.waitForActivity(HomeActivity.class);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.view_pager));
        solo.waitForFragmentByTag("QRItemFragment");

//        ImageView qr_arrow_icon = (ImageView) solo.getView(R.id.qr_arrow_icon);
//        solo.clickOnView(qr_arrow_icon);

        solo.waitForFragmentByTag("QRItemFragment");
    }

    @Test
    public void testDeleteQRItem() {
        solo.waitForActivity(HomeActivity.class);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        ImageView qr_delete_icon = (ImageView) solo.getView(R.id.qr_delete_icon);
        solo.clickOnView(qr_delete_icon);

        solo.waitForFragmentByTag("QRItemFragment");
    }


    @Test
    public void testQRListSort(){
        solo.waitForActivity(HomeActivity.class);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        solo.waitForFragmentByTag("QRFragment");

        //click on QR desk
        solo.clickOnView(solo.getView(R.id.user_qr_view_all));
        solo.waitForFragmentByTag("QrListviewFragment");

        FloatingActionButton sort_button = (FloatingActionButton) solo.getView(R.id.sort_button);
        solo.clickOnView(sort_button);
        solo.waitForFragmentByTag("QrListviewFragment");



    }




}
