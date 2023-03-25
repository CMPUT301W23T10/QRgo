package com.example.qrgo.listeners;

import com.example.qrgo.models.BasicPlayerProfile;

import java.util.List;

/**
 * Interface definition for a callback to be invoked when a user search is complete.
 */
public interface OnUserSearchListener {
    /**
     * Called when a user search is complete and at least one user is found.
     *
     * @param users The list of users that match the search criteria.
     */
    void onUserSearchComplete(List<BasicPlayerProfile> users);

    /**
     * Called when a user search is complete and no users are found.
     */
    void onUserSearchFailure(Exception e);
}
