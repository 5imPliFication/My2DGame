package main;

import entity.NPC_Yellow;
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
        gamePanel.target[0] = new ObjectDoor(gamePanel);
        gamePanel.target[0].worldX = gamePanel.tileSize*21;
        gamePanel.target[0].worldY = gamePanel.tileSize*22;

        gamePanel.target[1] = new ObjectDoor(gamePanel);
        gamePanel.target[1].worldX = gamePanel.tileSize*23;
        gamePanel.target[1].worldY = gamePanel.tileSize*25;
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
}
