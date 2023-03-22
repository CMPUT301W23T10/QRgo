package com.example.qrgo.listeners;

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
