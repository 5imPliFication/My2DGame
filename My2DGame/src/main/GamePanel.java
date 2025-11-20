package main;

import entity.Entity;
import entity.Player;
import main.tile.TileManager;
import object.SuperObject;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable {
    //screen setting
    public final int originalTileSize = 16;
    private final int scale = 3;
    public final int tileSize = originalTileSize * scale; //48x48 tile
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol; //768  px
    public final int screenHeight = tileSize * maxScreenRow; //576 px
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;

    //lock FPS
    final int FPS = 60;
    //System
    TileManager tileManager = new TileManager(this);
    public KeyHandler keyHandler = new KeyHandler(this);
    Sound music = new Sound();
    Sound se = new Sound();
    public AssetSetter assetSetter = new AssetSetter(this);
    public CollisionChecker collisionChecker = new CollisionChecker(this);
    public UI ui = new UI(this);
    //execute loop
    Thread gameThread;
    //entity and object
    public Player player = new Player(this, keyHandler);
    public SuperObject target[] = new SuperObject[10]; //temp number (only 10 objects)
    public Entity npc[] = new Entity[10];

    //game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;

    //game window
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyHandler);
        this.setFocusable(true);
    }

    public void setupGame() {
        assetSetter.setObject();
        assetSetter.setNPC();
        playMusic(0);
        stopMusic();
        gameState = titleState;
    }

    public void startGame() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    private boolean running = true;

    @Override
    public void run() {
        double drawInterval = 1_000_000_000.0 / FPS; //1/60 sec
        double nextDrawInterval = System.nanoTime() + drawInterval; //wait 1/60 sec to draw the next frame
        while (running) {
            long currentTime = System.nanoTime();
            update();
            repaint();

            long remainingTime = (long) (nextDrawInterval - currentTime);
            nextDrawInterval += drawInterval;

            if (remainingTime > 0) {  // Skip sleep if behind
                try {
                    Thread.sleep(remainingTime / 1_000_000, (int) (remainingTime % 1_000_000));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    running = false;  // Clean exit
                }
            }
        }

    }

    public void update() {
        if (gameState == playState) {
            player.update();
            for (int i = 0; i < target.length; i++) {
                if (npc[i] != null) {
                    npc[i].update();
                }
            }
        }
        if (gameState == pauseState) {

        }
        if (gameState == dialogueState) {

        }
    }

    //draw  the graphic
    public void paint(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        //debug mode
        long drawStartTime = 0;
        drawStartTime = System.nanoTime();

        //title screen
        if (gameState == titleState) {
            ui.draw(g2d);
        }
        //other
        else {

            //tile
            tileManager.draw(g2d);
            //object
            for (int i = 0; i < target.length; i++) {
                if (target[i] != null) {
                    target[i].drawImage(g2d, this);
                }
            }
            //player
            player.draw(g2d);
            //npcs
            for (int i = 0; i < npc.length; i++) {
                if (npc[i] != null) {
                    npc[i].draw(g2d);
                }
            }

            // UI
            ui.draw(g2d);

            //debug
            if (keyHandler.enableDebug) {
                long drawEndTime = System.nanoTime();
                long passedTime = drawEndTime - drawStartTime;
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 20));
                g2d.drawString("Draw Time: " + passedTime + "ns", 10, 520);
            }
        }

        g2d.dispose();
    }

    public void playMusic(int i) {
        music.setFile(i);
        music.play();
        music.loop();
    }

    public void stopMusic() {
        music.stop();
    }

    public void playSE(int i) {
        se.setFile(i);
        se.play();
    }

}
