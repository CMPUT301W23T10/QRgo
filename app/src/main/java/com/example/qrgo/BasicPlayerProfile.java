package com.example.qrgo;

public class BasicPlayerProfile {
    protected String username;
    protected Photo profilePhoto;;
    protected int totalScore;
    protected int higehstScore;
    protected int lowestScore;

    public BasicPlayerProfile(String username, int totalScore, int higehstScore, int lowestScore) {
        this.username = username;
        this.totalScore = totalScore;
        this.higehstScore = higehstScore;
        this.lowestScore = lowestScore;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Photo getProfilePhoto() {
        return profilePhoto;
    }

    public void setProfilePhoto(Photo profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    public int getHigehstScore() {
        return higehstScore;
    }

    public void setHigehstScore(int higehstScore) {
        this.higehstScore = higehstScore;
    }

    public int getLowestScore() {
        return lowestScore;
    }

    public void setLowestScore(int lowestScore) {
        this.lowestScore = lowestScore;
    }
}
