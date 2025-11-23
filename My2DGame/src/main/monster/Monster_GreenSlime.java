package main.monster;

import entity.Entity;
import main.GamePanel;

import java.awt.*;
import java.util.Random;

public class Monster_GreenSlime extends Entity {
    public Monster_GreenSlime(GamePanel gamePanel) {
        super(gamePanel);
        name = "Green Slime";
        entityType = 2;
        speed = 1;
        maxLife = 4;
        life = maxLife;

        solidArea.x = 3;
        solidArea.y = 18;
        solidArea.width = 42;
        solidArea.height = 30;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
        getImage();
    }

    public void getImage() {
        up1 = setup("/monster/greenslime_down_1");
        up2 = setup("/monster/greenslime_down_2");
        up3 = setup("/monster/greenslime_down_1");
        down1 = setup("/monster/greenslime_down_1");
        down2 = setup("/monster/greenslime_down_2");
        down3 = setup("/monster/greenslime_down_1");
        left1 = setup("/monster/greenslime_down_1");
        left2 = setup("/monster/greenslime_down_2");
        left3 = setup("/monster/greenslime_down_1");
        right1 = setup("/monster/greenslime_down_1");
        right2 = setup("/monster/greenslime_down_2");
        right3 = setup("/monster/greenslime_down_1");
    }

    public void setAction() {
        actionCooldown++;
        if (actionCooldown == 120) {
            Random random = new Random();
            int i = random.nextInt(100);  //pick random number from 1->99
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
}
