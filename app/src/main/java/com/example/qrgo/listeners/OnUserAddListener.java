package com.example.qrgo.listeners;

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
