package object;

import entity.Entity;
import main.GamePanel;

public class ObjectSwordNormal extends Entity {
    public ObjectSwordNormal(GamePanel gamePanel) {
        super(gamePanel);

        name = "Normal Sword";
        down1 = setup("/objects/sword_normal",gamePanel.tileSize,gamePanel.tileSize);
        attackValue = 1;
    }
}
