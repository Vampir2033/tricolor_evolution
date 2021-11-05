package graphics;

import entites.Entity;
import field.Grid;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class GameField extends JPanel implements ActionListener {
    private final int WIDTH;
    private final int HEIGHT;
    private final int DOT_SIZE;
    Timer timer;
    Grid grid;

    public GameField(int WIDTH, int HEIGHT, int DOT_SIZE) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        this.DOT_SIZE = DOT_SIZE;
        setBackground(Color.black);
        initGame();
    }

    public void initGame(){
        grid = new Grid(WIDTH/DOT_SIZE - 1, HEIGHT/DOT_SIZE - 1);
        timer = new Timer(32, this);
        timer.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        grid.updateEntities();
        for(Entity[] entities : grid.getEntitiesGrid()){
            for(Entity entity : entities){
                if(entity != null){
                    g.drawImage(entity.image, entity.flowPoint.x * DOT_SIZE, entity.flowPoint.y*DOT_SIZE, this);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
