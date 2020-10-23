import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;

public class Game {

    Player player;
    public final int OBJECT_SIZE = 100;
    private Level currentLevel;
    private MenuScreen mainMenu, currentMenu;
    private Effect[] effects = new Effect[100];
    private int runtime = 0, effectsCount;
    boolean debug = false;
    static boolean godPause = false;

    private boolean grid = true;
    private MouseEvent mouseEvent;
    private Point cursorLocation = new Point(0, 0);
    private Point screenCorner = new Point(0, 1250);
    private BufferedImage effectsSheet, objectSheet, playerSheet;
    public int selectedObjectID = 0;

    private boolean lClick = false, rClick = false, mClick = false;
    private boolean keyA = false, keyD = false, keyW = false, keyS = false, keySpace = false, keyShift = false, keyR = false;

    private int gameMode = -1;

    Game() {
        imageReadIn();
        this.player = new Player(0, 0, playerSheet);
        createNewLevel(player);
        mainMenu = new MenuScreen("main", this);
        setCurrentMenu(mainMenu);
    }

    private void imageReadIn() {
        try {
            effectsSheet = ImageIO.read(new File("assets/effects/effectsSheet.png"));
        } catch (Exception e) {
            System.out.println("image not found");
        }
        try {
            objectSheet = ImageIO.read(new File("assets/world/worldSheet.png"));
        }
        catch (Exception e) {
            System.out.println("Objects sprite sheet not found");
        }
        try {
            playerSheet = ImageIO.read(new File("assets/character/spriteSheet.png"));
        } catch (Exception e) {
            System.out.println("Character sprite sheet not found");
        }
    }

    public void Update() {
        runtime++;
        lifeTimeUpdate();
        switch (gameMode) {
            case -1:
                menuMode();
                break;
            case 0:
                buildMode();
                break;
            case 1:
                playMode();
                break;
            default: gameMode = -1;
        }
    }

    public void setCurrentMenu(MenuScreen newMenu) {
        this.currentMenu = newMenu;
    }

    public MenuScreen getCurrentMenu() {
        return currentMenu;
    }

    private void menuMode() {
        updateCursorLocation(mouseEvent);
        if (lClick) {
            currentMenu.interact(cursorLocation);
        }
    }

    private void buildMode() {
        if (keyR) {
            createNewLevel(player);
        }
        updateCursorLocation(mouseEvent);
        scrollUpdate();
        buildCheck();

    }

    private void playMode() {
        deathCheck();

        if (!godPause) {
            if (keyR) {
                player.incrementSelfDestruct();
            } else {
                player.setSelfDestructTime(0);
                if (!player.isEyesOpen())
                    player.toggleBlink();
                physicsUpdate();
                effectsUpdate();
                intersectUpdate();
//            lookDirectionUpdate();
            }
        }
        player.updateDeathSequence();
        scrollUpdate();
    }

    private void effectsUpdate() {
        walkEffects();
        damageEffects();
    }

    private void addEffect(Effect effect) {
        for (int i = 0; i < effects.length - 1; i++) {
            if (effects[i] == null) {
                effects[i] = effect;
                effectsCount++;
                i = effects.length;
            }
        }
    }

    private void walkEffects() {
        int ratio = (int) player.getVelocity().getX();
        if (ratio != 0 && runtime % (30 / ratio) == 0) {
            if (player.isOnGround()) {
                if (player.getVelocity().getX() != 0) {
                    addEffect(new Effect(0, player.getLocation().x, player.getLocation().y + 100, effectsSheet));
                }
            }
        }
    }

    private void damageEffects() {
        if (player.getHealth() < 3) {
            int ratio = player.getHealth();
            if (ratio != 0 && runtime % (25 * ratio) == 0) {
                int y = player.getLocation().y + (int) (Math.random() * 100);
                int x = player.getLocation().x - 50 + (int) (Math.random() * 100);
                addEffect(new Effect(0, x, y, effectsSheet));
            }
        }
    }

    private void deathCheck() {
        if (player.getLocation().y > currentLevel.getLevelHeight() * 100)
            player.death();
        if (player.getLives() < 0) {
            gameOver();
        }
    }

    private void intersectUpdate() {
        if (currentLevel.getCheckpoint() != null) {
            if (MathsMethods.checkIntersect(player.getLocationDec(), currentLevel.getCheckpoint().location.x, currentLevel.getCheckpoint().location.y) || MathsMethods.checkIntersect(player.getLocationDec(), currentLevel.getCheckpoint().location.x, currentLevel.getCheckpoint().location.y + 100)) {
                player.setOrigin(getCheckpoint().getSpawnPos());
                currentLevel.getCheckpoint().active = true;
            }
        }

        if (MathsMethods.checkIntersect(player.getLocationDec(), currentLevel.getEnd().getEndPoint().x, currentLevel.getEnd().getEndPoint().y) || MathsMethods.checkIntersect(player.getLocationDec(), currentLevel.getEnd().getEndPoint().x, currentLevel.getEnd().getEndPoint().y + 100) || MathsMethods.checkIntersect(player.getLocationDec(), currentLevel.getEnd().getEndPoint().x, currentLevel.getEnd().getEndPoint().y - 100)) {
            levelEnd();
        }
    }

    private void createNewLevel(Player player) {
        currentLevel = new Level(player);
    }

    private void buildCheck() {
        int x = MathsMethods.valueSnap(cursorLocation.x + getScreenCorner().x, OBJECT_SIZE);
        int y = MathsMethods.valueSnap(cursorLocation.y + getScreenCorner().y, OBJECT_SIZE);
        if (lClick) {
            if (!currentLevel.addObject(createLevelObject(x, y, selectedObjectID))) ;
        }
        else if (rClick) {
            if (currentLevel.removeObject(createLevelObject(x, y, 0))) {
            } else if (currentLevel.getCheckpoint() != null && currentLevel.getCheckpoint().getLocation().equals(new Point(x, y))) {
                currentLevel.removeCheckpoint();
            }
        }
        if (mClick) {
            if (cursorLocation.x + screenCorner.getX() <= currentLevel.getStart().WIDTH)
                currentLevel.moveStart(y);
            else if (cursorLocation.x + screenCorner.getX() >= currentLevel.getEnd().getLocation().getX())
                currentLevel.moveEnd(y);
            else {
                currentLevel.createCheckpoint(MathsMethods.valueSnap(cursorLocation.getX() + screenCorner.getX(), 100), MathsMethods.valueSnap(cursorLocation.y + screenCorner.getY(), 100));
            }
        }
    }

    private void physicsUpdate() {
        if (keyShift) {
            player.sprintTime++;
            if (player.sprintTime > 40)
                player.sprintTime = 40;
        } else {
            player.sprintTime--;
            if (player.sprintTime < 0)
                player.sprintTime = 0;
        }
        if (keyA) {
            if (player.getVelocity().getX() > 0) {
                player.sprintTime = 0;
            }
            player.setForce(-75 - (player.sprintTime * 2), player.getForce().getY());
        } else if (keyD) {
            if (player.getVelocity().getX() < 0) {
                player.sprintTime = 0;
            }
            player.setForce(75 + (player.sprintTime * 2), player.getForce().getY());
        }

        if (keySpace && player.isOnGround()) {
            if (player.getVelocity().getX() > 2 || player.getVelocity().getX() < -2)
                player.setForce(player.getForce().getX(), -350);
            else
                player.setForce(player.getForce().getX(), -300);
        }

        damageCheck();
        player.physicsUpdate(currentLevel);
    }

    private void scrollUpdate() {
        if (gameMode == 0) {
            int speed;
            if (keyShift)
                speed = 20;
            else
                speed = 10;

            if (keyA && !keyD) {
                screenCorner.x -= speed;
            } else if (keyD && !keyA) {
                screenCorner.x += speed;
            }

            if (keyW && !keyS) {
                screenCorner.y -= speed;
            }
            if (keyS && !keyW) {
                screenCorner.y += speed;
            }
        } else if (gameMode == 1) {
            screenCorner.x = player.getLocation().x - 1920 / 2;
            screenCorner.y = (player.getLocation().y + 100) - 1080 / 2;
        }

        if (screenCorner.x < 0) {
            screenCorner.x = 0;
        } else if (screenCorner.x > getLevelWidth() * 100 - 1921) {
            screenCorner.x = getLevelWidth() * 100 - 1921;
        }

        if (screenCorner.y < 0) {
            screenCorner.y = 0;
        } else if (screenCorner.y > getLevelHeight() * 100 - 1081) {
            screenCorner.y = getLevelHeight() * 100 - 1081;
        }
    }

    private void damageCheck() {
        player.damageUpdate();
    }

    private void lifeTimeUpdate() {
        for (LevelObject[] currentArray : currentLevel.getLevelObjects()) {
            for (LevelObject current : currentArray) {
                if (current != null && current.getBlockID() != -1) {
                    if (current.getLifeTime() < 10) {
                        current.setLifeTime(current.getLifeTime() + 1);
                    }
                }
            }
        }
        for (int i = 0; i < effects.length - 1; i++) {
            Effect currentEffect = effects[i];
            if (currentEffect != null && currentEffect.lifeTime < 120) {
                currentEffect.lifeTime++;
                if (currentEffect.updateCurrentTexture() == -1) {
                    effects[i] = null;
                    effectsCount--;
                }
            }
        }
    }

//    private void lookDirectionUpdate() {
//        if(cursorLocation.x < 1920/2) {
//            player.setLookRight(false);
//        }
//        else {
//            player.setLookRight(true);
//        }
//    }

    private void gameOver() {
        System.out.println("GAMEOVER");
        player.setLives(currentLevel.getLivesCount());
    }

    private void levelEnd() {
        System.out.println("END");
    }

    public LevelObject createLevelObject(int x, int y, int blockID) {
        LevelObject tempObject = new LevelObject(x, y, blockID, objectSheet);
        return tempObject;
    }

    public void setCursorLocation(Point location) {
        this.cursorLocation = location;
    }

    public void setCursorLocation(int x, int y) {
        this.cursorLocation = new Point(x, y);
    }

    public Point getCursorLocation() {
        return cursorLocation;
    }

    public void setGrid(boolean grid) {
        this.grid = grid;
    }

    public boolean isGrid() {
        return grid;
    }

    public Point getScreenCorner() {
        return screenCorner;
    }

    public void setScreenCorner(Point screenCorner) {
        this.screenCorner = screenCorner;
    }

    public void setKeyA(boolean keyA) {
        this.keyA = keyA;
    }

    public void setKeyD(boolean keyD) {
        this.keyD = keyD;
    }

    public void setKeyW(boolean keyW) {
        this.keyW = keyW;
    }

    public void setKeyS(boolean keyS) {
        this.keyS = keyS;
    }

    public void setKeyR(boolean keyR) {
        this.keyR = keyR;
    }

    public void setKeySpace(boolean keySpace) {
        this.keySpace = keySpace;
    }

    public boolean isKeySpace() {
        return keySpace;
    }

    public void setKeyShift(boolean keyShift) {
        this.keyShift = keyShift;
    }

    public boolean isKeyShift() {
        return keyShift;
    }

    public void setGameMode(int mode) {
        gameMode = mode;
    }

    public int getGameMode() {
        return gameMode;
    }

    public void setLClick(boolean click) {
        this.lClick = click;
    }

    public boolean isLClick() {
        return lClick;
    }

    public void setRClick(boolean click) {
        this.rClick = click;
    }

    public boolean isRClick() {
        return rClick;
    }

    public void setMClick(boolean click) {
        this.mClick = click;
    }

    public boolean isMClick() {
        return mClick;
    }

    public int getLevelObjectsCount() {
        return currentLevel.getLevelObjectsCount();
    }

    public LevelObject[][] getLevelObjects() {
        return currentLevel.getLevelObjects();
    }

    public int getLevelHeight() {
        return currentLevel.getLevelHeight();
    }

    public int getLevelWidth() {
        return currentLevel.getLevelWidth();
    }

    public void setMouseEvent(MouseEvent mouseEvent) {
        this.mouseEvent = mouseEvent;
    }

    private void updateCursorLocation(MouseEvent e) {
        int x = e.getLocationOnScreen().x, y = e.getLocationOnScreen().y;
        if (x > 1919)
            x = 1919;
        else if (x < 0)
            x = 0;
        if (y > 1079)
            y = 1079;
        else if (y < 0)
            y = 0;
        setCursorLocation(new Point(x, y));
    }

    public StartPlatform getStart() {
        return currentLevel.getStart();
    }

    public EndPlatform getEnd() {
        return currentLevel.getEnd();
    }

    public Checkpoint getCheckpoint() {
        return currentLevel.getCheckpoint();
    }

    public Effect[] getEffects() {
        return effects;
    }

    public boolean isDebug() {
        return debug;
    }

    public void setDebug(boolean debug) {
        this.debug = debug;
    }

    public static void godPause(boolean b) {
        godPause = b;
    }

    public int getEffectsCount() {
        return effectsCount;
    }
}