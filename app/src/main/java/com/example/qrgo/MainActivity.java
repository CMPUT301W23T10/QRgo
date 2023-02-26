package com.example.qrgo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseConnect chk = new FirebaseConnect();


//        chk.checkImeiExists("IMEITEST1", imeiExists -> {
//            // Handle the result here
//            if (imeiExists) {
//                System.out.println("IMEI exists");
//            } else {
//                System.out.println("IMEI does not exists");
//            }
//        });

//        chk.addNewUser("123456789012345", "john_doe", success -> {
//            // Handle the result here
//            if (success) {
//                System.out.println("User added successfully");
//            } else {
//                System.out.println("Failed to add user");
//            }
//        });

       chk.addNewUserProfile("john_doe", "+1234567890", "john_doe@example.com", 100,
                200, 50, success -> {
                    // Handle the result here
                    if (success) {
                        System.out.println("User profile added successfully");
                    } else {
                        System.out.println("Failed to add user profile");
                    }
                });

    }
}