import java.awt.*;

public class EndPlatform extends GameObject {
    final int HEIGHT = 400, WIDTH = 500;

    EndPlatform(int x, int y) {
        super(x, y);
    }

    public Point getEndPoint() {
        return new Point(location.x + 200, location.y + 200);
    }
}