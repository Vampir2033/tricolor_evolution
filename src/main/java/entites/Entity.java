package entites;

import java.awt.*;

public abstract class Entity {
    public EntityType entityType;
    public Point flowPoint;
    public Image image;
    public static Rectangle border;


    public Entity(EntityType entityType, Point flowPoint, Image image) {
        this.entityType = entityType;
        this.flowPoint = flowPoint;
        this.image = image;
    }

    public static void setBorder(Rectangle border) {
        Entity.border = border;
    }

    public void update(int iteration){

    }
}
