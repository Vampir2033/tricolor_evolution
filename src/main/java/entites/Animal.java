package entites;

import java.awt.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal extends Entity{
    private int flowIteration = 0;
    public static Entity[][] entities;
    private Point destinationPoint;

    public static void setEntities(Entity[][] entities) {
        Animal.entities = entities;
    }

    public Animal(Point point, Image image) {
        super(EntityType.ANIMAL, point, image);
        destinationPoint = (Point) flowPoint.clone();
    }

    @Override
    public void update(int iteration) {
        if(flowIteration == iteration)
            return;
        if(destinationPoint.equals(flowPoint))
            setNewDestinationPoint();
        Point newPosition = goOneStep();
        if(newPosition != null){
            entities[newPosition.y][newPosition.x] = this;
            entities[flowPoint.y][flowPoint.x] = null;
            flowPoint = newPosition;
        } else {
            setNewDestinationPoint();
        }
        flowIteration = iteration;
    }

    private void setNewDestinationPoint(){
        Random random = new Random();
        Point temp;
        do{
            temp = new Point(random.nextInt(border.width), random.nextInt(border.height));
        } while (temp.equals(destinationPoint));
        destinationPoint = temp;
    }

    private Point goOneStep(){
        Point tmp = new Point(destinationPoint.x - flowPoint.x, destinationPoint.y - flowPoint.y);
        List<Point> availablePoints = new LinkedList<>();
        if(tmp.x != 0)
            availablePoints.add(new Point(flowPoint.x + (tmp.x / Math.abs(tmp.x)), flowPoint.y));
        if(tmp.y != 0)
            availablePoints.add(new Point(flowPoint.x, flowPoint.y + (tmp.y / Math.abs(tmp.y))));
        return availablePoints.stream().filter(p -> entities[p.y][p.x] == null).findAny().orElse(null);
    }
}
