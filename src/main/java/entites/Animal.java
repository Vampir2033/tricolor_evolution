package entites;

import utils.MyPoint;
import utils.Range;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Animal extends Entity{
    private int flowIteration = 0;
    private Point destinationPoint;

    public static Range sizeRange  = new Range(1, 255);
    public static Range speedRange  = new Range(0, 10);
    public static Range sensRange  = new Range(0, 10);
    public static int maxSpeed = speedRange.minValue;

    private final int size;
    private final int speed;
    private final int sens;

    class ScannedData{
        public Plant nearestPlant;

        public ScannedData() {
            nearestPlant = null;
        }
    }

    public Animal(Point flowPoint, int size, int speed, int sens) {
        super(EntityType.ANIMAL, flowPoint);
        this.size = size;
        this.speed = speed;
        this.sens = sens;
        setImage(generateImage(size, speed, sens));
        destinationPoint = (Point) flowPoint.clone();
        maxSpeed = Math.max(speed + 1, maxSpeed);
    }

    @Override
    public void update(int iteration) {
        if(flowIteration == iteration)
            return;
        if(speed == 0 || iteration % (maxSpeed - speed) != 0)
            return;

        ScannedData scannedData = new ScannedData();
        scanLocal(scannedData);
        destinationPoint = (scannedData.nearestPlant == null)? destinationPoint : scannedData.nearestPlant.flowPoint;
        if(destinationPoint.equals(flowPoint))
            setNewDestinationPoint();

        Entity destinationEntity = entities[destinationPoint.y][destinationPoint.x];
        if(destinationEntity != null && MyPoint.getDistance(flowPoint, destinationPoint) == 1){
            if(destinationEntity.entityType == EntityType.PLANT) {
                eatPlant((Plant) destinationEntity);
            }
        }

        Point newPosition = goOneStep();
        if(newPosition != null){
            entities[newPosition.y][newPosition.x] = this;
            entities[flowPoint.y][flowPoint.x] = null;
            flowPoint = newPosition;
        }
        else {
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
                speedRange.transformToRange(rgbRange, speed),
                sizeRange.transformToRange(rgbRange, size),
                sensRange.transformToRange(rgbRange, sens));
        final int borderSize = 1;
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
        for(int i = 0; i < image.getHeight(); ++i){
            for(int j = 0; j < image.getWidth(); ++j){
                if(i < borderSize || j < borderSize || i >= 16 - borderSize || j >= 16 - borderSize)
                    image.setRGB(i, j, Color.WHITE.getRGB());
                else
                    image.setRGB(i, j, color.getRGB());
            }
        }
        return image;
    }

    private void scanLocal(ScannedData scannedData){
        Point startPoint = new Point(
                Math.max(0, flowPoint.x - sens),
                Math.max(0, flowPoint.y - sens)
        );
        Point endPoint = new Point(
                Math.min(flowPoint.x + sens, border.width),
                Math.min(flowPoint.y + sens, border.height)
        );
        for(int y = startPoint.y; y < endPoint.y; ++y){
            for(int x = startPoint.x; x < endPoint.x; ++x){
                if(entities[y][x] != null && entities[y][x].getEntityType() == EntityType.PLANT){
                    if (scannedData.nearestPlant == null ||
                        MyPoint.getDistance(flowPoint, entities[y][x].getFlowPoint()) <
                                MyPoint.getDistance(flowPoint, scannedData.nearestPlant.getFlowPoint())){
                        scannedData.nearestPlant = (Plant) entities[y][x];
                    }
                }
            }
        }
    }

    private void eatPlant(Plant plant){
        entities[plant.flowPoint.y][plant.flowPoint.x] = null;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "flowIteration=" + flowIteration +
                ", destinationPoint=" + destinationPoint +
                ", size=" + size +
                ", speed=" + speed +
                ", sens=" + sens +
                ", entityType=" + entityType +
                ", flowPoint=" + flowPoint +
                '}';
    }
}
