public class Vector {
    private double x;
    private double y;

    Vector(double x, double y) {
        setX(x);
        setY(y);
    }

    public void setX(double x) {
        this.x = Math.round(x * 10) / 10d;
    }

    public void setY(double y) {
        this.y = Math.round(y * 10) / 10d;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }
}
