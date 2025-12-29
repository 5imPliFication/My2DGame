package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectChest extends Entity {
    public ObjectChest(GamePanel gamePanel) {
        super(gamePanel);
        name="Chest";
        down1 = setup("/objects/chest", gamePanel.tileSize, gamePanel.tileSize);
    }
}
