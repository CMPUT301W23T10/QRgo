package com.example.qrgo.listeners;

import com.example.qrgo.models.BasicQRCode;

import java.util.List;

/**
 * An interface for listening to the result of the sorted QR functions
 */
public interface OnQrListLoadedListener {
    /**
     * Invoked when the function successfully retrieves a list of QR sorted by its points.
     *
     * @param qrcodes The list of sorted qrcodes.
     */
    void onQrListLoaded(List<BasicQRCode> qrcodes);

    /**
     * Invoked when the function fails to retrieve the list of sorted QR.
     *
     * @param e The exception that caused the failure.
     */
    void onQrListLoadFailure(Exception e);
}
