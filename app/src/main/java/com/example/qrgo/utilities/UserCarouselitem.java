package com.example.qrgo.utilities;

public class UserCarouselitem {

    private int userImage;

    private String username;
    private String name;
    private String userScore;
    private String collectedQrCodes;

public UserCarouselitem(int userImage, String name, String username, String userScore, String collectedQrCodes) {
        this.userImage = userImage;
        this.name = name;
        this.username = username;
        this.userScore = userScore;

        this.collectedQrCodes = collectedQrCodes;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public int getUserImage() {
        return userImage;
    }
    public void setUserImage(int userImage) {
        this.userImage = userImage;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
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
