package com.example.qrgo.models;

import java.util.Collection;
import java.util.List;

/**
 A class representing a player's profile, including their personal information,
 collected QR codes and comments, and game statistics.
 */
public class PlayerProfile {
    protected String username;
    protected String firstName;
    protected String lastName;
    protected String playerLocation;
    protected boolean locationEnabled;
    protected String contactPhone;
    protected String contactEmail;
    protected Collection<QRCode> qrCodes;
    protected Collection<Comment> comments;
    protected List<String> qrCodeIds; // A list of QR Code IDs specific to the user
    protected List<String> commentIds; // A list of Comment IDs specific to the user
    protected int totalScore;
    protected int highestScore;
    protected int lowestScore;
    protected int totalScans;

    protected List<BasicQRCode> qrCodeBasicProfiles;

    /**
     * Constructs a new PlayerProfile object with the specified properties.
     *
     * @param username the player's username
     * @param firstName the player's first name
     * @param lastName the player's last name
     * @param contactPhone the player's contact phone number
     * @param contactEmail the player's contact email address
     * @param totalScore the player's total score
     * @param highestScore the player's highest score
     * @param lowestScore the player's lowest score
     * @param totalScans the total number of QR codes scanned by the player
     * @param qrScans a list of QR Code IDs specific to the player
     * @param qrCodeBasicProfiles a list of BasicQRCode objects containing basic information about the QR codes
     * @param comments a collection of comments made by the player
     */
    public PlayerProfile(String username, String firstName, String lastName, String contactPhone, String contactEmail,
                       int totalScore, int highestScore, int lowestScore, int totalScans,
                       List<String> qrScans, List<BasicQRCode> qrCodeBasicProfiles,
                         List<Comment> comments){
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.contactPhone = contactPhone;
        this.contactEmail = contactEmail;
        this.totalScore = totalScore;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
        this.totalScans = totalScans;
        this.qrCodeIds = qrScans;
        this.qrCodeBasicProfiles = qrCodeBasicProfiles;
        this.comments = comments;
    }

    /**
     * Returns the player's username.
     *
     * @return the player's username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Sets the player's username.
     *
     * @param username the player's username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Returns the player's first name.
     *
     * @return the player's first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the player's first name.
     *
     * @param firstName the player's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the player's last name.
     *
     * @return the player's last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the player's last name.
     *
     * @param lastName the player's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the player's location.
     *
     * @return the player's location
     */
    public String getPlayerLocation() {
        return playerLocation;
    }

    /**
     * Sets the player's location.
     *
     * @param playerLocation the player's location
     */
    public void setPlayerLocation(String playerLocation) {
        this.playerLocation = playerLocation;
    }

    /**
     * Returns a boolean indicating whether the player's location is enabled.
     *
     * @return a boolean indicating whether the player's location is enabled
     */
    public boolean isLocationEnabled() {
        return locationEnabled;
    }

    /**
     * Sets a boolean indicating whether the player's location is enabled.
     *
     * @param locationEnabled a boolean indicating whether the player's location is enabled
     */
    public void setLocationEnabled(boolean locationEnabled) {
        this.locationEnabled = locationEnabled;
    }

    /**
     * Returns the player's contact phone number.
     *
     * @return the player's contact phone number
     */
    public String getContactPhone() {
        return contactPhone;
    }

    /**
     * Sets the player's contact phone number.
     *
     * @param contactPhone the player's contact phone number
     */
    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    /**
     * Returns the player's contact email address.
     *
     * @return the player's contact email address
     */
    public String getContactEmail() {
        return contactEmail;
    }

    /**
     * Sets the player's contact email address.
     *
     * @param contactEmail the player's contact email address
     */
    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    /**
     * Returns a collection of QRCode objects associated with the player.
     *
     * @return a collection of QRCode objects associated with the player
     */
    public Collection<QRCode> getQrCodes() {
        return qrCodes;
    }

    /**
     * Sets the QRCode objects associated with the player.
     *
     * @param qrCodes a collection of QRCode objects associated with the player
     */
    public void setQrCodes(Collection<QRCode> qrCodes) {
        this.qrCodes = qrCodes;
    }

    /**
     * Returns a collection of Comment objects made by the player.
     *
     * @return a collection of Comment objects made by the player
     */
    public Collection<Comment> getComments() {
        return comments;
    }

    /**
     Sets the comments associated with the player profile.
     @param comments the collection of comments to be associated with the player profile
     */
    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    /**
     Returns the list of QR Code IDs associated with the player profile.
     @return the list of QR Code IDs associated with the player profile
     */
    public List<String> getQrCodeIds() {
        return qrCodeIds;
    }

    /**
     Sets the list of QR Code IDs associated with the player profile.
     @param qrCodeIds the list of QR Code IDs to be associated with the player profile
     */
    public void setQrCodeIds(List<String> qrCodeIds) {
        this.qrCodeIds = qrCodeIds;
    }

    /**
     Returns the list of comment IDs associated with the player profile.
     @return the list of comment IDs associated with the player profile
     */
    public List<String> getCommentIds() {
        return commentIds;
    }

    /**
     Sets the list of comment IDs associated with the player profile.
     @param commentIds the list of comment IDs to be associated with the player profile
     */
    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    /**
     Returns the total score of the player.
     @return the total score of the player.
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     Sets the total score of the player.
     @param totalScore the total score to set.
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     Returns the highest score of the player.
     @return the highest score of the player.
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     Sets the highest score of the player.
     @param highestScore the highest score to set.
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    /**
     Returns the lowest score of the player.
     @return the lowest score of the player.
     */
    public int getLowestScore() {
        return lowestScore;
    }

    /**
     Sets the lowest score of the player.
     @param lowestScore the lowest score to set.
     */
    public void setLowestScore(int lowestScore) {
        this.lowestScore = lowestScore;
    }

    /**
     Returns the total number of scans the player has performed.
     @return the total number of scans the player has performed.
     */
    public int getTotalScans() {
        return totalScans;
    }

    /**
     Sets the total number of scans the player has performed.
     @param totalScans the total number of scans to set.
     */
    public void setTotalScans(int totalScans) {
        this.totalScans = totalScans;
    }

    /**
     Returns the list of basic QR code profiles associated with the player.
     @return the list of basic QR code profiles associated with the player.
     */
    public List<BasicQRCode> getQrCodeBasicProfiles() {
        return qrCodeBasicProfiles;
    }

    /**
     Sets the list of basic QR code profiles associated with the player.
     @param qrCodeBasicProfiles the list of basic QR code profiles to set.
     */
    public void setQrCodeBasicProfiles(List<BasicQRCode> qrCodeBasicProfiles) {
        this.qrCodeBasicProfiles = qrCodeBasicProfiles;
    }
}


