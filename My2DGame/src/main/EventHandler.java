package main;

import java.awt.*;

public class EventHandler {
    GamePanel gamePanel;
    EventRect eventRectangle[][];

    int previousEventX, previousEventY;
    boolean canTouchEvent = true;

    public EventHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        eventRectangle = new EventRect[gamePanel.maxWorldCol][gamePanel.maxWorldRow];
        int col = 0;
        int row = 0;
        while (col < gamePanel.maxWorldCol && row < gamePanel.maxWorldRow) {
            eventRectangle[col][row] = new EventRect();
            eventRectangle[col][row].x = 23;
            eventRectangle[col][row].y = 23;
            eventRectangle[col][row].width = 4;
            eventRectangle[col][row].height = 4;
            eventRectangle[col][row].eventRectDefaultX = eventRectangle[col][row].x;
            eventRectangle[col][row].eventRectDefaultY = eventRectangle[col][row].y;
            col++;
            if (col == gamePanel.maxWorldCol) {
                col = 0;
                row++;
            }
        }
    }

    public void checkEvent() {
        //check if the player is away from the event triggered previously
        int xDistance = Math.abs(gamePanel.player.worldX - previousEventX);
        int yDistance = Math.abs(gamePanel.player.worldY - previousEventY);
        int distance = Math.max(xDistance,yDistance);
        if (distance > gamePanel.tileSize) {
            canTouchEvent = true;
        }
        if(canTouchEvent){
            if (hit(27, 16, "right")) {
                damagePit(27, 16, gamePanel.dialogueState);
            }
            if (hit(23, 12, "up")) {
                healingPool(23, 12, gamePanel.dialogueState);
            }
            if (hit(19, 16, "left")) {
                teleportTile(19, 16, gamePanel.dialogueState);
            }
        }
    }

    private void teleportTile(int col, int row, int gameState) {
        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialogue = "You have been\nTELEPORTED!";
        gamePanel.player.worldX = gamePanel.tileSize * 37;
        gamePanel.player.worldY = gamePanel.tileSize * 10;

    }

    private void damagePit(int col, int row, int gameState) {
        gamePanel.gameState = gameState;
        gamePanel.ui.currentDialogue = "You fell into a pit.";
        gamePanel.player.life -= 1;
//        eventRectangle[col][row].eventDone = true;
        canTouchEvent = false;
    }

    private void healingPool(int col, int row, int gameState) {
        if (gamePanel.keyHandler.interact) {
            gamePanel.player.attackCancel = true;
            gamePanel.gameState = gameState;
            gamePanel.ui.currentDialogue = "You drank the water.\nYour life is restored";
            gamePanel.player.life = gamePanel.player.maxLife;
        }
    }

    public boolean hit(int eventCol, int eventRow, String regDirection) {
        boolean hit = false;
        gamePanel.player.solidArea.x = gamePanel.player.solidArea.x + gamePanel.player.worldX;
        gamePanel.player.solidArea.y = gamePanel.player.solidArea.y + gamePanel.player.worldY;
        eventRectangle[eventCol][eventRow].x = eventCol * gamePanel.tileSize + eventRectangle[eventCol][eventRow].x;
        eventRectangle[eventCol][eventRow].y = eventRow * gamePanel.tileSize + eventRectangle[eventCol][eventRow].y;

        //check player collision with event
        if (gamePanel.player.solidArea.intersects(eventRectangle[eventCol][eventRow])
                && eventRectangle[eventCol][eventRow].eventDone == false) {
            if (gamePanel.player.direction.contentEquals(regDirection) || regDirection.contentEquals("any")) {
                hit = true;
                previousEventX = gamePanel.player.worldX;
                previousEventY = gamePanel.player.worldY;
            }
        }
        gamePanel.player.solidArea.x = gamePanel.player.solidAreaDefaultX;
        gamePanel.player.solidArea.y = gamePanel.player.solidAreaDefaultY;
        eventRectangle[eventCol][eventRow].x = eventRectangle[eventCol][eventRow].eventRectDefaultX;
        eventRectangle[eventCol][eventRow].y = eventRectangle[eventCol][eventRow].eventRectDefaultY;
        return hit;
    }

}
