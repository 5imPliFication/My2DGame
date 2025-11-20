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

    }

    public void setNPC() {
        gamePanel.npc[0] = new NPC_Yellow(gamePanel);
        gamePanel.npc[0].worldX = gamePanel.tileSize * 21;
        gamePanel.npc[0].worldY = gamePanel.tileSize * 21;
    }
}
