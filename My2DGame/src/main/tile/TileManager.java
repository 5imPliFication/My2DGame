package main.tile;

import main.GamePanel;
import main.UtilityTool;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gamePanel;
    public Tile[] tiles;
    public int mapTileNum[][];

    public TileManager(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        tiles = new Tile[50];
        mapTileNum = new int[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
        getTileImage();
        loadMap("/maps/worldV2.txt");
    }

    public void getTileImage() {
        //placeholder
        setup(0, "grass00", false);
        setup(1, "grass00", false);
        setup(2, "grass00", false);
        setup(3, "grass00", false);
        setup(4, "grass00", false);
        setup(5, "grass00", false);
        setup(6, "grass00", false);
        setup(7, "grass00", false);
        setup(8, "grass00", false);
        setup(9, "grass00", false);
        //placeholder

        setup(10, "grass00", false);
        setup(11, "grass01", false);
        setup(12, "water00", true);
        setup(13, "water01", true);
        setup(14, "water02", true);
        setup(15, "water03", true);
        setup(16, "water04", true);
        setup(17, "water05", true);
        setup(18, "water06", true);
        setup(19, "water07", true);
        setup(20, "water08", true);
        setup(21, "water09", true);
        setup(22, "water10", true);
        setup(23, "water11", true);
        setup(24, "water12", true);
        setup(25, "water13", true);
        setup(26, "road00", false);
        setup(27, "road01", false);
        setup(28, "road02", false);
        setup(29, "road03", false);
        setup(30, "road04", false);
        setup(31, "road05", false);
        setup(32, "road06", false);
        setup(33, "road07", false);
        setup(34, "road08", false);
        setup(35, "road09", false);
        setup(36, "road10", false);
        setup(37, "road11", false);
        setup(38, "road12", false);
        setup(39, "earth", false);
        setup(40, "wall", true);
        setup(41, "tree", true);

    }

    public void setup(int index, String imgPath, boolean collision) {
        UtilityTool util = new UtilityTool();
        try {
            tiles[index] = new Tile();
            tiles[index].image = ImageIO.read(getClass().getResourceAsStream("/tiles/" + imgPath + ".png"));
            tiles[index].image = util.scaledImage(tiles[index].image, gamePanel.tileSize, gamePanel.tileSize);
            tiles[index].collided = collision;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath) {
        try {
            InputStream stream = getClass().getResourceAsStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

            int col = 0;
            int row = 0;
            while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
                String line = reader.readLine(); //read a single line, put it in  the 'line' string
                if (line == null) break;
                String[] numbers = line.split(" ");//split numbers into an array
                while (col < gamePanel.maxWorldCol) {

                    int num = Integer.parseInt(numbers[col]);
                    mapTileNum[col][row] = num;
                    col++;
                }
                if (col == gamePanel.maxWorldCol) {
                    col = 0;
                    row++;
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2d) {
//        // not culling, wasting cpu resources
//        int worldCol = 0;
//        int worldRow = 0;
//        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
//            int tileNum = mapTileNum[worldCol][worldRow];
//
//            int worldX = worldCol * gamePanel.tileSize;
//            int worldY = worldRow * gamePanel.tileSize;
//            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
//            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
//            if (screenX + gamePanel.tileSize > -gamePanel.tileSize &&      // allow 1 tile off left
//                    screenX < gamePanel.screenWidth &&    // allow 1 tile off right ← THIS FIXES IT
//                    screenY + gamePanel.tileSize > -gamePanel.tileSize &&
//                    screenY < gamePanel.screenHeight) {
//                g2d.drawImage(tiles[tileNum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
//            } // this is screen oriented, utilizing culling method
//
//            worldCol++;
//            if (worldCol == gamePanel.maxWorldCol) {
//                worldCol = 0;
//                worldRow++;
//            }
//        }
        // Get stable camera position
        float camX = gamePanel.player.worldX;
        float camY = gamePanel.player.worldY;
        int pScreenX = gamePanel.player.screenX;
        int pScreenY = gamePanel.player.screenY;

        int worldCol = 0;
        int worldRow = 0;

        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;

            // Use float math for smooth movement, cast to int only at the end
            float screenXf = worldX - camX + pScreenX;
            float screenYf = worldY - camY + pScreenY;

            int screenX = (int) screenXf;
            int screenY = (int) screenYf;

            // Allow 1 extra tile on all sides for smooth edges
            if (screenX + gamePanel.tileSize > -gamePanel.tileSize &&
                    screenX < gamePanel.screenWidth + gamePanel.tileSize &&
                    screenY + gamePanel.tileSize > -gamePanel.tileSize &&
                    screenY < gamePanel.screenHeight) {

                // Draw with anti-aliased edges
                g2d.drawImage(tiles[tileNum].image,
                        screenX,
                        screenY,
                        gamePanel.tileSize,
                        gamePanel.tileSize,
                        null);
            }

            worldCol++;
            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
    }
}
