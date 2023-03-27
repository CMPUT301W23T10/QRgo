package com.example.qrgo;

import static com.example.qrgo.MainActivity.imei;
import static com.example.qrgo.MainActivity.sharedPrefdb;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.qrgo.R;
import com.example.qrgo.listeners.OnUserAddListener;
import com.example.qrgo.listeners.OnUserProfileAddListener;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.UUID;

/**
 * This class is where the user will sign up for some QRGO action!
 */
public class SignupActivity extends AppCompatActivity {

    static String userID;
    static String user;
    static String userFirstLast;
    String sharedDB = sharedPrefdb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        SharedPreferences sharedPreferences = getSharedPreferences(sharedDB, Context.MODE_PRIVATE);
        userID = imei.substring(0, 4);

        // Hide the action bar
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowTitleEnabled(false);
            actionBar.hide();
        }

        // Hide the status bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.transparent));
            window.setNavigationBarColor(getResources().getColor(R.color.transparent));
            window.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));
        }

        //Set up variables
        final String TAG = "Sample";
        Button mRegister;
        //First name
        final EditText firstNameEntry;
        //Last name
        final EditText lastNameEntry;
        //Email
        final EditText emailEntry;
        //Phone
        final EditText phoneEntry;

        FirebaseConnect db = new FirebaseConnect();

        mRegister = findViewById(R.id.register);

        // The users first name
        firstNameEntry = findViewById(R.id.firstNameEntry);
        //last name
        lastNameEntry = findViewById(R.id.lastNameEntry);
        //email
        emailEntry = findViewById(R.id.emailEntry);
        //phone
        phoneEntry = findViewById(R.id.phoneEntry);



        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disable button
                mRegister.setClickable(false);
                //Retrieve sign up info
                final String firstName = firstNameEntry.getText().toString();
                final String lastName = lastNameEntry.getText().toString();
                final String userName = lastName.charAt(0) + firstName + "#" + userID;
                final String contactEmail = emailEntry.getText().toString();
                final String contactPhone = phoneEntry.getText().toString();
                final String imei = getIntent().getStringExtra("imei");
                user = userName;

                // Check if email is in valid format
                if (!isValidEmail(contactEmail)) {
                    emailEntry.setError("Invalid email address");
                    mRegister.setClickable(true);
                    return;
                }
                // Check if phone is in valid format
                if (!isValidPhoneNumber(contactPhone)) {
                    phoneEntry.setError("Invalid phone number");
                    mRegister.setClickable(true);
                    return;
                }

                // Save username (first last) to shared preferences, to be used in QRProfileActivity
                userFirstLast = firstName + " " + lastName;
                //Add User

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("user", user);
                editor.commit();




                db.getUserManager().addNewUser(imei, userName, new OnUserAddListener() {
                    @Override
                    public void onUserAdd(boolean success) {
                        //Add Profile
                        db.getPlayerProfileManager().addNewPlayerProfile(userName, firstName, lastName, contactEmail, contactPhone, 0, 0, 0, 0, new OnUserProfileAddListener() {
                            @Override

                            public void onUserProfileAdd(boolean success) {
                                // Clear fields on signup page
                                firstNameEntry.setText("");
                                lastNameEntry.setText("");
                                emailEntry.setText("");
                                phoneEntry.setText("");
                                // Navigate to Home Activity
                                Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                startActivity(intent);

                            }
                        });
                    }
                });





            } // end of onClick
        });


    } // end of onCreate

    private boolean isValidEmail(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        // Regular expression to match a phone number in any format
        String regex = "^(\\+\\d{1,3})?\\s*\\(?\\d{3}\\)?[-.\\s]?\\d{3}[-.\\s]?\\d{4}$";
        return phoneNumber.matches(regex);
    }
} // end of class
