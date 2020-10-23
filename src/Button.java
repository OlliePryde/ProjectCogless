import java.awt.*;

public class Button {
    Point location, size;
    int functionID;
    private Game game;
    private MenuScreen nextScreen;

    Button(int x, int y, int width, int height, int functionID, Game game) {
        location = new Point(x,y);
        size = new Point(width, height);
        this.functionID = functionID;
        this.game = game;
    }

    void setNextScreen() {

    }

    MenuScreen getNextScreen() {
        return nextScreen;
    }

    Point getLocation() {
        return location;
    }

    Point getSize() {
        return size;
    }

    boolean clickableAt(Point clickLocation) {
        int mouseX = clickLocation.x;
        int mouseY = clickLocation.y;
        int butX = location.x;
        int butY = location.y;
        int width = size.x;
        int height = size.y;
        if ((mouseX >= butX && mouseX <= butX + width) && (mouseY >= butY && mouseY <= butY + height))
            return true;
        return false;
    }

    void action() {
        switch(functionID) {
            case 0:
                System.exit(0);
            case 1:
                game.setCurrentMenu(nextScreen);
            case 2:
                game.setGameMode(0);
                game.setLClick(false);
        }
    }
}
