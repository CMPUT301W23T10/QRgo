package com.example.qrgo.IntentTests;

import static org.junit.Assert.assertNotNull;

import android.widget.ListView;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.R;
import com.robotium.solo.Solo;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class SearchFragmentTest {

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
    public void testSearchFragment() {
        solo.waitForActivity(HomeActivity.class);
        solo.assertCurrentActivity("Expected HomeActivity", HomeActivity.class);

        // Add code to navigate to the SearchFragment if it's not the initial fragment displayed
        // For example, if you need to click a button to navigate to the SearchFragment:
        solo.clickOnView(solo.getView(R.id.call_search_fragment));

        solo.waitForView(R.id.call_search_fragment);



        // Test search functionality
        testSearchUser();
    }

    @Test
    public void testSearchUser() {
        // tests when something is not there
        solo.enterText(0, "testUser");
        solo.sleep(2000); // Wait for search results to load
        ListView userList = (ListView) solo.getView(R.id.user_list);
        assertNotNull("User list not found", userList);

    }
}
