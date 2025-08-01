import javax.swing.*;
import java.awt.*;
import java.io.InputStream;
import java.util.Enumeration;

public class FontLoader {

    public static void setGlobalFont(String fontPath, float size) {
        try {
            InputStream is = FontLoader.class.getResourceAsStream(fontPath);
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(size);

            // Set this font globally for all Swing components
            Enumeration<Object> keys = UIManager.getDefaults().keys();
            while (keys.hasMoreElements()) {
                Object key = keys.nextElement();
                Object value = UIManager.get(key);
                if (value instanceof Font) {
                    UIManager.put(key, customFont);
                }
            }
            UIManager.put("Button.font", customFont.deriveFont(Font.BOLD,16f));
        } catch (Exception e) {
            System.out.println("Error setting global font: " + e.getMessage());
        }
    }
}
