package lab2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RC4Test {

    @Test
    void PRGA() {
        try {
            String key = "strongKey";
            String stringToEncrypt = "test string";

            RC4 rc4 = new RC4(key);
            char[] result = rc4.PRGA(stringToEncrypt.toCharArray());

            assertEquals("»Åd.b+Öà' f", new String(result));
            assertEquals("test string", new String(rc4.PRGA(result)));
        } catch (InvalidKeyException e) {
            System.err.println(e.getMessage());
        }
    }
}