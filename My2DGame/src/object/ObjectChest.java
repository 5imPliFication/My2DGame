package object;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.io.IOException;

public class ObjectChest extends SuperObject{
    GamePanel gamePanel;
    public ObjectChest(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        name="Chest";
        try{
            image = ImageIO.read(getClass().getResourceAsStream("/objects/chest.png"));
            util.scaledImage(image,gamePanel.tileSize,gamePanel.tileSize);
        }catch(IOException e){
            e.printStackTrace();
        }
    }
}
