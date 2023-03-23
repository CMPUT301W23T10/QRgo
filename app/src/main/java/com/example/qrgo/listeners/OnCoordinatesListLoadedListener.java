package com.example.qrgo.listeners;

import java.util.List;
import java.util.Map;

/**
 * An interface for listening to the result of the coordinate list functions
 */
public interface OnCoordinatesListLoadedListener {
    /**
     * Invoked when the function successfully retrieves a list of coordinates.
     *
     * @param mapped_coordinates A map of qr string to a list of its coordinates.
     */
    void onCoordinatesListLoaded(Map<String, List<List<Double>>> mapped_coordinates);

    /**
     * Invoked when the function fails to retrieve the list of coordinates.
     *
     * @param e The exception that caused the failure.
     */
    void onCoordinatesListLoadFailure(Exception e);
}
