package com.example.qrgo.utilities;

import static org.junit.jupiter.api.Assertions.*;

import android.util.Log;

import com.example.qrgo.QRIntakeActivity;
import com.example.qrgo.models.QRCode;

import org.junit.jupiter.api.Test;

class QRIntakeControllerTest {

    @Test
    void generateHash() {
    }

    @Test
    void calculateFields() {
        String testString = "69";
        String hash = QRIntakeController.generateHash(testString);
        QRCode testQr = new QRCode(hash);
        QRIntakeController.calculateFields(testQr);
        System.out.println(testQr.getHumanReadableQR());
        System.out.println(testQr.getQrCodePoints());
    }
}