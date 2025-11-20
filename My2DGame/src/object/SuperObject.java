package object;

import main.GamePanel;
import main.UtilityTool;

import java.awt.*;
import java.awt.image.BufferedImage;

public class SuperObject {

    public BufferedImage image;
    public String name;
    public boolean collided = false;
    public int worldX, worldY;
    public Rectangle solidArea = new Rectangle(0, 0, 48, 48);
    public int solidAreaDefaultX = 0, solidAreaDefaultY = 0;
    UtilityTool util = new UtilityTool();

    public void drawImage(Graphics2D g2d, GamePanel gamePanel) {
        int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
        int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
        if (screenX + gamePanel.tileSize > -gamePanel.tileSize &&      // allow 1 tile off left
                screenX < gamePanel.screenWidth + gamePanel.tileSize &&    // allow 1 tile off right ← THIS FIXES IT
                screenY + gamePanel.tileSize > -gamePanel.tileSize &&
                screenY < gamePanel.screenHeight + gamePanel.tileSize) {
            g2d.drawImage(image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
        }

    }
}
