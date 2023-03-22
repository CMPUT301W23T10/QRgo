package com.example.qrgo.listeners;

import com.example.qrgo.models.BasicPlayerProfile;

import java.util.List;

/**
 * An interface for listening to the result of the sorted Player functions
 */
public interface OnPlayerListLoadedListener {
    /**
     * Invoked when the function successfully retrieves a list of players sorted by their highest score.
     *
     * @param playerList The list of sorted players.
     */
    void onPlayerListLoaded(List<BasicPlayerProfile> playerList);

    /**
     * Invoked when the function fails to retrieve the list of sorted players.
     *
     * @param e The exception that caused the failure.
     */
    void onPlayerListLoadFailure(Exception e);
}
