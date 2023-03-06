package com.example.qrgo.models;

import java.util.Date;

public class Comment {

    protected String commentId;
    protected String commentString;
    protected String qrCodeId;
    protected String playerUsername;
    protected String datetime;

    protected String playerFirstName;
    protected String playerLastName;
    protected Date date;
    public Comment(String commentId, String commentString, String qrCodeId, String playerUsername, String playerFirstName, String playerLastName,
                   Date date) {
        this.commentId = commentId;
        this.commentString = commentString;
        this.qrCodeId = qrCodeId;
        this.playerUsername = playerUsername;
        this.date = date;
        this.playerFirstName = playerFirstName;
        this.playerLastName = playerLastName;
    }

    public String getCommentId() {
        return commentId;
    }

    public void setCommentId(String commentId) {
        this.commentId = commentId;
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

    public String getPlayerUsername() {
        return playerUsername;
    }

    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPlayerFirstName() {
        return playerFirstName;
    }

    public void setPlayerFirstName(String playerFirstName) {
        this.playerFirstName = playerFirstName;
    }

    public String getPlayerLastName() {
        return playerLastName;
    }

    public void setPlayerLastName(String playerLastName) {
        this.playerLastName = playerLastName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
