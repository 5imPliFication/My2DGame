package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Player extends Entity {
    KeyHandler keyHandler;
    public final int screenX;
    public final int screenY;
    //    public int keyOwned = 0;
    private int standCounter = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        //set hit box for player
        solidArea = new Rectangle(9, 15, 30, 32);
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        setDefaultValues();
        getPlayerImage();
    }

    public void setDefaultValues() {
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        speed = 4;
        direction = "up";
    }

    public void getPlayerImage() {
        up1 = setup("/player/player-up-0");
        up2 = setup("/player/player-up-1");
        up3 = setup("/player/player-up-2");
        down1 = setup("/player/player-down-0");
        down2 = setup("/player/player-down-1");
        down3 = setup("/player/player-down-2");
        left1 = setup("/player/player-left-0");
        left2 = setup("/player/player-left-1");
        left3 = setup("/player/player-left-2");
        right1 = setup("/player/player-right-0");
        right2 = setup("/player/player-right-1");
        right3 = setup("/player/player-right-2");
    }

    public void objectCollide(int index) {
        if (index != 999) {

        }
    }

    public void npcCollide(int index) {
        if (index != 999) {
            if (gamePanel.keyHandler.interact) {
                gamePanel.gameState = gamePanel.dialogueState;
                gamePanel.npc[index].speak();
            }
        }
        gamePanel.keyHandler.interact = false;
    }

    public void update() {
        if (keyHandler.up || keyHandler.down || keyHandler.left || keyHandler.right) {
            if (keyHandler.up) {
                direction = "up";
            }
            if (keyHandler.down) {
                direction = "down";
            }
            if (keyHandler.left) {
                direction = "left";
            }
            if (keyHandler.right) {
                direction = "right";
            }

            //check tile collision
            collided = false;
            gamePanel.collisionChecker.checkTile(this);

            // check obj collision
            int objIndex = gamePanel.collisionChecker.checkObject(this, true);
            objectCollide(objIndex);

            // check npc collision
            int npcIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.npc);
            npcCollide(npcIndex);

            //if collided == false, player can move
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
        } else {
            standCounter++;
            if (standCounter == 20) {
                spriteNum = 2;
                standCounter = 0;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
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
        g2d.drawImage(image, screenX, screenY, null);
    }
}
