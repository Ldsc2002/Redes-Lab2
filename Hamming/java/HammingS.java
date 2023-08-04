import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
//import java.util.Arrays;

public class HammingS {

    public static int calculateParityBits(int m) {
        int r = 1;
        while (Math.pow(2, r) < m + r + 1) {
            r++;
        }
        return r;
    }

    public static String generateHammingCode(String binaryPlot) {
        int m = binaryPlot.length();
        int r = calculateParityBits(m);
        int n = m + r;

        char[] hammingCode = new char[n];

        for (int i = 0; i < r; i++) {
            hammingCode[(int) Math.pow(2, i) - 1] = '0';
        }

        List<Integer> trueMessageBitsPos = new ArrayList<>();
        List<Integer> parityBitsPos = new ArrayList<>();
        //System.out.println("Trama: " + Arrays.toString(hammingCode));
        for (int i = 0; i < n; i++) {
            if (hammingCode[i] == 0) {
                trueMessageBitsPos.add(i + 1);
                hammingCode[i] = binaryPlot.charAt(0);
                binaryPlot = binaryPlot.substring(1);
            } else {
                parityBitsPos.add(i + 1);
            }
        }
        //System.out.println("Trama: " + Arrays.toString(hammingCode));
        //System.out.println("Posiciones de bits de paridad: " + parityBitsPos);
        //System.out.println("Posiciones de bits de mensaje: " + trueMessageBitsPos);
        Map<Integer, List<Integer>> table = new HashMap<>();
        for (int i : trueMessageBitsPos) {
            table.put(i, sumPowersOf2(i));
        }

        for (int e : parityBitsPos) {
            calculateParityBit(table, e, hammingCode);
        }

        return new String(hammingCode);
    }

    public static void calculateParityBit(Map<Integer, List<Integer>> table, int bit, char[] trama) {
        List<Integer> l = new ArrayList<>();
        for (int key : table.keySet()) {
            if (table.get(key).contains(bit)) {
                l.add(key-1);
            }
        }
        List<Character> parityCheck = new ArrayList<>();
        for (int i : l) {
            parityCheck.add(trama[i - 1]);
        }
        trama[bit - 1] = evenParity(parityCheck);
    }

    public static char evenParity(List<Character> bits) {
        int count = 0;
        for (char c : bits) {
            if (c == '1') {
                count++;
            }
        }
        return (count % 2 == 0) ? '0' : '1';
    }

    public static boolean isPowerOf2(int n) {
        return n != 0 && (n & (n - 1)) == 0;
    }

    public static List<Integer> sumPowersOf2(int number) {
        if (isPowerOf2(number)) {
            System.out.println(number + " is already a power of 2.");
            return null;
        }

        int currentNumber = number;
        List<Integer> powers = new ArrayList<>();

        while (currentNumber > 0) {
            int powerOf2 = 1;
            int acum = 0;
            while (powerOf2 <= currentNumber) {
                powerOf2 *= 2;
                acum++;
            }

            powerOf2 /= 2;
            currentNumber -= powerOf2;
            powers.add(acum - 1);
        }

        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < powers.size(); i++) {
            positions.add((int) Math.pow(2, powers.get(i)));
        }

        return positions;
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese la trama: ");
        String trama = scanner.nextLine();
        scanner.close();
        System.out.println("Trama original: " + trama);
        String hammingCode = generateHammingCode(trama);
        System.out.println("Trama en código de Hamming: " + hammingCode);
    }
}
