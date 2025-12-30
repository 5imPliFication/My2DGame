package main.monster;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class Monster_GreenSlime extends Entity {

    GamePanel gamePanel;

    public Monster_GreenSlime(GamePanel gamePanel) {
        super(gamePanel);
        this.gamePanel = gamePanel;

        name = "Green Slime";
        entityType = 2;
        speed = 1;
        maxLife = 4;
        life = maxLife;
        attack = 3;
        defense = 0;
        exp = 2;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage() {
        up1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        up2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
        down1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        down2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
        left1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        left2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
        right1 = setup("/monster/greenslime_down_1", gamePanel.tileSize, gamePanel.tileSize);
        right2 = setup("/monster/greenslime_down_2", gamePanel.tileSize, gamePanel.tileSize);
    }

    public void setAction() {
        actionCooldown++;
        if (actionCooldown == 120) {
            Random random = new Random();
            int i = random.nextInt(101);  //pick random number from 1->100
            if (i <= 25) {
                direction = "up";
            }
            if (i > 25 && i <= 50) {
                direction = "down";
            }
            if (i > 50 && i <= 75) {
                direction = "left";
            }
            if (i > 75 && i < 100) {
                direction = "right";
            }
            actionCooldown = 0;
        }
    }

    public void damagedReaction(){
        actionCooldown = 0;
        direction = gamePanel.player.direction;
    }
}
