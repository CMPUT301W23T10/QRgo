package com.example.qrgo.utilities;
public class CustomCarouselItem {
    private int qrCodeImage;
    private String qrCodeRank;
    private String qrCodeName;
    private String qrCodePoints;

    public CustomCarouselItem(int qrCodeImage, String qrCodeRank, String qrCodeName, String qrCodePoints) {
        this.qrCodeImage = qrCodeImage;
        this.qrCodeRank = qrCodeRank;
        this.qrCodeName = qrCodeName;
        this.qrCodePoints = qrCodePoints;
    }

    public int getQrCodeImage() {
        return qrCodeImage;
    }

    public void setQrCodeImage(int qrCodeImage) {
        this.qrCodeImage = qrCodeImage;
    }

    public String getQrCodeRank() {
        return qrCodeRank;
    }

    public void setQrCodeRank(String qrCodeRank) {
        this.qrCodeRank = qrCodeRank;
    }

    public String getQrCodeName() {
        return qrCodeName;
    }

    public void setQrCodeName(String qrCodeName) {
        this.qrCodeName = qrCodeName;
    }

    public String getQrCodePoints() {
        return qrCodePoints;
    }

    public void setQrCodePoints(String qrCodePoints) {
        this.qrCodePoints = qrCodePoints;
    }
}

