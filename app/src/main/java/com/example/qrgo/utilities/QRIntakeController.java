package com.example.qrgo.utilities;

import static java.security.MessageDigest.getInstance;

import android.media.Image;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * This class is a container for a variety of assistive methods that help create and update
 * {@link com.example.qrgo.models.QRCode} entries in the database. These methods assist with hashing
 * QR codes, calculating fields, updating fields, as well as validating ownership and location data
 * in accordance with the tools in {@link FirebaseConnect} to ensure full and complete QR entries.
 */
public class QRIntakeController {

    public static String generateHash(String qrText) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        byte[] hash = digest.digest(qrText.getBytes(StandardCharsets.UTF_8));
        StringBuilder hexHash = new StringBuilder();
        for (byte aByte : hash) {
            hexHash.append(String.format("%02x", aByte));
        }
        return hexHash.toString();
    }

//    public static int calculateScore(String hash) {
//        int score;
//
//        return score;
//    }
//
//    public static String generateHumanReadableName(String hash) {
//        String humanReadableName;
//
//        return humanReadableName;
//    }
//
//    public static List<String> generateVisualRepresentation(String hash) {
//        List<String> visualRepresentationArray;
//
//        return visualRepresentationArray;
//    }


}
