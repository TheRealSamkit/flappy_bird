import javax.swing.*;
import java.awt.*;

public class App  {
    public static void main(String[] args) throws Exception {
        int boardWidth = 360;
        int boardHeight = 640;
        FontLoader.setGlobalFont("assets/p-1.ttf", 24f); // font path relative to `resources`
        JFrame frame = new JFrame("Flappy Bird");
        // frame.setVisible(true);
        frame.setForeground(Color.BLACK);
		frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        homeScreen homeScreen = new homeScreen();
        frame.add(homeScreen);
        frame.pack();
        homeScreen.requestFocus();
        frame.setVisible(true);
        // FlappyBird flappyBird = new FlappyBird();
        // frame.add(flappyBird);
        // frame.pack();
        // flappyBird.requestFocus();
        // frame.setVisible(true);
    }
}
