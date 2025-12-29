package main;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

public class KeyHandler implements KeyListener {
    GamePanel gamePanel;

    public boolean up, down, left, right, interact = false;
    boolean enableDebug = false;

    public KeyHandler(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();
        //title state
        if (gamePanel.gameState == gamePanel.titleState) {
            if (gamePanel.ui.titleScreenState == 0) {
                if (code == KeyEvent.VK_W) {
                    gamePanel.ui.choice--;
                    if (gamePanel.ui.choice < 0) {
                        gamePanel.ui.choice = 2;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gamePanel.ui.choice++;
                    if (gamePanel.ui.choice > 2) {
                        gamePanel.ui.choice = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    if (gamePanel.ui.choice == 0) {
                        gamePanel.ui.titleScreenState = 1;
                    }
                    if (gamePanel.ui.choice == 1) {

                    }
                    if (gamePanel.ui.choice == 2) {
                        System.exit(0);
                    }
                }
            }
            else if (gamePanel.ui.titleScreenState == 1) {
                if (code == KeyEvent.VK_W) {
                    gamePanel.ui.choice--;
                    if (gamePanel.ui.choice < 0) {
                        gamePanel.ui.choice = 4;
                    }
                }
                if (code == KeyEvent.VK_S) {
                    gamePanel.ui.choice++;
                    if (gamePanel.ui.choice > 4) {
                        gamePanel.ui.choice = 0;
                    }
                }
                if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_SPACE) {
                    if (gamePanel.ui.choice == 0) {
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.playMusic(0);
                    }
                    if (gamePanel.ui.choice == 1) {
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.playMusic(0);
                    }
                    if (gamePanel.ui.choice == 2) {
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.playMusic(0);
                    }
                    if (gamePanel.ui.choice == 3) {
                        gamePanel.gameState = gamePanel.playState;
                        gamePanel.playMusic(0);
                    }
                    if (gamePanel.ui.choice == 4) {
                        gamePanel.ui.titleScreenState = 0;
                    }
                }
            }
        }

        //play state
        //interaction
        else if (gamePanel.gameState == gamePanel.playState) {
            if (code == KeyEvent.VK_W) {
                up = true;
            }
            if (code == KeyEvent.VK_A) {
                left = true;
            }
            if (code == KeyEvent.VK_S) {
                down = true;
            }
            if (code == KeyEvent.VK_D) {
                right = true;
            }
            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.pauseState;
                gamePanel.stopMusic();
            }
            if (code == KeyEvent.VK_F || code == KeyEvent.VK_ENTER) {
                interact = true;
            }


            //debug
            if (code == KeyEvent.VK_F5) {
                enableDebug = !enableDebug;
            }
        }
        //pause state
        else if (gamePanel.gameState == gamePanel.pauseState) {
            if (code == KeyEvent.VK_ESCAPE) {
                gamePanel.gameState = gamePanel.playState;
                gamePanel.playMusic(0);
            }
        }
        //dialogue state
        else if (gamePanel.gameState == gamePanel.dialogueState) {
            if (code == KeyEvent.VK_SPACE || code == KeyEvent.VK_ENTER
                    || code == KeyEvent.VK_ESCAPE || code == KeyEvent.VK_F) {
                gamePanel.gameState = gamePanel.playState;
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();
        if (code == KeyEvent.VK_W) {
            up = false;
        }
        if (code == KeyEvent.VK_A) {
            left = false;
        }
        if (code == KeyEvent.VK_S) {
            down = false;
        }
        if (code == KeyEvent.VK_D) {
            right = false;
        }
    }
}
