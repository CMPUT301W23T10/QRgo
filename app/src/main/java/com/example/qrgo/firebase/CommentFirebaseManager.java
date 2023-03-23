package com.example.qrgo.firebase;

import com.example.qrgo.listeners.OnCommentLoadedListener;
import com.example.qrgo.models.Comment;
import com.example.qrgo.utilities.FirebaseConnect;

import java.util.Date;

public class CommentFirebaseManager extends BaseFirebaseConnectManager{

    public CommentFirebaseManager() {
        super();
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

}
