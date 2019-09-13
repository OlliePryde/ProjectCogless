import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class Player extends GameObject {

    private BufferedImage spriteSheet;
    private BufferedImage[] outfit = new BufferedImage[7];
    private int[] outfitIndex = new int[4];
    private int height = 200, width = 100;
    private boolean eyesOpen = true, step = true;
    final private int MASS = 5;
    private Vector velocity = new Vector(0, 0);
    private Vector force = new Vector(0, 0);
    private Vector acceleration = new Vector(0, 0);
    int sprintTime = 0, airTime = 0, selfDestructTime = 0, invincibility = 0, deathTime = 0;
    private int lives = 3, health = 3;
    private double actingFriction = 0, actingStick = 1;
    private boolean onGround = false, lookRight = true, hit = false;

    Player(int x, int y, BufferedImage spriteSheet) {
        super(x, y);
        this.spriteSheet = spriteSheet;
        setOutfitIndex(0, 0, 0, 0);
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int playerHeight) {
        this.height = playerHeight;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int playerWidth) {
        this.width = playerWidth;
    }

    public void toOrigin() {
        this.setLocationDec(origin.x, origin.y);
    }

    public void setOutfitIndex(int head, int body, int arm, int legs) {
        outfitIndex[0] = head;
        outfitIndex[1] = body;
        outfitIndex[2] = arm;
        outfitIndex[3] = legs;
        updateOutfit();
    }

    /**
     * 0 - head
     * 1 - body
     * 2 - arm
     * 3 - leg [1]
     * 4 - leg [2]
     * 5 - eyes [open]
     * 6 - eyes [closed]
     */
    private void updateOutfit() {
        final int IMGDIM = 100;
        outfit[0] = spriteSheet.getSubimage(0, (outfitIndex[0] * IMGDIM), IMGDIM, IMGDIM);
        outfit[1] = spriteSheet.getSubimage(IMGDIM, outfitIndex[1] * IMGDIM, IMGDIM, IMGDIM);
        outfit[2] = spriteSheet.getSubimage(IMGDIM * 2, outfitIndex[2] * IMGDIM, IMGDIM, IMGDIM);
        outfit[3] = spriteSheet.getSubimage(IMGDIM * 3, outfitIndex[3] * IMGDIM, IMGDIM, IMGDIM);
        outfit[4] = spriteSheet.getSubimage(IMGDIM * 4, outfitIndex[3] * IMGDIM, IMGDIM, IMGDIM);
        outfit[5] = spriteSheet.getSubimage(IMGDIM * 5, outfitIndex[0] * IMGDIM, IMGDIM, IMGDIM);
        outfit[6] = spriteSheet.getSubimage(IMGDIM * 6, outfitIndex[0] * IMGDIM, IMGDIM, IMGDIM);
    }

    public void toggleBlink() {
        BufferedImage temp = outfit[5];
        outfit[5] = outfit[6];
        outfit[6] = temp;
        eyesOpen = !eyesOpen;
    }

    public void toggleDirection() {
        BufferedImage temp = outfit[5];
        outfit[5] = outfit[6];
        outfit[6] = temp;
        eyesOpen = !eyesOpen;
    }

    public void toggleLegs() {
        BufferedImage temp = outfit[3];
        outfit[3] = outfit[4];
        outfit[4] = temp;
    }

    public BufferedImage[] getOutfit() {
        return outfit;
    }

    public void setEyesOpen(boolean eyesOpen) {
        this.eyesOpen = eyesOpen;
    }

    public boolean isEyesOpen() {
        return this.eyesOpen;
    }

    public boolean isStep() {
        return step;
    }

    public void setStep(boolean step) {
        this.step = step;
    }

    public void physicsUpdate(Level currentLevel) {
        force.setX(force.getX() / actingStick);
        acceleration.setX(force.getX() / MASS);
        velocity.setX(acceleration.getX() / 15);
        acceleration.setY(force.getY() / MASS);
        velocity.setY(acceleration.getY() / 15);
        double x = getLocationDec().getX() + (velocity.getX() * 100 / 15);
        double y = getLocationDec().getY() + (velocity.getY() * 100 / 15);
        Vector newLocation = collisionUpdate(location, new Vector(x, y), currentLevel);
        setLocationDec(newLocation.getX(), newLocation.getY());

        if (onGround) {
            if (force.getX() > 0) {
                force.setX(force.getX() - (MASS * 9.81 * actingFriction / 15));
                if (force.getX() < 0)
                    force.setX(0);
            } else if (force.getX() < 0) {
                force.setX(force.getX() + (MASS * 9.81 * actingFriction / 15));
                if (force.getX() > 0)
                    force.setX(0);
            }
        }
        if (!onGround) {
            force.setY(force.getY() + (MASS * 9.81 / 2.5));
            if (force.getY() >= 200) {
                force.setY(200);
            }
        } else
            force.setY(0);
    }

    public void incrementSelfDestruct() {
        selfDestructTime++;
        if (selfDestructTime == 120) {
            deathSequence();
            selfDestructTime = 0;
        }
    }

    public void deathSequence() {
        deathTime = 200;
        Game.godPause(true);
    }

    public void updateDeathSequence() {
        if (deathTime == 1) {
            death();
            Game.godPause(false);
            deathTime = 0;
        }
        if (deathTime != 0)
            deathTime--;
    }

    public void death() {
        this.setLocationDec(origin.getX(),origin.getY());
        health = 3;
        if (!eyesOpen)
            toggleBlink();
        this.velocity.setX(0);
        this.velocity.setY(0);
        this.force.setX(0);
        this.force.setY(0);
        this.acceleration.setX(0);
        this.acceleration.setY(0);
        lives--;
    }

    public void damageUpdate() {
        if (invincibility == 0 && hit) {
            health--;
            if (force.getX() <= 0)
                this.force.setX(-100);
            else
                this.force.setX(100);
            if (force.getY() >= 0)
                this.force.setY(-300);
            else
                this.force.setY(300);
            invincibility = 200;
        }
        else if (invincibility != 0) {
            invincibility--;
        }
        hit = false;
        if (health <= 0) {
            deathSequence();
        }
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void gainLife() {
        lives++;
    }

    public int getHealth() {
        return health;
    }

    public int getInvincibility() {
        return invincibility;
    }

    public int getLives() {
        return lives;
    }

    public void setSelfDestructTime(int selfDestructTime) {
        this.selfDestructTime = selfDestructTime;
    }

    public int getSelfDestructTime() {
        return selfDestructTime;
    }

    private Vector collisionUpdate(Point oldLocation, Vector newLocation, Level currentLevel) {
        LevelObject[][] levelObjects = currentLevel.getLevelObjects();
        airTime++;
        if (newLocation.getX() < 0) {
            newLocation.setX(1);
            force.setX(0);
            velocity.setX(0);
            acceleration.setX(0);
            sprintTime = 0;
        }
        else if (newLocation.getX() > currentLevel.getLevelWidth()*100 - 100) {
            newLocation.setX(currentLevel.getLevelWidth()*100 - 100);
            force.setX(0);
            velocity.setX(0);
            acceleration.setX(0);
            sprintTime = 0;
        }
        if ((location.y / 100) + 1 > 0 && (location.y / 100) + 1 < currentLevel.getLevelHeight() - 1) {
            if (location.y % 100 == 0 && location.y > 0 && location.y < currentLevel.getLevelHeight() * 100)
                if (levelObjects[location.x / 100][(location.y / 100) + 2] != null && levelObjects[location.x / 100][(location.y / 100) + 2].getBlockID() != -1) {
                    onGround = true;
                    airTime = 0;
                    actingFriction = levelObjects[location.x / 100][(location.y / 100) + 2].getFrictionalValue();
                    actingStick = levelObjects[location.x / 100][(location.y / 100) + 2].getStickyValue();
                } else if (location.x % 100 != 0 && levelObjects[location.x / 100 + 1][(location.y / 100) + 2] != null && levelObjects[location.x / 100 + 1][(location.y / 100) + 2].getBlockID() != -1) {
                    onGround = true;
                    airTime = 0;
                    actingFriction = levelObjects[location.x / 100 + 1][(location.y / 100) + 2].getFrictionalValue();
                    actingStick = levelObjects[location.x / 100 + 1][(location.y / 100) + 2].getStickyValue();
                } else {
                    onGround = false;
                    actingFriction = 0;
                    actingStick = 1;
                }
            else
                onGround = false;
        }
        else
            onGround = false;
        for (int x = oldLocation.x / 100 - 3; x < oldLocation.x / 100 + 4; x++) {
            for (int y = oldLocation.y / 100 - 3; y < oldLocation.y / 100 + 4; y++) {
                // NEED TO ADD MORE TO STOP OUT OF ARRAY CHECKS
                if (x > 0 && y > 0 && x < currentLevel.getLevelWidth() && y < currentLevel.getLevelHeight() && levelObjects[x][y] != null && levelObjects[x][y].getBlockID() != -1 && MathsMethods.checkIntersect(newLocation, levelObjects[x][y])) {
                    newLocation.setY(newLocation.getY() - (velocity.getY() * 100d / 15d));
                    System.out.println("1");
                    if (levelObjects[x][y] != null && levelObjects[x][y].getBlockID() != -1 && MathsMethods.checkIntersect(newLocation, levelObjects[x][y])) {
                        newLocation.setY(newLocation.getY() + (velocity.getY() * 100d / 15d));
                        newLocation.setX(newLocation.getX() - (velocity.getX() * 100d / 15d));
                        System.out.println("2");
                        if (levelObjects[x][y] != null && levelObjects[x][y].getBlockID() != -1 && MathsMethods.checkIntersect(newLocation, levelObjects[x][y])) {
                            newLocation.setX(newLocation.getX() + (velocity.getX() * 100d / 15d));
                            System.out.println("3");
                        } else {
                            // X CO-ORD CORRECTION, CAUSES OTHER BUGS (NOT NECESSARY RN)
                            if (velocity.getX() > 0)
                                newLocation.setX(MathsMethods.valueSnap(newLocation.getX() + 99, 100));
                            else if (velocity.getX() < 0)
                                newLocation.setX(MathsMethods.valueSnap(newLocation.getX(), 100));
                            force.setX(0);
                            velocity.setX(0);
                            acceleration.setX(0);
                            sprintTime = 0;
                        }
                    } else {
                        if (velocity.getY() > 0)
                            newLocation.setY(MathsMethods.valueSnap(newLocation.getY() + 99, 100));
                        else {
                            force.setY(0);
                            newLocation.setY(MathsMethods.valueSnap(newLocation.getY(), 100));
                        }
                    }
                }
            }
        }
        return newLocation;
    }

    public void setAcceleration(double x, double y) {
        acceleration.setX(x);
        acceleration.setY(y);
    }

    public Vector getAcceleration() {
        return acceleration;
    }

    public void setForce(double x, double y) {
        this.force.setX(x);
        this.force.setY(y);
    }

    public Vector getForce() {
        return force;
    }

    public boolean isOnGround() {
        return onGround;
    }

    public Vector getVelocity() {
        return velocity;
    }

    public boolean isHit() {
        return hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }
}