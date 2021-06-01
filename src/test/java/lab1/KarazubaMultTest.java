package lab1;

import java.math.BigInteger;

import lab1.KarazubaMult;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class KarazubaMultTest {

    @Test
    void karazubaMult() {
        assertEquals(0, KarazubaMult.karazubaMult(new BigInteger("100"), new BigInteger("244")).compareTo(new BigInteger("24400")));
        assertEquals(0, KarazubaMult.karazubaMult(new BigInteger("5534535"), new BigInteger("2423454353")).compareTo(new BigInteger("13412692937580855")));
    }
}