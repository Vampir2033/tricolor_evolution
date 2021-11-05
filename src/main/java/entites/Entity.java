package entites;

import java.awt.*;

public abstract class Entity {
    public EntityType entityType;
    public Point flowPoint;
    public Image image;
    public static Rectangle border;

    public void setImage(Image image) {
        this.image = image;
    }

    public Entity(EntityType entityType, Point flowPoint) {
        this.entityType = entityType;
        this.flowPoint = flowPoint;
    }

    public static void setBorder(Rectangle border) {
        Entity.border = border;
    }

    public void update(int iteration){

    }
}
