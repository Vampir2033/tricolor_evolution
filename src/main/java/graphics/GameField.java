package graphics;

import entites.Animal;
import entites.Entity;
import entites.EntityType;
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
        timer = new Timer(500, this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        java.util.List<Entity> entityList = new ArrayList<>();
        for(Entity[] entities : grid.getEntitiesGrid()){
            for(Entity entity : entities){
                if(entity != null){
                    g.drawImage(entity.image, entity.posX * DOT_SIZE, entity.posY*DOT_SIZE, this);
                    entityList.add(entity);
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        repaint();
    }
}
