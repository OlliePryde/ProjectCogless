import javax.swing.*;
import java.awt.*;

public class GameWindow extends JFrame {
    GraphicDraw coglessGameGraphics;

    public GameWindow(String windowTitle) {
        super(windowTitle);
    }

    public void setup(JPanel panel, Controller con){
        panel.setLayout(new BorderLayout());
        this.addKeyListener(con);
        this.addMouseListener(con);
        this.addMouseMotionListener(con);
        this.addMouseWheelListener(con);

        coglessGameGraphics = new GraphicDraw();
        panel.add(coglessGameGraphics);

        this.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.setUndecorated(true);

        this.setContentPane(panel);
        this.setVisible(true);
    }

}
