import java.awt.*;

public class GameObject {
    Point origin, location;
    private Vector locationDec = new Vector(0,0);

    GameObject(int x, int y) {
        setLocationDec(x,y);
        setOrigin(location.x,location.y);

    }

    public void setOrigin(Point location) {
        this.origin = location;
    }

    public void setOrigin(int x, int y) {
        this.origin = new Point(x,y);
    }
    
    public Point getOrigin() {
        return origin;
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setLocation(int x, int y) {
        this.location = new Point(x,y);
    }

    public Point getLocation() {
        return location;
    }

    public void setLocationDec(double x, double y) {
        this.locationDec.setX(x);
        this.locationDec.setY(y);
        updateLocation();
    }

    public Vector getLocationDec() {
        return locationDec;
    }

    private void updateLocation() {
        this.setLocation(new Point((int)Math.round(getLocationDec().getX()),(int)Math.round(getLocationDec().getY())));
    }




    
}