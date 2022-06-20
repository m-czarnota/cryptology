package org.example;

import demo.math.bls.BLSDemo;
import iaik.security.ec.common.SecurityStrength;
import iaik.security.ec.math.curve.*;
import iaik.security.ec.math.field.GenericField;
import iaik.security.ec.math.field.GenericFieldElement;
import iaik.security.ec.math.curve.ECPoint;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class Main {
    public static void main(String[] args) {
        // write your code here
//        BLSDemo demo = new BLSDemo();
//        demo.run();

//        paringtests();
        zad2();
    }

    public static void paringtests() {
        //a - inicjalizacja
        int size = 160;
        //int size = 256;
        //int size = 384;
        //int size = 512;
        //int size = 638;

        //inicjalizacja odwzorowania typu 3
        final Pairing pair3 = AtePairingOverBarretoNaehrigCurveFactory.getPairing(PairingTypes.TYPE_3, size);
        EllipticCurve curve1 = pair3.getGroup1();
        EllipticCurve curve2 = pair3.getGroup2();

        //b - liczby losowe
        final SecureRandom random = SecurityStrength.getSecureRandom(
                SecurityStrength.getSecurityStrength(curve1.getField().getFieldSize()));

        //tworzenie liczby losowej
        BigInteger s_TA = new BigInteger(size - 1, random);

        //c - pobieranie generatora dla danej krzywej
        ECPoint P = curve1.getGenerator();
        ECPoint Q = curve2.getGenerator();

        //mnozenie punktu przez skalar
        //jak potrzebujemy losowy punkt na krzywej, to losujemy punkt i mnozymy przez skalar
        ECPoint P_0 = P.multiplyPoint(s_TA);
        ECPoint Q_0 = Q.multiplyPoint(s_TA);

        //obliczanie odwzorowania
        GenericFieldElement e1 = pair3.pair(P, Q_0);
        GenericFieldElement e2 = pair3.pair(P_0, Q);
        System.out.println(e1);
        System.out.println(e2);

        if (e1.equals(e2)) {
            System.out.println("Wszystko dziala jak powinno :)");
        }

        //obliczanie skrotow i tworzenie z nich liczby BigInt
        //jak chcemy jako wyjscie funkcji skrotu otrzymac punkt to przemnazamy przez generator
        try {
            String tmp = "tutaj dowlna wiadomosc";
            tmp += P_0.toString();
            tmp += e1.toString();

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(tmp.getBytes());

            BigInteger result = new BigInteger(1, messageDigest);
            ECPoint result2 = P.multiplyPoint(result);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    public static void zad2() {
        //a - inicjalizacja
        int size = 160;
        //int size = 256;
        //int size = 384;
        //int size = 512;
        //int size = 638;

        //inicjalizacja odwzorowania typu 3
        final Pairing pair3 = AtePairingOverBarretoNaehrigCurveFactory.getPairing(PairingTypes.TYPE_3, size);
        EllipticCurve curve1 = pair3.getGroup1();
        EllipticCurve curve2 = pair3.getGroup2();

        //b - liczby losowe
        final SecureRandom random = SecurityStrength.getSecureRandom(
                SecurityStrength.getSecurityStrength(curve1.getField().getFieldSize()));

        //tworzenie liczby losowej
        BigInteger s_TA = new BigInteger(size - 1, random);
        BigInteger s_TB = new BigInteger(size - 1, random);

        //c - pobieranie generatora dla danej krzywej
        ECPoint P = curve1.getGenerator();
        ECPoint Q = curve2.getGenerator();

        //mnozenie punktu przez skalar
        //jak potrzebujemy losowy punkt na krzywej, to losujemy punkt i mnozymy przez skalar
        ECPoint P_0 = P.multiplyPoint(s_TA);
        ECPoint Q_0 = Q.multiplyPoint(s_TA);

        ECPoint P_1 = P.multiplyPoint(s_TB);
        ECPoint Q_1 = Q.multiplyPoint(s_TB);

        //obliczanie odwzorowania
        GenericFieldElement e1 = pair3.pair(P, Q_0);
        GenericFieldElement e2 = pair3.pair(P_0, Q);
        GenericFieldElement e3 = pair3.pair(P, Q_1);
        GenericFieldElement e4 = pair3.pair(P_1, Q);

        if (e1.equals(e2) && e3.equals(e4)) {
            System.out.println("Wszystko dziala jak powinno :)");
        }

        //obliczanie skrotow i tworzenie z nich liczby BigInt
        //jak chcemy jako wyjscie funkcji skrotu otrzymac punkt to przemnazamy przez generator
        try {
            String tmp = "tutaj dowlna wiadomosc";
            tmp += P_0.toString();
            tmp += e1.toString();
            tmp += P_1.toString();
            tmp += e3.toString();

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(tmp.getBytes());

            BigInteger result = new BigInteger(1, messageDigest);
            ECPoint result2 = P.multiplyPoint(result);
            ECPoint result3 = Q.multiplyPoint(result);

            BigInteger resultFinal = new BigInteger(1, result2.encodePoint()).multiply(new BigInteger(1, result3.encodePoint()));
            System.out.println(resultFinal.toString());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

