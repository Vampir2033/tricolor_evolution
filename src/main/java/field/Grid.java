package field;

import entites.Animal;
import entites.Entity;
import entites.EntityType;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Grid {
    private final Rectangle border;
    private final int AMM_CELLS;
    static Random random = new Random();
    private int flowIteration = 0;
    private Entity[][] entitiesGrid;

    public Entity[][] getEntitiesGrid() {
        return entitiesGrid;
    }


    public Grid(int sizeX, int sizeY) {
        border = new Rectangle(sizeX, sizeY);
        AMM_CELLS = sizeX * sizeY;
        entitiesGrid = new Entity[sizeY][sizeX];
        Entity.setBorder(border);
        Animal.setEntities(entitiesGrid);
        generateAnimals(20);
    }

    public void generateAnimals(int amm){
        for(int i = 0; i < amm; ++i){
            int cellNum = random.nextInt(AMM_CELLS);
            int posY = cellNum / border.width;
            int posX = cellNum % border.width;
            entitiesGrid[posY][posX] = new Animal(new Point(posX, posY), generateRandImage());
        }
    }

    public void updateEntities(){
        for(Entity[] entities : entitiesGrid){
            for(Entity entity : entities){
                if(entity == null)
                    continue;
                entity.update(flowIteration);
            }
        }
        ++flowIteration;
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
