package field;

import entites.Animal;
import entites.Entity;
import entites.EntityType;
import entites.Plant;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Grid {
    private final Rectangle border;
    private final int AMM_CELLS;
    static Random random = new Random();
    private int flowIteration = 0;
    private Entity[][] entitiesGrid;
    private final int plantGenPercent = 20;
    private final int maxAmmPlantsGenOnce = 3;

    public Entity[][] getEntitiesGrid() {
        return entitiesGrid;
    }


    public Grid(int sizeX, int sizeY) {
        border = new Rectangle(sizeX, sizeY);
        AMM_CELLS = sizeX * sizeY;
        entitiesGrid = new Entity[sizeY][sizeX];
        Entity.setBorder(border);
        Entity.setEntities(entitiesGrid);
        generateAnimals(40);
    }

    public void generateAnimals(int amm){
        for(int i = 0; i < amm; ++i){
            int cellNum = random.nextInt(AMM_CELLS);
            int posY = cellNum / border.width;
            int posX = cellNum % border.width;
            entitiesGrid[posY][posX] = new Animal(
                    new Point(posX, posY),
                    Animal.sizeRange.getRandValue(),
                    Animal.speedRange.getRandValue(),
                    Animal.sensRange.getRandValue());
            System.out.println(entitiesGrid[posY][posX]);
        }
    }

    public void generatePlants(){
        if(random.nextInt(100) + 1 > plantGenPercent)
            return;
        int amm = random.nextInt(maxAmmPlantsGenOnce) + 1;
        for(int i = 0; i < amm; ++i){
            int cellNum = random.nextInt(AMM_CELLS);
            int posY = cellNum / border.width;
            int posX = cellNum % border.width;
            if(entitiesGrid[posY][posX] != null)
                continue;
            entitiesGrid[posY][posX] = new Plant(new Point(posX, posY));
        }
    }

    public void updateEntities(){
        generatePlants();
        for(Entity[] entities : entitiesGrid){
            for(Entity entity : entities){
                if(entity == null)
                    continue;
                entity.update(flowIteration);
                if(!entity.isAlive()){
                    removeEntity(entity);
                }
            }
        }
        ++flowIteration;
    }

    private void removeEntity(Entity entity){
        entitiesGrid[entity.getFlowPoint().y][entity.getFlowPoint().x] = null;
    }

}
