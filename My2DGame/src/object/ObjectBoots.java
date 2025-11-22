package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectBoots extends Entity {
    public ObjectBoots(GamePanel gamePanel) {
        super(gamePanel);
        name = "Boots";
        down1 = setup("/objects/boots");
    }
}
