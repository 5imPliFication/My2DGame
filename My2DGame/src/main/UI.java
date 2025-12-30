package main;

import entity.Entity;
import object.ObjectHeart;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class UI {
    GamePanel gamePanel;
    Font quinqueFive;
    BufferedImage heart_full, heart_half, heart_blank;
    Graphics2D g2d;
    ArrayList<String> message = new ArrayList<>();
    ArrayList<Integer> messageCounter = new ArrayList<>();
    public boolean gameFinished = false;
    public String currentDialogue = "";
    public int choice = 0;
    public int titleScreenState = 0; //state 0 = first screen
    public int slotCol = 0;
    public int slotRow = 0;


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

    public void addMessage(String text) {
        message.add(text);
        messageCounter.add(0);
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
            drawMessage();
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
        //character state
        if (gamePanel.gameState == gamePanel.characterState) {
            characterScreen();
            inventoryScreen();
        }
    }

    private void inventoryScreen() {
        int frameX, frameY, frameWidth, frameHeight;
        frameX = gamePanel.tileSize * 9;
        frameY = gamePanel.tileSize;
        frameWidth = gamePanel.tileSize * 6;
        frameHeight = gamePanel.tileSize * 5;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        //item slots
        final int slotXstart = frameX + 20;
        final int slotYstart = frameY + 20;
        int slotX = slotXstart;
        int slotY = slotYstart;
        //draw player's items
        for (int i = 0; i < gamePanel.player.inventory.size(); i++) {
            g2d.drawImage(gamePanel.player.inventory.get(i).down1, slotX, slotY, null);
            slotX += gamePanel.tileSize;
            if (i == 4 || i == 9 || i == 14) {
                slotX = slotXstart;
                slotY += gamePanel.tileSize;
            }
        }

        //cursor
        int cursorX = slotXstart + (gamePanel.tileSize * slotCol), cursorY = slotYstart + (gamePanel.tileSize * slotRow);
        int cursorWidth = gamePanel.tileSize;
        int cursorHeight = gamePanel.tileSize;
        g2d.setColor(Color.white);
        g2d.setStroke(new BasicStroke(3.5f));
        g2d.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10);
        //item description
        int dFrameX = frameX, dFrameY = frameY + frameHeight, dFrameWidth = frameWidth, dFrameHeight = gamePanel.tileSize * 3;
        drawSubWindow(dFrameX, dFrameY, dFrameWidth, dFrameHeight);
        //draw des text
        int textX = dFrameX + 15;
        int textY = dFrameY + gamePanel.tileSize - 20;
        g2d.setFont(g2d.getFont().deriveFont(10F));

        int itemIndex = getItemIndex();
        if (itemIndex < gamePanel.player.inventory.size()) {
            for (String line : gamePanel.player.inventory.get(itemIndex).description.split("\n")) {
                g2d.drawString(line, textX, textY);
                textY += 20;
            }
        }
    }

    private int getItemIndex() {
        return slotCol + (slotRow * 5);
    }

    private void drawMessage() {
        int messageX = gamePanel.tileSize, messageY = gamePanel.tileSize * 4; //msg position
        g2d.setFont(g2d.getFont().deriveFont(12F));
        for (int i = 0; i < messageCounter.size(); i++) {
            if (message.get(i) != null) {
                g2d.setColor(Color.white);
                g2d.drawString(message.get(i), messageX, messageY);

                int counter = messageCounter.get(i) + 1;
                messageCounter.set(i, counter);
                messageY += 50;
                if (messageCounter.get(i) > 180) {
                    message.remove(i);
                    messageCounter.remove(i);
                }
            }
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
            if (i < gamePanel.player.life) {
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

    public void characterScreen() {
        //draw sub window
        final int frameX = gamePanel.tileSize;
        final int frameY = gamePanel.tileSize;
        final int frameWidth = gamePanel.tileSize * 7;
        final int frameHeight = gamePanel.tileSize * 10;
        drawSubWindow(frameX, frameY, frameWidth, frameHeight);

        g2d.setColor(Color.white);
        g2d.setFont(g2d.getFont().deriveFont(15F));

        int textX = frameX + 20;
        int textY = frameY + gamePanel.tileSize;
        final int lineHeight = 32;
        //names
        g2d.drawString("Level", textX, textY);
        textY += lineHeight;
        g2d.drawString("Life", textX, textY);
        textY += lineHeight;
        g2d.drawString("Strength", textX, textY);
        textY += lineHeight;
        g2d.drawString("Dexterity", textX, textY);
        textY += lineHeight;
        g2d.drawString("Attack", textX, textY);
        textY += lineHeight;
        g2d.drawString("Defense", textX, textY);
        textY += lineHeight;
        g2d.drawString("Exp", textX, textY);
        textY += lineHeight;
        g2d.drawString("Next Level", textX, textY);
        textY += lineHeight;
        g2d.drawString("Coin", textX, textY);
        textY += lineHeight + 20;
        g2d.drawString("Weapon", textX, textY);
        textY += lineHeight + 15;
        g2d.drawString("Shield", textX, textY);

        int tailX = (frameX + frameWidth) - 30;
        //reset textY
        textY = frameY + gamePanel.tileSize;
        //draw values
        String value;
        value = String.valueOf(gamePanel.player.level);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = gamePanel.player.life + "/" + gamePanel.player.maxLife;
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.strength);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.dexterity);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.attack);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.defense);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.exp);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.nextLevelExp);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        value = String.valueOf(gamePanel.player.coin);
        textX = getXaxisAllignToRightText(value, tailX);
        g2d.drawString(value, textX, textY);
        textY += lineHeight;

        g2d.drawImage(gamePanel.player.currentWeapon.down1, tailX - gamePanel.tileSize, textY - 14, null);
        textY += lineHeight + 15;
        g2d.drawImage(gamePanel.player.currentShield.down1, tailX - gamePanel.tileSize, textY - 14, null);

    }

    public int getXaxisAllignToRightText(String text, int tailX) {
        int textLength = (int) g2d.getFontMetrics().getStringBounds(text, g2d).getWidth();
        return tailX - textLength;
    }
}
