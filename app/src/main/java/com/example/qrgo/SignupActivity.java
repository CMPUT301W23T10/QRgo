package com.example.qrgo;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

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
        FirebaseFirestore db;

        mRegister = findViewById(R.id.register);
        username = findViewById(R.id.username);
        email = findViewById(R.id.address);
        phone = findViewById(R.id.phone_number);

        db = FirebaseFirestore.getInstance();

        //Get to the choppa!!!!!
        final CollectionReference collectionUsers = db.collection("Users");

        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Retrieve sign up info
                final String userName = username.getText().toString();
                final String emailAddress = email.getText().toString();
                final String phoneNum = phone.getText().toString();

                HashMap<String, String> data = new HashMap<>();

                if (userName.length()>0 && emailAddress.length()>0 && phoneNum.length()>0){
                    data.put("Email Address", emailAddress);
                    data.put("Phone Number", phoneNum);
                }
                collectionUsers
                        .document(userName)
                        .set(data)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                // These are a method which gets executed when the task is succeeded
                                Log.d(TAG, "Data has been added successfully!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.d(TAG, "Data could not be added!" + e.toString());
                            }
                        });

                //Snapshot listener function potentially  needing implementation
                collectionUsers.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        //Prime programming real estate
                    }
                });
                username.setText("");
                email.setText("");
                phone.setText("");

                // Navigate to Player Activity
                Intent intent = new Intent(SignupActivity.this, PlayerActivity.class);
                startActivity(intent);
            } // end of onClick
        });


    } // end of onCreate
} // end of class
