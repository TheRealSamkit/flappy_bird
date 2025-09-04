import javax.swing.*;
import java.awt.*;

public class homeScreen extends JPanel{
    int boardWidth = 360;
    int boardHeight = 640;

    Image backgroundImg;
    JButton startButton;

    homeScreen() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        setLayout(null); // Important to use absolute positioning

        backgroundImg = new ImageIcon(getClass().getResource("assets/flappybirdbg.png")).getImage();

        // Create and style button
        JLabel titleLabel = new JLabel("Flappy Bird");
        titleLabel.setBounds(boardWidth / 8, boardHeight / 2 - 100, boardWidth, 50);
        // Create and style button
        JLabel creditLabel = new JLabel("By Group 4");
        creditLabel.setBounds(boardWidth / 8 + 10, boardHeight / 2 - 70, boardWidth, 50);
        startButton = new JButton("Start Game");
        startButton.setBounds(boardWidth / 2 - 105, boardHeight / 2 - 15, 200, 40); // center it
        styleButton(startButton);
        startButton.setFocusable(false); // Prevent button from gaining focus
        startButton.addActionListener(e -> {
            // Action to start the game
            JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
            frame.getContentPane().removeAll();
            FlappyBird flappyBird = new FlappyBird();
            frame.add(flappyBird);
            frame.pack();
            flappyBird.requestFocus();
            frame.setVisible(true);
        });
        
        add(titleLabel);
        add(creditLabel);
        add(startButton);
    }

    private void styleButton(JButton button) {
        button.setFocusPainted(false);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g) {
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);
    }
}
