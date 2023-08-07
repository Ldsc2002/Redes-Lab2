import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
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
        //System.out.println("Trama original: " + binaryPlot);
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
        //System.out.println("Tabla: " + table);

        for (int e : parityBitsPos) {
            //System.out.println("Calculando bit de paridad para la posición " + e);
            calculateParityBit(table, e, hammingCode);
        }
        //System.out.println("Trama: " + Arrays.toString(hammingCode));

        return new String(hammingCode);
    }

    public static void calculateParityBit(Map<Integer, List<Integer>> table, int bit, char[] trama) {
        List<Integer> l = new ArrayList<>();
        for (int key : table.keySet()) {
            if (table.get(key).contains(bit)) {
                l.add(key-1);
            }
        }
        //System.out.println("Bits a comprobar: " + l);
        List<Character> parityCheck = new ArrayList<>();
        for (int i : l) {
            parityCheck.add(trama[i]);
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


    public static String[] ASCII_to_Binary(String ascii) {
        String[] binary = new String[ascii.length()];
        for (int i = 0; i < ascii.length(); i++) {
            binary[i] = Integer.toBinaryString(ascii.charAt(i));
        }
        return binary;
    }

    public static String Noise(String b){
        StringBuilder sb = new StringBuilder();
        int seed = (int) System.currentTimeMillis();
        Random r = new Random(seed);
        for(char c : b.toCharArray()){
            double d = r.nextDouble();
            //System.out.println(" Probability_: "+d);
            if(d > 0.985){
                //System.out.println("RUIDO A LA VERGA RUIDO");
                sb.append(c == '0' ? '1' : '0');
            }else{
                sb.append(c);
            }
        }
        return sb.toString();
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingrese el mensaje: ");
        String dummy = scanner.nextLine();
        //String dummy = "Dark Souls 3";
        String[] binario = ASCII_to_Binary(dummy);
        //System.out.print("\bBinario: " + Arrays.toString(binario));
        //System.out.print("Ingrese la trama: ");
        //String trama = scanner.nextLine();
        scanner.close();
        //System.out.println("Trama original: " + trama);
        //String hammingCode = generateHammingCode(trama);
        //System.out.println("Trama en código de Hamming: " + hammingCode);
        String[] hammingCode = new String[binario.length];
        for (int i = 0; i < binario.length; i++) {
            hammingCode[i] = generateHammingCode(binario[i]);
        }
        //System.out.println("\nTrama sin ruido: " + Arrays.toString(hammingCode));
        String[] hammingCodeNoise = new String[hammingCode.length];
        for (int i = 0; i < hammingCode.length; i++) {
            hammingCodeNoise[i] = Noise(hammingCode[i]);
        }
        System.out.println("Trama con ruido: " + Arrays.toString(hammingCodeNoise));
    }
}
