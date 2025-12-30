package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectKey extends Entity {
    public ObjectKey(GamePanel gamePanel) {
        super(gamePanel);
        name="Key";
        down1 = setup("/objects/key", gamePanel.tileSize, gamePanel.tileSize);
        description = "["+name+"]\n"+"Can open door type shi";
    }
}
