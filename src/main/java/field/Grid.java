package field;

import entites.Animal;
import entites.Entity;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Grid {
    private final int sizeX;
    private final int sizeY;
    private final int AMM_CELLS;
    static Random random = new Random();
    List<Entity> entityList;

    public Entity[][] getEntitiesGrid() {
        return entitiesGrid;
    }

    private Entity[][] entitiesGrid;

    public Grid(int sizeX, int sizeY) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        AMM_CELLS = sizeX * sizeY;
        entitiesGrid = new Entity[sizeY][sizeX];
        Animal.setEntities(entitiesGrid);
        generateAnimals(80);
    }

    public void generateAnimals(int amm){
        entityList = new ArrayList<>();
        for(int i = 0; i < amm; ++i){
            int cellNum = random.nextInt(AMM_CELLS);
            int posY = cellNum / sizeX;
            int posX = cellNum % sizeX;
            entitiesGrid[posY][posX] = new Animal(posX, posY, generateRandImage());
            entityList.add(entitiesGrid[posY][posX]);
        }
        entityList = entityList.stream().filter(entity -> entity.posX >= 18 || entity.posY >= 18).toList();
    }

    static Image generateRandImage(){
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
        Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
        for(int i = 0; i < image.getHeight(); ++i){
            for(int j = 0; j < image.getWidth(); ++j){
                image.setRGB(i, j, color.getRGB());
            }
        }
        return image;
    }
}
