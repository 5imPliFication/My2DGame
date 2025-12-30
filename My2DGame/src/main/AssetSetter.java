package main;

import entity.NPC_Yellow;
import main.monster.Monster_GreenSlime;
import object.ObjectBoots;
import object.ObjectChest;
import object.ObjectDoor;
import object.ObjectKey;

public class AssetSetter {
    GamePanel gamePanel;

    public AssetSetter(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    public void setObject() {
//        gamePanel.target[0] = new ObjectDoor(gamePanel);
//        gamePanel.target[0].worldX = gamePanel.tileSize*21;
//        gamePanel.target[0].worldY = gamePanel.tileSize*22;
//
//        gamePanel.target[1] = new ObjectDoor(gamePanel);
//        gamePanel.target[1].worldX = gamePanel.tileSize*23;
//        gamePanel.target[1].worldY = gamePanel.tileSize*25;
    }

    public void setNPC() {
        gamePanel.npc[0] = new NPC_Yellow(gamePanel);
        gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[0].worldY = gamePanel.tileSize * 21;

        gamePanel.npc[1] = new NPC_Yellow(gamePanel);
        gamePanel.npc[1].worldX = gamePanel.tileSize * 10;
        gamePanel.npc[1].worldY = gamePanel.tileSize * 21;

        gamePanel.npc[2] = new NPC_Yellow(gamePanel);
        gamePanel.npc[2].worldX = gamePanel.tileSize * 12;
        gamePanel.npc[2].worldY = gamePanel.tileSize * 8;
    }
    public void setMonster() {
        gamePanel.monster[0] = new Monster_GreenSlime(gamePanel);
        gamePanel.monster[0].worldX = gamePanel.tileSize * 23;
        gamePanel.monster[0].worldY = gamePanel.tileSize * 36;

        gamePanel.monster[1] = new Monster_GreenSlime(gamePanel);
        gamePanel.monster[1].worldX = gamePanel.tileSize * 24;
        gamePanel.monster[1].worldY = gamePanel.tileSize * 37;

        gamePanel.monster[2] = new Monster_GreenSlime(gamePanel);
        gamePanel.monster[2].worldX = gamePanel.tileSize * 25;
        gamePanel.monster[2].worldY = gamePanel.tileSize * 37;

        gamePanel.monster[3] = new Monster_GreenSlime(gamePanel);
        gamePanel.monster[3].worldX = gamePanel.tileSize * 26;
        gamePanel.monster[3].worldY = gamePanel.tileSize * 37;

        gamePanel.monster[4] = new Monster_GreenSlime(gamePanel);
        gamePanel.monster[4].worldX = gamePanel.tileSize * 22;
        gamePanel.monster[4].worldY = gamePanel.tileSize * 37;
    }
}
