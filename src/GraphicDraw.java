import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class GraphicDraw extends JPanel {
    Game game = new Game();
    final int LOOPTIME = 30;
    private int runtime = LOOPTIME, animFrame;
    private BufferedImage missingTexture;

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        try {
            missingTexture = ImageIO.read(new File("assets/missingImage.png"));
        } catch (Exception e) {
        }
        runtime++;
        animFrame = runtime / LOOPTIME;
        if (animFrame > LOOPTIME) {
            runtime = LOOPTIME;
            animFrame = runtime / LOOPTIME;
        }

        drawLevel(g);
        if (game.player.invincibility == 0 || game.player.invincibility % 3 != 0)
            drawPlayer(g);
        drawEffects(g);

        // HUD ELEMENTS
        if (game.isGrid())
            drawGrid(g);
        if (game.isDebug())
            drawDebug(g);
        if (game.getGameMode() == 0 || game.getGameMode() == -1) {
            if (game.getGameMode() == -1)
                drawMenu(g);
            drawCursor(g);
        }

    }

    private void drawDebug(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.green);
        g.drawString("CURSOR POS (GAME): X: " + (game.getCursorLocation().x + game.getScreenCorner().x) + " Y: " + (game.getCursorLocation().y + game.getScreenCorner().y), 20, 20);
        g.drawString("CURSOR POS (PANEL): X: " + game.getCursorLocation().x + " Y: " + game.getCursorLocation().y, 20, 40);
        g.drawString("LEVEL OBJ COUNT: " + game.getLevelObjectsCount() + " / " + game.getLevelHeight() * game.getLevelWidth() + " EFFECTS COUNT: " + game.getEffectsCount() + " / " + game.getEffects().length, 20, 60);
        g.drawString("PLAYER POS: X: " + game.player.location.getX() + " Y: " + game.player.location.getY(), 20, 80);
        g.drawString("PLAYER POS DEC: X: " + game.player.getLocationDec().getX() + " Y: " + game.player.getLocationDec().getY(), 20, 100);
        g.drawString("PLAYER FORCES: X: " + game.player.getForce().getX() + " Y: " + game.player.getForce().getY(), 20, 120);
        g.drawString("PLAYER VEL: X: " + game.player.getVelocity().getX() + " Y: " + game.player.getVelocity().getY(), 20, 140);
        g.drawString("PLAYER ACC: X: " + game.player.getAcceleration().getX() + " Y: " + game.player.getAcceleration().getY(), 20, 160);
        g.drawString("PLAYER ON GROUND: " + game.player.isOnGround() + " AIRTIME: " + game.player.airTime, 20, 180);
        g.drawString("PLAYER STATS: HEALTH: " + game.player.getHealth() + " LIVES: " + game.player.getLives() + " INVIN: " + game.player.getInvincibility() + " DEATH: " + game.player.deathTime, 20, 200);
        g.drawString("SELECTED BLOCK ID (BUILD MODE): " + game.selectedObjectID, 20, 220);
    }

    private void drawMenu(Graphics gr) {
        Graphics2D g =  (Graphics2D) gr;
        MenuScreen menu = game.getCurrentMenu();
        g.setColor(Color.RED);
        for (Button button : menu.menuButtons) {
            if (button != null)
                g.fillRect(button.location.x, button.location.y, button.size.x, button.size.y);
        }
    }

    private void drawGrid(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.BLACK);

        for (int i = 0; i <= game.getLevelWidth(); i++) {
            if (i % 10 == 0)
                g.setColor(Color.RED);
            else
                g.setColor(Color.BLACK);
            g.drawLine(applyLevelShiftX(100 * i), 0, applyLevelShiftX(100 * i), 1080);
        }

        for (int j = 0; j <= game.getLevelWidth(); j++) {
            if (j % 10 == 0)
                g.setColor(Color.RED);
            else
                g.setColor(Color.BLACK);
            g.drawLine(0, applyLevelShiftY(100 * j), 1920, applyLevelShiftY(100 * j));
        }
    }

    private void drawPlayer(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.blue);
        BufferedImage[] outfit = game.player.getOutfit();

        int x = applyLevelShiftX(game.player.location.x);
        int y = applyLevelShiftY(game.player.location.y);

        //HITBOX
        //g.fillRect(x, y, game.OBJECT_SIZE, game.player.getHeight());
        if (game.player.deathTime == 0) {
            if (game.player.getSelfDestructTime() > 0) {
                if (game.player.isEyesOpen()) {
                    game.player.toggleBlink();
                }
                playerDestruct(g, outfit, x, y);
            } else {
                if (game.player.getVelocity().getX() == 0 && game.player.isOnGround())
                    playerIdle(g, outfit, x, y);
                else
                    playerMove(g, outfit, x, y);
                playerBlink();
            }
            //ARM
            g.drawImage(outfit[2], x + 15, y + 55, null);
        } else {
            playerDeath(g, outfit, x, y);
        }

//        //BODY
//        g.drawImage(outfit[1],x,y+70,null);
//        //HEAD
//        g.drawImage(outfit[0],x,y-15,null);
//        //LEGS
//        g.drawImage(outfit[3],x,y+115,null);
//        // g.drawImage(outfit[4],x,y+115,null);
//        //EYES
//        g.drawImage(outfit[5],x,y-15,null);
//        //g.drawImage(outfit[6],x,y-15,null);
//        //ARM
//        g.drawImage(outfit[2],x+15,y+55,null);

    }

    private void playerDeath(Graphics gr, BufferedImage[] outfit, int x, int y) {
        Graphics g = (Graphics2D) gr;
        int deathTime = game.player.deathTime;
//        if (game.player.deathTime > 180) {
//
//        } else {
//            // NEED TO FIND A WAY TO MAKE AN EXPLOSION ARC
//            //BODY
//            g.drawImage(outfit[1], x + (), y + 70, null);
//            //HEAD
//            g.drawImage(outfit[0], x, y - 15, null);
//            //LEGS
//            g.drawImage(outfit[3], x, y + 115, null);
//            // g.drawImage(outfit[4],x,y+115,null);
//            //EYES
//            g.drawImage(outfit[5], x, y - 15, null);
//            //g.drawImage(outfit[6],x,y-15,null);
//            //ARM
//            g.drawImage(outfit[2], x + 15, y + 55, null);
//        }
    }

    private void playerBlink() {
        if (animFrame == 1 && game.player.isEyesOpen()) {
            game.player.toggleBlink();
        } else if (animFrame == 2 && !game.player.isEyesOpen()) {
            game.player.toggleBlink();
        } else if (animFrame == 15 && game.player.isEyesOpen()) {
            game.player.toggleBlink();
        } else if (animFrame == 16 && !game.player.isEyesOpen()) {
            game.player.toggleBlink();
        }

    }

    private void playerIdle(Graphics gr, BufferedImage[] outfit, int x, int y) {
        Graphics2D g = (Graphics2D) gr;

        if (animFrame % 2 == 0) {
            //BODY
            g.drawImage(outfit[1], x, y + 70, null);
            //HEAD
            g.drawImage(outfit[0], x, y - 15, null);
            //EYES
            g.drawImage(outfit[5], x, y - 15, null);
        } else {
            //BODY
            g.drawImage(outfit[1], x, y + 75, null);
            //HEAD
            g.drawImage(outfit[0], x, y - 10, null);
            //EYES
            g.drawImage(outfit[5], x, y - 12, null);
        }
        //LEGS
        g.drawImage(outfit[3], x, y + 115, null);
    }

    private void playerMove(Graphics gr, BufferedImage[] outfit, int x, int y) {
        Graphics2D g = (Graphics2D) gr;
        if (game.player.isOnGround()) {
            int loop;
            if (game.player.getVelocity().getX() >= 1.5 || game.player.getVelocity().getX() <= -1.5) {
                loop = 8;
            } else
                loop = 15;
            if ((runtime / loop) % 2 == 0) {
                if (game.player.isStep()) {
                    game.player.toggleLegs();
                    game.player.setStep(false);
                }
                //BODY
                g.drawImage(outfit[1], x, y + 70, null);
                //HEAD
                g.drawImage(outfit[0], x, y - 15, null);
                //EYES
                g.drawImage(outfit[5], x, y - 15, null);
            } else {
                if (!game.player.isStep()) {
                    game.player.toggleLegs();
                    game.player.setStep(true);
                }
                //BODY
                g.drawImage(outfit[1], x, y + 75, null);
                //HEAD
                g.drawImage(outfit[0], x, y - 10, null);
                //EYES
                g.drawImage(outfit[5], x, y - 12, null);
            }
            //LEGS
            g.drawImage(outfit[3], x, y + 115, null);
        } else {
            if (game.player.getVelocity().getY() < 0) {
                if (game.player.airTime <= 5) {
                    //BODY
                    g.drawImage(outfit[1], x, y + 80, null);
                    //HEAD
                    g.drawImage(outfit[0], x, y - 5, null);
                    //EYES
                    g.drawImage(outfit[5], x, y - 5, null);
                    //LEGS
                    g.drawImage(outfit[3], x, y + 115, null);
                } else {
                    if ((runtime / 15) % 2 == 0) {
                        //BODY
                        g.drawImage(outfit[1], x, y + 70, null);
                        //HEAD
                        g.drawImage(outfit[0], x, y - 25, null);
                        //EYES
                        g.drawImage(outfit[5], x, y - 25, null);
                        //LEGS
                        g.drawImage(outfit[3], x, y + 125, null);
                    } else {
                        //BODY
                        g.drawImage(outfit[1], x, y + 70, null);
                        //HEAD
                        g.drawImage(outfit[0], x, y - 20, null);
                        //EYES
                        g.drawImage(outfit[5], x, y - 20, null);
                        //LEGS
                        g.drawImage(outfit[3], x, y + 130, null);
                    }
                }
            } else if (game.player.getVelocity().getY() >= 0) {
                if ((runtime / 15) % 2 == 0) {
                    //BODY
                    g.drawImage(outfit[1], x, y + 65, null);
                    //HEAD
                    g.drawImage(outfit[0], x, y - 30, null);
                    //EYES
                    g.drawImage(outfit[5], x, y - 30, null);
                    //LEGS
                    g.drawImage(outfit[3], x, y + 125, null);
                } else {
                    //BODY
                    g.drawImage(outfit[1], x, y + 65, null);
                    //HEAD
                    g.drawImage(outfit[0], x, y - 25, null);
                    //EYES
                    g.drawImage(outfit[5], x, y - 25, null);
                    //LEGS
                    g.drawImage(outfit[3], x, y + 130, null);
                }
            }
        }
    }

    private void playerDestruct(Graphics gr, BufferedImage[] outfit, int x, int y) {
        Graphics2D g = (Graphics2D) gr;
        int looptime;
        if (game.player.getSelfDestructTime() > 60)
            looptime = 4;
        else
            looptime = 8;
        if ((runtime / looptime) % 2 == 0) {
            //BODY
            g.drawImage(outfit[1], x, y + 75, null);
            //HEAD
            g.drawImage(outfit[0], x, y - 20, null);
            //EYES
            g.drawImage(outfit[5], x, y - 20, null);
            //LEGS
            g.drawImage(outfit[3], x, y + 110, null);
        } else {
            //BODY
            g.drawImage(outfit[1], x, y + 70, null);
            //HEAD
            g.drawImage(outfit[0], x, y - 15, null);
            //EYES
            g.drawImage(outfit[5], x, y - 15, null);
            //LEGS
            g.drawImage(outfit[3], x, y + 115, null);
        }
    }

    private void drawStart(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        StartPlatform start = game.getStart();
        g.setColor(Color.orange);
        int x = start.getLocation().x, y = start.getLocation().y;
        g.fillRect(applyLevelShiftX(x), applyLevelShiftY(y), start.WIDTH, start.HEIGHT);
    }

    private void drawCheckpoint(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        Checkpoint checkpoint = game.getCheckpoint();
        g.setColor(Color.orange);
        int x = checkpoint.getLocation().x, y = checkpoint.getLocation().y;
        g.fillRect(applyLevelShiftX(x), applyLevelShiftY(y), checkpoint.WIDTH, checkpoint.HEIGHT);
    }

    private void drawEnd(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        EndPlatform end = game.getEnd();
        g.setColor(Color.orange);
        int x = end.getLocation().x, y = end.getLocation().y;
        g.fillRect(applyLevelShiftX(x), applyLevelShiftY(y), end.WIDTH, end.HEIGHT);
    }

    private void drawEffects(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        for (Effect current : game.getEffects()) {
            if (current != null) {
//                g.drawImage(current.currentEffectTexture, applyLevelShiftX(current.getLocation().x), applyLevelShiftY(current.getLocation().y), null);
                Image texture;
                int x = applyLevelShiftX(current.getLocation().x);
                int y = applyLevelShiftY(current.getLocation().y);
                int size = current.lifeTime;
                if (size == 0)
                    size = 1;
                if (size < 10) {
                    texture = current.currentEffectTexture.getScaledInstance(size * 10, size * 10, Image.SCALE_FAST);
                    x = (current.SIZE - size * 10) / 2 + x;
                    y = (current.SIZE - size * 10) / 2 + y;
                } else
                    texture = current.currentEffectTexture;
                g.drawImage(texture, x, y, null);
            }
        }
    }

    private void drawLevel(Graphics gr) {
        drawStart(gr);
        drawEnd(gr);
        if (game.getCheckpoint() != null)
            drawCheckpoint(gr);

        Graphics2D g = (Graphics2D) gr;
        LevelObject[][] temp = game.getLevelObjects();
        for (LevelObject[] currentList : temp) {
            for (LevelObject current : currentList) {
                if (current != null && current.getBlockID() != -1) {
                    int x = applyLevelShiftX(current.getLocation().x);
                    int y = applyLevelShiftY(current.getLocation().y);
                    int size = current.getLifeTime();
                    Image texture;
                    if (size < 10) {
                        try {
                            texture = current.getTextures()[current.getConnectedTextureID()].getScaledInstance(size * 10, size * 10, Image.SCALE_FAST);
                        } catch (Exception e) {
                            texture = missingTexture.getScaledInstance(size * 10, size * 10, Image.SCALE_FAST);
                        }
                        x = (current.SIZE - size * 10) / 2 + x;
                        y = (current.SIZE - size * 10) / 2 + y;
                    } else
                        texture = current.getTextures()[current.getConnectedTextureID()];
                    if (texture == null)
                        texture = missingTexture;
                    g.drawImage(texture, x, y, null);
                }
            }
        }
    }

    private void drawCursor(Graphics gr) {
        Graphics2D g = (Graphics2D) gr;
        g.setColor(Color.red);
        g.fillRect(game.getCursorLocation().x - 5, game.getCursorLocation().y - 5, 10, 10);
    }

    private int applyLevelShiftX(int xvalue) {
        return Math.round(xvalue - game.getScreenCorner().x);
    }

    private int applyLevelShiftY(int yvalue) {
        return Math.round(yvalue - game.getScreenCorner().y);
    }
}