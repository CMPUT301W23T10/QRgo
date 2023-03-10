package com.example.qrgo.models;

import com.google.firebase.firestore.GeoPoint;

import java.util.Collection;
import java.util.List;

/**
 * QRCode class represents a QR code entity in the system
 */
public class QRCode {
    // Crucial Attributes
    protected String qrString;
    protected String humanReadableQR;
    protected int qrCodePoints;
    protected List<GeoPoint> locations;
    protected List<String> photoIds;
    protected List<String> scannedPlayerIds;
    protected List<String> commentIds;
    protected PlayerProfile player;
    // Display only attributes
    protected List<Photo> photos;
    protected List<Comment> comments;
    protected List<BasicPlayerProfile> scannedPlayer;

    /**
     * Creates a new instance of QRCode with the given parameters
     *
     * @param qrString         the string that the QR code represents
     * @param humanReadableQR  the human-readable text of the QR code
     * @param qrCodePoints     the number of points assigned to this QR code
     * @param locations        the list of geographic locations associated with this QR code
     * @param photoIds         the list of photo IDs associated with this QR code
     * @param scannedPlayerIds the list of player IDs who have scanned this QR code
     * @param commentIds       the list of comment IDs associated with this QR code
     */
    public QRCode(String qrString, String humanReadableQR, int qrCodePoints, List<GeoPoint> locations, List<String> photoIds, List<String> scannedPlayerIds, List<String> commentIds) {
        this.qrString = qrString;
        this.humanReadableQR = humanReadableQR;
        this.qrCodePoints = qrCodePoints;
        this.locations = locations;
        this.photoIds = photoIds;
        this.scannedPlayerIds = scannedPlayerIds;
        this.commentIds = commentIds;
    }

    /**
     * Returns the list of photo IDs associated with this QR code
     *
     * @return the list of photo IDs
     */
    public List<String> getPhotoIds() {
        return photoIds;
    }

    /**
     * Sets the list of photo IDs associated with this QR code
     *
     * @param photoIds the list of photo IDs
     */
    public void setPhotoIds(List<String> photoIds) {
        this.photoIds = photoIds;
    }

    /**
     * Returns the string that the QR code represents
     *
     * @return the QR code string
     */
    public String getQrString() {
        return qrString;
    }

    /**
     * Sets the string that the QR code represents
     *
     * @param qrString the QR code string
     */
    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    /**
     * Returns the human-readable text of the QR code
     *
     * @return the human-readable text
     */
    public String getHumanReadableQR() {
        return humanReadableQR;
    }

    /**
     * Sets the human-readable text of the QR code
     *
     * @param humanReadableQR the human-readable text
     */
    public void setHumanReadableQR(String humanReadableQR) {
        this.humanReadableQR = humanReadableQR;
    }

    /**
     * Returns the player profile who created this QR code
     *
     * @return the player profile
     */
    public PlayerProfile getPlayer() {
        return player;
    }

    /**
     * Sets the player profile who created this QR code
     *
     * @param player the player profile
     */
    public void setPlayer(PlayerProfile player) {
        this.player = player;
    }

    /**
     * Returns the number of points assigned to this QR code
     *
     * @return the number of points
     */
    public int getQrCodePoints() {
        return qrCodePoints;
    }

    /**
     * Sets the number of points assigned to this QR code
     *
     * @param qrCodePoints the number of points
     */
    public void setQrCodePoints(int qrCodePoints) {
        this.qrCodePoints = qrCodePoints;
    }

    /**
     * Returns the list of player IDs who have scanned this QR code
     *
     * @return the list of player IDs
     */
    public List<String> getScannedPlayerIds() {
        return scannedPlayerIds;
    }

    /**
     * Sets the list of scanned player IDs associated with this match.
     *
     * @param scannedPlayerIds the list of scanned player IDs
     */

    public void setScannedPlayerIds(List<String> scannedPlayerIds) {
        this.scannedPlayerIds = scannedPlayerIds;
    }

    /**
     * Returns the list of comment IDs associated with this match.
     *
     * @return the list of comment IDs
     */
    public List<String> getCommentIds() {
        return commentIds;
    }

    /**
     * Sets the list of comment IDs associated with this match.
     *
     * @param commentIds the list of comment IDs
     */
    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    /**
     * Returns the list of locations associated with this match.
     *
     * @return the list of locations
     */
    public List<GeoPoint> getLocations() {
        return locations;
    }

    /**
     * Sets the list of locations associated with this match.
     *
     * @param locations the list of locations
     */
    public void setLocations(List<GeoPoint> locations) {
        this.locations = locations;
    }

    /**
     * Returns the list of photos associated with this match.
     *
     * @return the list of photos
     */
    public List<Photo> getPhotos() {
        return photos;
    }

    /**
     * Sets the list of photos associated with this match.
     *
     * @param photos the list of photos
     */
    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    /**
     * Returns the list of comments associated with this match.
     *
     * @return the list of comments
     */
    public List<Comment> getComments() {
        return comments;
    }

    /**
     * Sets the list of comments associated with this match.
     *
     * @param comments the list of comments
     */
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    /**
     * Returns the list of scanned players associated with this match.
     *
     * @return the list of scanned players
     */
    public List<BasicPlayerProfile> getScannedPlayer() {
        return scannedPlayer;
    }

    /**
     * Sets the list of scanned players associated with this match.
     *
     * @param scannedPlayer the list of scanned players
     */
    public void setScannedPlayer(List<BasicPlayerProfile> scannedPlayer) {
        this.scannedPlayer = scannedPlayer;
    }

}