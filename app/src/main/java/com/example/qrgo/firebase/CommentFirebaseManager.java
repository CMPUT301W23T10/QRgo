package com.example.qrgo.firebase;

import com.example.qrgo.listeners.OnCommentAddListener;
import com.example.qrgo.listeners.OnCommentLoadedListener;
import com.example.qrgo.models.Comment;
import com.example.qrgo.utilities.FirebaseConnect;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CommentFirebaseManager extends BaseFirebaseConnectManager {

    public CommentFirebaseManager() {
        super();
    }

    /**
     * Get Comment object for given comment id.
     *
     * @param commentId The id of the comment.
     * @param listener  The listener to be called when the Comment is loaded or load fails.
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
     * Adds a new comment to a QR code.
     *
     * @param commentText   The text content of the comment.
     * @param username      The username of the user who wrote the comment.
     * @param qrString      The hash of the QR code that the comment is being added to.
     * @param userFirstName The first name of the user who wrote the comment.
     * @param userLastName  The last name of the user who wrote the comment.
     * @param listener      An OnCommentAddListener object that will be notified when the comment is added.
     */
    public void addComment(String commentText, String username, String qrString, String userFirstName, String userLastName, OnCommentAddListener listener) {
        DocumentReference newCommentRef = db.collection("Comments").document();
        String commentId = newCommentRef.getId();
        Date dateAndTime = new Date();

        Map<String, Object> data = new HashMap<>();
        data.put("commentText", commentText);
        data.put("commentQRCode", qrString);
        data.put("commentUser", username);
        data.put("userFirstName", userFirstName);
        data.put("userLastName", userLastName);
        data.put("dateAndTime", dateAndTime);

        newCommentRef.set(data).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                db.collection("QRCodes").whereEqualTo("qrString", qrString).get().addOnCompleteListener(qrTask -> {
                    if (qrTask.isSuccessful()) {
                        QuerySnapshot querySnapshot = qrTask.getResult();
                        if (querySnapshot.size() == 1) {
                            DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                            DocumentReference qrCodeRef = db.collection("QRCodes").document(document.getId());
                            qrCodeRef.update("comments",  FieldValue.arrayUnion(commentId)).addOnCompleteListener(qtask -> {
                                if (qtask.isSuccessful()) {
                                    db.collection("Profiles").document(username).get().addOnCompleteListener(profileTask -> {
                                        if (profileTask.isSuccessful()) {
                                            DocumentSnapshot profileDocument = profileTask.getResult();
                                            DocumentReference profileRef = db.collection("Profiles").document(username);
                                            profileRef.update("comments",FieldValue.arrayUnion(commentId)).addOnCompleteListener(ptask -> {
                                                if (ptask.isSuccessful()) {
                                                    listener.onCommentAdd(true);
                                                } else {
                                                    listener.onCommentAdd(false);
                                                }
                                            });
                                        } else {
                                            listener.onCommentAdd(false);
                                        }
                                    });
                                } else {
                                    listener.onCommentAdd(false);
                                }
                            });
                        } else {
                            listener.onCommentAdd(false);
                        }
                    } else {
                        listener.onCommentAdd(false);
                    }
                });
            } else {
                listener.onCommentAdd(false);
            }
        });
    }
}

