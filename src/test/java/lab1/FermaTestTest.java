package lab1;

import java.math.BigDecimal;
import java.math.BigInteger;

import lab1.FermaTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class FermaTestTest {

    @Test
    void checkPrime() {
        assertTrue(FermaTest.checkPrime(new BigInteger("7"), 20));
        assertFalse(FermaTest.checkPrime(new BigInteger("1"), 20));
        assertFalse(FermaTest.checkPrime(new BigDecimal("1.34078e161").toBigInteger(), 20));
    }
}