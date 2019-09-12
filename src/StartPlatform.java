import java.awt.*;

public class StartPlatform extends GameObject {
    final int HEIGHT = 400, WIDTH = 500;
    StartPlatform(int y) {
        super(0,y);
    }

    public Point getSpawnPos() {
        return new Point(getLocation().x + 200, getLocation().y + 200);
    }
}