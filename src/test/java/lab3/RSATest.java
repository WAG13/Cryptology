package lab3;

import lab3.RSA;
import lab3.utils.RSAUtils;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RSATest {

    public final String message = "This is my message";

    @Test
    void encryptAndDecrypt() {
        RSA rsa = new RSA();
        BigInteger number = new BigInteger("123456789");
        BigInteger encryptNumber = rsa.encrypt(number);
        assertEquals(0, rsa.decrypt(encryptNumber).compareTo(number));
    }

    @Test
    void encryptAndDecryptMessage() {
        RSA rsa = new RSA();
        List<BigInteger> encryptMessage = rsa.encryptMessage(message);
        assertEquals(message,  RSAUtils.bigIntegerToString(rsa.decryptMessages(encryptMessage)));
    }

    @Test
    void signAndVerifyMessage() {
        RSA rsa = new RSA();
        List<BigInteger> signed = rsa.signMessage(message);
        List<BigInteger> verify = rsa.verify(signed);
        assertEquals(message, RSAUtils.bigIntegerToString(verify));
    }

}