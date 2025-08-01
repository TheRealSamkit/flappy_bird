import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GameData {
    private static final String FILE_PATH = "gameData.txt";

    // Load score and skins
    public static Map<String, String> load() {
        Map<String, String> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("=", 2);
                if (parts.length == 2) {
                    data.put(parts[0], parts[1]);
                }
            }
        } catch (IOException e) {
            // default values
            data.put("highScore", "0");
            data.put("skins", "default");
        }
        return data;
    }

    // Save updated score and skins
    public static void save(Map<String, String> data) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (String key : data.keySet()) {
                writer.write(key + "=" + data.get(key));
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving game data: " + e.getMessage());
        }
    }
}
