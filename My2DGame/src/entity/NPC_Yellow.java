package entity;

import main.GamePanel;
import java.util.Random;

public class NPC_Yellow extends Entity {
    public NPC_Yellow(GamePanel gamePanel) {
        super(gamePanel);
        direction = "down";
        setDialogue();
        speed = 1;
        getNPCImage();
    }

    public void getNPCImage() {
        up1 = setup("/npcs/npc00/npc-up-0");
        up2 = setup("/npcs/npc00/npc-up-1");
        up3 = setup("/npcs/npc00/npc-up-2");
        down1 = setup("/npcs/npc00/npc-down-0");
        down2 = setup("/npcs/npc00/npc-down-1");
        down3 = setup("/npcs/npc00/npc-down-2");
        left1 = setup("/npcs/npc00/npc-left-0");
        left2 = setup("/npcs/npc00/npc-left-1");
        left3 = setup("/npcs/npc00/npc-left-2");
        right1 = setup("/npcs/npc00/npc-right-0");
        right2 = setup("/npcs/npc00/npc-right-1");
        right3 = setup("/npcs/npc00/npc-right-2");
    }

    public void setDialogue() {
        dialogues[0] = "Hello there!";
        dialogues[1] = "Welcome to ngulon vile.";
        dialogues[2] = "Why am I piss color.";
        dialogues[3] = "Nice to meet you!";
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

    public void speak() {
        super.speak();
    }
}
