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

        // not culling, wasting cpu resources
        int worldCol = 0;
        int worldRow = 0;
        while (worldCol < gamePanel.maxWorldCol && worldRow < gamePanel.maxWorldRow) {
            int tileNum = mapTileNum[worldCol][worldRow];

            int worldX = worldCol * gamePanel.tileSize;
            int worldY = worldRow * gamePanel.tileSize;
            int screenX = worldX - gamePanel.player.worldX + gamePanel.player.screenX;
            int screenY = worldY - gamePanel.player.worldY + gamePanel.player.screenY;
//            if (worldX + gamePanel.tileSize > gamePanel.player.worldX - gamePanel.player.screenX &&
//                    worldX - gamePanel.tileSize < gamePanel.player.worldX + gamePanel.player.screenX &&
//                    worldY + gamePanel.tileSize > gamePanel.player.worldY - gamePanel.player.screenY &&
//                    worldY - gamePanel.tileSize < gamePanel.player.worldY + gamePanel.player.screenY) {
//                g2d.drawImage(tiles[tileNum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
//            } //this is player oriented, resource hogging
            if (screenX + gamePanel.tileSize > -gamePanel.tileSize &&      // allow 1 tile off left
                    screenX < gamePanel.screenWidth + gamePanel.tileSize &&    // allow 1 tile off right ← THIS FIXES IT
                    screenY + gamePanel.tileSize > -gamePanel.tileSize &&
                    screenY < gamePanel.screenHeight + gamePanel.tileSize) {
                g2d.drawImage(tiles[tileNum].image, screenX, screenY, gamePanel.tileSize, gamePanel.tileSize, null);
            } // this is screen oriented, utilizing culling method

            worldCol++;
            if (worldCol == gamePanel.maxWorldCol) {
                worldCol = 0;
                worldRow++;
            }
        }
//        // Calculate world tile offset (camera center)
//        int worldColOffset = (int)(gamePanel.player.worldX / gamePanel.tileSize);
//        int worldRowOffset = (int)(gamePanel.player.worldY / gamePanel.tileSize);
//
//        // CLAMP: prevent array out of bounds
//        worldColOffset = Math.max(0, Math.min(worldColOffset, gamePanel.maxWorldCol - gamePanel.maxScreenCol - 1));
//        worldRowOffset = Math.max(0, Math.min(worldRowOffset, gamePanel.maxWorldRow - gamePanel.maxScreenRow - 1));
//
//        // DRAW ONLY SCREEN TILES +1 BUFFER (17x13 = 221 tiles max vs 2500!)
//        int screenColCount = gamePanel.maxScreenCol + 1;
//        int screenRowCount = gamePanel.maxScreenRow + 1;
//
//        for (int screenCol = 0; screenCol < screenColCount; screenCol++) {
//            int worldCol = worldColOffset + screenCol;
//            if (worldCol >= gamePanel.maxWorldCol) continue;
//
//            int screenX = screenCol * gamePanel.tileSize;
//
//            for (int screenRow = 0; screenRow < screenRowCount; screenRow++) {
//                int worldRow = worldRowOffset + screenRow;
//                if (worldRow >= gamePanel.maxWorldRow) continue;
//
//                int tileNum = mapTileNum[worldCol][worldRow];
//                int screenY = screenRow * gamePanel.tileSize;
//
//                g2d.drawImage(tiles[tileNum].image, screenX, screenY,
//                        gamePanel.tileSize, gamePanel.tileSize, null);
//            }
//        }
    }
}
