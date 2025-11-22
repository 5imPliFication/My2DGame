package object;

import entity.Entity;
import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectHeart extends Entity {

    public ObjectHeart(GamePanel gamePanel) {
        super(gamePanel);
        name = "Heart";
        image1 = setup("/objects/heart_full");
        image2 = setup("/objects/heart_half");
        image3 = setup("/objects/heart_blank");
    }
}
