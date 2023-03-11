package com.example.qrgo.utilities;

public class UserCarouselitem {
    private int userImage;
    private String userName;
    private String userScore;
    private String collectedQrCodes;

public UserCarouselitem(int userImage, String userName, String userScore, String collectedQrCodes) {
        this.userImage = userImage;
        this.userName = userName;
        this.userScore = userScore;
        this.collectedQrCodes = collectedQrCodes;
    }
    public int getUserImage() {
        return userImage;
    }
    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }
    public String getUserName() {
        return userName;
    }
    public void setUserName(String userName) {
        this.userName = userName;
    }
    public String getUserScore() {
        return userScore;
    }
    public void setUserScore(String userScore) {
        this.userScore = userScore;
    }
    public String getCollectedQrCodes() {
        return collectedQrCodes;
    }
    public void setCollectedQrCodes(String collectedQrCodes) {
        this.collectedQrCodes = collectedQrCodes;
    }

}
