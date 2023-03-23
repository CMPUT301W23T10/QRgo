package com.example.qrgo.listeners;

import com.example.qrgo.models.BasicPlayerProfile;

import java.util.List;

public interface OnScannedUsersLoadedListener {
    void onScannedUsersLoaded(List<BasicPlayerProfile> scannedUsers);

    void onScannedUsersLoadFailure(Exception e);
}
