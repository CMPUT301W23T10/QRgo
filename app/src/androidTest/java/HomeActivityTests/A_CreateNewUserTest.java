package HomeActivityTests;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.qrgo.MainActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
@RunWith(AndroidJUnit4.class)
public class A_CreateNewUserTest {

    private Solo solo;
    @Rule
    public ActivityTestRule<MainActivity> rule =
            new ActivityTestRule<>(MainActivity.class, true, true);

    /**
     * Runs before all tests and creates solo instance.
     *
     * @throws Exception
     */
    @Before
    public void setUp() throws Exception {
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    /**
     * Gets the Activity
     *
     * @throws Exception
     */
    @Test
    public void start() throws Exception {
        Activity activity = rule.getActivity();
    }

    /**
     * Test to check if the shared preferences are correctly retrieved and the user data is obtained.
     */
    @Test
    public void testSharedPrefUser() {

        // Set up the test environment
        MainActivity activity = rule.getActivity();
        SharedPreferences sharedPreferences = activity.getSharedPreferences("sharedPrefdb", Context.MODE_PRIVATE);

        // Set up the expected output
        String expectedUser = "testUser";
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("user", expectedUser);
        editor.apply();

        // Get the actual output
        String actualUser = sharedPreferences.getString("user", "");

        // Verify the output
        assert (expectedUser.equals(actualUser));
    }
    /**
     * Test to ACTION_BAR is hidden
     */
    @Test
    public void testActionBarIsHidden() {
        Activity activity = rule.getActivity();
        ActionBar actionBar = activity.getActionBar();
        assert (actionBar == null);
    }



}




