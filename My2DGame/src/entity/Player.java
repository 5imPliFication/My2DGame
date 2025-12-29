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
    private int standCounter = 0;

    public Player(GamePanel gamePanel, KeyHandler keyHandler) {
        super(gamePanel);
        this.keyHandler = keyHandler;

        screenX = gamePanel.screenWidth / 2 - (gamePanel.tileSize / 2);
        screenY = gamePanel.screenHeight / 2 - (gamePanel.tileSize / 2);

        //attack area
        attackArea.width = 36;
        attackArea.height = 36;
        getPlayerAttackImage();

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

        direction = "up";

        //player stats
        maxLife = 8;
        speed = 4;
        life = maxLife;
    }

    public void getPlayerImage() {
        up1 = setup("/player/player_up_1", gamePanel.tileSize, gamePanel.tileSize);
        up2 = setup("/player/player_up_2", gamePanel.tileSize, gamePanel.tileSize);
        down1 = setup("/player/player_down_1", gamePanel.tileSize, gamePanel.tileSize);
        down2 = setup("/player/player_down_2", gamePanel.tileSize, gamePanel.tileSize);
        left1 = setup("/player/player_left_1", gamePanel.tileSize, gamePanel.tileSize);
        left2 = setup("/player/player_left_2", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/player/player_right_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/player/player_right_2", gamePanel.tileSize, gamePanel.tileSize);
    }

    public void getPlayerAttackImage() {
        attackUp1 = setup("/player/player_attack_up_1", gamePanel.tileSize, gamePanel.tileSize * 2);
        attackUp2 = setup("/player/player_attack_up_2", gamePanel.tileSize, gamePanel.tileSize * 2);
        attackDown1 = setup("/player/player_attack_down_1", gamePanel.tileSize, gamePanel.tileSize * 2);
        attackDown2 = setup("/player/player_attack_down_2", gamePanel.tileSize, gamePanel.tileSize * 2);
        attackLeft1 = setup("/player/player_attack_left_1", gamePanel.tileSize * 2, gamePanel.tileSize);
        attackLeft2 = setup("/player/player_attack_left_2", gamePanel.tileSize * 2, gamePanel.tileSize);
        attackRight1 = setup("/player/player_attack_right_1", gamePanel.tileSize * 2, gamePanel.tileSize);
        attackRight2 = setup("/player/player_attack_right_2", gamePanel.tileSize * 2, gamePanel.tileSize);
    }

    public void objectCollide(int index) {
        if (index != 999) {

        }
    }

    public void npcCollide(int index) {
        if (gamePanel.keyHandler.interact) {
            if (index != 999) {
                gamePanel.gameState = gamePanel.dialogueState;
                gamePanel.npc[index].speak();
            } else {
                gamePanel.playSE(7); //hit swing weapon
                attacking = true;
            }
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (!gamePanel.monster[i].iFrame) {
                gamePanel.monster[i].life -= 1;
                gamePanel.playSE(5); //hit monster
                gamePanel.monster[i].iFrame = true;
                gamePanel.monster[i].damagedReaction();
                if (gamePanel.monster[i].life <= 0) {
                    gamePanel.monster[i].dying = true;
                }
            }
        }
    }

    public void attack() {
        spriteCounter++;
        //start animation
        if (spriteCounter <= 5) {
            spriteNum = 1;
        }
        if (spriteCounter > 5 && spriteCounter <= 25) {
            spriteNum = 2;
            //temp vars
            int currentWorldX = worldX;
            int currentWorldY = worldY;
            int solidAreaWidth = solidArea.width;
            int solidAreaHeight = solidArea.height;
            //move player hit area
            switch (direction) {
                case "up":
                    worldY -= attackArea.height;
                    break;
                case "down":
                    worldY += attackArea.height;
                    break;
                case "left":
                    worldX -= attackArea.width;
                    break;
                case "right":
                    worldX += attackArea.width;
                    break;
            }
            //materialize hit area
            solidArea.width = attackArea.width;
            solidArea.height = attackArea.height;
            // check monster hitbox
            int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
            damageMonster(monsterIndex);
            //restore original coordinate
            worldX = currentWorldX;
            worldY = currentWorldY;
            solidArea.width = solidAreaWidth;
            solidArea.height = solidAreaHeight;

        }
        if (spriteCounter > 25) {
            spriteNum = 1;
            spriteCounter = 0;
            attacking = false;
        }
    }

    public void update() {
        if (attacking) {
            attack();
        } else if (keyHandler.up || keyHandler.down || keyHandler.left || keyHandler.right || keyHandler.interact) {
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

            //check monster collision
            int monsterIndex = gamePanel.collisionChecker.checkEntity(this, gamePanel.monster);
            monsterCollide(monsterIndex);

            //check event collision
            gamePanel.eventHandler.checkEvent();

            //if collided == false, player can move
            if (!collided && !keyHandler.interact) {
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
            gamePanel.keyHandler.interact = false;
            //render player sprite
            spriteCounter++;
            if (spriteCounter > 10) { //animation speed
                if (spriteNum == 1) {
                    spriteNum = 2;
                } else if (spriteNum == 2) {
                    spriteNum = 1;
                }
                spriteCounter = 0;
            } else {
                standCounter++;
                if (standCounter == 20) {
                    spriteNum = 1;
                    standCounter = 0;
                }
            }
            if (iFrame) {
                iFrameCounter++;
                if (iFrameCounter > 60) {
                    iFrame = false;
                    iFrameCounter = 0;
                }
            }
        }
    }

    public void monsterCollide(int i) {
        if (i != 999) {
            if (!iFrame) {
                gamePanel.playSE(6); //receive damage
                life -= 1;
                iFrame = true;
            }
        }
    }

    public void draw(Graphics2D g2d) {
        BufferedImage image = null;
        int tempScreenX = screenX;
        int tempScreenY = screenY;
        switch (direction) {
            case "up":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = up1;
                    }
                    if (spriteNum == 2) {
                        image = up2;
                    }
                }
                if (attacking) {
                    tempScreenY = screenY - gamePanel.tileSize;
                    if (spriteNum == 1) {
                        image = attackUp1;
                    }
                    if (spriteNum == 2) {
                        image = attackUp2;
                    }
                }
                break;
            case "down":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = down1;
                    }
                    if (spriteNum == 2) {
                        image = down2;
                    }
                }
                if (attacking) {
                    if (spriteNum == 1) {
                        image = attackDown1;
                    }
                    if (spriteNum == 2) {
                        image = attackDown2;
                    }
                }
                break;
            case "left":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = left1;
                    }
                    if (spriteNum == 2) {
                        image = left2;
                    }
                }
                if (attacking) {
                    tempScreenX = screenX - gamePanel.tileSize;
                    if (spriteNum == 1) {
                        image = attackLeft1;
                    }
                    if (spriteNum == 2) {
                        image = attackLeft2;
                    }
                }
                break;
            case "right":
                if (!attacking) {
                    if (spriteNum == 1) {
                        image = right1;
                    }
                    if (spriteNum == 2) {
                        image = right2;
                    }
                }
                if (attacking) {
                    if (spriteNum == 1) {
                        image = attackRight1;
                    }
                    if (spriteNum == 2) {
                        image = attackRight2;
                    }
                }
                break;
        }
        if (iFrame) {
            //set opacity to 50% if in iFrame
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));
        }
        g2d.drawImage(image, tempScreenX, tempScreenY, null);
        //reset opacity to normal after iFrame
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f));
    }
}
