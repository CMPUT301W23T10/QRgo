package com.example.qrgo.UserProfileTests;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.qrgo.HomeActivity;
import com.example.qrgo.PlayerActivity;
import com.example.qrgo.R;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.After;
import org.junit.Test;

public class D_UserQRScansTest extends BaseUserProfileTest {
    @Test
    public void testTotalRanking() {
        signUpTestUser();

        // Wait for home activity to launch
        solo.waitForActivity(HomeActivity.class, 2000);

        addTestQR("123456");
        solo.sleep(2000);

        solo.clickOnView(solo.getView(R.id.main_profile_picture));
        solo.waitForActivity(PlayerActivity.class, 2000);

        solo.assertCurrentActivity("Expected PlayerActivity",PlayerActivity.class);
        SharedPreferences sharedPreferences = rule.getActivity().getSharedPreferences("qrgodb", Context.MODE_PRIVATE);
        username = sharedPreferences.getString("user", "");

        solo.clickOnView(solo.getView(R.id.play_qr_view_all));
        solo.sleep(2000);
        solo.waitForText("Human Re..",1,2000);
    }

    @Override
    @After
    public void cleanup() {
        if (imei.equals("") && username.equals("")) {
            Log.d("testSignUpUser", "imei or username is empty");
        } else {
            Log.d("testSignUpUser", "imei: " + imei + " username: " + username);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("Users").whereEqualTo("username", username).get().addOnCompleteListener(task -> {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.size() == 1) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    db.collection("Users").document(document.getId()).delete();
                }
            });

            db.collection("QRCodes").whereEqualTo("qrString", "123456").get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    QuerySnapshot querySnapshot = task.getResult();
                    if (querySnapshot.size() == 1) {
                        DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                        DocumentReference qrCodeRef = db.collection("QRCodes").document(document.getId());
                        qrCodeRef.delete();
                    }
                }
            });

            db.collection("Profiles").document(username).delete(); // replace with the actual username used in the tests
            solo.finishOpenedActivities();
        }
    }

}
