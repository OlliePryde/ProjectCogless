import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class LevelObject extends GameObject {
    private String material = "null";
    private int blockID;
    LevelObject up, down, left, right;
    final int SIZE = 100;
    private int lifeTime = 1;
    private int connectedTextureID;
    private BufferedImage spriteSheet;
    private BufferedImage[] textures = new BufferedImage[16];
    private double frictionalValue, stickyValue;
    private boolean harmful = false;

    LevelObject(int x, int y, int blockID, BufferedImage spriteSheet) {
        super(x,y);
        this.spriteSheet = spriteSheet;
        setBlockType(blockID);
        connectedTextureUpdate();
    }

    public void connectedTextureUpdate() {
        boolean hasUp = false, hasDown = false, hasLeft = false, hasRight = false;
        if (up != null && up.getBlockID() == this.blockID)
            hasUp = true;
        if (down != null && down.getBlockID() == this.blockID)
            hasDown = true;
        if (left != null && left.getBlockID() == this.blockID)
            hasLeft = true;
        if (right != null && right.getBlockID() == this.blockID)
            hasRight = true;

        if (!hasUp && !hasDown && !hasLeft && !hasRight)
            connectedTextureID = 0;
        else if (hasUp && hasDown && hasLeft && hasRight)
            connectedTextureID = 1;
        else if (!hasUp && !hasDown && hasLeft && !hasRight)
            connectedTextureID = 2;
        else if (hasUp && !hasDown && !hasLeft && !hasRight)
            connectedTextureID = 3;
        else if (!hasUp && !hasDown && !hasLeft)
            connectedTextureID = 4;
        else if (!hasUp && hasDown && !hasLeft && !hasRight)
            connectedTextureID = 5;
        else if (!hasUp && hasDown && hasLeft && !hasRight)
            connectedTextureID = 6;
        else if (hasUp && !hasDown && hasLeft && !hasRight)
            connectedTextureID = 7;
        else if (hasUp && !hasDown && !hasLeft)
            connectedTextureID = 8;
        else if (!hasUp && hasDown && !hasLeft)
            connectedTextureID = 9;
        else if (!hasUp && !hasDown)
            connectedTextureID = 10;
        else if (hasUp && hasDown && !hasLeft && !hasRight)
            connectedTextureID = 11;
        else if (!hasUp)
            connectedTextureID = 12;
        else if (!hasRight)
            connectedTextureID = 13;
        else if (!hasDown)
            connectedTextureID = 14;
        else
            connectedTextureID = 15;
    }

    public void setBlockType(int blockID) {
        this.blockID = blockID;
        switch (blockID) {
            case 0:
                frictionalValue = 1.5;
                stickyValue = 1;
                grabTextures(textures, 0);
                harmful = false;
                material = "ground";
                break;
            case 1:
                frictionalValue = 3;
                stickyValue = 2;
                grabTextures(textures, 1);
                material = "mud";
                harmful = false;
                break;
            case 2:
                frictionalValue = 0.5;
                stickyValue = 1;
                harmful = false;
                grabTextures(textures, 2);
                material = "ice";
                break;
            case 3:
                frictionalValue = 1.5;
                stickyValue = 1;
                harmful = true;
                material = "spike";

        }
    }

    public int getBlockID() {
        return blockID;
    }

    public void setUp(LevelObject up) {
        this.up = up;
        connectedTextureUpdate();
    }

    public void setDown(LevelObject down) {
        this.down = down;
        connectedTextureUpdate();
    }

    public void setLeft(LevelObject left) {
        this.left = left;
        connectedTextureUpdate();
    }

    public void setRight(LevelObject right) {
        this.right = right;
        connectedTextureUpdate();
    }

    private void grabTextures(BufferedImage[] blockTextures, int id) {
        final int IMGDIM = 100;
        for (int x = 0; x < 16; x++) {
            blockTextures[x] = spriteSheet.getSubimage(x*IMGDIM,id*IMGDIM,IMGDIM,IMGDIM);
        }
    }

    public BufferedImage[] getTextures() {
        return textures;
    }

    public double getFrictionalValue() {
        return frictionalValue;
    }

    public double getStickyValue() {
        return stickyValue;
    }

    public int getConnectedTextureID() {
        return connectedTextureID;
    }

    public int getLifeTime() {
        return lifeTime;
    }

    public void setLifeTime(int lifeTime) {
        this.lifeTime = lifeTime;
    }

    public boolean isHarmful() {
        return harmful;
    }
}