import javax.swing.*;
import java.awt.event.*;

public class Controller implements KeyListener, MouseListener, MouseMotionListener, MouseWheelListener {
    private GameWindow gameWindow;
    private Game game;
    private boolean isRunning = true;
    final private int TICKSPEED = 15;
    Controller(GameWindow gameWindow) {
        setupWindow(gameWindow);
        this.game = gameWindow.coglessGameGraphics.game;
        while (isRunning) {
            try {
                gameWindow.repaint();
                game.Update();
                Thread.sleep( TICKSPEED);
            }
            catch (Exception e) {

            }
        }
    }

    private void setupWindow(GameWindow gameWindow) {
        this.gameWindow = gameWindow;
        JPanel gamePanel = new JPanel();
        gameWindow.setup(gamePanel, this);
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        if(e.getKeyCode() == KeyEvent.VK_A) {
            game.setKeyA(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_D) {
            game.setKeyD(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_W) {
            game.setKeyW(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_S) {
            game.setKeyS(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_P) {
            System.out.println(game.getCursorLocation());
            game.player.setHit(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.setKeySpace(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            game.setKeyShift(true);
        }

        if(e.getKeyCode() == KeyEvent.VK_G) {
            if (game.isGrid())
                game.setGrid(false);
            else
                game.setGrid(true);
        }

        if (e.getKeyCode() == KeyEvent.VK_B) {
            if (game.getGameMode() == 0)
                game.setGameMode(1);
            else
                game.setGameMode(0);
        }

        if (e.getKeyCode() == KeyEvent.VK_F3) {
            game.setDebug(!game.isDebug());
        }

        if(e.getKeyCode() == KeyEvent.VK_R) {
            game.setKeyR(true);
        }


        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            System.exit(1);
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if(e.getKeyCode() == KeyEvent.VK_A) {
            game.setKeyA(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_D) {
            game.setKeyD(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_W) {
            game.setKeyW(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_S) {
            game.setKeyS(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            game.setKeySpace(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_SHIFT) {
            game.setKeyShift(false);
        }

        if(e.getKeyCode() == KeyEvent.VK_R) {
            game.setKeyR(false);
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            game.setLClick(true);
        }
        else if (e.getButton() == MouseEvent.BUTTON3) {
            game.setRClick(true);
        }
        else if (e.getButton() == MouseEvent.BUTTON2) {
            game.setMClick(true);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            game.setLClick(false);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            game.setRClick(false);
        } else if (e.getButton() == MouseEvent.BUTTON2) {
            game.setMClick(false);
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        game.setMouseEvent(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        game.setMouseEvent(e);
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        game.selectedObjectID += e.getPreciseWheelRotation();
        if (game.selectedObjectID < 0) {
            game.selectedObjectID = 0;
        }
        else if (game.selectedObjectID > 3) {
            game.selectedObjectID = 3;
        }
}
}