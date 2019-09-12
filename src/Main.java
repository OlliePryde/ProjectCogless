import javax.swing.*;

public class Main {
    static final String GAME_NAME = "Project Cogless";

    public static void main(String[] args) {

        GameWindow gameWindow = new GameWindow(GAME_NAME);
        gameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        Controller con = new Controller(gameWindow);
    }
}