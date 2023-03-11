package com.example.qrgo.models;

import java.util.Date;

/**
 * The Comment class represents a comment that a player leaves on a QR code.
 * It contains information such as the comment ID, comment text, QR code ID,
 * player username, date, and player first and last name.
 */
public class Comment {
    protected String commentId;
    protected String commentString;
    protected String qrCodeId;
    protected String playerUsername;
    protected String datetime;
    protected String playerFirstName;
    protected String playerLastName;
    protected Date date;

    /**
     * Constructs a Comment object with the specified parameters.
     *
     * @param commentId       the ID of the comment
     * @param commentString   the text of the comment
     * @param qrCodeId        the ID of the QR code that the comment belongs to
     * @param playerUsername  the username of the player who made the comment
     * @param playerFirstName the first name of the player who made the comment
     * @param playerLastName  the last name of the player who made the comment
     * @param date            the date when the comment was made
     */
    public Comment(String commentId, String commentString, String qrCodeId, String playerUsername,
                   String playerFirstName, String playerLastName, Date date) {
        this.commentId = commentId;
        this.commentString = commentString;
        this.qrCodeId = qrCodeId;
        this.playerUsername = playerUsername;
        this.playerFirstName = playerFirstName;
        this.playerLastName = playerLastName;
        this.date = date;
    }

    /**
     * Returns the ID of the comment.
     *
     * @return the ID of the comment
     */
    public String getCommentId() {
        return commentId;
    }

    /**
     * Sets the ID of the comment.
     *
     * @param commentId the ID of the comment
     */
    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    /**
     * Returns the text of the comment.
     *
     * @return the text of the comment
     */
    public String getCommentString() {
        return commentString;
    }

    /**
     * Sets the text of the comment.
     *
     * @param commentString the text of the comment
     */
    public void setCommentString(String commentString) {
        this.commentString = commentString;
    }

    /**
     * Returns the first name of the player.
     *
     * @return the first name of the player.
     */
    public String getPlayerFirstName() {
        return playerFirstName;
    }

    /**
     * Sets the first name of the player.
     *
     * @param playerFirstName the first name of the player.
     */
    public void setPlayerFirstName(String playerFirstName) {
        this.playerFirstName = playerFirstName;
    }

    /**
     * Returns the last name of the player.
     *
     * @return the last name of the player.
     */
    public String getPlayerLastName() {
        return playerLastName;
    }

    /**
     * Sets the last name of the player.
     *
     * @param playerLastName the last name of the player.
     */
    public void setPlayerLastName(String playerLastName) {
        this.playerLastName = playerLastName;
    }

    /**
     * Returns the ID of the QR code that the comment belongs to.
     *
     * @return the ID of the QR code that the comment belongs to
     */
    public String getQrCodeId() {
        return qrCodeId;
    }

    /**
     * Sets the ID of the QR code that the comment belongs to.
     *
     * @param qrCodeId the ID of the QR code that the comment belongs to
     */
    public void setQrCodeId(String qrCodeId) {
        this.qrCodeId = qrCodeId;
    }

    /**
     * Returns the username of the player who made the comment.
     *
     * @return the username of the player who made the comment
     */
    public String getPlayerUsername() {
        return playerUsername;
    }

    /**
     * Sets the username of the player who made the comment.
     *
     * @param playerUsername the username of the player who made the comment
     */
    public void setPlayerUsername(String playerUsername) {
        this.playerUsername = playerUsername;
    }

    /**
     * Returns the date when the comment was made.
     *
     * @return the date when the comment was made
     */
    public Date getDate() {
        return date;
    }

    /**
     * Sets the date when the comment was made.
     *
     * @param date the date of the comment
     */
    public void setDate(Date date) {
        this.date = date;
    }
}
