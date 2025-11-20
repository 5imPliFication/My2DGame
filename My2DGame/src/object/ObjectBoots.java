package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectBoots extends SuperObject {
    GamePanel gamePanel;
    public ObjectBoots(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        name = "Boots";
        try {
            image = ImageIO.read(getClass().getResourceAsStream("/objects/boots.png"));
            util.scaledImage(image,gamePanel.tileSize,gamePanel.tileSize);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
