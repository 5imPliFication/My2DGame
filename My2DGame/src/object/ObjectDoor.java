package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectDoor extends Entity {
    public ObjectDoor(GamePanel gamePanel) {
        super(gamePanel);
        name="Door";
        down1 = setup("/objects/door");
        collided = true;

        //setup render box for render order
        solidArea.x = 0;
        solidArea.y = 16;
        solidArea.width = 48;
        solidArea.height = 32;
        solidAreaDefaultX = solidArea.x;
        solidAreaDefaultY = solidArea.y;
    }
}
