import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
    int boardWidth = 360;
    int boardHeight = 640;
    boolean testMode = false; //set to true to skip the game logic and just place pipes for testing purposes

    //images
    Image backgroundImg,birdImg,topPipeImg,bottomPipeImg;

    //bird class
    int birdX = boardWidth/8;
    int birdY = boardHeight/2;
    int birdWidth = 34;
    int birdHeight = 24;
    
    Font customFont;
    JLabel restart;

    class Bird {
        int x = birdX;
        int y = birdY;
        int width = birdWidth;
        int height = birdHeight;
        Image img;

        Bird(Image img) {
            this.img = img;
        }
    }

    //pipe class
    int pipeX = boardWidth;
    int pipeY = 0;
    int pipeWidth = 64;  //scaled by 1/6
    int pipeHeight = 512;
    
    class Pipe {
        int x = pipeX;
        int y = pipeY;
        int width = pipeWidth;
        int height = pipeHeight;
        Image img;
        boolean passed = false;

        Pipe(Image img) {
            this.img = img;
        }
    }

    //game logic
    Bird bird;
    int velocityX = -4;
    /*move pipes to the left speed (simulates bird
     moving right)
     * move bird up/down speed.
    */
    int velocityY = 0; 
    int gravity = 1;
    int difficulty = 24;

    ArrayList<Pipe> pipes;
    Random random = new Random();

    Timer gameLoop;
    int delay = 1700;
    Timer placePipeTimer;
    boolean gameOver = false;
    double score = 0;
    int highScore = 0;
    int lastScoreCheck = 0; 
    // declare at class level
    int lastDelayCheck = 0; 
    // declare at class level

    FlappyBird() {
        setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        //load images
        backgroundImg = new ImageIcon(getClass().getResource("assets/flappybirdbg.png")).getImage();
        birdImg = new ImageIcon(getClass().getResource("assets/flappybird.png")).getImage();
        topPipeImg = new ImageIcon(getClass().getResource("assets/toppipe.png")).getImage();
        bottomPipeImg = new ImageIcon(getClass().getResource("assets/bottompipe.png")).getImage();

        //bird
        bird = new Bird(birdImg);
        pipes = new ArrayList<Pipe>();
        restart = new JLabel("Yeboi");

        Map<String, String> gameData = GameData.load();
        highScore = Integer.parseInt(gameData.get("highScore"));

        //place pipes timer
        placePipeTimer = new Timer(delay, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
              // Code to be executed
              placePipes();
            }
        });
        placePipeTimer.start();
        
		//game timer
		gameLoop = new Timer(1000/difficulty, this); 
        /*how long it takes to start timer,
         milliseconds gone between frames */
        gameLoop.start();
	}
    
    void placePipes() {
        //(0-1) * pipeHeight/2.
        // 0 -> -128 (pipeHeight/4)
        // 1 -> -128 - 256 (pipeHeight/4 - pipeHeight/2) = -3/4 pipeHeight
        int randomPipeY = (int) (pipeY - pipeHeight/4 - Math.random()*(pipeHeight/2));
        int openingSpace = boardHeight/3;
    
        Pipe topPipe = new Pipe(topPipeImg);
        topPipe.y = randomPipeY;
        pipes.add(topPipe);
    
        Pipe bottomPipe = new Pipe(bottomPipeImg);
        bottomPipe.y = topPipe.y  + pipeHeight + openingSpace;
        pipes.add(bottomPipe);
    }
    
    
    public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
        //background
        g.drawImage(backgroundImg, 0, 0, this.boardWidth, this.boardHeight, null);

        //bird
        g.drawImage(birdImg, bird.x, bird.y, bird.width, bird.height, null);

        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
        }
        //score
       
        if (gameOver) {
            g.setColor(Color.darkGray);
            g.fill3DRect(boardWidth / 8 - 10, boardHeight / 2 - 210, 135 * 2 + 20, 210 + 20, true);
            g.setColor(Color.black);
            g.fillRect(boardWidth / 8, boardHeight / 2 - 200, 135 * 2, 200);
            
            g.setColor(Color.white);
            g.drawString("Game Over", boardWidth / 5, boardHeight / 2 - 150);
            g.drawString("Score: " + (int) score, boardWidth / 4, boardHeight / 2 - 100);
            g.drawString("High Score", boardWidth / 6, boardHeight / 2 - 50);
            g.drawString(""+highScore, boardWidth / 2 - 25, boardHeight / 2 - 10);
        }
        else {
            g.drawString(String.valueOf((int) score),boardWidth / 2 - 10, boardHeight / 2 - 175 );
            if (testMode) {
                g.drawString("Delay: " + delay, boardWidth / 2 - 100, boardHeight / 2 - 155);
                g.drawString("FPS: " + difficulty, boardWidth / 2 - 100, boardHeight / 2 - 135);
            }            
        }
        
	}

    public void move() {
    if (score % 5 == 0 && score > 5 && score != lastScoreCheck) {
        if(difficulty < 60){
            difficulty += 5;
            gameLoop.setDelay(1000 / difficulty);
        }
        if(delay>500){
            delay -= 100;
            placePipeTimer.setDelay(delay);
        }
        System.out.println("FPS: " + difficulty);
        System.out.println("Delay: " + delay);
        lastScoreCheck = (int) score; 
        // Update lastScoreCheck to the current score
    }        
        //bird
        if(!testMode){
            bird.y = Math.max(bird.y, 0);
             /*apply gravity to current bird.y,
              limit the bird.y to top of the canvas*/
            bird.y += velocityY;
            velocityY += gravity;
        }
        //pipes
        for (int i = 0; i < pipes.size(); i++) {
            Pipe pipe = pipes.get(i);
            pipe.x += velocityX;

            if (!pipe.passed && bird.x > pipe.x + pipe.width) {
                score += 0.5; 
                //0.5 because there are 2 pipes! so 0.5*2 = 1, 1 for each set of pipes
                pipe.passed = true;
            }

            if (collision(bird, pipe)) {
                if(!testMode){
                    gameOver = true;
                }
            }
        }

        if (bird.y > boardHeight) {
            gameOver = true;
        }
    }

    boolean collision(Bird a, Pipe b) {
        return a.x < b.x + b.width &&  
         //a's top left corner doesn't reach b's top right corner
               a.x + a.width > b.x &&   
               //a's top right corner passes b's top left corner
               a.y < b.y + b.height &&  
               //a's top left corner doesn't reach b's bottom left corner
               a.y + a.height > b.y;    
               //a's bottom left corner passes b's top left corner
    }

    @Override
    public void actionPerformed(ActionEvent e) {
         //called every x milliseconds by gameLoop timer
        move();
        repaint();
        if (gameOver) {
            if ((int) score > highScore) {
                highScore = (int) score;
                GameData.updateHighScore((int)score);
            }            
            placePipeTimer.stop();
            gameLoop.stop();
        }
    }  

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            // System.out.println("JUMP!");
            velocityY = -9;

            if (gameOver) {
                //restart game by resetting conditions
                bird.y = birdY;
                velocityY = 0;
                pipes.clear();
                gameOver = false;
                score = 0;
                gameLoop.start();
                placePipeTimer.start();
            }
        }
    }

    //not needed
    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
}
