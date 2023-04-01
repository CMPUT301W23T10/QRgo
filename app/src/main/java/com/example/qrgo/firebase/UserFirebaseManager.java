package com.example.qrgo.firebase;

import com.example.qrgo.listeners.OnImeiCheckListener;
import com.example.qrgo.listeners.OnUserAddListener;
import com.example.qrgo.listeners.OnUserDeleteListener;
import com.example.qrgo.listeners.OnUserSearchListener;
import com.example.qrgo.listeners.OnUsernameCheckListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserFirebaseManager extends BaseFirebaseConnectManager{
    public UserFirebaseManager() {
        super();
    }

    /**
     * Checks if a user with the given IMEI exists in the database.
     *
     * @param imei The IMEI to check for.
     * @param listener The listener to handle the result of the operation.
     */
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


    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username The username to check for.
     * @param listener The listener to handle the result of the operation.
     */
    public void checkUsernameExists(String username, OnUsernameCheckListener listener) {
        DocumentReference docRef = db.collection("Profiles").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    listener.onUsernameCheck(true);
                } else {
                    listener.onUsernameCheck(false);
                }
            } else {
                listener.onUsernameCheck(false);
            }
        });
    }

    /**
     * Adds a new user to the database.
     *
     * @param imei The IMEI of the new user.
     * @param username The username of the new user.
     * @param listener The listener to handle the result of the operation.
     */
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
    /**
     * Deletes a user from the database.
     *
     * @param imei The IMEI of the user to be deleted.
     * @param listener The listener to handle the result of the operation.
     */
    public void deleteUser(String imei, OnUserDeleteListener listener) {
        DocumentReference docRef = db.collection("Users").document(imei);
        docRef.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onUserDelete(true);
            } else {
                listener.onUserDelete(false);
            }
        });
    }

    /**
     * Search for users based on their username, first name, and last name.
     *
     * @param searchQuery The search query to use.
     * @param listener The listener to call when the search is complete.
     */

    public void searchUsers(String searchQuery, OnUserSearchListener listener) {

        db.collection("Users")

                .whereGreaterThanOrEqualTo("username", searchQuery)
                .whereLessThanOrEqualTo("username", searchQuery + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String username = document.getString("username");
                        onUserNameFound(username, listener);
                    }


                })
                .addOnFailureListener(e -> listener.onUserSearchFailure(e));
    }

    public void onUserNameFound(String username, OnUserSearchListener listener) {
        db.collection("Profiles")
                .document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {

                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        int totalScans = documentSnapshot.getLong("totalScans").intValue();
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, totalScans, highestScore, lowestScore);
                        listener.onUserSearchComplete(Collections.singletonList(basicPlayerProfile));
                    } else {
                        listener.onUserSearchComplete(Collections.emptyList());
                    }
                })
                .addOnFailureListener(e -> listener.onUserSearchFailure(e));
    }


}




