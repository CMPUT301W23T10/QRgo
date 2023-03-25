package com.example.qrgo.listeners;

import com.example.qrgo.models.BasicPlayerProfile;

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
