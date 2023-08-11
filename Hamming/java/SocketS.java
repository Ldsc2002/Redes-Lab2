import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class SocketS {

    public static String[] generate() {
        String filePath = "./Hamming/message.txt"; // Replace with the actual file path

        List<String> lines = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        String[] arr = lines.toArray(new String[lines.size()]);
        System.out.println(arr.length);

        return arr;
    }

    private static String HOST = "127.0.0.1";
    private static int PORT = 65432;
    static String[] lyrics = generate();






    public static void main(String[] args) throws IOException, UnknownHostException, InterruptedException {

        System.out.println("Emisor Java Sockets\n");
        int i = 0;
        Socket socketCliente = new Socket(InetAddress.getByName(HOST), PORT);
        OutputStreamWriter writer = new OutputStreamWriter(socketCliente.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        while (i < lyrics.length) {

            //System.out.println("Enviando Data\n");
            String dummy = lyrics[i];
            System.out.print("Ingrese el mensaje: " + dummy + "\n");
            //String dummy = scanner.nextLine();
            
            HammingS h = new HammingS();
            String[] hamming = h.SendMessage(dummy);
            String payload = Arrays.toString(hamming);
            
            writer.write(payload);
            writer.flush();  
            //System.out.println("Jaghatai Khan\n");
            //System.out.println("Leman Russ\n");
            //System.out.println("Rogal Dorn\n");

            Thread.sleep(100);
            i++;
        }
        scanner.close();
        writer.close();
        socketCliente.close(); 
    }
}
