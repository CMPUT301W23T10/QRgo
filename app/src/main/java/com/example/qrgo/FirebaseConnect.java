package com.example.qrgo;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class FirebaseConnect {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    boolean boolResult = false;

    public boolean checkImeiExists(String imei) {
        DocumentReference docRef = db.collection("Users").document("IMEI");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        boolResult = true;
                    } else {
                        boolResult = false;
                    }
                } else {
                    boolResult = false;
                }
            }
        });

        return boolResult;
    }
}
