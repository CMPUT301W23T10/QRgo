package com.example.qrgo;

import java.util.Collection;
import java.util.List;

public class QRCode {
   // Crucial Attributes
    protected String username;
    protected Photo profilePhoto;
    protected String qrString;
    protected String humanReadableQR;
    protected String qrCodePoints;
    protected List<GeoLocation> locations;
    protected List<String> photoIds;
    protected List<String> scannedPlayerIds;
    protected List<String> commentIds;
    protected PlayerProfile player;

    // Display only attributes
    protected List<Photo> photos;
    protected List<Comment> comments;
    protected List<BasicPlayerProfile> scannedPlayer;

    public QRCode(String username, Photo profilePhoto, String qrString, String humanReadableQR, String qrCodePoints, List<GeoLocation> locations, List<String> photoIds, List<String> scannedPlayerIds, List<String> commentIds) {
        this.username = username;
        this.profilePhoto = profilePhoto;
        this.qrString = qrString;
        this.humanReadableQR = humanReadableQR;
        this.qrCodePoints = qrCodePoints;
        this.locations = locations;
        this.photoIds = photoIds;
        this.scannedPlayerIds = scannedPlayerIds;
        this.commentIds = commentIds;
    }

    public List<String> getPhotoIds() {
        return photoIds;
    }

    public void setProfilePhoto(Photo profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    public Photo getProfilePhoto() {
        return profilePhoto;
    }

    public void setPhotoIds(List<String> photoIds) {
        this.photoIds = photoIds;
    }

    public String getQrString() {
        return qrString;
    }

    public void setQrString(String qrString) {
        this.qrString = qrString;
    }

    public String getHumanReadableQR() {
        return humanReadableQR;
    }

    public void setHumanReadableQR(String humanReadableQR) {
        this.humanReadableQR = humanReadableQR;
    }


    public PlayerProfile getPlayer() {
        return player;
    }

    public void setPlayer(PlayerProfile player) {
        this.player = player;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getQrCodePoints() {
        return qrCodePoints;
    }

    public void setQrCodePoints(String qrCodePoints) {
        this.qrCodePoints = qrCodePoints;
    }


    public List<String> getScannedPlayerIds() {
        return scannedPlayerIds;
    }

    public void setScannedPlayerIds(List<String> scannedPlayerIds) {
        this.scannedPlayerIds = scannedPlayerIds;
    }

    public List<String> getCommentIds() {
        return commentIds;
    }

    public void setCommentIds(List<String> commentIds) {
        this.commentIds = commentIds;
    }

    public List<GeoLocation> getLocations() {
        return locations;
    }

    public void setLocations(List<GeoLocation> locations) {
        this.locations = locations;
    }

    public List<Photo> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Photo> photos) {
        this.photos = photos;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<BasicPlayerProfile> getScannedPlayer() {
        return scannedPlayer;
    }

    public void setScannedPlayer(List<BasicPlayerProfile> scannedPlayer) {
        this.scannedPlayer = scannedPlayer;
    }

    public interface LocationQRSearch{
        public Collection<QRCode> getClosestQRByGeo();
    }

    public interface QRSearching{
        public Collection<QRCode> searchByGeo();
    }
}
