package com.example.qrgo;


import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
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
                                                        Date date = commentDoc.getDate("dateAndTime");
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
     Scans a QR code and updates the user's profile and the QR code document in the database accordingly.
     @param qrString The hash of the QR code to be scanned.
     @param username The username of the user scanning the QR code.
     @param latitude The latitude of the user's location when scanning the QR code. Use 181 to skip.
     @param longitude The longitude of the user's location when scanning the QR code. Use 181 to skip.
     @param photoUrl The URL of the photo taken by the user when scanning the QR code.
     @param points The number of points awarded to the user for scanning the QR code.
     @param listener A callback listener to notify when the QR code scan is complete.
     */
    public void scanQRCode(String qrString, String username, String humanReadableQR, double latitude, double longitude, String photoUrl, int points, OnQRCodeScannedListener listener) {
        // Check if a QR Code document exists with the given qrString attribute
        Query query = db.collection("QRCodes").whereEqualTo("qrString", qrString);
        query.get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                // QR Code document with the given qrString exists
                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                String qrCodeId = documentSnapshot.getId();

                List<String> scanUsers = (List<String>) documentSnapshot.get("scannedUsers");

                // The user has scanned the same code previously
                if (scanUsers.contains(username)) {
                    listener.onQRScanComplete(false);
                    return;
                }

                // Add the user's photo URL to "locationObjectPhoto"
                db.collection("QRCodes").document(qrCodeId)
                        .update("locationObjectPhoto", FieldValue.arrayUnion(photoUrl));

                // Add the user's location coordinates to the "locations" array
                if (latitude != 181 && longitude != 181) {
                    GeoPoint location = new GeoPoint(latitude, longitude);
                    db.collection("QRCodes").document(qrCodeId)
                            .update("locations", FieldValue.arrayUnion(location));
                }

                // Add the user's username to the "scannedUsers" array
                db.collection("QRCodes").document(qrCodeId)
                        .update("scannedUsers", FieldValue.arrayUnion(username));
            } else {
                // QR Code document with the given qrString does not exist, so create a new document
                Map<String, Object> data = new HashMap<>();
                data.put("humanReadableQR", humanReadableQR);
                data.put("qrString", qrString);
                data.put("scannedUsers", Arrays.asList(username));
                data.put("locationObjectPhoto", Arrays.asList(photoUrl));
                data.put("qrPoints", points);
                if (latitude != 0 && longitude != 0) {
                    data.put("locations", Arrays.asList(new GeoPoint(latitude, longitude)));
                }
                db.collection("QRCodes").add(data).addOnSuccessListener(documentReference -> {
                    String qrCodeId = documentReference.getId();

                    // Add the newly created QR Code ID to the user's "qrScans" array
                    db.collection("Profiles").document(username)
                            .update("qrScans", FieldValue.arrayUnion(qrCodeId));
                });
            }

            // Check if the points assigned is greater than the user's highestScore or lower than their lowestScore and update them accordingly
            db.collection("Profiles").document(username).get().addOnSuccessListener(documentSnapshot -> {
                int totalScore = documentSnapshot.getLong("totalScore").intValue();
                int highestScore = documentSnapshot.getLong("highestScore").intValue();
                int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                if (points > highestScore) {
                    db.collection("Profiles").document(username)
                            .update("highestScore", points);
                }
                if (points < lowestScore) {
                    db.collection("Profiles").document(username)
                            .update("lowestScore", points);
                }
                if (totalScore == 0) {
                    db.collection("Profiles").document(username)
                            .update("lowestScore", points);
                }
                // Update the user's points and totalScans
                db.collection("Profiles").document(username)
                    .update("totalScore", FieldValue.increment(points),
                            "totalScans", FieldValue.increment(1));
                listener.onQRScanComplete(true);
            });
        });
    }

    /**
     Retrieves a QRCode from the Firestore database given a QR string.
     If the QR string does not match any QRCode in the database, the listener will receive an onQRCodeNotFound() callback.
     If the QRCode is successfully retrieved, the listener will receive an onQRCodeRetrieved() callback with the retrieved QRCode.
     If the retrieval process fails, the listener will receive an onQRCodeLoadFailure() callback with the Exception that caused the failure.
     @param qrString the QR string to match with a QRCode in the database
     @param listener the listener that will receive the callbacks after the QRCode is retrieved or if there is a failure
     */
    public void getQRCode(String qrString, QRCodeListener listener) {
        Task<QuerySnapshot> docRef = db.collection("QRCodes").whereEqualTo("qrString", qrString).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    listener.onQRCodeNotFound();
                } else {
                    DocumentSnapshot documentSnapshot = queryDocumentSnapshots.getDocuments().get(0);
                    QRCode qrCode = new QRCode(documentSnapshot.getString("qrString"),
                            documentSnapshot.getString("humanReadableQR"),
                            (int) documentSnapshot.getLong("qrPoints").intValue(),
                            (List<GeoPoint>) documentSnapshot.get("locations"),
                            (List<String>) documentSnapshot.get("locationObjectPhoto"),
                            (List<String>) documentSnapshot.get("scannedUsers"),
                            (List<String>) documentSnapshot.get("comments")
                    );

                    // Scanned Users
                    List<String> scannedUsers = (List<String>) documentSnapshot.get("scannedUsers");
                    if (scannedUsers != null) {
                        List<BasicPlayerProfile> scannedPlayer = new ArrayList<>();
                        for (String username : scannedUsers) {
                            getBasicPlayerProfile(username, new OnBasicPlayerProfileLoadedListener() {
                                @Override
                                public void onBasicPlayerProfileLoaded(BasicPlayerProfile basicPlayerProfile) {
                                    scannedPlayer.add(basicPlayerProfile);
                                    if (scannedPlayer.size() == scannedUsers.size()) {
                                        qrCode.setScannedPlayer(scannedPlayer);
                                    }
                                }

                                @Override
                                public void onBasicPlayerProfileLoadFailure(Exception e) {

                                }

                            });
                        }
                    }

                    // Comments
                    List<String> comments = (List<String>) documentSnapshot.get("comments");
                    if (comments != null) {
                        List<Comment> commentList = new ArrayList<>();
                        for (String commentId : comments) {
                            getComment(commentId, new OnCommentLoadedListener() {
                                @Override
                                public void onCommentLoaded(Comment comment) {
                                    commentList.add(comment);
                                    if (commentList.size() == comments.size()) {
                                        qrCode.setComments(commentList);
                                        listener.onQRCodeRetrieved(qrCode);
                                    }
                                }

                                @Override
                                public void onCommentLoadFailure(Exception e) {

                                }
                            });
                        }
                    } else {
                        listener.onQRCodeRetrieved(qrCode);
                    }
                }
            }
        });
    }

    /**
     Get BasicPlayerProfile object for given username.
     @param username The username of the player.
     @param listener The listener to be called when the BasicPlayerProfile is loaded or load fails.
     */
     public void getBasicPlayerProfile(String username, OnBasicPlayerProfileLoadedListener listener) {
        db.collection("Profiles")
                .document(username)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, totalScore, highestScore, lowestScore);
                        listener.onBasicPlayerProfileLoaded(basicPlayerProfile);
                    } else {
                        listener.onBasicPlayerProfileLoadFailure(new Exception("Profile does not exist."));
                    }
                })
                .addOnFailureListener(e -> listener.onBasicPlayerProfileLoadFailure(e));
    }

    /**
     Get Comment object for given comment id.
     @param commentId The id of the comment.
     @param listener The listener to be called when the Comment is loaded or load fails.
     */
     public void getComment(String commentId, OnCommentLoadedListener listener) {
        db.collection("Comments")
                .document(commentId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String commentString = documentSnapshot.getString("commentText");
                        String qrCodeId = documentSnapshot.getString("commentQRCode");
                        String playerUsername = documentSnapshot.getString("commentUser");
                        Date datetime = documentSnapshot.getDate("dateAndTime");
                        Comment comment = new Comment(commentId, commentString, qrCodeId, playerUsername, datetime);
                        listener.onCommentLoaded(comment);
                    } else {
                        listener.onCommentLoadFailure(new Exception("Comment does not exist."));
                    }
                })
                .addOnFailureListener(e -> listener.onCommentLoadFailure(e));
    }

    /**
     * Listener for when a BasicPlayerProfile object has been successfully loaded from Firebase Firestore.
     */
    public interface OnBasicPlayerProfileLoadedListener {

        /**
         * Called when a BasicPlayerProfile object has been successfully loaded from Firebase Firestore.
         *
         * @param basicPlayerProfile the BasicPlayerProfile object that was loaded
         */
        void onBasicPlayerProfileLoaded(BasicPlayerProfile basicPlayerProfile);

        /**
         * Called when there was an error loading the BasicPlayerProfile object from Firebase Firestore.
         *
         * @param e the exception that occurred during loading
         */
        void onBasicPlayerProfileLoadFailure(Exception e);
    }


    /**
     * Listener for when a Comment object has been successfully loaded from Firebase Firestore.
     */
    public interface OnCommentLoadedListener {

        /**
         * Called when a Comment object has been successfully loaded from Firebase Firestore.
         *
         * @param comment the Comment object that was loaded
         */
        void onCommentLoaded(Comment comment);

        /**
         * Called when there was an error loading the Comment object from Firebase Firestore.
         *
         * @param e the exception that occurred during loading
         */
        void onCommentLoadFailure(Exception e);
    }


    /**
     * Listener for when a QRCode object has been retrieved from Firebase Firestore.
     */
    public interface QRCodeListener {

        /**
         * Called when a QRCode object has been successfully retrieved from Firebase Firestore.
         *
         * @param qrCode the QRCode object that was retrieved
         */
        void onQRCodeRetrieved(QRCode qrCode);

        /**
         * Called when a QRCode object could not be found in Firebase Firestore.
         */
        void onQRCodeNotFound();
    }



    /**
     An interface that defines a callback method to be invoked when a QR code is scanned.
     */
    public interface OnQRCodeScannedListener {

        /**
         Called when a QR code is successfully scanned.
         @param success Indicates whether the scan was successful or not.
         */
        void onQRScanComplete(boolean success);
    }


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

