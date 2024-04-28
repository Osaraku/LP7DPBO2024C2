import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class FlappyBird extends JPanel implements ActionListener, KeyListener{
    int frameWidth = 360;
    int frameHeight = 640;

    Image backgroundImage;
    Image birdImage;
    Image lowerPipeImage;
    Image upperPipeImage;

    int playerStartPosX = frameWidth / 8;
    int playerStartPosY = frameHeight / 2;
    int playerWidth = 34;
    int playerHeight = 24;
    Player player;

    int pipeStartPosX = frameWidth;
    int pipeStartPosY = 0;
    int pipeWidth = 64;
    int pipeHeight = 512;
    ArrayList<Pipe> pipes;
    Timer pipesCooldown;

    Timer gameLoop;
    private int gravity = 1;
    boolean gameOver = false;
    double score = 0;
    double highscore = 0;
    private JLabel scoreLabel;

    public FlappyBird(){
        setPreferredSize(new Dimension(frameWidth, frameHeight));
        setBackground(Color.blue);
        setFocusable(true);
        addKeyListener(this);

        // load images
        backgroundImage = new ImageIcon(getClass().getResource("assets/background.png")).getImage();
        birdImage = new ImageIcon(getClass().getResource("assets/bird.png")).getImage();
        lowerPipeImage = new ImageIcon(getClass().getResource("assets/lowerPipe.png")).getImage();
        upperPipeImage = new ImageIcon(getClass().getResource("assets/upperPipe.png")).getImage();

        player = new Player(playerStartPosX, playerStartPosY, playerWidth, playerHeight, birdImage);
        pipes = new ArrayList<Pipe>();

        pipesCooldown = new Timer(1500, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                placePipes();
            }
        });
        pipesCooldown.start();

        gameLoop = new Timer(1000/60 , this);
        gameLoop.start();

        scoreLabel = new JLabel("Score: " + String.valueOf((int) score));
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setForeground(Color.WHITE);
        this.add(scoreLabel);
    }

    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){
        g.drawImage(backgroundImage,0,0,frameWidth,frameHeight,null);

        g.drawImage(player.getImage(), player.getPosX(), player.getPosY(), player.getWidth(), player.getHeight(), null);

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            g.drawImage(pipe.getImage(), pipe.getPosX(), pipe.getPosY(), pipe.getWidth(), pipe.getHeight(), null);
        }

        if(gameOver){
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 32));
            g.drawString("Game Over", 100, frameHeight / 2 - 25);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.BOLD, 20));
            if(score > highscore){
                highscore = score;
            }
            g.drawString("Your Score : " + String.valueOf((int) score) + " | Highscore : " + String.valueOf((int)highscore), 40, frameHeight / 2);
            g.setFont(new Font("Arial", Font.BOLD, 16));
            g.drawString("Press 'R' to restart!", 110, frameHeight / 2 + 25);
        }
    }

    public void move(){
        player.setVelocityY(player.getVelocityY() + gravity);
        player.setPosY(player.getPosY() + player.getVelocityY());
        player.setPosY(Math.max(player.getPosY(), 0));

        for(int i = 0; i < pipes.size(); i++){
            Pipe pipe = pipes.get(i);
            pipe.setPosX(pipe.getPosX() + pipe.getVelocityX());

            if(!pipe.passed && player.getPosX() > pipe.getPosX() + pipe.getWidth()){
                pipe.passed = true;
                score += 0.5;
                scoreLabel.setText("Score: " + String.valueOf((int) score));
            }

            if(collision(player, pipe)){
                gameOver = true;
            }
        }

        if(player.getPosY() > 640){
            gameOver = true;
        }
    }

    public void placePipes(){
        //menampilkan posisi pipa secara random
        int randomPosY = (int)(pipeStartPosY - pipeHeight/4 - Math.random() * (pipeHeight/2));
        int openingSpace = frameHeight/4;

        Pipe upperPipe = new Pipe(pipeStartPosX, randomPosY, pipeWidth, pipeHeight, upperPipeImage);
        pipes.add(upperPipe);

        Pipe lowerPipe = new Pipe(pipeStartPosX, (randomPosY+openingSpace+pipeHeight), pipeWidth, pipeHeight, lowerPipeImage);
        pipes.add(lowerPipe);
    }

    public boolean collision(Player player, Pipe pipe){
        return player.getPosX() < pipe.getPosX() + pipe.getWidth() &&
                player.getPosX() + player.getWidth() > pipe.getPosX() &&
                player.getPosY() < pipe.getPosY() + pipe.getHeight() &&
                player.getPosY() + player.getHeight() > pipe.getPosY();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        move();
        repaint();
        // Game berhenti ketika game over
        if(gameOver){
            pipesCooldown.stop();
            gameLoop.stop();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            player.setVelocityY(-10);
        } else if (gameOver && e.getKeyCode() == KeyEvent.VK_R) {
            player.setPosY(playerStartPosY);
            pipes.clear();
            score = 0;
            scoreLabel.setText("Score: " + String.valueOf((int) score));
            gameOver = false;
            gameLoop.start();
            pipesCooldown.start();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {

    }
}
