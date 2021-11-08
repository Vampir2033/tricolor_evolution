package entites;

import java.awt.*;

public abstract class Entity {
    protected EntityType entityType;
    protected Point flowPoint;
    protected Image image;
    protected static Rectangle border;
    protected static Entity[][] entities;

    public static void setEntities(Entity[][] entities) {
        Entity.entities = entities;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Entity(EntityType entityType, Point flowPoint) {
        this.entityType = entityType;
        this.flowPoint = flowPoint;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public Point getFlowPoint() {
        return flowPoint;
    }

    public void setFlowPoint(Point flowPoint) {
        this.flowPoint = flowPoint;
    }

    public Image getImage() {
        return image;
    }

    public static Rectangle getBorder() {
        return border;
    }

    public static void setBorder(Rectangle border) {
        Entity.border = border;
    }

    public void update(int iteration){

    }
}
