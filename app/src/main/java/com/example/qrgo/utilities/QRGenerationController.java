package com.example.qrgo.utilities;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * QRGenerationController is a utility class that generates the required fields for a {@link com.example.qrgo.models.QRCode}
 * from the string data of a scanned QR code from {@link com.example.qrgo.QRScanActivity}. Fields
 * generated are based on a hash computed from the string that a QR code represents
 */
public class QRGenerationController {
    private HashMap<Character, int[]> hexKey = new HashMap<Character, int[]>();
    private HashMap<Character, String> color = new HashMap<Character, String>();
    private String hash;
    private ArrayList<int[]> parsedHash = new ArrayList<int[]>();
    private String rarity;
    private int score;
    private String humanReadableName;
    private ArrayList<Integer> featureList = new ArrayList<Integer>();

    /**
     * constructor that takes a string and automatically generates all the required fields from that
     * string
     * @param qrText the String received from a QR scan
     */
    public QRGenerationController(String qrText) {
        initializeHashMaps();
        generateHash(qrText);
        parseHash();
        calculateScore();
        generateFeatureList();
        generateHumanReadableName();

    }

    private void initializeHashMaps() {
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
    }

    /**
     * hashes the QR code using the SHA-256 hashing algorithm, the hash becomes the basis of the QR
     * code object and all fields are generated from the hash
     * @param qrText the String received from a QR scan
     */
    private void generateHash(String qrText) {
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
        this.hash = hexHash.toString().toUpperCase();
    }

    /**
     * parses each character into the hash into a list of items that contain useful information such
     * as number of ones (used for feature list and score) and the rarity of the letter occurring
     */
    private void parseHash() {
        char[] hashArray = this.hash.toCharArray();
        for (char c : hashArray) {
            this.parsedHash.add(hexKey.get(c));
        }
    }

    /**
     * calculates the score of the QR code based on the hash/parsed hash
     */
    private void calculateScore() {
        int score = 0;
        int rarity = 0;
        ArrayList<Integer> rarityArray = new ArrayList<Integer>();

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
                this.rarity = "(C) ";
                break;
            case 2:
                score += 500;
                this.rarity = "(R) ";
                break;
            case 3:
                score += 1000;
                this.rarity = "(E) ";
                break;
            case 4:
                score += 1500;
                this.rarity = "(L) ";
                break;
        }
        this.score = score;
    }

    /**
     * generates the feature list based on the hash to be used for the human readable name and the
     * visual representation.
     */
    private void generateFeatureList() {
        for (int i = 1; i <= 4; i++) {
            if (this.parsedHash.get(i) != null) {
                this.featureList.add(this.parsedHash.get(i)[1]);
            }
        }
    }

    /**
     * generates the human readable name based on the hash
     */
    private void generateHumanReadableName() {
        String humanReadableName;
        String[] monsterNames = {"Skralix", "Gloombrute", "Phantasmaur", "Murkfiend", "Vilegloom", "Doomfang", "Nightshade", "Spectrashock", "Dreadmaw", "Blightspawn", "Necroclaw", "Shadowbeak", "Wraithhound", "Bloodbane", "Voidspawn", "Graveclaw", "Darkhowl", "Venomwing", "Thundercrush", "Frostbite", "Soulripper", "Infernojaw", "Bonecruncher", "Nightstalker", "Deathshade"};
        String[] monsterAdjectives = {"Sinister ", "Menacing ", "Ghostly ", "Mysterious ", "Repulsive ", "Lethal ", "Shadowy ", "Eerie ", "Terrifying ", "Foul ", "Ghastly ", "Dark ", "Haunting ", "Deadly ", "Otherworldly ", "Gruesome ", "Ominous ", "Poisonous ", "Powerful ", "Cold ", "Malevolent ", "Fiery ", "Brutal ", "Stealthy ", "Fatal "};
        humanReadableName = this.rarity;
        humanReadableName += color.get(this.hash.charAt(0));
        humanReadableName += monsterAdjectives[this.featureList.get(0) *5 + this.featureList.get(1)];
        humanReadableName += monsterNames[this.featureList.get(2) *5 + this.featureList.get(3)];
        this.humanReadableName = humanReadableName;
    }

    public String getHash() {
        return this.hash;
    }

    public int getScore() {
        return this.score;
    }

    public String getHumanReadableName() {
        return this.humanReadableName;
    }
}
