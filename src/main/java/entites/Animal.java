package entites;

import utils.Range;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal extends Entity{
    private int flowIteration = 0;
    public static Entity[][] entities;
    private Point destinationPoint;

    public static Range sizeRange  = new Range(1, 255);
    public static Range speedRange  = new Range(0, 7);
    public static Range sensRange  = new Range(0, 10);

    private int size;
    private int speed;
    private int sens;

    public static void setEntities(Entity[][] entities) {
        Animal.entities = entities;
    }

    public Animal(Point flowPoint, int size, int speed, int sens) {
        super(EntityType.ANIMAL, flowPoint);
        this.size = size;
        this.speed = speed;
        this.sens = sens;
        setImage(generateImage(size, speed, sens));
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

    static Image generateImage(int size, int speed, int sens){
        Range rgbRange = new Range(0, 255);
        Color color = new Color(
                sizeRange.transformToRange(rgbRange, size),
                speedRange.transformToRange(rgbRange, speed),
                sensRange.transformToRange(rgbRange, sens));
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
        Random random = new Random();
        for(int i = 0; i < image.getHeight(); ++i){
            for(int j = 0; j < image.getWidth(); ++j){
                image.setRGB(i, j, color.getRGB());
            }
        }
        return image;
    }
}
