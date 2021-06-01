package lab1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Random;
import java.util.Scanner;

/** 2.	Реализовать тест Миллера - Рабина **/
public class MillerRabin {

    public static boolean isProbablePrime(BigInteger n, int maxIterations) {
        if (n.compareTo(BigInteger.ONE) <= 0) return false;
        if (n.compareTo(BigInteger.TWO) == 0) return true;

        int s = 0;
        BigInteger d = n.subtract(BigInteger.ONE);        // n - 1 = 2^s * d, where d % 2 = 1

        // while d is even, make it odd dividing by two
        while (d.mod(BigInteger.TWO).equals(BigInteger.ZERO)) {
            s++;
            d = d.divide(BigInteger.TWO);
        }

        for (int i = 0; i < maxIterations; i++) {
            BigInteger a = uniformRandom(BigInteger.TWO, n.subtract(BigInteger.ONE));
            BigInteger x = a.modPow(d, n);    // x = a^d mod n

            // if x == 1 or x == (n - 1) then it may be prime
            if (x.equals(BigInteger.ONE) || x.equals(n.subtract(BigInteger.ONE)))
                continue;

            int r = 0;
            for (; r < s; r++) {
                x = x.modPow(BigInteger.TWO, n);    // x = x^2 mod n

                // n is definitely not prime
                if (x.equals(BigInteger.ONE))
                    return false;

                if (x.equals(n.subtract(BigInteger.ONE)))
                    break;
            }

            // None of the steps made x equal n - 1 then n is definitely not prime
            if (r == s)
                return false;
        }
        return true;
    }

    // Returns number between bottom and top
    private static BigInteger uniformRandom(BigInteger bottom, BigInteger top) {
        Random rnd = new Random();

        BigInteger res;
        do {
            res = new BigInteger(top.bitLength(), rnd);
        } while (res.compareTo(bottom) < 0 || res.compareTo(top) > 0);
        return res;
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Input number: ");
        BigInteger numb = new BigDecimal(scan.nextLine()).toBigInteger();

        System.out.print("Input max number of iterations: ");
        int maxIterations =  Integer.parseInt(scan.nextLine());

        System.out.println(isProbablePrime(numb, maxIterations));
    }
}