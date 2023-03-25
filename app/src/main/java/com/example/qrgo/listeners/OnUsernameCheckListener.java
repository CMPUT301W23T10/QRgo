package com.example.qrgo.listeners;

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
