package com.example.qrgo.listeners;

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
