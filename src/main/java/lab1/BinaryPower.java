package lab1;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Scanner;

/** 3.  Реализовать операцию возведения по модулю в степень методом двоичного потенцирования **/
public class BinaryPower {

    public static BigInteger binpow(BigInteger a, BigInteger n) {
        // a^0 == 1
        if (n.equals(BigInteger.ZERO)) {
            return BigInteger.ONE;
        }

        // a^1 == a
        if (n.equals(BigInteger.ONE)) {
            return a;
        }

        // n непарне
        if (n.mod(BigInteger.TWO).equals(BigInteger.ONE)) {
            return a.multiply(binpow(a, n.subtract(BigInteger.ONE)));
        }

        // n парне
        else {
            BigInteger b = binpow(a, n.divide(BigInteger.TWO));
            return b.multiply(b);
        }
    }

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("Input number: ");
        BigInteger numb = new BigDecimal(scan.nextLine()).toBigInteger();

        System.out.print("Input degree: ");
        BigInteger deg = new BigDecimal(scan.nextLine()).toBigInteger();

        System.out.println(binpow(numb, deg));
    }
}