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
    public BufferedImage up1, up2, down1, down2, left1, left2, right1, right2;
    //attack animation
    public BufferedImage attackUp1, attackUp2, attackDown1, attackDown2, attackLeft1, attackLeft2, attackRight1, attackRight2;
    public boolean attacking = false;
    public Rectangle attackArea = new Rectangle(0, 0, 0, 0);
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
    public boolean alive = true;
    public boolean dying = false;

    public int dyingCounter = 0;

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
    public void damagedReaction(){

    }

    public void update() {
        collided = false;
        gamePanel.collisionChecker.checkTile(this);
        gamePanel.collisionChecker.checkObject(this, false);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
        gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
        boolean playerCollision = gamePanel.collisionChecker.checkPlayer(this);
        if (this.entityType == 2 && playerCollision) {
            if (!gamePanel.player.iFrame) {
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
                spriteNum = 1;
            }
            spriteCounter = 0;
        }
        setAction();
        // iframe setting
        if (iFrame) {
            iFrameCounter++;
            if (iFrameCounter > 40) {
                iFrame = false;
                iFrameCounter = 0;
            }
        }
    }

    public BufferedImage setup(String imgName, int width, int height) {
        UtilityTool util = new UtilityTool();
        BufferedImage image = null;
        try {
            image = ImageIO.read(getClass().getResourceAsStream(imgName + ".png"));
            image = util.scaledImage(image, width, height);
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
                    break;
                case "down":
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                    break;
                case "left":
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                    break;
                case "right":
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                    break;
            }

            //monster/npc HP bar
            if (entityType == 2) {
                if (maxLife != life) {
                    double oneScale = (double) gamePanel.tileSize/maxLife;
                    double hpBarValue = oneScale*life;

                    g2d.setColor(new Color(35,35,35));
                    g2d.fillRect(screenX, screenY - 15, gamePanel.tileSize+4, 14);
                    g2d.setColor(new Color(255, 69, 93));
                    g2d.fillRect(screenX+2, screenY - 13, (int)hpBarValue, 10);
                }
            }
            if (iFrame) {
                //set opacity to 40% if in iFrame
                g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));
            }
            if (dying) {
                dyingAnimation(g2d);
            }
            g2d.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
        }
    }

    public void changeAlpha(Graphics2D g2d, float alphaValue) {
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alphaValue));
    }

    private void dyingAnimation(Graphics2D g2d) {
        dyingCounter++;
        if (dyingCounter <= 5) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > 5 && dyingCounter <= 10) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > 10 && dyingCounter <= 15) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > 15 && dyingCounter <= 20) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > 20 && dyingCounter <= 25) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > 25 && dyingCounter <= 30) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > 30 && dyingCounter <= 35) {
            changeAlpha(g2d, 0f);
        }
        if (dyingCounter > 35 && dyingCounter <= 40) {
            changeAlpha(g2d, 1f);
        }
        if (dyingCounter > 40) {
            dying = false;
            alive = false;
        }

    }
}
