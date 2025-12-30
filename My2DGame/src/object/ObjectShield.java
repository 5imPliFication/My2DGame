package object;

import entity.Entity;
import main.GamePanel;

public class ObjectShield extends Entity {
    public ObjectShield(GamePanel gamePanel) {
        super(gamePanel);

        name = "Wooden Shield";
        down1 = setup("/objects/wooden_shield", gamePanel.tileSize, gamePanel.tileSize);
        defenseValue = 1;
    }
}
