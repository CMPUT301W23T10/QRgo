package com.example.qrgo.firebase;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.qrgo.listeners.OnBasicPlayerProfileLoadedListener;
import com.example.qrgo.listeners.OnCommentLoadedListener;
import com.example.qrgo.listeners.OnCommentsLoadedListener;
import com.example.qrgo.listeners.OnCoordinatesListLoadedListener;
import com.example.qrgo.listeners.OnQRCodeScannedListener;
import com.example.qrgo.listeners.OnQRCodeUploadListener;
import com.example.qrgo.listeners.OnQrListLoadedListener;
import com.example.qrgo.listeners.OnScannedUsersLoadedListener;
import com.example.qrgo.listeners.OnUserDeleteFromQRCodeListener;
import com.example.qrgo.listeners.QRCodeListener;
import com.example.qrgo.models.BasicPlayerProfile;
import com.example.qrgo.models.BasicQRCode;
import com.example.qrgo.models.Comment;
import com.example.qrgo.models.QRCode;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.j256.ormlite.stmt.query.In;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QRCodeFirebaseManager extends BaseFirebaseConnectManager{
    protected final FirebaseStorage st;
    public QRCodeFirebaseManager() {
        super();
        st = FirebaseStorage.getInstance();
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
    public void scanQRCode(String qrString, String username, String humanReadableQR, double latitude, double longitude, String photoUrl, int points, OnQRCodeScannedListener listener, ArrayList<Integer> featureList) {
        // Check if a QR Code document exists with the given qrString attribute
        Query query = db.collection("QRCodes").whereEqualTo("qrString", qrString);
        query.get().addOnSuccessListener(querySnapshot -> {
            if (!querySnapshot.isEmpty()) {
                // QR Code document with the given qrString exists
                DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                String qrCodeId = documentSnapshot.getId();

                List<String> scanUsers = (List<String>) documentSnapshot.get("scannedUsers");
                // Allow USERS to update pictures
                // Add the user's photo URL to "locationObjectPhoto"
                if(photoUrl != "None" && photoUrl != null && photoUrl != "") {
                    db.collection("QRCodes").document(qrCodeId)
                            .update("locationObjectPhoto", FieldValue.arrayUnion(photoUrl));
                }

                // The user has scanned the same code previously
                if (scanUsers.contains(username)) {
                    listener.onQRScanComplete(false);
                    return;
                }
                // Add the user's location coordinates to the "locations" array
                if (latitude != 181 && longitude != 181) {
                    GeoPoint location = new GeoPoint(latitude, longitude);
                    db.collection("QRCodes").document(qrCodeId)
                            .update("locations", FieldValue.arrayUnion(location));
                }
                // Add the user's username to the "scannedUsers" array
                db.collection("QRCodes").document(qrCodeId)
                        .update("scannedUsers", FieldValue.arrayUnion(username));

                db.collection("Profiles").document(username)
                        .update("qrScans", FieldValue.arrayUnion(qrCodeId));
            } else {
                // QR Code document with the given qrString does not exist, so create a new document
                Map<String, Object> data = new HashMap<>();
                data.put("humanReadableQR", humanReadableQR);
                data.put("qrString", qrString);
                data.put("scannedUsers", Arrays.asList(username));
                data.put("qrPoints", points);
                data.put("featureList", featureList);
                if (photoUrl != "None") {
                    data.put("locationObjectPhoto", Arrays.asList(photoUrl));
                }
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
    // This functions uploads a file to Firebase Storage and returns the download URL
    public void uploadAndRetrieveDownloadUrl(Uri file, String qrString, OnQRCodeUploadListener listener) {
        StorageReference locationPhotosRef = st.getReference().child("LocationPhotos/" + qrString + "/" + file.getLastPathSegment());
        Task<Uri> urlTask = locationPhotosRef.putFile(file).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return locationPhotosRef.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    listener.onQRCodeUploadSuccess(task.getResult().toString());
                } else {
                }
            }
        });
    }

    /**
     * Deletes a user's username from a QR code's scannedUsers array in the database.
     *
     * @param qrString The string value of the QR code.
     * @param username The username of the user to be deleted.
     * @param listener The listener to handle the result of the operation.
     */
    public void deleteUserFromQRCode(String qrString, String username, OnUserDeleteFromQRCodeListener listener) {
        CollectionReference qrCodesRef = db.collection("QRCodes");
        CollectionReference profilesRef = db.collection("Profiles");
        qrCodesRef.whereEqualTo("qrString", qrString).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                QuerySnapshot querySnapshot = task.getResult();
                if (querySnapshot.size() == 1) {
                    DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                    DocumentReference qrCodeRef = qrCodesRef.document(document.getId());
                    ArrayList<String> scannedUsers = (ArrayList<String>) document.get("scannedUsers");
                    String qrCodeId = document.getId();
                    int qrPoints = document.getLong("qrPoints").intValue();
                    if (scannedUsers != null && scannedUsers.contains(username)) {
                        scannedUsers.remove(username);
                        qrCodeRef.update("scannedUsers", scannedUsers).addOnCompleteListener(updateTask -> {

                            if (updateTask.isSuccessful()) {
                                profilesRef.document(username).get().addOnCompleteListener(profileTask -> {
                                    if (profileTask.isSuccessful()) {
                                        DocumentSnapshot profileDocument = profileTask.getResult();
                                        DocumentReference profileRef = profilesRef.document(username);
                                        ArrayList<String> qrScans = (ArrayList<String>) profileDocument.get("qrScans");
                                        int totalScore = profileDocument.getLong("totalScore").intValue();
                                        if (qrScans.contains(qrCodeId)) {
                                            qrScans.remove(qrCodeId);
                                            totalScore -= qrPoints;
                                            profileRef.update("qrScans", qrScans, "totalScore", totalScore).addOnCompleteListener(updateProfileTask -> {
                                                if (updateProfileTask.isSuccessful()) {
                                                    listener.onUserDeleteFromQRCode(true);
                                                } else {
                                                    listener.onUserDeleteFromQRCode(false);
                                                }
                                            });
                                        } else {
                                            listener.onUserDeleteFromQRCode(false);
                                        }
                                    } else {
                                        listener.onUserDeleteFromQRCode(false);
                                    }
                                });
                            } else {
                                listener.onUserDeleteFromQRCode(false);
                            }
                        });
                    } else {
                        listener.onUserDeleteFromQRCode(false);
                    }
                } else {
                    listener.onUserDeleteFromQRCode(false);
                }
            } else {
                listener.onUserDeleteFromQRCode(false);
            }
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
                    ArrayList<Integer> featureList = new ArrayList<Integer>();
                    for (Long feature : (ArrayList<Long>) documentSnapshot.get("featureList")) {
                        featureList.add(feature.intValue());
                    }
                    QRCode qrCode = new QRCode(documentSnapshot.getString("qrString"),
                            documentSnapshot.getString("humanReadableQR"),
                            (int) documentSnapshot.getLong("qrPoints").intValue(),
                            (List<GeoPoint>) documentSnapshot.get("locations"),
                            (List<String>) documentSnapshot.get("locationObjectPhoto"),
                            null,
                            null,
                            featureList
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
            PlayerProfileFirebaseManager mgr =
                    new PlayerProfileFirebaseManager();
            mgr.getBasicPlayerProfile(username, new OnBasicPlayerProfileLoadedListener() {
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
            CommentFirebaseManager mgr = new CommentFirebaseManager();
            mgr.getComment(commentId, new OnCommentLoadedListener() {
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



}
