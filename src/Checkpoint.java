import java.awt.*;

public class Checkpoint extends GameObject {

    boolean active = false;
    final int HEIGHT = 200, WIDTH = 100;
    Checkpoint(int x, int y) {
        super(x,y);
    }

    public Point getSpawnPos() {
        return location;
    }
}