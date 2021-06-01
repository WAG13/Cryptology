package lab3.algorithm;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;

/** RSA algorithm   **/

public interface RSA extends Serializable {

    BigInteger encrypt(BigInteger bigInteger);
    List<BigInteger> encryptMessage(final String message);

    BigInteger decrypt(BigInteger encrypted);
    List<BigInteger> decryptMessages(List<BigInteger> encryption);

    BigInteger sign(BigInteger bigInteger);
    List<BigInteger> signMessage(final String message);
    List<BigInteger> messageToDecimal(final String message);

    List<BigInteger> verify(List<BigInteger> signedMessages);
    BigInteger verifySignedMessage(BigInteger signedMessage);
    boolean isVerified(BigInteger signedMessage, BigInteger message);

}
