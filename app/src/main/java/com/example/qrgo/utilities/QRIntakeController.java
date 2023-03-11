package com.example.qrgo.utilities;

import static java.security.MessageDigest.getInstance;

import android.media.Image;
import android.util.Log;

import com.example.qrgo.models.QRCode;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This class is a container for a variety of assistive methods that help create and update
 * {@link com.example.qrgo.models.QRCode} entries in the database. These methods assist with hashing
 * QR codes, calculating fields, updating fields, as well as validating ownership and location data
 * in accordance with the tools in {@link FirebaseConnect} to ensure full and complete QR entries.
 */
public class QRIntakeController {

    private HashMap<Character, String> hexKey = new HashMap<Character, String>();

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
        return hexHash.toString().toUpperCase();
    }
    private static ArrayList<int[]> parseHash(String hash) {
        HashMap<Character, int[]> hexKey = new HashMap<Character, int[]>();
        hexKey.put('0', new int[] {3, 0});
        hexKey.put('1', new int[] {2, 1});
        hexKey.put('2', new int[] {1, 1});
        hexKey.put('3', new int[] {1, 2});
        hexKey.put('4', new int[] {1, 1});
        hexKey.put('5', new int[] {1, 2});
        hexKey.put('6', new int[] {1, 2});
        hexKey.put('7', new int[] {2, 3});
        hexKey.put('8', new int[] {2, 1});
        hexKey.put('9', new int[] {1, 2});
        hexKey.put('A', new int[] {4, 2});
        hexKey.put('B', new int[] {1, 3});
        hexKey.put('C', new int[] {1, 2});
        hexKey.put('D', new int[] {1, 3});
        hexKey.put('E', new int[] {2, 3});
        hexKey.put('F', new int[] {3, 4});

        ArrayList<int[]> parsedHash = new ArrayList<int[]>();
        char[] hashArray = hash.toCharArray();
        for (char c : hashArray) {
            parsedHash.add(hexKey.get(c));
        }
        return parsedHash;
    }
    public static void calculateFields(QRCode currentQR) {
        String hash = currentQR.getQrString();
        ArrayList<int[]> parsedHash = new ArrayList<int[]>();
        parsedHash = parseHash(hash);
        String[] monsterNames = {"Skralix", "Gloombrute", "Phantasmaur", "Murkfiend", "Vilegloom", "Doomfang", "Nightshade", "Spectrashock", "Dreadmaw", "Blightspawn", "Necroclaw", "Shadowbeak", "Wraithhound", "Bloodbane", "Voidspawn", "Graveclaw", "Darkhowl", "Venomwing", "Thundercrush", "Frostbite", "Soulripper", "Infernojaw", "Bonecruncher", "Nightstalker", "Deathshade"};
        String[] monsterAdjectives = {"Sinister ", "Menacing ", "Ghostly ", "Mysterious ", "Repulsive ", "Lethal ", "Shadowy ", "Eerie ", "Terrifying ", "Foul ", "Ghastly ", "Dark ", "Haunting ", "Deadly ", "Otherworldly ", "Gruesome ", "Ominous ", "Poisonous ", "Powerful ", "Cold ", "Malevolent ", "Fiery ", "Brutal ", "Stealthy ", "Fatal "};
        int score = 0;
        int rarity = 0;
        ArrayList<Integer> rarityArray = new ArrayList<Integer>();
        ArrayList<Integer> featureList = new ArrayList<Integer>();
        String humanReadableName = "";

        for (int[] i : parsedHash) {
            if (i != null) {
                score += i[1];
                rarityArray.add(i[0]);
            }
        }
        int highestCount = 0;
        for (int i = 1; i <=4; i ++) {
            int count = 0;
            for (int x : rarityArray) {
                if (x == i) { count++; }
            }
            if (count >= highestCount) { rarity = i; highestCount = count; }
        }
        score = (int) Math.ceil((double) score * 500 / 256);

        switch(rarity) {
            case 1:
                score += 0;
                humanReadableName = "Common ";
                break;
            case 2:
                score += 500;
                humanReadableName = "Rare ";
                break;
            case 3:
                score += 1000;
                humanReadableName = "Epic ";
                break;
            case 4:
                score += 1500;
                humanReadableName = "Legendary ";
                break;
        }

        HashMap<Character, String> color = new HashMap<Character, String>();
        color.put('0', "Black ");
        color.put('1', "White ");
        color.put('2', "Gray ");
        color.put('3', "Silver ");
        color.put('4', "Maroon ");
        color.put('5', "Red ");
        color.put('6', "Purple ");
        color.put('7', "Fushsia ");
        color.put('8', "Green ");
        color.put('9', "Lime ");
        color.put('A', "Olive ");
        color.put('B', "Yellow ");
        color.put('C', "Navy ");
        color.put('D', "Blue ");
        color.put('E', "Teal ");
        color.put('F', "Aqua ");
        humanReadableName += color.get(hash.charAt(0));

        for (int i = 1; i <= 4; i++) {
            if (parsedHash.get(i) != null) {
                featureList.add(parsedHash.get(i)[1]);
            }
        }
        humanReadableName += monsterAdjectives[featureList.get(0) *5 + featureList.get(1)];
        humanReadableName += monsterNames[featureList.get(2) *5 + featureList.get(3)];

        currentQR.setQrCodePoints(score);
        currentQR.setHumanReadableQR(humanReadableName);

    }

//    public static List<String> generateVisualRepresentation(String hash) {
//        List<String> visualRepresentationArray;
//
//        return visualRepresentationArray;
//    }


}
