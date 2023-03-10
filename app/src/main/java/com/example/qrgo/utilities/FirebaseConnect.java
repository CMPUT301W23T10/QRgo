package com.example.qrgo.utilities;


import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.Comment;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.models.QRCode;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
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

    public FirebaseConnect(FirebaseFirestore firebase) {
        this.db = firebase.getInstance();
    }
    /**
     * Constructor that initializes the Firebase Firestore database instance.
     */
    public FirebaseConnect() {
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
     * Adds a new player profile to the database.
     *
     * @param username The username of the player.
     * @param contactPhone The phone number of the player's emergency contact.
     * @param contactEmail The email address of the player's emergency contact.
     * @param totalScore The total score of the player. Pass in 0 initially.
     * @param totalScans The total scans of the player. Pass in 0 initially.
     * @param highestScore The highest score of the player. Pass in 0 initially.
     * @param lowestScore The lowest score of the player. Pass in 0 initially.
     * @param listener The listener to handle the result of the operation.
     */
    public void addNewPlayerProfile(String username, String firstName, String lastName, String contactPhone, String contactEmail,
                               int totalScore, int totalScans, int highestScore, int lowestScore, OnUserProfileAddListener listener) {
        DocumentReference docRef = db.collection("Profiles").document(username);
        Map<String, Object> data = new HashMap<>();
        data.put("firstName", firstName);
        data.put("lastName", lastName);
        data.put("contactPhone", contactPhone);
        data.put("contactEmail", contactEmail);
        data.put("totalScore", totalScore);
        data.put("totalScans", totalScans);

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
     * Deletes a player profile from the database.
     *
     * @param username The username of the player whose profile is to be deleted.
     * @param listener The listener to handle the result of the operation.
     */
    public void deletePlayerProfile(String username, OnUserProfileDeleteListener listener) {
        DocumentReference docRef = db.collection("Profiles").document(username);
        docRef.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                listener.onUserProfileDelete(true);
            } else {
                listener.onUserProfileDelete(false);
            }
        });
    }


    /**
     * Splits a list of items into batches with a maximum size.
     *
     * @param items the list of items to split into batches
     * @param batchSize the maximum number of items per batch
     * @return a list of batches, where each batch is a list of items with at most batchSize elements
     */
    private List<List<String>> batchList(List<String> items, int batchSize) {
        List<List<String>> batches = new ArrayList<>();
        for (int i = 0; i < items.size(); i += batchSize) {
            int end = Math.min(i + batchSize, items.size());
            List<String> batch = items.subList(i, end);
            batches.add(batch);
        }
        return batches;
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
                    String firstName = document.getString("firstName");
                    String lastName = document.getString("lastName");
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

                    // Split qrScans and comments into smaller lists with a maximum of 10 elements each
                    List<List<String>> qrScanBatches = batchList(qrScans, 10);
                    List<List<String>> commentBatches = batchList(comments, 10);

                    // Query the qrCodes collection for the qrScans IDs in batches
                    List<BasicQRCode> qrCodes = new ArrayList<BasicQRCode>();
                    List<Task<QuerySnapshot>> qrCodeTasks = new ArrayList<>();
                    for (List<String> qrScanBatch : qrScanBatches) {
                        qrCodeTasks.add(db.collection("QRCodes")
                                .whereIn(FieldPath.documentId(), qrScanBatch)
                                .get());
                    }

                    Tasks.whenAllComplete(qrCodeTasks).addOnCompleteListener(qrCodesTask -> {
                        if (qrCodesTask.isSuccessful()) {
                            for (Task<QuerySnapshot> qtask : qrCodeTasks) {
                                QuerySnapshot qrCodeBatch = qtask.getResult();
                                for (QueryDocumentSnapshot qrCodeDoc : qrCodeBatch) {
                                    String firebaseid = qrCodeDoc.getId();
                                    String humanReadableQR = qrCodeDoc.getString("humanReadableQR");
                                    String qrString = qrCodeDoc.getString("qrString");
                                    int qrPoints = qrCodeDoc.getLong("qrPoints").intValue();
                                    BasicQRCode qrCode = new BasicQRCode(firebaseid, qrString, humanReadableQR, qrPoints);
                                    qrCodes.add(qrCode);
                                }
                            }

                            // Query the Comments collection for the comments IDs in batches
                            List<Comment> commentList = new ArrayList<>();
                            List<Task<QuerySnapshot>> commentTasks = new ArrayList<>();
                            for (List<String> commentBatch : commentBatches) {
                                commentTasks.add(db.collection("Comments")
                                        .whereIn(FieldPath.documentId(), commentBatch)
                                        .get());
                            }

                            Tasks.whenAllComplete(commentTasks).addOnCompleteListener(commentsTask -> {
                                if (commentsTask.isSuccessful()) {
                                    for (Task<QuerySnapshot> ctask : commentTasks) {
                                        QuerySnapshot commentBatch = ctask.getResult();
                                        for (QueryDocumentSnapshot commentDoc : commentBatch) {
                                            String id = commentDoc.getId();
                                            String commentString = commentDoc.getString("commentText");
                                            String qrCodeId = commentDoc.getString("commentQRCode");
                                            String commentUser = commentDoc.getString("commentUser");
                                            String userFirstName = commentDoc.getString("userFirstName");
                                            String userLastName = commentDoc.getString("userLastName");
                                            Date date = commentDoc.getDate("dateAndTime");
                                            Comment comment = new Comment(id, commentString, qrCodeId, commentUser, userFirstName, userLastName, date);
                                            commentList.add(comment);
                                        }
                                    }

                                    // Create a PlayerProfile object with the retrieved data
                                    PlayerProfile playerProfile = new PlayerProfile(username, firstName, lastName, contactPhone, contactEmail,
                                            totalScore, highestScore, lowestScore, totalScans, qrScans, qrCodes, commentList);
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
                if (latitude != 181 && longitude != 181) {
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
                            null,
                            null
                    );

                    // Get scanned users
                    getScannedUsers(documentSnapshot, new OnScannedUsersLoadedListener() {
                        @Override
                        public void onScannedUsersLoaded(List<BasicPlayerProfile> scannedPlayers) {
                            qrCode.setScannedPlayer(scannedPlayers);

                            // Get comments
                            getComments(documentSnapshot, new OnCommentsLoadedListener() {
                                @Override
                                public void onCommentsLoaded(List<Comment> comments) {
                                    qrCode.setComments(comments);
                                    listener.onQRCodeRetrieved(qrCode);
                                }

                                @Override
                                public void onCommentsLoadFailure(Exception e) {
                                    listener.onQRCodeRetrievalFailure(e);
                                }
                            });
                        }

                        @Override
                        public void onScannedUsersLoadFailure(Exception e) {
                            listener.onQRCodeRetrievalFailure(e);
                        }
                    });
                }
            }
        });
    }

    public void getScannedUsers(DocumentSnapshot documentSnapshot, OnScannedUsersLoadedListener listener) {
        List<String> scannedUsers = (List<String>) documentSnapshot.get("scannedUsers");
        if (scannedUsers == null) {
            listener.onScannedUsersLoaded(new ArrayList<>());
            return;
        }

        List<BasicPlayerProfile> scannedPlayers = new ArrayList<BasicPlayerProfile>();
        int numScannedUsers = scannedUsers.size();
        for (String username : scannedUsers) {
            getBasicPlayerProfile(username, new OnBasicPlayerProfileLoadedListener() {
                @Override
                public void onBasicPlayerProfileLoaded(BasicPlayerProfile basicPlayerProfile) {
                    scannedPlayers.add(basicPlayerProfile);
                    if (scannedPlayers.size() == numScannedUsers) {
                        listener.onScannedUsersLoaded(scannedPlayers);
                    }
                }

                @Override
                public void onBasicPlayerProfileLoadFailure(Exception e) {
                    listener.onScannedUsersLoadFailure(e);
                }
            });
        }
    }

    public void getComments(DocumentSnapshot documentSnapshot, OnCommentsLoadedListener listener) {
        List<String> comments = (List<String>) documentSnapshot.get("comments");
        if (comments == null) {
            listener.onCommentsLoaded(new ArrayList<>());
            return;
        }

        List<Comment> commentList = new ArrayList<>();
        int numComments = comments.size();
        for (String commentId : comments) {
            getComment(commentId, new OnCommentLoadedListener() {
                @Override
                public void onCommentLoaded(Comment comment) {
                    commentList.add(comment);
                    if (commentList.size() == numComments) {
                        listener.onCommentsLoaded(commentList);
                    }
                }

                @Override
                public void onCommentLoadFailure(Exception e) {
                    // Handle error
                }
            });
        }
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
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, highestScore, lowestScore);
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
                        String userFirstName = documentSnapshot.getString("userFirstName");
                        String userLastName = documentSnapshot.getString("userLastName");
                        Date datetime = documentSnapshot.getDate("dateAndTime");
                        Comment comment = new Comment(commentId, commentString, qrCodeId, playerUsername, userFirstName, userLastName, datetime);
                        listener.onCommentLoaded(comment);
                    } else {
                        listener.onCommentLoadFailure(new Exception("Comment does not exist."));
                    }
                })
                .addOnFailureListener(e -> listener.onCommentLoadFailure(e));
    }


    /**
     * Search for users based on their username, first name, and last name.
     *
     * @param searchQuery The search query to use.
     * @param listener The listener to call when the search is complete.
     */
    public void searchUsers(String searchQuery, OnUserSearchListener listener) {
        db.collection("Profiles")
                .whereGreaterThanOrEqualTo("firstName", searchQuery)
                .whereLessThanOrEqualTo("firstName", searchQuery + "\uf8ff")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<BasicPlayerProfile> users = new ArrayList<>();
                    for (DocumentSnapshot document : queryDocumentSnapshots) {
                        String username = document.getId();
                        String firstName = document.getString("firstName");
                        String lastName = document.getString("lastName");
                        int totalScore = document.getLong("totalScore").intValue();
                        int highestScore = document.getLong("highestScore").intValue();
                        int lowestScore = document.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, highestScore, lowestScore);
                        users.add(basicPlayerProfile);
                    }
                    listener.onUserSearchComplete(users);

                })
                .addOnFailureListener(e -> listener.onUserSearchFailure(e));
    }




    /**
     A function that retrieves a list of players sorted by their highest score.
     @param listener The listener to be notified of the result of the function.
     */
    public void getPlayersSortedByHighestScore(OnPlayerListLoadedListener listener) {
        db.collection("Profiles")
                .orderBy("highestScore", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<BasicPlayerProfile> playerList = new ArrayList<BasicPlayerProfile>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String username = documentSnapshot.getId();
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, highestScore, lowestScore);
                        playerList.add(basicPlayerProfile);
                    }
                    listener.onPlayerListLoaded(playerList);
                })
                .addOnFailureListener(e -> listener.onPlayerListLoadFailure(e));
    }

    /**

     A function that retrieves a list of players sorted by their lowest score.
     @param listener The listener to be notified of the result of the function.
     */
    public void getPlayersSortedByLowestScore(OnPlayerListLoadedListener listener) {
        db.collection("Profiles")
                .orderBy("lowestScore", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<BasicPlayerProfile> playerList = new ArrayList<BasicPlayerProfile>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String username = documentSnapshot.getId();
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, highestScore, lowestScore);
                        playerList.add(basicPlayerProfile);
                    }
                    listener.onPlayerListLoaded(playerList);
                })
                .addOnFailureListener(e -> listener.onPlayerListLoadFailure(e));
    }

    /**
     A function that retrieves a list of players sorted by their total score.
     @param listener The listener to be notified of the result of the function.
     */
    public void getPlayersSortedByTotalScore(OnPlayerListLoadedListener listener) {
        db.collection("Profiles")
                .orderBy("totalScore", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<BasicPlayerProfile> playerList = new ArrayList<BasicPlayerProfile>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String username = documentSnapshot.getId();
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, highestScore, lowestScore);
                        playerList.add(basicPlayerProfile);
                    }
                    listener.onPlayerListLoaded(playerList);
                })
                .addOnFailureListener(e -> listener.onPlayerListLoadFailure(e));
    }

    /**
     A function that retrieves a list of players sorted by their total score.
     @param listener The listener to be notified of the result of the function.
     */
    public void getPlayersSortedByTotalScans(OnPlayerListLoadedListener listener) {
        db.collection("Profiles")
                .orderBy("totalScans", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<BasicPlayerProfile> playerList = new ArrayList<BasicPlayerProfile>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        String username = documentSnapshot.getId();
                        String firstName = documentSnapshot.getString("firstName");
                        String lastName = documentSnapshot.getString("lastName");
                        int totalScore = documentSnapshot.getLong("totalScore").intValue();
                        int highestScore = documentSnapshot.getLong("highestScore").intValue();
                        int lowestScore = documentSnapshot.getLong("lowestScore").intValue();
                        BasicPlayerProfile basicPlayerProfile = new BasicPlayerProfile(username, firstName, lastName, totalScore, highestScore, lowestScore);
                        playerList.add(basicPlayerProfile);
                    }
                    listener.onPlayerListLoaded(playerList);
                })
                .addOnFailureListener(e -> listener.onPlayerListLoadFailure(e));
    }


    /**
     A function that retrieves a list of qr codes sorted by its points.
     @param listener The listener to be notified of the result of the function.
     */
    public void getQrCodesSortedByPoints(OnQrListLoadedListener listener) {
        db.collection("QRCodes")
                .orderBy("qrPoints", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    List<BasicQRCode> qrCodes = new ArrayList<BasicQRCode>();
                    for (DocumentSnapshot qrCodeDoc : querySnapshot.getDocuments()) {
                        String firebaseid = qrCodeDoc.getId();
                        String humanReadableQR = qrCodeDoc.getString("humanReadableQR");
                        String qrString = qrCodeDoc.getString("qrString");
                        int qrPoints = qrCodeDoc.getLong("qrPoints").intValue();
                        BasicQRCode qrCode = new BasicQRCode(firebaseid, qrString, humanReadableQR, qrPoints);
                        qrCodes.add(qrCode);
                    }
                    listener.onQrListLoaded(qrCodes);
                })
                .addOnFailureListener(e -> listener.onQrListLoadFailure(e));
    }

    /**
     A function that retrieves a list of all qr coordinates.
     @param listener The listener to be notified of the result of the function.
     */
    public void getAllQrCoordinates(OnCoordinatesListLoadedListener listener) {
        db.collection("QRCodes")
                .get()
                .addOnSuccessListener(querySnapshot -> {
                    Map<String, List<List<Double>>> mapped_coordinates = new HashMap<String, List<List<Double>>>();
                    for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                        List<GeoPoint> locations = (List<GeoPoint>) documentSnapshot.get("locations");
                        if (locations != null) {
                            List<List<Double>> coordinates = new ArrayList<List<Double>>();
                            for (GeoPoint location : locations) {
                                List<Double> coordinate = new ArrayList<Double>();
                                coordinate.add(location.getLatitude());
                                coordinate.add(location.getLongitude());
                                coordinates.add(coordinate);
                            }

                        mapped_coordinates.put((String) documentSnapshot.get("qrString").toString(), coordinates);
                        }
                    }
                    listener.onCoordinatesListLoaded(mapped_coordinates);
                })
                .addOnFailureListener(e -> listener.onCoordinatesListLoadFailure(e));
    }


    public interface OnUserDeleteListener {
        void onUserDelete(boolean success);
    }

    public interface OnUserProfileDeleteListener {
        void onUserProfileDelete(boolean success);
    }


    public interface OnScannedUsersLoadedListener {
        void onScannedUsersLoaded(List<BasicPlayerProfile> scannedUsers);
        void onScannedUsersLoadFailure(Exception e);
    }


    public interface OnCommentsLoadedListener {
        void onCommentsLoaded(List<Comment> comments);
        void onCommentsLoadFailure(Exception e);
    }

    /**

     An interface for listening to the result of the coordinate list functions
     */
    public interface OnCoordinatesListLoadedListener {
        /**

         Invoked when the function successfully retrieves a list of coordinates.
         @param mapped_coordinates A map of qr string to a list of its coordinates.
         */
        void onCoordinatesListLoaded(Map<String, List<List<Double>>> mapped_coordinates);

        /**
         Invoked when the function fails to retrieve the list of coordinates.
         @param e The exception that caused the failure.
         */
        void onCoordinatesListLoadFailure(Exception e);
    }
    /**

     An interface for listening to the result of the sorted QR functions
     */
    public interface OnQrListLoadedListener {
        /**

         Invoked when the function successfully retrieves a list of QR sorted by its points.
         @param qrcodes The list of sorted qrcodes.
         */
        void onQrListLoaded(List<BasicQRCode> qrcodes);

        /**
         Invoked when the function fails to retrieve the list of sorted QR.
         @param e The exception that caused the failure.
         */
        void onQrListLoadFailure(Exception e);
    }
    /**

     An interface for listening to the result of the sorted Player functions
     */
    public interface OnPlayerListLoadedListener {
        /**

         Invoked when the function successfully retrieves a list of players sorted by their highest score.
         @param playerList The list of sorted players.
         */
        void onPlayerListLoaded(List<BasicPlayerProfile> playerList);

        /**
         Invoked when the function fails to retrieve the list of sorted players.
         @param e The exception that caused the failure.
         */
        void onPlayerListLoadFailure(Exception e);
    }

    /**
     * Interface definition for a callback to be invoked when a user search is complete.
     */
    public interface OnUserSearchListener {
        /**
         * Called when a user search is complete and at least one user is found.
         *
         * @param users The list of users that match the search criteria.
         */
        void onUserSearchComplete(List<BasicPlayerProfile> users);

        /**
         * Called when a user search is complete and no users are found.
         */
        void onUserSearchFailure(Exception e);
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

        void onQRCodeRetrievalFailure(Exception e);
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

    /**
     * Interface for callbacks when a username check is performed.
     */
    public interface OnUsernameCheckListener {
        /**
         * Called when a username check is performed.
         *
         * @param usernameExists whether the IMEI exists or not
         */
        void onUsernameCheck(boolean usernameExists);
    }


}
