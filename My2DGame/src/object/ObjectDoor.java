package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectDoor extends SuperObject {
    GamePanel gamePanel;
    public ObjectDoor(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        name="Door";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/door.png"));
            util.scaledImage(image,gamePanel.tileSize,gamePanel.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
        collided = true;
    }
}
