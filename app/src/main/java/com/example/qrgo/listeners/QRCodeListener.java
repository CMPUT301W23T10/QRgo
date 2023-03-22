package com.example.qrgo.listeners;

import com.example.qrgo.models.QRCode;

/**
 * Listener for when a QRCode object has been retrieved from Firebase Firestore.
 */
public interface QRCodeListener {

    /**
     * Called when a QRCode object has been successfully retrieved from Firebase Firestore.
     *
     * @param qrCode the QRCode object that was retrieved
     */
    void onQRCodeRetrieved(QRCode qrCode);

    /**
     * Called when a QRCode object could not be found in Firebase Firestore.
     */
    void onQRCodeNotFound();

    void onQRCodeRetrievalFailure(Exception e);
}
