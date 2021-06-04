package lab3;

import lab1.MillerRabin;
import lab3.utils.RSAUtils;

import java.io.*;
import java.math.BigInteger;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * RSA algorithm
 **/

public class RSA implements Serializable {

    private final static BigInteger ONE = BigInteger.ONE;
    private final BigInteger privateKey;
    private final BigInteger p;
    private final BigInteger q;
    private final BigInteger modulus;
    private BigInteger e;

    public RSA(BigInteger p, BigInteger q, BigInteger e) {
        BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE)); //phi = (p-1)*(q-1)
        this.e = e;
        this.p = p;
        this.q = q;
        modulus = p.multiply(q);
        privateKey = e.modInverse(phi);
    }

    public RSA() {
        int length = 2048; //length of prime
        p = getPrime(length);
        q = getPrime(length);
        modulus = p.multiply(q);
        BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE)); //phi = (p-1)*(q-1)
        e = getPrime(6);
        while (!e.gcd(phi).equals(ONE)) {
            e = getPrime(6);
        }
        privateKey = e.modInverse(phi);
    }

    public BigInteger getPrime(int length) {
        BigInteger prime = new BigInteger(length, 1, new Random());
        while (!MillerRabin.isProbablePrime(prime, 3)) {
            prime = new BigInteger(length, 1, new Random());
        }
        return prime;
    }

    public BigInteger encrypt(BigInteger bigInteger) {
        if (isModulusSmallerThanMessage(bigInteger))
            throw new IllegalArgumentException("Cannot cypher");
        return bigInteger.modPow(e, modulus);
    }

    public List<BigInteger> encryptMessage(final String message) {
        List<BigInteger> toEncrypt = new ArrayList<BigInteger>();
        BigInteger messageBytes = new BigInteger(message.getBytes());
        if (isModulusSmallerThanMessage(messageBytes)) {
            toEncrypt = getValidEncryptionBlocks(RSAUtils.splitMessages(new ArrayList<String>() {{
                add(message);
            }}));
        } else {
            toEncrypt.add((messageBytes));
        }

        List<BigInteger> encrypted = new ArrayList<BigInteger>();
        for (BigInteger bigInteger : toEncrypt) {
            encrypted.add(encrypt(bigInteger));
        }
        return encrypted;
    }

    public BigInteger decrypt(BigInteger encrypted) {
        return encrypted.modPow(privateKey, modulus);
    }

    public List<BigInteger> decryptMessages(List<BigInteger> encryption) {
        List<BigInteger> decryption = new ArrayList<BigInteger>();
        for (BigInteger bigInteger : encryption) {
            decryption.add(decrypt(bigInteger));
        }
        return decryption;
    }

    public BigInteger sign(BigInteger bigInteger) {
        return bigInteger.modPow(privateKey, modulus);
    }

    public List<BigInteger> signMessage(final String message) {
        List<BigInteger> toSign = new ArrayList<BigInteger>();
        BigInteger messageBytes = new BigInteger(message.getBytes());
        if (isModulusSmallerThanMessage(messageBytes)) {
            toSign = getValidEncryptionBlocks(RSAUtils.splitMessages(new ArrayList<String>() {
                {
                    add(message);
                }
            }));
        } else {
            toSign.add((messageBytes));
        }
        List<BigInteger> signed = new ArrayList<BigInteger>();
        for (BigInteger bigInteger : toSign) {
            signed.add(sign(bigInteger));
        }
        return signed;
    }

    public BigInteger verifySignedMessage(BigInteger signedMessage) {
        return signedMessage.modPow(e, modulus);
    }

    public List<BigInteger> verify(List<BigInteger> signedMessages) {
        List<BigInteger> verification = new ArrayList<BigInteger>();
        for (BigInteger bigInteger : signedMessages) {
            verification.add(verifySignedMessage(bigInteger));
        }
        return verification;
    }

    public boolean isVerified(BigInteger signedMessage, BigInteger message) {
        return verifySignedMessage(signedMessage).equals(message);
    }

    private List<BigInteger> getValidEncryptionBlocks(List<String> messages) {
        List<BigInteger> validBlocks = new ArrayList<BigInteger>();
        BigInteger messageBytes = new BigInteger(messages.get(0).getBytes());
        if (!isModulusSmallerThanMessage(messageBytes)) {
            for (String msg : messages) {
                validBlocks.add(new BigInteger(msg.getBytes()));
            }
            return validBlocks;
        } else
            return getValidEncryptionBlocks(RSAUtils.splitMessages(messages));
    }

    public List<BigInteger> messageToDecimal(final String message) {
        List<BigInteger> toDecimal = new ArrayList<BigInteger>();
        BigInteger messageBytes = new BigInteger(message.getBytes());
        if (isModulusSmallerThanMessage(messageBytes)) {
            toDecimal = getValidEncryptionBlocks(RSAUtils.splitMessages(new ArrayList<String>() {
                {
                    add(message);
                }
            }));
        } else toDecimal.add((messageBytes));

        return new ArrayList<BigInteger>(toDecimal);
    }

    private boolean isModulusSmallerThanMessage(BigInteger messageBytes) {
        return modulus.compareTo(messageBytes) < 0;
    }

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in, "US-ASCII"));
        System.out.print("Message: ");
        String message = in.readLine();

        RSA RSA = new RSA();

        List<BigInteger> decimalMessage = RSA.messageToDecimal(message);
        List<BigInteger> encryption = RSA.encryptMessage(message);
        List<BigInteger> signed = RSA.signMessage(message);
        List<BigInteger> decrypt = RSA.decryptMessages(encryption);
        List<BigInteger> verify = RSA.verify(signed);

        System.out.println("message(plain text)   = " + RSAUtils.bigIntegerToString(decimalMessage));
        System.out.println("message(decimal)      = " + RSAUtils.bigIntegerSum(decimalMessage));
        System.out.println("encripted(decimal)    = " + RSAUtils.bigIntegerSum(encryption));
        System.out.println("decrypted(decimal)    = " + RSAUtils.bigIntegerSum(decrypt));
        System.out.println("decrypted(plain text) = " + RSAUtils.bigIntegerToString(decrypt));
        System.out.println("signed(decimal)       = " + RSAUtils.bigIntegerSum(signed));
        System.out.println("verified(decimal)     = " + RSAUtils.bigIntegerSum(verify));
        System.out.println("verified(plain text)  = " + RSAUtils.bigIntegerToString(verify));
    }

}
