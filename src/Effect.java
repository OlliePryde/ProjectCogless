import java.awt.*;
import java.awt.image.BufferedImage;

public class Effect{
    private int effectID;
    int lifeTime = 0;
    final int SIZE = 100;
    int frameTime = 5;
    Point location;
    BufferedImage effectsSheet;
    BufferedImage[] effectTextures = new BufferedImage[6];
    BufferedImage currentEffectTexture;

    Effect(int effectID, int x, int y, BufferedImage effectsSheet) {
        this.effectsSheet = effectsSheet;
        location = new Point(x,y);
        updateEffect();
        updateCurrentTexture();

        this.effectID = effectID;
    }

    public int updateCurrentTexture() {
        if (lifeTime % frameTime == 0 || lifeTime == 0) {
            if (lifeTime == 6*frameTime) {
                return -1;
            }
            currentEffectTexture = effectTextures[lifeTime/frameTime];
            return 1;
        }
        return 0;
    }

    private void updateEffect() {
        final int IMGDIM = 100;
        effectTextures[0] = effectsSheet.getSubimage(0, (effectID * IMGDIM), IMGDIM, IMGDIM);
        effectTextures[1] = effectsSheet.getSubimage(IMGDIM, effectID * IMGDIM, IMGDIM, IMGDIM);
        effectTextures[2] = effectsSheet.getSubimage(IMGDIM * 2, effectID * IMGDIM, IMGDIM, IMGDIM);
        effectTextures[3] = effectsSheet.getSubimage(IMGDIM * 3, effectID * IMGDIM, IMGDIM, IMGDIM);
        effectTextures[4] = effectsSheet.getSubimage(IMGDIM * 4, effectID * IMGDIM, IMGDIM, IMGDIM);
        effectTextures[5] = effectsSheet.getSubimage(IMGDIM * 5, effectID * IMGDIM, IMGDIM, IMGDIM);
    }

    public Point getLocation() {
        return location;
    }

    //    ALL EFFECT IMAGES WILL BE 100x100, 6 FRAMES FOR EVERY EFFECT
}
