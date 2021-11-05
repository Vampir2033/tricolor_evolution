package graphics;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private final int WIDTH = 800;
    private final int HEIGHT = 800;
    private final int DOT_SIZE = 16;
    public MainWindow(int width, int height, int dotSize){
        setTitle("Tricolor Evolution");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(width, height + 25);
        setLocation(40, 40);
        add(new GameField(width, height, dotSize));
        setVisible(true);
    }
}
