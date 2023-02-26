package com.example.qrgo;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FirebaseConnectTest {
    @Test
    public void testIMEICheck() {
        FirebaseConnect check = new FirebaseConnect();
        check.checkImeiExists("IMEITEST1", imeiExists -> {
            // Handle the result here
            if (imeiExists) {
                System.out.println("IMEI exists");
            } else {
                System.out.println("IMEI does not exists");
            }
        });

    }
}
