package com.example.qrgo;

import java.util.Date;

public class Comment {

    protected String commentId;
    protected String commentString;
    protected String qrCodeId;
    protected String playerUsername;
    protected String datetime;
    protected PlayerProfile player;
    protected Date date;
    public Comment(String commentId, String commentString, String qrCodeId, String playerUsername,  Date date) {
        this.commentId = commentId;
        this.commentString = commentString;
        this.qrCodeId = qrCodeId;
        this.playerUsername = playerUsername;
        this.date = date;
    }

    public String getCommentString() {
        return commentString;
    }

    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    public String getQrCodeId() {
        return qrCodeId;
    }

    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    public PlayerProfile getPlayer() {
        return player;
    }

    public void setPlayer(PlayerProfile player) {
        this.player = player;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }


}
