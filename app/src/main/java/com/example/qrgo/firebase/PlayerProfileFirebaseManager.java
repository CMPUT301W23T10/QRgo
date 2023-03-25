package com.example.qrgo.firebase;

import com.example.qrgo.listeners.OnBasicPlayerProfileLoadedListener;
import com.example.qrgo.listeners.OnPlayerListLoadedListener;
import com.example.qrgo.listeners.OnPlayerProfileGetListener;
import com.example.qrgo.listeners.OnUserProfileAddListener;
import com.example.qrgo.listeners.OnUserProfileDeleteListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.Comment;
import com.example.qrgo.models.PlayerProfile;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldPath;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerProfileFirebaseManager extends BaseFirebaseConnectManager{
    public PlayerProfileFirebaseManager() {
        super();
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

}
