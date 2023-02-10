package com.example.qrgo;

import java.util.Collection;

public class PlayerProfile {
    protected String username;
    protected String playerLocation;
    protected boolean locationEnabled;
    protected String contactPhone;
    protected String contactEmail;
    protected Collection<QRCode> qrCodes;
    protected Collection<Comment> comments;
    protected Integer sumScore;
    protected Integer totalScans;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPlayerLocation() {
        return playerLocation;
    }

    public void setPlayerLocation(String playerLocation) {
        this.playerLocation = playerLocation;
    }

    public boolean isLocationEnabled() {
        return locationEnabled;
    }

    public void setLocationEnabled(boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public Collection<QRCode> getQrCodes() {
        return qrCodes;
    }

    public void setQrCodes(Collection<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
    }

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public Integer getSumScore() {
        return sumScore;
    }

    public void setSumScore(Integer sumScore) {
        this.sumScore = sumScore;
    }

    public Integer getTotalScans() {
        return totalScans;
    }

    public void setTotalScans(Integer totalScans) {
        this.totalScans = totalScans;
    }

    public interface Scoring{
        public Collection<PlayerProfile> viewLeaderBoard();
        public Integer viewHighestQrRanking();
    }

    public interface PlayerSearching{
        public Collection<PlayerProfile> searchByUsername();
    }

}


