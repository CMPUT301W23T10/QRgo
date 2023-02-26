package com.example.qrgo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class FirebaseConnect {
    FirebaseFirestore db;
    boolean boolResult = false;

    FirebaseConnect() {
        db = FirebaseFirestore.getInstance();
    }

    public void checkImeiExists(String imei, OnImeiCheckListener listener) {
        DocumentReference docRef = db.collection("Users").document(imei);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    listener.onImeiCheck(true);
                } else {
                    listener.onImeiCheck(false);
                }
            } else {
                listener.onImeiCheck(false);
            }
        });
    }

    public void addNewUser(String imei, String username, OnUserAddListener listener) {
        DocumentReference docRef = db.collection("Users").document(imei);
        Map<String, Object> data = new HashMap<>();
        data.put("username", username);
        docRef.set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onUserAdd(true);
            } else {
                listener.onUserAdd(false);
            }
        });
    }


    public void addNewUserProfile(String username, String contactPhone, String contactEmail,
                               int totalScore, int highestScore, int lowestScore, OnUserProfileAddListener listener) {
        DocumentReference docRef = db.collection("Profiles").document(username);
        Map<String, Object> data = new HashMap<>();
        data.put("contactPhone", contactPhone);
        data.put("contactEmail", contactEmail);
        data.put("totalScore", totalScore);
        data.put("highestScore", highestScore);
        data.put("lowestScore", lowestScore);
        data.put("qrScans", new ArrayList<String>());
        data.put("comments", new ArrayList<String>());
        docRef.set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onUserProfileAdd(true);
            } else {
                listener.onUserProfileAdd(false);
            }
        });
    }

    public interface OnUserProfileAddListener {
        void onUserProfileAdd(boolean success);
    }

    public interface OnUserAddListener {
        void onUserAdd(boolean success);
    }

    public interface OnImeiCheckListener {
        void onImeiCheck(boolean imeiExists);
    }

}

