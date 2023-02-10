package com.example.qrgo;

import java.util.Collection;

public class QRCode {
    protected GeoLocation location;
    protected Photo locationObjectPhoto;
    protected String QRString;
    protected String humanReadableQR;
    protected Collection<Comment> comments;
    protected PlayerProfile player;

    public GeoLocation getLocation() {
        return location;
    }

    public void setLocation(GeoLocation location) {
        this.location = location;
    }

    public Photo getLocationObjectPhoto() {
        return locationObjectPhoto;
    }

    public void setLocationObjectPhoto(Photo locationObjectPhoto) {
        this.locationObjectPhoto = locationObjectPhoto;
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

    public Collection<Comment> getComments() {
        return comments;
    }

    public void setComments(Collection<Comment> comments) {
        this.comments = comments;
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public void setPlayer(PlayerProfile player) {
        this.player = player;
    }

    public interface LocationQRSearch{
        public Collection<QRCode> getClosestQRByGeo();
    }

    public interface QRSearching{
        public Collection<QRCode> searchByGeo();
    }
}
