package com.example.qrgo;

import android.content.Context;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

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
        final EditText username;
        final EditText email;
        final EditText phone;

        FirebaseConnect db = new FirebaseConnect();

        mRegister = findViewById(R.id.register);

        // The users real name
        username = findViewById(R.id.username);
        email = findViewById(R.id.address);
        phone = findViewById(R.id.phone_number);





        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disable button
                mRegister.setClickable(false);
                //Retrieve sign up info
//                final String imei = getIntent().getStringExtra("imei");

                final String realName = username.getText().toString();



                // Traverse the string
                for (int i = 0; i < realName.length(); i++) {
                    if(realName.charAt(i) == ' '){
                        //DO NOT FORGET TO ENSURE UNIQUE USERNAME HERE
                        final String firstName = realName.substring(0,i);
                        final String lastName = realName.substring(i+1);
                        final String userName = lastName.charAt(0)+firstName;
                        final String contactEmail = email.getText().toString();
                        final String contactPhone = phone.getText().toString();
                        final String imei = getIntent().getStringExtra("imei");

                        //Add User
                        db.addNewUser(imei, userName, new FirebaseConnect.OnUserAddListener() {
                            @Override
                            public void onUserAdd(boolean success) {
                                //Add Profile
                                db.addNewPlayerProfile(userName, firstName, lastName, contactEmail, contactPhone, 0, 0, 0, new FirebaseConnect.OnUserProfileAddListener() {
                                    @Override
                                    public void onUserProfileAdd(boolean success) {
                                        // Clear fields on signup page
                                        username.setText("");
                                        email.setText("");
                                        phone.setText("");

                                        // Navigate to Home Activity
                                        Intent intent = new Intent(SignupActivity.this, HomeActivity.class);
                                        startActivity(intent);
                                    }
                                });
                            }
                        });

                    }
                }



            } // end of onClick
        });


    } // end of onCreate

} // end of class
