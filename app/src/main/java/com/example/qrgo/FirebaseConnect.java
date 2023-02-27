package com.example.qrgo;


import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 The FirebaseConnect class provides methods to connect to and interact with the Firebase Firestore database.
 It supports adding new users, checking if a user already exists, and retrieving player profiles.
 It uses listeners to handle results and errors from database operations.
 */
public class FirebaseConnect {

    /**
     * The instance of the Firebase Firestore database.
     */
    FirebaseFirestore db;

    /**
     * Constructor that initializes the Firebase Firestore database instance.
     */
    FirebaseConnect() {
        db = FirebaseFirestore.getInstance();
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
     * Adds a new player profile to the database.
     *
     * @param username The username of the player.
     * @param contactPhone The phone number of the player's emergency contact.
     * @param contactEmail The email address of the player's emergency contact.
     * @param totalScore The total score of the player. Pass in 0 initially.
     * @param highestScore The highest score of the player. Pass in 0 initially.
     * @param lowestScore The lowest score of the player. Pass in 0 initially.
     * @param listener The listener to handle the result of the operation.
     */
    public void addNewPlayerProfile(String username, String contactPhone, String contactEmail,
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

    /**
     * Retrieves the player profile for the specified username from the Firestore database and constructs a
     * {@link PlayerProfile} object containing the profile data along with associated QR codes and comments.
     * Calls the {@link OnPlayerProfileGetListener#onPlayerProfileGet(PlayerProfile)} callback method with the
     * resulting player profile object. If the username is not found in the database, calls the
     * {@link OnPlayerProfileGetListener#onPlayerProfileGet(PlayerProfile)} callback method with a null parameter.
     *
     * @param username the username of the player whose profile is to be retrieved
     * @param listener the callback listener to receive the player profile object or null if not found
     */
    public void getPlayerProfile(String username, OnPlayerProfileGetListener listener) {
        DocumentReference docRef = db.collection("Profiles").document(username);
        docRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    String contactPhone = document.getString("contactPhone");
                    String contactEmail = document.getString("contactEmail");
                    int totalScore = document.getLong("totalScore").intValue();
                    int highestScore = document.getLong("highestScore").intValue();
                    int lowestScore = document.getLong("lowestScore").intValue();
                    int totalScans = document.getLong("totalScans").intValue();
                    List<String> qrScans = document.contains("qrScans") ?
                            (List<String>) document.get("qrScans") : new ArrayList<>();
                    List<String> comments = document.contains("comments") ?
                            (List<String>) document.get("comments") : new ArrayList<>();

                    // Query the qrCodes collection for the qrScans IDs
                    db.collection("qrCodes")
                            .whereIn(FieldPath.documentId(), qrScans)
                            .get()
                            .addOnCompleteListener(qrCodesTask -> {
                                if (qrCodesTask.isSuccessful()) {
                                    List<BasicQRCode> qrCodes = new ArrayList<BasicQRCode>();
                                    for (QueryDocumentSnapshot qrCodeDoc : qrCodesTask.getResult()) {
                                        String firebaseid = qrCodeDoc.getId();
                                        String humanReadableQR = qrCodeDoc.getString("humanReadableQR");
                                        String qrString = qrCodeDoc.getString("qrString");
                                        String qrPoints = qrCodeDoc.getString("qrPoints");
                                        BasicQRCode qrCode = new BasicQRCode(firebaseid,humanReadableQR, qrString, qrPoints);
                                        qrCodes.add(qrCode);
                                    }

                                    db.collection("comments")
                                            .whereIn(FieldPath.documentId(), comments)
                                            .get()
                                            .addOnCompleteListener(commentsTask -> {
                                                if (commentsTask.isSuccessful()) {
                                                    List<Comment> commentList = new ArrayList<>();
                                                    for (QueryDocumentSnapshot commentDoc : commentsTask.getResult()) {
                                                        String id = commentDoc.getId();
                                                        String commentString = commentDoc.getString("commentText");
                                                        String qrCodeId = commentDoc.getString("commentQRCode");
                                                        String commentUser = commentDoc.getString("commentUser");
                                                        String date = commentDoc.getString("dateAndTime");
                                                        Comment comment = new Comment(id, commentString,qrCodeId, commentUser, date);

                                                        commentList.add(comment);
                                                    }

                                                    // Create a PlayerProfile object with the retrieved data
                                                    PlayerProfile playerProfile = new PlayerProfile(username, contactPhone, contactEmail,
                                                            totalScore, highestScore, lowestScore, totalScans, qrScans, comments, qrCodes, commentList);
                                                    listener.onPlayerProfileGet(playerProfile);
                                                } else {
                                                    listener.onPlayerProfileGet(null);
                                                }
                                            });
                                } else {
                                    listener.onPlayerProfileGet(null);
                                }
                            });
                } else {
                    listener.onPlayerProfileGet(null);
                }
            } else {
                listener.onPlayerProfileGet(null);
            }
        });
    }

    /**
     * Interface for callbacks when a player profile is retrieved.
     */
    /**
     * Interface for callbacks when a player profile is retrieved.
     */
    public interface OnPlayerProfileGetListener {
        /**
         * Called when a player profile is retrieved.
         *
         * @param userProfile the retrieved player profile
         */
        void onPlayerProfileGet(PlayerProfile userProfile);
    }

    /**
     * Interface for callbacks when a user profile is added.
     */
    public interface OnUserProfileAddListener {
        /**
         * Called when a user profile is added.
         *
         * @param success whether the operation was successful or not
         */
        void onUserProfileAdd(boolean success);
    }

    /**
     * Interface for callbacks when a user is added.
     */
    public interface OnUserAddListener {
        /**
         * Called when a user is added.
         *
         * @param success whether the operation was successful or not
         */
        void onUserAdd(boolean success);
    }

    /**
     * Interface for callbacks when an IMEI check is performed.
     */
    public interface OnImeiCheckListener {
        /**
         * Called when an IMEI check is performed.
         *
         * @param imeiExists whether the IMEI exists or not
         */
        void onImeiCheck(boolean imeiExists);
    }


}

