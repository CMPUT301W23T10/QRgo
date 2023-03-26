package com.example.qrgo.listeners;


/**
 * An interface for listening to the result of the global rank function.
 */
public interface OnGlobalRankLoadedListener {
    /**
     * Invoked when the function successfully retrieves the global rank for a given user.
     *
     * @param rank      The rank of the user.
     * @param maxScore  The Max Score.
     * @param userScore The Score of the user
     */
    void onGlobalRankLoaded(int rank, int maxScore, int userScore);

    /**
     * Invoked when the function fails to retrieve the global rank for a given user.
     *
     * @param e The exception that caused the failure.
     */
    void onGlobalRankLoadFailure(Exception e);
}
