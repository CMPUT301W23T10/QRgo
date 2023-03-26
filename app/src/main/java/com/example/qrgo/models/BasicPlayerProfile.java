package com.example.qrgo.models;

/**

 BasicPlayerProfile is a class that represents a basic player profile.
 It contains information about the player such as their username, first name,
 last name, profile photo, total score, highest score and lowest score.
 */
public class BasicPlayerProfile {
    protected String username; // the username of the player
    protected String firstName; // the first name of the player
    protected String lastName; // the last name of the player
    protected Photo profilePhoto; // the profile photo of the player
    protected int totalScore; // the total score of the player
    protected int totalScans; // the total score of the player
    protected int highestScore; // the highest score of the player
    protected int lowestScore; // the lowest score of the player

    /**
     * Constructor for BasicPlayerProfile class
     * @param username the username of the player
     * @param firstName the first name of the player
     * @param lastName the last name of the player
     * @param totalScore the total score of the player
     * @param totalScans the total score of the player
     * @param highestScore the highest score of the player
     * @param lowestScore the lowest score of the player
     */
    public BasicPlayerProfile(String username, String firstName, String lastName, int totalScore, int totalScans, int highestScore, int lowestScore) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.totalScore = totalScore;
        this.totalScans = totalScans;
        this.highestScore = highestScore;
        this.lowestScore = lowestScore;
    }

    public int getTotalScans() {
        return totalScans;
    }

    public void setTotalScans(int totalScans) {
        this.totalScans = totalScans;
    }

    /**
     * Getter method for the username of the player
     * @return the username of the player
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter method for the username of the player
     * @param username the username of the player to be set
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter method for the first name of the player
     * @return the first name of the player
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter method for the first name of the player
     * @param firstName the first name of the player to be set
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter method for the last name of the player
     * @return the last name of the player
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setter method for the last name of the player
     * @param lastName the last name of the player to be set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Getter method for the profile photo of the player
     * @return the profile photo of the player
     */
    public Photo getProfilePhoto() {
        return profilePhoto;
    }

    /**
     * Setter method for the profile photo of the player
     * @param profilePhoto the profile photo of the player to be set
     */
    public void setProfilePhoto(Photo profilePhoto) {
        this.profilePhoto = profilePhoto;
    }

    /**
     * Getter method for the total score of the player
     * @return the total score of the player
     */
    public int getTotalScore() {
        return totalScore;
    }

    /**
     * Setter method for the total score of the player
     * @param totalScore the total score of the player to be set
     */
    public void setTotalScore(int totalScore) {
        this.totalScore = totalScore;
    }

    /**
     * Returns the highest score achieved by the player.
     *
     * @return the highest score achieved by the player
     */
    public int getHighestScore() {
        return highestScore;
    }

    /**
     * Sets the highest score achieved by the player.
     *
     * @param highestScore the highest score achieved by the player
     */
    public void setHighestScore(int highestScore) {
        this.highestScore = highestScore;
    }

    /**
     * Returns the lowest score achieved by the player.
     *
     * @return the lowest score achieved by the player
     */
    public int getLowestScore() {
        return lowestScore;
    }

    /**
     * Sets the lowest score achieved by the player.
     *
     * @param lowestScore the lowest score achieved by the player
     */
    public void setLowestScore(int lowestScore) {
        this.lowestScore = lowestScore;
    }
}

