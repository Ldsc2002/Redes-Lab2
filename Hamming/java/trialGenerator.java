import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class trialGenerator {
    public static String[] generate() {
        String filePath = "./Hamming/java/trials.txt"; // Replace with the actual file path
        int targetLength = 10000;

        List<String> lines = new ArrayList<>();

        while (lines.size() < targetLength) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    lines.add(line);
                    if (lines.size() >= targetLength) {
                        break;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String[] lyricsArray = lines.toArray(new String[0]);

        return lyricsArray;
    }

    public static void main(String[] args) {
        String[] lyrics = generate();
        for (String line : lyrics) {
            System.out.println(line);
        }
    }
}
