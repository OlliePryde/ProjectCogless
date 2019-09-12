public class MathsMethods {

    public static int valueSnap(double val, int snapVal) {
        if (val != 0)
            return (int) (val / snapVal) * snapVal;
        return 0;
    }

    public static boolean checkIntersect(Vector location, LevelObject object) {
        int x = object.getLocation().x;
        int y = object.getLocation().y;
        return location.getX() + 100 > x && location.getX() < x + 100 && location.getY() + 200 > y && location.getY() < y + 100;
    }

    public static boolean checkIntersect(Vector location, int x, int y) {
        return location.getX() + 100 > x && location.getX() < x + 100 && location.getY() + 200 > y && location.getY() < y + 100;
    }
}
