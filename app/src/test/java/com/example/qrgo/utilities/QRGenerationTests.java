package com.example.qrgo.utilities;

import org.junit.jupiter.api.Test;

public class QRGenerationTests {

    //ensures that a variety of different strings produce a 64 hash with a valid hex value.
    @Test
    public void testHashGeneration() {
        String testString1 = "";
        String testString2 = "abc";
        String testString3 = "123";
        String testString4 = "!@#";
        String testString5 = "abc123!@#";
        String testString6 = "dklfdfkldfjdfdlfdjfgdgft74pfgpfudgu47tp74tfgktb7t8rfdgfgghfjf4g7t743t47t8fgyaffgfouif74t47";
        QRGenerationController generator1 = new QRGenerationController(testString1);
        QRGenerationController generator2 = new QRGenerationController(testString2);
        QRGenerationController generator3 = new QRGenerationController(testString3);
        QRGenerationController generator4 = new QRGenerationController(testString4);
        QRGenerationController generator5 = new QRGenerationController(testString5);
        QRGenerationController generator6 = new QRGenerationController(testString6);

        assert (generator1.getHash().length() == 64);
        assert (generator2.getHash().length() == 64);
        assert (generator3.getHash().length() == 64);
        assert (generator4.getHash().length() == 64);
        assert (generator5.getHash().length() == 64);
        assert (generator6.getHash().length() == 64);

        for (int i = 0; i < 64; i++) {
            assert (generator1.getHash().charAt(i) >= '0' && generator1.getHash().charAt(i) <= '9' | generator1.getHash().charAt(i) >= 'A' && generator1.getHash().charAt(i) <= 'F');
            assert (generator2.getHash().charAt(i) >= '0' && generator2.getHash().charAt(i) <= '9' | generator2.getHash().charAt(i) >= 'A' && generator2.getHash().charAt(i) <= 'F');
            assert (generator3.getHash().charAt(i) >= '0' && generator3.getHash().charAt(i) <= '9' | generator3.getHash().charAt(i) >= 'A' && generator3.getHash().charAt(i) <= 'F');
            assert (generator4.getHash().charAt(i) >= '0' && generator4.getHash().charAt(i) <= '9' | generator4.getHash().charAt(i) >= 'A' && generator4.getHash().charAt(i) <= 'F');
            assert (generator5.getHash().charAt(i) >= '0' && generator5.getHash().charAt(i) <= '9' | generator5.getHash().charAt(i) >= 'A' && generator5.getHash().charAt(i) <= 'F');
            assert (generator6.getHash().charAt(i) >= '0' && generator6.getHash().charAt(i) <= '9' | generator6.getHash().charAt(i) >= 'A' && generator6.getHash().charAt(i) <= 'F');

        }

    }
}
