package com.example.qrgo.listeners;

import com.example.qrgo.models.PlayerProfile;

/**
 * Interface for callbacks when a player profile is retrieved.
 */
public interface OnPlayerProfileGetListener {
    /**
     * Called when a player profile is retrieved.
     *
     * @param userProfile the retrieved player profile
     */
    void onPlayerProfileGet(PlayerProfile userProfile);
}
