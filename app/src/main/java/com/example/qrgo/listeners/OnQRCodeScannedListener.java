package com.example.qrgo.listeners;

/**
 * An interface that defines a callback method to be invoked when a QR code is scanned.
 */
public interface OnQRCodeScannedListener {

    /**
     * Called when a QR code is successfully scanned.
     *
     * @param success Indicates whether the scan was successful or not.
     */
    void onQRScanComplete(boolean success);
}
