package com.example.qrgo;


import static com.example.qrgo.SignupActivity.user;

import android.*;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import android.view.Window;


import com.example.qrgo.listeners.OnImeiCheckListener;
import com.example.qrgo.utilities.FirebaseConnect;

import java.util.UUID;
/**
 * MainActivity
 * This is the first activity that is called when the app is opened.
 * It checks if the user is already signed up, and if they are, it navigates to the HomeActivity.
 * If they are not signed up, it navigates to the SignupActivity.
 */
public class MainActivity extends AppCompatActivity {

    // not actually imei, but a unique id
    static String imei;
    // The shared database instance name
    static String sharedPrefdb = "qrgodb";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences sharedPreferences = getSharedPreferences(sharedPrefdb, Context.MODE_PRIVATE);
        user = sharedPreferences.getString("user", "");


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



//        System.out.println();

        // Begin Firebase CONNECTION
        FirebaseConnect db = new FirebaseConnect();

        // Get IMEI: note that this is not an actual IMEI, but rather a unique identifier of an app instance.
        imei = getUniqueID(this);

        // Check if IMEI is already in Firebase
        db.getUserManager().checkImeiExists(imei, new OnImeiCheckListener() {
            @Override
            public void onImeiCheck(boolean imeiExists) {
                if(!imeiExists){
                    // Navigate to Signup Page if not signed up
                    // Need to implement if statement for if they are not signed up
                    Intent intent_signup = new Intent(MainActivity.this, SignupActivity.class);
                    intent_signup.putExtra("imei", imei);
                    startActivity(intent_signup);

                }else{
                    // Navigate to player page directly
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
    // When BACK BUTTON is pressed, the activity on the stack is restarted (REMOVE THIS)
    @Override
    public void onRestart() {
        super.onRestart();
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



        // Begin Firebase CONNECTION
        FirebaseConnect db = new FirebaseConnect();

        // Get IMEI: note that this is not an actual IMEI, but rather a unique identifier of an app instance.
        imei = getUniqueID(this);

        // Check if IMEI is already in Firebase
        db.getUserManager().checkImeiExists(imei, new OnImeiCheckListener() {
            @Override
            public void onImeiCheck(boolean imeiExists) {
                if(!imeiExists){
                    // Navigate to Signup Page if not signed up
                    // Need to implement if statement for if they are not signed up
                    Intent intent_signup = new Intent(MainActivity.this, SignupActivity.class);
                    intent_signup.putExtra("imei", imei);
                    startActivity(intent_signup);

                }else{
                    // Navigate to player page directly
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    /**
     * get a unique ID
     * This code was taken from stackoverflow, I need to cite it properly
     * https://stackoverflow.com/questions/43415278/unique-id-for-my-android-app
     * @param context
     * @return
     */
    public static String getUniqueID(Context context){

        //lets get device Id if not available, we create and save it
        //means device Id is created once

        //if the deviceId is not null, return it
        if(imei != null){
            return imei;
        }//end

        //shared preferences
        SharedPreferences sharedPref = context.getSharedPreferences("qrgodb",context.MODE_PRIVATE);

        //lets get the device Id
        imei = sharedPref.getString("qrgodb",null);

        //if the saved device Id is null, lets create it and save it

        if(imei == null) {

            //generate new device id
            imei = generateUniqueID();

            //Shared Preference editor
            SharedPreferences.Editor sharedPrefEditor = sharedPref.edit();

            //save the device id
            sharedPrefEditor.putString("qrgodb",imei);

            //commit it
            sharedPrefEditor.commit();
        }//end if device id was null

        //return
        return imei;
    }//end get device Id


    /**
     * generateUniqueID - Generate Device Id
     * @return
     */
    public static String generateUniqueID() {

        String id = UUID.randomUUID().toString();

        return id;
    }//end method
}
