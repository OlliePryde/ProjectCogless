import java.awt.*;

public class MenuScreen {
    private String menuName;
    private Game game;
    Button[] menuButtons = new Button[20];

    MenuScreen(String menuName, Game game) {
        this.menuName = menuName;
        this.game = game;
        menuButtons[0] = new Button(20,20,100,20,0, game);
        menuButtons[1] = new Button(20,60,100,20,2, game);
    }

    public void interact(Point cursorLocation) {
        for (Button currentButton : menuButtons) {
            if (currentButton.clickableAt(cursorLocation))
                currentButton.action();
        }
    }

    public String getMenuName() {
        return menuName;
    }
}
