package rsa.company;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

public class Main {
    static BigInteger generateE(BigInteger phi) {
        BigInteger e = BigInteger.ZERO;
        do {
            if (e.equals(BigInteger.ZERO)) {
                e = BigInteger.valueOf(256).pow(2).nextProbablePrime();
            } else {
                e = e.nextProbablePrime();
            }
        } while (!e.gcd(phi).equals(BigInteger.ONE));

        return e;
    }

    static BigInteger signMessage(String message, BigInteger d, BigInteger n) throws NoSuchAlgorithmException {
        byte[] messageBytes = message.getBytes();
        byte[] hashMessage = MessageDigest.getInstance("SHA-256").digest(messageBytes);
        BigInteger messageInteger = new BigInteger(hashMessage);

        BigInteger sign = messageInteger.modPow(d, n);
        System.out.println("Message: " + message);
        System.out.println("Sign: " + sign);

        return sign;
    }

    static Boolean verifySign(BigInteger sign, BigInteger e, BigInteger n) {
        BigInteger verifySign = sign.modPow(e, n);
        return sign.compareTo(verifySign) == 0;
    }

    static void experiment1(BigInteger sign, BigInteger e, BigInteger n) {
        BigInteger s = sign.subtract(BigInteger.valueOf(3));
        BigInteger m = s.modPow(e, n);
        Boolean isSignVerified = verifySign(s, e, n);

        System.out.println("\nExperiment 1:");
        System.out.println(m);
        System.out.println("Sign is " + (isSignVerified ? "valid" : "invalid"));
    }

    static void experiment2(BigInteger d, BigInteger n) {
        String m1 = "Chcialbym to podpisac";
        String m2 = "Pathfinder";

        BigInteger m1Int = new BigInteger(m1.getBytes());
        BigInteger m2Int = new BigInteger(m2.getBytes());
        BigInteger m = m1Int.multiply(m2Int).mod(n);

        BigInteger s1 = m1Int.modPow(d, n);
        BigInteger s2 = m2Int.modPow(d, n);
        BigInteger s3 = s1.multiply(s2).mod(n);

        System.out.println("\nExperiment 2:");
        System.out.println(s3);
    }

    static void experiment3() {
        BigInteger n = new BigInteger("74863748466636279180898113945573168800167314349007339734664557033677222985045895878321130196223760783214379338040678233908010747773264003237620590141174028330154012139597068236121542945442426074367017838349905866915120469978361986002240362282392181726265023378796284600697013635003150020012763665368297013349");
        BigInteger e = BigInteger.valueOf(3);
        BigInteger cInt = new BigInteger("2829246759667430901779973875");

        BigInteger m4 = cInt.modPow(e, n);
        String m4String = m4.toString();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < m4String.length(); i++) {
            String hs = m4String.substring(i, 2);
            sb.append((Integer.valueOf(hs)));
        }

        System.out.println("\nExperiment 3:");
        System.out.println(m4);
    }

    public static void main(String[] args) throws NoSuchAlgorithmException {
        int countOfBits = 4096;
        Random rand = new Random();

        BigInteger p = BigInteger.probablePrime(countOfBits / 2, rand);
        BigInteger q = BigInteger.probablePrime(countOfBits / 2, rand);

        BigInteger n = p.multiply(q);

        BigInteger pSubstract1 = p.subtract(BigInteger.ONE);
        BigInteger qSubstract1 = q.subtract(BigInteger.ONE);
        BigInteger phi = pSubstract1.multiply(qSubstract1);

        BigInteger e = generateE(phi);
        BigInteger d = e.modInverse(phi);

        Boolean test = e.multiply(d).mod(phi).equals(BigInteger.ONE);
        System.out.println("Test e*d == 1: " + (test ? "test passed" : "test didn't pass"));

        String message = "Chcialbym to podpisac";
        BigInteger sign = signMessage(message, d, n);
        Boolean isSignVerified = verifySign(sign, e, n);
        System.out.println("Sign is " + (isSignVerified ? "valid" : "invalid"));

        experiment1(sign, e, n);
        experiment2(d, n);
//        experiment3();
    }
}
