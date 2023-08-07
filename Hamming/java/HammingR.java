import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.lang.Math;
import java.util.HashMap;
//import java.util.Scanner;


public class HammingR {
    // Function to calculate the number of parity bits required
    private static int[] calculateParityBits(int m) {
        // given a message of length m, calculate the number of parity bits required and their positions
        int[] positions;
        int acu = 0;
        int i = 0;
        while (acu < m) {
            acu += Math.pow(2, i);
            i++;
        }
        positions = new int[i];
        for (int j = 0; j < i; j++) {
            positions[j] = (int) Math.pow(2, j);
        }
        //System.out.println("Number of parity bits required: " + positions.length);
        return positions;
            
    }

    
    //public static void main(String[] args) 
    public static String caracter(String receivedHammingCode)
    {
        //Scanner input = new Scanner(System.in);
        // Received Hamming code (You can receive this code from the transmitter)
        //String receivedHammingCode = "00100001001";
        //receivedHammingCode = "01100001001"; // el segundo bit esta mal
        //receivedHammingCode = "00100101001"; // el sexto bit esta mal
        // el sexto y el segundo bit estan mal
        //receivedHammingCode = "00100111011";

        // input code
        //receivedHammingCode = input.nextLine();
        //input.close();

        //System.out.println("Received Hamming code: " + receivedHammingCode);
        int m = receivedHammingCode.length();
        int[] r = calculateParityBits(m);
        String receivedParityBits = "";
        //String r_String = Arrays.toString(r);
        //System.out.println(r_String);
    
        for (int i = 0; i < r.length; i++) {
            //System.out.println("r[" + r[i] + "] = " + receivedHammingCode.charAt(r[i] - 1));
            receivedParityBits += receivedHammingCode.charAt(r[i] - 1);
        }

        int[] m_pos = new int[m - r.length];
        int acu = 0;

        for (int i = 1; i <= receivedHammingCode.length(); i++) {
            if (isInArray(r, i) == false) {
                //receivedHammingCode = receivedHammingCode.substring(0, i - 1) + receivedHammingCode.substring(i);
                //System.out.println("i = " + i);
                m_pos[acu] = i;
                acu++;
            }
        }
        //System.out.println("Received parity bits: " + receivedParityBits);
        //System.out.println(Arrays.toString(m_pos));
        String new_parity_bits = generate_hamming_parity(receivedHammingCode, r, m_pos);
        //System.out.println("New parity bits: " + new_parity_bits);
        String letter = "";
        int error = findError(receivedParityBits, new_parity_bits);
        if (error > 0){
            String correctedHammingCode = fixError(receivedHammingCode, error);
            //System.out.println("Corrected Hamming code: " + correctedHammingCode);
            String correctedMessage = original(correctedHammingCode, r);
            //System.out.println("Corrected message: " + correctedMessage);
            letter = correctedMessage;
        }
        else {
            String originalMessage = original(receivedHammingCode, r);
            //System.out.println("Mensaje: " +  originalMessage);
            letter = originalMessage;
        }
        letter = fromBinaryToAscii(letter);
        return letter;
        
    }

    public static String fromBinaryToAscii(String b){
        String ascii = "";
        int decimal_value = Integer.parseInt(b, 2);
        char c = (char) decimal_value;
        ascii += c;
        return ascii;
    }


    public static void main(String[] args) {
        // ingresar el arreglo de códigos hamming
        String[] helloWorld = {"01100001100", "01111000001", "01101101010", "10111011011", "0010000000", "00110101011", "11111010111", "11101101100", "00111011100", "01101101011", "0010000000", "0011100111"};
        String mensaje = "";
        for (int i = 0; i < helloWorld.length; i++) {
            mensaje += caracter(helloWorld[i]);
        }
        System.out.println("El mensaje es: " + mensaje);
        String[] helloWorld2 = {"01100001100", "10111001001", "01101101010", "00101010011", "1110000000", "00110100011", "10101011111", "01101100101", "00101011100", "10101100011", "1110000000", "1011100011"};
        String mensaje2 = "";
        for (int i = 0; i < helloWorld2.length; i++) {
            mensaje2 += caracter(helloWorld2[i]);
        }
        System.out.println("El mensaje es: " + mensaje2);
    }


    public static int fromBinaryToDecimal(String binary) {
        int decimal = 0;
        int n = binary.length();
        for (int i = 0; i < n; i++) {
            decimal += Integer.parseInt(binary.charAt(i) + "") * Math.pow(2, n - i - 1);
        }
        return decimal;
    }

    public static boolean isInArray(int[] array, int value) {
        for (int i = 0; i < array.length; i++) {
            if (value == array[i]) {
                return true;
            }
        }
        return false;
    }

    public static char even_parity(int[] bits){
        int count = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == 1) {
                count++;
            }
        }
        if (count % 2 == 0) {
            return '0';
        }
        else {
            return '1';
        }
    }
        
    public static char odd_parity(int[] bits){
        int count = 0;
        for (int i = 0; i < bits.length; i++) {
            if (bits[i] == 1) {
                count++;
            }
        }
        if (count % 2 == 0) {
            return '1';
        }
        else {
            return '0';
        }
    }
    
    public static String generate_hamming_parity(String bits, int[] r, int[] m ){
        HashMap<Integer, Integer[]> map = new HashMap<Integer, Integer[]>();

        // Guardar en un mapa los valores de las potencias de 2 de cada m
        for (int i = 0; i < m.length; i++){
            int[] powers = sum_powers_of_2(m[i]);
            //System.out.println("Powers of " + m[i] + ": " + Arrays.toString(powers));
            Integer[] powers_obj = new Integer[powers.length];
            for (int j = 0; j < powers.length; j++) {
                powers_obj[j] = powers[j];
            }
            map.put(m[i], powers_obj);
        }
        // print the hashmap

        /*
         * for (Map.Entry<Integer, Integer[]> entry : map.entrySet()) {
            Integer[] values = entry.getValue();
            System.out.println(entry.getKey() + " " + Arrays.toString(values));
        }
         */

        //System.out.println("Bits: " + Arrays.toString(r));

        List <int[]> parities = new ArrayList<int[]>();
        for (int i = 0; i < r.length; i++) {
            //int parity = calc_parity(map, r[i], bits);
            //System.out.println("Parity: " + parity);
            parities.add(calc_parity(map, r[i], bits));
        }

        /*for (int i = 0; i < parities.size(); i++) {
            System.out.println("Parity " + (i + 1) + ": " + Arrays.toString(parities.get(i)));
        }*/

        String new_parity_bits = "";
        for (int i = 0; i < parities.size(); i++) {
            new_parity_bits += even_parity(parities.get(i));
        }
        
        return new_parity_bits;
    }

    public static int[] calc_parity(HashMap<Integer, Integer[]> map, int bit, String plot){

        List <Integer> powers = new ArrayList<Integer>();
        for (Map.Entry<Integer, Integer[]> entry : map.entrySet()) {
            Integer[] values = entry.getValue();
            //System.out.println(" - " + entry.getKey() + " -- " + Arrays.toString(values));
            int[] values_int = new int[values.length];
            for (int i = 0; i < values.length; i++) {
                values_int[i] = values[i];
            }
            //System.out.println(" - " + entry.getKey() + " -- " + Arrays.toString(values_int));
            //System.out.println("b = " + bit);
            if (isInArray(values_int, bit)) {
                powers.add(entry.getKey());
            }
        }
        
        List <Integer> parity_check = new ArrayList<Integer>();

        
        for (int i = 0; i < powers.size(); i++) {
            //System.out.println(" - " + powers.get(i) + " -- " + plot.charAt(powers.get(i) - 1));
            parity_check.add(Integer.parseInt(plot.charAt(powers.get(i) - 1) + ""));
        }

        //System.out.println(" - " + bit + " -- " + parity_check);
        int[] parity_check_array = new int[parity_check.size()];
        for (int i = 0; i < parity_check.size(); i++) {
            parity_check_array[i] = parity_check.get(i);
        }
        return parity_check_array;
    }

    public static int[] sum_powers_of_2(int v){

        int copy_v = v;
        List <Integer> powers = new ArrayList<Integer>();
        while (copy_v > 0) {
            int power = 1;
            int acu = 0;
            while (power <= copy_v) {
                power *= 2;
                acu++;
            }
            power /= 2;
            copy_v -= power;
            powers.add(acu - 1);
        }

        //System.out.println(" - " + v + " -- " + powers);

        int[] powers_array = new int[powers.size()];
        for (int i = 0; i < powers.size(); i++) {
            double potencia = Math.pow(2, powers.get(i));
            powers_array[i] = (int) potencia;
        }
        return powers_array;
    }

    public static int findError(String b1, String b2){
        String b3 = "";
        for (int i = 0; i < b1.length(); i++) {
            if (b1.charAt(i) == b2.charAt(i)) {
                b3 += '0';
            }
            else {
                b3 += '1';
            }
        }

        //System.out.println("b3: " + b3);
        b3 = reverseStringUsingLoop(b3);
        //System.out.println("b3: " + b3);
        
        int error = fromBinaryToDecimal(b3);
        //System.out.println("Error: " + error);
        if (error == 0){
            //System.out.println("No hay error");
            return 0;
        }
        else {
            System.out.println("Error en la posicion: " + error);
            return error;
        }
    }

    public static String reverseStringUsingLoop(String input) {
        StringBuilder reversed = new StringBuilder();

        for (int i = input.length() - 1; i >= 0; i--) {
            reversed.append(input.charAt(i));
        }

        return reversed.toString();
    }

    public static String fixError(String bits, int error){
        String new_bits = "";
        for (int i = 0; i < bits.length(); i++) {
            if (i == error - 1) {
                if (bits.charAt(i) == '0') {
                    new_bits += '1';
                }
                else {
                    new_bits += '0';
                }
            }
            else {
                new_bits += bits.charAt(i);
            }
        }
        return new_bits;
    }

    public static String original(String bits, int[] r){
        // remove parity bits
        String new_bits = "";
        
        for (int i = 0; i < bits.length(); i++) {
            if (!isInArray(r, i + 1)) {
                new_bits += bits.charAt(i);
            }
        }
        return new_bits;

    }
}
