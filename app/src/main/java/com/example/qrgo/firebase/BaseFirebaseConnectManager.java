package com.example.qrgo.firebase;

import com.google.firebase.firestore.FirebaseFirestore;

public class BaseFirebaseConnectManager {
    protected final FirebaseFirestore db;

    public BaseFirebaseConnectManager() {
        this.db = FirebaseFirestore.getInstance();
    }


}
