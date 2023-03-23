package com.example.qrgo.utilities;


import com.example.qrgo.firebase.CommentFirebaseManager;
import com.example.qrgo.firebase.PlayerProfileFirebaseManager;
import com.example.qrgo.firebase.QRCodeFirebaseManager;
import com.example.qrgo.firebase.UserFirebaseManager;

/**
 The FirebaseConnect class provides methods to connect to and interact with the Firebase Firestore database.
 It supports adding new users, checking if a user already exists, and retrieving player profiles.
 It uses listeners to handle results and errors from database operations.
 */
public class FirebaseConnect {
    private final UserFirebaseManager userFirebaseManager;
    private final PlayerProfileFirebaseManager playerProfileFirebaseManager;
    private final QRCodeFirebaseManager qrCodeFirebaseManager;
    private final CommentFirebaseManager commentFirebaseManager;

    public FirebaseConnect() {
        this.userFirebaseManager = new UserFirebaseManager();
        this.playerProfileFirebaseManager = new PlayerProfileFirebaseManager();
        this.qrCodeFirebaseManager = new QRCodeFirebaseManager();
        this.commentFirebaseManager = new CommentFirebaseManager();
    }

    public UserFirebaseManager getUserManager() {
        return userFirebaseManager;
    }

    public PlayerProfileFirebaseManager getPlayerProfileManager() {
        return playerProfileFirebaseManager;
    }

    public QRCodeFirebaseManager getQRCodeManager() {
        return qrCodeFirebaseManager;
    }

    public CommentFirebaseManager getCommentManager() {
        return commentFirebaseManager;
    }

}


