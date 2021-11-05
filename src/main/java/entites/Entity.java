package entites;

import java.awt.*;

public abstract class Entity {
    public EntityType entityType;
    public int posX;
    public int posY;
    public Image image;
    public static Entity[][] entities;

    public Entity(EntityType entityType, int posX, int posY, Image image) {
        this.entityType = entityType;
        this.posX = posX;
        this.posY = posY;
        this.image = image;
    }

//    public Entity() {
//        entityType = EntityType.VOID;
//    }

    public static void setEntities(Entity[][] entities) {
        Entity.entities = entities;
    }

    public abstract void update();
}
