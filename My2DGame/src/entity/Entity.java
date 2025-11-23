package entity;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Entity {

    GamePanel gamePanel;
    public int speed;
    //location
    public int worldX, worldY;
    //facing direction
    public String direction = "down";
    // animation imgs
    public BufferedImage up1, up2, up3, down1, down2, down3, left1, left2, left3, right1, right2, right3;
    // animation img order
    public int spriteCounter = 0;
    // idk bruh
    public int spriteNum = 1;
    //iframe
    public boolean iFrame = false;
    public int iFrameCounter = 0;
    //  ini hit boxes
    public Rectangle solidArea = new Rectangle(0, 0, 40, 32);
    public int solidAreaDefaultX, solidAreaDefaultY;
    public boolean collided = false;
    //actions
    public int actionCooldown = 0;
    String dialogues[] = new String[20];
    int dialogueIndex = 0;
    //assets for objbects
    public BufferedImage image1, image2, image3;
    public String name;

    //char stats
    public int maxLife;
    public int life;

    public int entityType;

    public Entity(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void speak() {
        switch (gamePanel.player.direction) {
            case "up":
                direction = "down";
                break;
            case "down":
                direction = "up";
                break;
            case "left":
                direction = "right";
                break;
            case "right":
                direction = "left";
                break;
        }
        if (dialogues[dialogueIndex] == null) {
            dialogueIndex = 0;
        }
        gamePanel.ui.currentDialogue = dialogues[dialogueIndex];
        dialogueIndex++;
    }

    public void setAction() {
    }

    public void update() {
        collided = false;
        gamePanel.collisionChecker.checkTile(this);
        gamePanel.collisionChecker.checkObject(this, false);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
        boolean playerCollision = gamePanel.collisionChecker.checkPlayer(this);
        if(this.entityType == 2 && playerCollision) {
            if(!gamePanel.player.iFrame) {
                gamePanel.player.life -= 1;
                gamePanel.player.iFrame = true;
            }
        }
        if (!collided) {
            switch (direction) {
                case "up":
                    worldY -= speed;
                    break;
                case "down":
                    worldY += speed;
                    break;
                case "left":
                    worldX -= speed;
                    break;
                case "right":
                    worldX += speed;
                    break;
            }
        }
        //render player sprite
        spriteCounter++;
        if (spriteCounter > 10) { //animation speed
            if (spriteNum == 1) {
                spriteNum = 2;
            } else if (spriteNum == 2) {
                spriteNum = 3;
            } else if (spriteNum == 3) {
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        setAction();
    }

    public BufferedImage setup(String imgName) {
        UtilityTool util = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imgName + ".png"));
            image = util.scaledImage(image, gamePanel.tileSize, gamePanel.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return image;
    }

    public void draw(Graphics2D g2d) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        BufferedImage image = null;
        if (screenX + gamePanel.tileSize > -gamePanel.tileSize &&      // allow 1 tile off left
                screenX < gamePanel.screenWidth + gamePanel.tileSize &&    // allow 1 tile off right ← THIS FIXES IT
                screenY + gamePanel.tileSize > -gamePanel.tileSize &&
                screenY < gamePanel.screenHeight + gamePanel.tileSize) {
            switch (direction) {
                case "up":
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                    if (spriteNum == 3) {
                        image = up3;
                    }

                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    if (spriteNum == 3) {
                        image = down3;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    if (spriteNum == 3) {
                        image = left3;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    if (spriteNum == 3) {
                        image = right3;
                    }
                    break;
            }
            g2d.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }
    }
}
