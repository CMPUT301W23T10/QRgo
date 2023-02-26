package com.example.qrgo;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class FirebaseConnectTest {
    @Test
    public void testFiresbaseConnect() {
        FirebaseConnect check = new FirebaseConnect();
        assertEquals(true,check.checkImeiExists("IMEITEST1"));
        assertEquals(false,check.checkImeiExists("IMEITEST2"));

    }
}
