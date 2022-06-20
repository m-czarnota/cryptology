import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class Sekret {
    private static final Integer BIT_LENGTH = 2048;

    public static void main(String[] args) {
        Integer N = 20;
        Integer a = 15;

        String message = "wiadomosc testowa";
//        lagrange1(N, a, message);
        lagrangeUniversal(N, a, message);
    }

    private static BigInteger rebuildSecret(Map<BigInteger, BigInteger> rebuildMap, BigInteger p) {
        List<BigInteger> results = new ArrayList<>();

        rebuildMap.forEach((key, value) -> {
            List<BigInteger> parts = new ArrayList<>();

            rebuildMap.forEach((key2, value2) -> {
                if (key.equals(key2)) {
                    return;
                }

                BigInteger part1 = key2.multiply(BigInteger.valueOf(-1)).mod(p);
                BigInteger part2 = key.subtract(key2).modInverse(p);
                parts.add(part1.multiply(part2));
            });

            BigInteger multiplyers = value;
            for (BigInteger part : parts) {
                multiplyers = multiplyers.multiply(part);
            }

            results.add(multiplyers.mod(p));
        });

        BigInteger endResult = BigInteger.valueOf(0);
        for (BigInteger result : results) {
            endResult = endResult.add(result);
        }

        return endResult.mod(p);
    }

    private static BigInteger calcLagrange(BigInteger a1, BigInteger a2, BigInteger p, Integer x, BigInteger message) {
        return a2
                .multiply(BigInteger.valueOf((long) Math.pow(x, 2)))
                .add(a1.multiply(BigInteger.valueOf(x)))
                .add(message)
                .mod(p);
    }

    private static BigInteger calcLagrange(BigInteger p, Integer x, BigInteger message, List<BigInteger> aBag) {
        BigInteger result = BigInteger.valueOf(0);

        for (int i = aBag.size() - 1; i >= 0; i--) {
            BigInteger toAdd = i > 0 ? aBag.get(i).multiply(BigInteger.valueOf(x).pow(i + 1)) : BigInteger.valueOf(x);
            result = result.add(toAdd);
        }

        return result.add(message).mod(p);
    }

    private static void lagrange1(Integer N, Integer a, String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.US_ASCII);
        BigInteger m = new BigInteger(messageBytes);
        System.out.println("message: " + m);

        Random random = new Random();

        BigInteger a1 = BigInteger.probablePrime(BIT_LENGTH - 1, random);
        BigInteger a2 = BigInteger.probablePrime(BIT_LENGTH - 1, random);
        BigInteger p = BigInteger.probablePrime(BIT_LENGTH, random);

        Map<Integer, BigInteger> secretParts = new HashMap<>();
        for (int i = 1; i <= N; i++) {
            secretParts.put(i, calcLagrange(a1, a2, p, i, m));
        }

        secretParts.forEach((key, value) -> System.out.println("number " + key + ": " + value));

        BigInteger recoveredSecret = getRecoveredSecret(secretParts, p, N, a);
        System.out.println("recovered secret: " + recoveredSecret);
    }

    private static void lagrangeUniversal(Integer N, Integer a, String message) {
        byte[] messageBytes = message.getBytes(StandardCharsets.US_ASCII);
        BigInteger m = new BigInteger(messageBytes);
        System.out.println("message: " + m);

        Random random = new Random();
        List<BigInteger> aBag = new ArrayList<>();
        for (int i = 0; i < a - 1; i++) {
            aBag.add(BigInteger.probablePrime(BIT_LENGTH - 1, random));
        }

        BigInteger p = BigInteger.probablePrime(BIT_LENGTH, random);
        Map<Integer, BigInteger> secretParts = new HashMap<>();

        for (int i = 1; i <= N; i++) {
            secretParts.put(i, calcLagrange(p, i, m, aBag));
        }

        secretParts.forEach((key, value) -> System.out.println("number " + key + ": " + value));

        BigInteger recoveredSecret = getRecoveredSecret(secretParts, p, N, a);
        System.out.println("recovered secret: " + recoveredSecret);
    }

    private static BigInteger getRecoveredSecret(Map<Integer, BigInteger> secretParts, BigInteger p, Integer N, Integer a) {
        Map<BigInteger, BigInteger> uniqueParts = new HashMap<>();
        while (uniqueParts.size() < a) {
            Random random = new Random();
            Integer next = random.nextInt(N) + 1;
            uniqueParts.put(BigInteger.valueOf(next), secretParts.get(next));
        }

        return rebuildSecret(uniqueParts, p);
    }
}
