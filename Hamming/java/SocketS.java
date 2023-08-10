import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Scanner;


public class SocketS {

    private static String HOST = "127.0.0.1";
    private static int PORT = 65432;

    public static void main(String[] args) throws IOException, UnknownHostException, InterruptedException {

        System.out.println("Emisor Java Sockets\n");
        int i = 0;
        Socket socketCliente = new Socket(InetAddress.getByName(HOST), PORT);
        OutputStreamWriter writer = new OutputStreamWriter(socketCliente.getOutputStream());
        Scanner scanner = new Scanner(System.in);

        while (i < 2) {

            System.out.println("Enviando Data\n");
            System.out.print("Ingrese el mensaje: ");
            String dummy = scanner.nextLine();
            
            HammingS h = new HammingS();
            String[] hamming = h.SendMessage(dummy);
            String payload = Arrays.toString(hamming);
            
            writer.write(payload);
            writer.flush();  
            //System.out.println("Jaghatai Khan\n");
            //System.out.println("Leman Russ\n");
            //System.out.println("Rogal Dorn\n");

            Thread.sleep(1000);
            i++;
        }
        scanner.close();
        writer.close();
        socketCliente.close(); 
    }
}
