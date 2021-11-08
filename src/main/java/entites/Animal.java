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
    public static Range speedRange  = new Range(1,20);
    public static Range sensRange  = new Range(0, 20);
    public static int maxSpeed = speedRange.minValue;

    private final int size;
    private final int speed;
    private final int sens;

    private final int maxEnergy;
    private int flowEnergy;
    private final int idlingEnergy;
    private final int stepEnergy;

    private static final float reproductionEnergyPercent = 0.8f;
    private static final int mutationPercent = 10;
    private static final float mutationRangePercent = 0.1f;

    static class ScannedData{
        public Plant nearestPlant;

        public ScannedData() {
            nearestPlant = null;
        }
    }

    public Animal(Point flowPoint, int size, int speed, int sens) {
        super(EntityType.ANIMAL, flowPoint);
        this.size = sizeRange.inRange(size);
        this.speed = speedRange.inRange(speed);
        this.sens = sensRange.inRange(sens);

        setImage(generateImage(size, speed, sens));
        destinationPoint = (Point) flowPoint.clone();
        maxSpeed = Math.max(speed + 1, maxSpeed);
        maxEnergy = size * 100;
        flowEnergy = maxEnergy / 2;
        idlingEnergy = (size*maxSpeed/5 + sens) / 20;
        stepEnergy = (size*maxSpeed + sens * 4) / 20;
    }

    @Override
    public void update(int iteration) {
        if(flowIteration == iteration)
            return;
        if(iteration % (maxSpeed - speed) != 0)
            return;
        if(tryReproduct(iteration) != null){
            return;
        }
        ScannedData scannedData = new ScannedData();
        scanLocal(scannedData);
        destinationPoint = (scannedData.nearestPlant == null)? destinationPoint : scannedData.nearestPlant.flowPoint;
        if(destinationPoint.equals(flowPoint))
            setNewDestinationPoint();

        Point newPosition = goOneStep();
        if(newPosition == null) {
            setFlowEnergy(flowEnergy - idlingEnergy);
            return;
        }
        if(!newPosition.equals(flowPoint) && !setFlowEnergy(flowEnergy - stepEnergy))
            return;

        Entity destinationEntity = entities[newPosition.y][newPosition.x];
        if(destinationEntity != null && destinationEntity.entityType == EntityType.PLANT) {
            eatPlant((Plant) destinationEntity);
        }

        entities[newPosition.y][newPosition.x] = this;
        entities[flowPoint.y][flowPoint.x] = null;
        flowPoint = newPosition;
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
        if(MyPoint.getDistance(flowPoint, destinationPoint) == 1 &&
                entities[destinationPoint.y][destinationPoint.x] != null &&
                entities[destinationPoint.y][destinationPoint.x].entityType == EntityType.PLANT)
            return destinationPoint;
        Point tmp = new Point(destinationPoint.x - flowPoint.x, destinationPoint.y - flowPoint.y);
        List<Point> availablePoints = new LinkedList<>();
        if(tmp.x != 0)
            availablePoints.add(new Point(flowPoint.x + (tmp.x / Math.abs(tmp.x)), flowPoint.y));
        if(tmp.y != 0)
            availablePoints.add(new Point(flowPoint.x, flowPoint.y + (tmp.y / Math.abs(tmp.y))));
        return availablePoints.stream().filter(p -> entities[p.y][p.x] == null).findAny().orElse(null);
    }

    static Image generateImage(int size, int speed, int sens){
        try {
            Range rgbRange = new Range(0, 255);
            Color color = new Color(
                    speedRange.transformToRange(rgbRange, speed),
                    sizeRange.transformToRange(rgbRange, size),
                    sensRange.transformToRange(rgbRange, sens));
            final int borderSize = 1;
            BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
            for (int i = 0; i < image.getHeight(); ++i) {
                for (int j = 0; j < image.getWidth(); ++j) {
                    if (i < borderSize || j < borderSize || i >= 16 - borderSize || j >= 16 - borderSize)
                        image.setRGB(i, j, Color.WHITE.getRGB());
                    else
                        image.setRGB(i, j, color.getRGB());
                }
            }
            return image;
        } catch (Exception e){
            return new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);

        }
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
        flowEnergy += plant.eatPlant(maxEnergy - flowEnergy);
        if(!plant.isAlive())
            entities[plant.flowPoint.y][plant.flowPoint.x] = null;
    }

    public boolean setFlowEnergy(int flowEnergy) {
        this.flowEnergy = flowEnergy;
        if(flowEnergy <= 0) {
            alive = false;
            flowEnergy = 0;
            return false;
        } else if(flowEnergy > maxEnergy){
            flowEnergy = maxEnergy;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Animal{" +
                "size=" + size +
                ", speed=" + speed +
                ", sens=" + sens +
                ", maxEnergy=" + maxEnergy +
                ", flowEnergy=" + flowEnergy +
                ", idlingEnergy=" + idlingEnergy +
                ", stepEnergy=" + stepEnergy +
                ", entityType=" + entityType +
                '}';
    }

    public int getSpeed() {
        return speed;
    }

    public static void setMaxSpeed(int maxSpeed) {
        Animal.maxSpeed = maxSpeed;
    }

    private Animal tryReproduct(int flowIteration){
        if(flowEnergy >= maxEnergy * reproductionEnergyPercent){
            Point point = findClearNeighborCell();
            if(point == null){
                return null;
            }

            Animal child = new Animal(
                    point,
                    size + addMutation(sizeRange.maxValue),
                    speed + addMutation(speedRange.maxValue),
                    sens + addMutation(sensRange.maxValue)
            );
            child.flowIteration = flowIteration;
            setFlowEnergy(flowEnergy - child.flowEnergy);
            entities[child.flowPoint.y][child.flowPoint.x] = child;
            return child;
        } else {
            return null;
        }
    }

    private int addMutation(int maxValue){
        Random random = new Random();
        if(random.nextInt(100) >= mutationPercent){
            return 0;
        } else {
            return (random.nextInt((int) (maxValue * mutationRangePercent)) + 1) *
                    (-1 * random.nextInt(2));
        }

    }

    private Point findClearNeighborCell(){
        for(int y = -1; y <= 1; ++y){
            for(int x = -1; x <= 1; ++x){
                if(Math.abs(y) == Math.abs(x)){
                    continue;
                }
                Point point = new Point(flowPoint.x + x, flowPoint.y + y);
                if(!border.contains(point)){
                    continue;
                }
                if (entities[point.y][point.x] == null){
                    return point;
                }
            }
        }
        return null;
    }
}

