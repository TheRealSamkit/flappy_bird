import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class GameData {
    private static final String DATA_FILE = "gameData.txt";
    private static Map<String, String> data = new HashMap<>();

    static {
        load();
    }

    // Load data from file and return the map
    public static Map<String, String> load() {
        data.clear();
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            // Defaults if no file exists
            data.put("skins", "default");
            data.put("highScore", "0");
            save();
            return data;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("=")) {
                    String[] parts = line.split("=", 2);
                    data.put(parts[0].trim(), parts[1].trim());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return data; // return loaded data
    }

    // Save data to file
    public static void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (Map.Entry<String, String> entry : data.entrySet()) {
                bw.write(entry.getKey() + "=" + entry.getValue());
                bw.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Get value by key
    public static String get(String key) {
        return data.getOrDefault(key, "");
    }

    // Set and save immediately
    public static void set(String key, String value) {
        data.put(key, value);
        save();
    }

    // High score update
    public static void updateHighScore(int score) {
        int currentHigh = Integer.parseInt(get("highScore"));
        if (score > currentHigh) {
            set("highScore", String.valueOf(score));
        }
    }
}
