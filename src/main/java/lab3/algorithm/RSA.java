package lab3.algorithm;

import lab1.MillerRabin;
import lab3.algorithm.utils.RSAUtils;

import java.io.Serializable;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/** RSA algorithm   **/

public class RSA implements Serializable {


    private final static BigInteger ONE = BigInteger.ONE;
    private BigInteger privateKey;
    private BigInteger e;
    private BigInteger modulus;
    private BigInteger p;
    private BigInteger q;

    public RSA(BigInteger p, BigInteger q, BigInteger e) {
        BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE)); //phi = (p-1)*(q-1)
        this.e = e;
        this.p = p;
        this.q = q;
        modulus = p.multiply(q);
        privateKey = e.modInverse(phi);
    }

    public RSA() {
        int lenth = 2048;
        p = getPrime(lenth);
        q = getPrime(lenth);
        modulus = p.multiply(q);
        BigInteger phi = (p.subtract(ONE)).multiply(q.subtract(ONE)); //phi = (p-1)*(q-1)
        e = getPrime(6);
        while (!e.gcd(phi).equals(BigInteger.ONE)){
            e = getPrime(6);
        }
        privateKey = e.modInverse(phi);
    }

    public BigInteger getPrime (int length){
        BigInteger prime = new BigInteger(length,1,new Random());
        while (!MillerRabin.isProbablePrime(prime,3)){
            prime = new BigInteger(length,1,new Random());
        }
        return prime;
    }


    /**
     * Шифрування
     */

    public BigInteger encrypt(BigInteger bigInteger) {
        if (isModulusSmallerThanMessage(bigInteger))
            throw new IllegalArgumentException("Cannot cypher");
        return bigInteger.modPow(e, modulus);
    }

    public List<BigInteger> encryptMessage(final String message) {
        List<BigInteger> toEncrypt = new ArrayList<BigInteger>();
        BigInteger messageBytes = new BigInteger(message.getBytes());
        if (isModulusSmallerThanMessage(messageBytes)) {
            toEncrypt = getValidEncryptionBlocks(RSAUtils.splitMessages(new ArrayList<String>() {
                {
                    add(message);
                }
            }));
        } else {
            toEncrypt.add((messageBytes));
        }

        List<BigInteger> encrypted = new ArrayList<BigInteger>();
        for (BigInteger bigInteger : toEncrypt) {
            encrypted.add(encrypt(bigInteger));
        }
        return encrypted;
    }

    /**
     * Розшифровування
     */

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

    /**
     * Digital signature
     */

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

    /**
     * Verification
     */

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

    public String toString() {
        String s = "";
        s += "p                     = " + p + "\n";
        s += "q                     = " + q + "\n";
        s += "e                     = " + e + "\n";
        s += "privateKey            = " + privateKey + "\n";
        s += "modulus               = " + modulus;
        return s;
    }

}
