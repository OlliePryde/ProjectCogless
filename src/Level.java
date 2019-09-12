import java.awt.*;

public class Level {
    private int levelHeight = 40, levelWidth = 80;
    private int levelObjectsCount = 0;
    private LevelObject godBlock;
    private Player player;
    private StartPlatform start;
    private Checkpoint checkpoint;
    private EndPlatform end;
    private LevelObject[][] levelObjects;
    private int livesCount = 3;

    Level(Player player) {
        this.player = player;
        godBlock = new LevelObject(-100, -100, -1, null);
        levelObjects = new LevelObject[levelWidth][levelHeight];
        createStart();
        createEnd();
    }

    public int getLevelObjectsCount() {
        return levelObjectsCount;
    }

    private void createStart() {
        start = new StartPlatform(MathsMethods.valueSnap((levelHeight * 100 - 500)/2,100));
        player.setOrigin(start.getSpawnPos());
        player.toOrigin();
        buildStartGodBlock();
    }

    public void createCheckpoint(int xNew, int yNew) {
        for (int x = xNew; x < xNew + 100; x++) {
            for (int y = yNew; y < yNew + 200; y++) {
                if (presenceCheck(x, y) == 1)
                    return;
            }
        }
        if (checkpoint != null) {
            removeCheckpoint();
        }
        checkpoint = new Checkpoint(xNew,yNew);
        buildCheckpointGodBlock();
    }

    public void removeCheckpoint() {
        removeCheckpointGodBlock();
        checkpoint = null;
    }


    private void createEnd() {
        end = new EndPlatform(MathsMethods.valueSnap((levelWidth * 100 - 500),100), MathsMethods.valueSnap((levelHeight * 100 - 500)/2,100));
        buildEndGodBlock();
    }

    private void buildStartGodBlock() {
        for (int x = start.getLocation().x / 100; x < (start.getLocation().x + start.WIDTH) / 100; x++) {
            for (int y = start.getLocation().y / 100; y < (start.getLocation().y + start.HEIGHT) / 100; y++) {
                levelObjects[x][y] = godBlock;
            }
        }
    }

    private void buildEndGodBlock() {
        for (int x = end.getLocation().x / 100; x < (end.getLocation().x + end.WIDTH) / 100; x++) {
            for (int y = end.getLocation().y / 100; y < (end.getLocation().y + end.HEIGHT) / 100; y++) {
                levelObjects[x][y] = godBlock;
            }
        }
    }

    private void buildCheckpointGodBlock() {
        for (int x = checkpoint.getLocation().x / 100; x < (checkpoint.getLocation().x + checkpoint.WIDTH) / 100; x++) {
            for (int y = checkpoint.getLocation().y / 100; y < (checkpoint.getLocation().y + checkpoint.HEIGHT) / 100; y++) {
                levelObjects[x][y] = godBlock;
            }
        }
    }

    private void removeCheckpointGodBlock() {
        for (int x = checkpoint.getLocation().x / 100; x < (checkpoint.getLocation().x + checkpoint.WIDTH) / 100; x++) {
            for (int y = checkpoint.getLocation().y / 100; y < (checkpoint.getLocation().y + checkpoint.HEIGHT) / 100; y++) {
                levelObjects[x][y] = null;
            }
        }
    }

    public boolean moveStart(int yNew) {
        for (int x = 0; x < start.WIDTH; x++) {
            for (int y = yNew; y < yNew + start.HEIGHT; y++) {
                if (presenceCheck(x, y) == 1)
                    return false;
            }
        }
        for (int x = start.getLocation().x / 100; x < (start.getLocation().x + start.WIDTH) / 100; x++) {
            for (int y = start.getLocation().y / 100; y < (start.getLocation().y + start.HEIGHT) / 100; y++) {
                levelObjects[x][y] = null;
            }
        }
        start.setLocationDec(0, yNew);

        player.setOrigin(start.getSpawnPos());
        player.toOrigin();
        buildStartGodBlock();
        return true;
    }

    public boolean moveEnd(int yNew) {
        for (int x = end.getLocation().x; x < end.getLocation().x + end.WIDTH; x++) {
            for (int y = yNew; y < yNew + end.HEIGHT; y++) {
                if (presenceCheck(x, y) == 1)
                    return false;
            }
        }
        for (int x = end.getLocation().x / 100; x < (end.getLocation().x + end.WIDTH) / 100; x++) {
            for (int y = end.getLocation().y / 100; y < (end.getLocation().y + end.HEIGHT) / 100; y++) {
                levelObjects[x][y] = null;
            }
        }
        end.setLocationDec(MathsMethods.valueSnap((levelWidth * 100 - 500),100), yNew);
        buildEndGodBlock();
        return true;
    }

    public boolean addObject(LevelObject object) {
        int presenceCheck = presenceCheck(object.getLocation());
        if (presenceCheck == 1 || presenceCheck == -1)
            return false;
        levelObjects[object.location.x / 100][object.location.y / 100] = object;
        updateAdjacent(object);
        updateAdjacent(object.left);
        updateAdjacent(object.right);
        updateAdjacent(object.up);
        updateAdjacent(object.down);
        levelObjectsCount++;
        return true;
    }

    public boolean removeObject(LevelObject object) {
        int presenceCheck = presenceCheck(object.getLocation());
        if (presenceCheck == 1) {
            levelObjects[object.location.x / 100][object.location.y / 100] = null;
            updateAdjacent(object);
            updateAdjacent(object.left);
            updateAdjacent(object.right);
            updateAdjacent(object.up);
            updateAdjacent(object.down);
            levelObjectsCount--;
            return true;
        }
        return false;
    }

    private void updateAdjacent(LevelObject object) {
        if (object != null) {
            int xPos = object.location.x / 100;
            int yPos = object.location.y / 100;

            if (yPos == 0 || levelObjects[xPos][yPos - 1] == godBlock)
                object.up = null;
            else
                object.up = levelObjects[xPos][yPos - 1];

            if (yPos == levelHeight - 1 || levelObjects[xPos][yPos + 1] == godBlock)
                object.down = null;
            else
                object.down = levelObjects[xPos][yPos + 1];

            if (xPos == 0 || levelObjects[xPos - 1][yPos] == godBlock)
                object.left = null;
            else
                object.left = levelObjects[xPos - 1][yPos];

            if (xPos == levelWidth - 1 || levelObjects[xPos + 1][yPos] == godBlock)
                object.right = null;
            else
                object.right = levelObjects[xPos + 1][yPos];

            object.connectedTextureUpdate();
        }
    }

    private int presenceCheck(int x, int y) {
        if (insideBoundaryCheck(x,y) && levelObjects[x / 100][y / 100] != null) {
            if (levelObjects[x / 100][y / 100] == godBlock) {
                return -1;
            }
            else
                return 1;
        }
        return 0;
    }

    private int presenceCheck(Point location) {
        if (insideBoundaryCheck(location) && levelObjects[location.x / 100][location.y / 100] != null) {
            if (levelObjects[location.x / 100][location.y / 100] == godBlock) {
                return -1;
            }
            else
                return 1;
        }
        return 0;
    }



    private boolean insideBoundaryCheck(int x, int y) {
        return x <= levelWidth * 100 && x >= 0 && y <= levelHeight * 100 && y >= 0;
    }

    private boolean insideBoundaryCheck(Point location) {
        return location.x <= levelWidth * 100 && location.x >= 0 && location.y <= levelHeight * 100 && location.y >= 0;
    }

    public LevelObject[][] getLevelObjects() {
        return levelObjects;
    }

    public void setLevelHeight(int levelHeight) {
        this.levelHeight = levelHeight;
    }

    public void setLevelWidth(int levelWidth) {
        this.levelWidth = levelWidth;
    }

    public int getLevelHeight() {
        return levelHeight;
    }

    public int getLevelWidth() {
        return levelWidth;
    }

    public StartPlatform getStart() {
        return start;
    }

    public EndPlatform getEnd() {
        return end;
    }

    public Checkpoint getCheckpoint() {
        return checkpoint;
    }

    public int getLivesCount() {
        return livesCount;
    }

    public void setLivesCount(int livesCount) {
        this.livesCount = livesCount;
    }
}