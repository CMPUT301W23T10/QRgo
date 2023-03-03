package com.example.qrgo;

public class BasicQRCode {
    protected String firebaseId;
    protected String QRString;
    protected String humanReadableQR;
    protected String QrCodePoints;


    public BasicQRCode(String firebaseId, String QRString, String humanReadableQR, String qrCodePoints) {
        this.QRString = QRString;
        this.humanReadableQR = humanReadableQR;
        QrCodePoints = qrCodePoints;
        this.firebaseId = firebaseId;
    }

    public String getQRString() {
        return QRString;
    }

    public void setQRString(String QRString) {
        this.QRString = QRString;
    }

    public String getHumanReadableQR() {
        return humanReadableQR;
    }

    public void setHumanReadableQR(String humanReadableQR) {
        this.humanReadableQR = humanReadableQR;
    }

    public String getQrCodePoints() {
        return QrCodePoints;
    }

    public void setQrCodePoints(String qrCodePoints) {
        QrCodePoints = qrCodePoints;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }
}