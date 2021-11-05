package entites;

import java.awt.*;

public class Animal extends Entity{

    static Entity[][] entities;

    public static void setEntities(Entity[][] entities) {
        Animal.entities = entities;
    }

    public Animal(int posX, int posY, Image image) {
        super(EntityType.ANIMAL, posX, posY, image);
    }

    @Override
    public void update() {

    }

}
