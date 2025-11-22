package main;

import entity.Entity;
import object.ObjectHeart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class UI {
    GamePanel gamePanel;
    Font quinqueFive;
    BufferedImage heart_full, heart_half, heart_blank;
    Graphics2D g2d;
    private int messageCounter = 0;
    public boolean messageOn = false;
    public String message = "";
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int choice = 0;
    public int titleScreenState = 0; //state 0 = first screen


    public UI(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            InputStream is = getClass().getResourceAsStream("/font/QuinqueFive.ttf");
            quinqueFive = Font.createFont(Font.TRUETYPE_FONT, is);
            quinqueFive = quinqueFive.deriveFont(Font.PLAIN, 20F);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        //create hub
        Entity heart = new ObjectHeart(gamePanel);
        heart_full = heart.image1;
        heart_half = heart.image2;
        heart_blank = heart.image3;
    }

    public void ShowMessage(String text) {
        message = text;
        messageOn = true;
    }

    public void draw(Graphics2D g2d) {
        this.g2d = g2d;
        g2d.setFont(quinqueFive);
        g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2d.setColor(Color.white);
        //title state
        if (gamePanel.gameState == gamePanel.titleState) {
            titleScreen();
        }
        //play state
        if (gamePanel.gameState == gamePanel.playState) {
            playerLife();
        }
        //pause state
        if (gamePanel.gameState == gamePanel.pauseState) {
            playerLife();
            pauseScreen();
        }
        //dialogue state
        if (gamePanel.gameState == gamePanel.dialogueState) {
            dialogueScreen();
        }
    }

    private void playerLife() {
        int x = gamePanel.tileSize / 2;
        int y = gamePanel.tileSize / 2;
        int i = 0;
        //draw empty heart
        while (i < gamePanel.player.maxLife / 2) {
            g2d.drawImage(heart_blank, x, y, null);
            i++;
            x += gamePanel.tileSize;
        }

        //reset position, value
        x = gamePanel.tileSize / 2;
        y = gamePanel.tileSize / 2;
        i = 0;
        //draw half, full heart
        while (i < gamePanel.player.life) {
            g2d.drawImage(heart_half, x, y, null);
            i++;
            if(i<gamePanel.player.life){
                g2d.drawImage(heart_full, x, y, null);
            }
            i++;
            x += gamePanel.tileSize;
        }

    }

    private void titleScreen() {
        if (titleScreenState == 0) {
            //BG
            g2d.setColor(new Color(70, 120, 80));
            g2d.fillRect(0, 0, gamePanel.screenWidth + gamePanel.tileSize, gamePanel.screenHeight);
            //Title
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 50F));
            String title = "Ngulon vile";
            int x = getXaxisCenteredText(title);
            int y = gamePanel.tileSize * 3;
            //shadow
            g2d.setColor(Color.gray);
            g2d.drawString(title, x + 5, y + 5);
            //main color
            g2d.setColor(Color.white);
            g2d.drawString(title, x, y);
            //player
            x = gamePanel.screenWidth / 2 - gamePanel.tileSize;
            y += gamePanel.tileSize * 2;
            g2d.drawImage(gamePanel.player.down2, x, y, gamePanel.tileSize * 2, gamePanel.tileSize * 2, null);
            //menu
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30F));
            String text = "NEW GAME";
            x = getXaxisCenteredText(text);
            y += (int) (gamePanel.tileSize * 3.5);
            g2d.drawString(text, x, y);
            if (choice == 0) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "LOAD GAME";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize;
            g2d.drawString(text, x, y);
            if (choice == 1) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }

            text = "QUIT";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize;
            g2d.drawString(text, x, y);
            if (choice == 2) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }
        } else if (titleScreenState == 1) {
            //class selection screen
            g2d.setColor(Color.white);
            g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 30F));
            String text = "Choose your class:";
            int x = getXaxisCenteredText(text);
            int y = gamePanel.tileSize * 3;
            g2d.drawString(text, x, y);

            text = "Warrior";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize;
            g2d.drawString(text, x, y);
            if (choice == 0) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }
            text = "Hunter";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize;
            g2d.drawString(text, x, y);
            if (choice == 1) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }
            text = "Assassin";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize;
            g2d.drawString(text, x, y);
            if (choice == 2) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }
            text = "Mage";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize;
            g2d.drawString(text, x, y);
            if (choice == 3) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }
            text = "Back";
            x = getXaxisCenteredText(text);
            y += gamePanel.tileSize * 2;
            g2d.drawString(text, x, y);
            if (choice == 4) {
                g2d.drawString(">", x - gamePanel.tileSize, y);
            }
        }
    }

    public int getXaxisCenteredText(String text) {
        int textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return gamePanel.screenWidth / 2 - textLength / 2;
    }

    public void dialogueScreen() {
        int x, y, width, height;
        x = gamePanel.tileSize * 2;
        y = gamePanel.tileSize / 2;
        width = gamePanel.screenWidth - (gamePanel.tileSize * 4);
        height = gamePanel.tileSize * 4;
        drawSubWindow(x, y, width, height);

        x += gamePanel.tileSize;
        y += gamePanel.tileSize;
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN, 15F));
        //handle dialogue overflow
        for (String line : currentDialogue.split("\n")) {
            g2d.drawString(line, x, y);
            y += 40;
        }
    }

    public void drawSubWindow(int x, int y, int width, int height) {
        g2d.setColor(new Color(0, 0, 0, 180));
        g2d.fillRoundRect(x, y, width, height, 35, 35);
        g2d.setColor(new Color(255, 255, 255));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25);
    }

    public void pauseScreen() {
        g2d.setFont(g2d.getFont().deriveFont(Font.BOLD, 80F));
        String text = "PAUSED";
        int x, y;
        x = getXaxisCenteredText(text);
        y = gamePanel.screenHeight / 2;
        g2d.drawString(text, x, y);
    }
}
