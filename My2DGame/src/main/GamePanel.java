package main;

import entity.Entity;
import entity.Player;
import main.tile.TileManager;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public EventHandler eventHandler = new EventHandler(this);
    //execute loop
    Thread gameThread;
    //entity and object
    public Player player = new Player(this, keyHandler);
    public Entity target[] = new Entity[10]; //temp number (only 10 objects)
    public Entity npc[] = new Entity[10];
    public Entity monster[] = new Entity[20];
    ArrayList<Entity> entityList = new ArrayList<>();

    //game state
    public int gameState;
    public final int titleState = 0;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int dialogueState = 3;
    public final int characterState = 4;

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
        assetSetter.setMonster();
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
            //player
            player.update();
            //npc
            for (Entity value : npc) {
                if (value != null) {
                    value.update();
                }
            }
            //monster
            for (int i = 0; i < monster.length; i++) {
                if (monster[i] != null) {
                    if (monster[i].alive && !monster[i].dying) {
                        monster[i].update();
                    }
                    if (monster[i].dying) { //remove monster attacks and hitbox
                        monster[i].attackValue = 0;
                        monster[i].solidArea = new Rectangle(0, 0, 0, 0);
                    }
                    if (!monster[i].alive) {
                        monster[i] = null;
                    }
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

        //anti aliasing
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);

        //title screen
        if (gameState == titleState) {
            ui.draw(g2d);
        }
        //other
        else {
            //tile
            tileManager.draw(g2d);
            //add player and other entities (object, npc,...)
            entityList.add(player);
            for (Entity value : npc) {
                if (value != null) {
                    entityList.add(value);
                }
            }
            for (Entity entity : target) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }
            for (Entity entity : monster) {
                if (entity != null) {
                    entityList.add(entity);
                }
            }
            //sort Y position
            Collections.sort(entityList, new Comparator<Entity>() {
                @Override
                public int compare(Entity e1, Entity e2) {
                    return Integer.compare(e1.worldY, e2.worldY);
                }
            });

            //draw entities according to the sorting method
            for (Entity entity : entityList) {
                entity.draw(g2d);
            }
            // Empty entity list
            entityList.clear();
            // UI
            ui.draw(g2d);

            //debug
            if (keyHandler.enableDebug) {
                long drawEndTime = System.nanoTime();
                long passedTime = drawEndTime - drawStartTime;
                g2d.setColor(Color.WHITE);
                g2d.setFont(new Font("Arial", Font.PLAIN, 15));
                int x = 10;
                int y = 450;
                int lineHeight = 15;
                g2d.drawString("Draw Time: " + passedTime + "ns", x, y);
                y+=lineHeight;
                g2d.drawString(String.valueOf(FPS), x, y);
                y+=lineHeight;
                g2d.drawString("World X: "+player.worldX, x, y);
                y+=lineHeight;
                g2d.drawString("World Y: "+player.worldY, x, y);
                y+=lineHeight;
                g2d.drawString("Col: "+(player.worldX+player.solidArea.x)/tileSize, x, y);
                y+=lineHeight;
                g2d.drawString("Row: "+(player.worldY+player.solidArea.y)/tileSize, x, y);
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
