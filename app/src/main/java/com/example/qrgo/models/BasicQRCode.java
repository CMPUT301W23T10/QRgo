package com.example.qrgo.models;

/**
 * BasicQRCode represents a basic QR code object with its corresponding properties and methods.
 */
public class BasicQRCode {
    protected String firebaseId;
    protected String QRString;
    protected String humanReadableQR;
    protected int QrCodePoints;

    /**
     * Constructs a BasicQRCode object with the given parameters.
     *
     * @param firebaseId      The Firebase ID of the QR code.
     * @param QRString        The QR code string.
     * @param humanReadableQR The human readable representation of the QR code.
     * @param qrCodePoints    The points assigned to the QR code.
     */
    public BasicQRCode(String firebaseId, String QRString, String humanReadableQR, int qrCodePoints) {
        this.QRString = QRString;
        this.humanReadableQR = humanReadableQR;
        this.QrCodePoints = qrCodePoints;
        this.firebaseId = firebaseId;
    }

    /**
     * Returns the QR code string.
     *
     * @return The QR code string.
     */
    public String getQRString() {
        return QRString;
    }

    /**
     * Sets the QR code string.
     *
     * @param QRString The QR code string to set.
     */
    public void setQRString(String QRString) {
        this.QRString = QRString;
    }

    /**
     * Returns the human readable representation of the QR code.
     *
     * @return The human readable representation of the QR code.
     */
    public String getHumanReadableQR() {
        return humanReadableQR;
    }

    /**
     * Sets the human readable representation of the QR code.
     *
     * @param humanReadableQR The human readable representation to set.
     */
    public void setHumanReadableQR(String humanReadableQR) {
        this.humanReadableQR = humanReadableQR;
    }

    /**
     * Returns the points assigned to the QR code.
     *
     * @return The points assigned to the QR code.
     */
    public int getQrCodePoints() {
        return QrCodePoints;
    }

    /**
     * Sets the points assigned to the QR code.
     *
     * @param qrCodePoints The points to set.
     */
    public void setQrCodePoints(int qrCodePoints) {
        QrCodePoints = qrCodePoints;
    }

    /**
     * Returns the Firebase ID of the QR code.
     *
     * @return The Firebase ID of the QR code.
     */
    public String getFirebaseId() {
        return firebaseId;
    }

    /**
     * Sets the Firebase ID of the QR code.
     *
     * @param firebaseId The Firebase ID to set.
     */
    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}