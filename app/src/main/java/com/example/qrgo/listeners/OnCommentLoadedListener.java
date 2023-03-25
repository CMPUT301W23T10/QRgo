package com.example.qrgo.listeners;

import com.example.qrgo.models.Comment;

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
