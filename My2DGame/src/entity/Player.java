package entity;

import main.GamePanel;
import main.KeyHandler;
import main.UtilityTool;
import object.ObjectKey;
import object.ObjectShield;
import object.ObjectSwordNormal;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

public class Player extends Entity {
    KeyHandler keyHandler;
    public final int screenX;
    public final int screenY;
    private int standCounter = 0;
    public boolean attackCancel = false;
    public ArrayList<Entity> inventory = new ArrayList<>();
    public final int inventorySize = 20;

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
        setItems();
    }

    public void setDefaultValues() {
        worldX = gamePanel.tileSize * 23;
        worldY = gamePanel.tileSize * 21;
        direction = "up";

        //player stats
        level = 1;
        strength = 1;
        dexterity = 1;
        exp = 0;
        nextLevelExp = 5;
        maxLife = 8;
        speed = 4;
        life = maxLife;

        coin = 0;
        currentWeapon = new ObjectSwordNormal(gamePanel);
        currentShield = new ObjectShield(gamePanel);

        attack = getAttack();
        defense = getDefense();
    }

    public void setItems(){
        inventory.add(currentWeapon);
        inventory.add(currentShield);
        inventory.add(new ObjectKey(gamePanel));

    }

    public int getAttack() {
        return strength + currentWeapon.attackValue;
    }

    public int getDefense() {
        return dexterity + currentShield.defenseValue;
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
                attackCancel = true;
                gamePanel.gameState = gamePanel.dialogueState;
                gamePanel.npc[index].speak();
            } else {
                gamePanel.playSE(7); //hit swing weapon
                attacking = true;
            }
        }
    }

    public void monsterCollide(int i) {
        if (i != 999) {
            if (!iFrame) {
                gamePanel.playSE(6); //receive damage

                int dmg = gamePanel.monster[i].attack - defense;
                if (dmg <= 0) {
                    dmg = 0;
                }
                life -= dmg;
                iFrame = true;
            }
        }
    }

    public void damageMonster(int i) {
        if (i != 999) {
            if (!gamePanel.monster[i].iFrame) {
                gamePanel.playSE(5); //hit monster
                int dmg = attack - gamePanel.monster[i].defense;
                if (dmg <= 0) {
                    dmg = 0;
                }
                gamePanel.monster[i].life -= dmg;
                gamePanel.ui.addMessage(dmg + " damage!");
                gamePanel.monster[i].iFrame = true;
                gamePanel.monster[i].damagedReaction();
                if (gamePanel.monster[i].life <= 0) {
                    gamePanel.ui.addMessage(gamePanel.monster[i].name + " slayed!");
                    gamePanel.monster[i].dying = true;
                    gamePanel.ui.addMessage("+" + gamePanel.monster[i].exp + " EXP!");
                    exp += gamePanel.monster[i].exp;
                    checkLevelUp();
                }
            }
        }
    }

    private void checkLevelUp() {
        if (exp >= nextLevelExp) {
            level++;
            nextLevelExp += (nextLevelExp / 2 + nextLevelExp);
            maxLife += 2;
            strength++;
            dexterity++;
            attack = getAttack();
            defense = getDefense();

            gamePanel.playSE(7); //level up SE
            gamePanel.gameState = gamePanel.dialogueState;
            gamePanel.ui.currentDialogue = "Level up!\n" + "You are now level " + level;
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
            int moveX = 0;
            int moveY = 0;
            String newDirection = direction;

            if (keyHandler.up) {
                moveY -= speed;
                newDirection = "up";
            }
            if (keyHandler.down) {
                moveY += speed;
                newDirection = "down";
            }
            if (keyHandler.left) {
                moveX -= speed;
                newDirection = "left";
            }
            if (keyHandler.right) {
                moveX += speed;
                newDirection = "right";
            }
            if (keyHandler.left || keyHandler.right) {
                if (keyHandler.left) direction = "left";
                if (keyHandler.right) direction = "right";
            } else if (keyHandler.up || keyHandler.down) {
                direction = newDirection;
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
                //move diagonally
                if (moveX != 0) {
                    worldX += moveX;
                }
                if (moveY != 0) {
                    worldY += moveY;
                }
            }

            if (keyHandler.interact && !attackCancel) {
                gamePanel.playSE(7);//attack sound
                attacking = true;
                spriteCounter = 0;
            }
            attackCancel = false;

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
        }
        if (iFrame) {
            iFrameCounter++;
            if (iFrameCounter > 60) {
                iFrame = false;
                iFrameCounter = 0;
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
