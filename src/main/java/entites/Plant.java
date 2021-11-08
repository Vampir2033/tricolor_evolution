package entites;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Plant extends Entity{
    private static final Image plantImage;
    private int foodValue;

    static {
        plantImage = generatePlantImage();
    }

    static Image generatePlantImage(){
        BufferedImage image = new BufferedImage(16, 16, BufferedImage.TYPE_3BYTE_BGR);
        int radius_2 = 64;
        for(int i = 0; i < image.getHeight(); ++i){
            for(int j = 0; j < image.getWidth(); ++j){
                if(Math.pow(i-8, 2) + Math.pow(j-8, 2) <= radius_2){
                    image.setRGB(i, j, Color.GREEN.getRGB());
                } else {
                    image.setRGB(i, j, Color.BLACK.getRGB());
                }
            }
        }
        return image;
    }

    public Plant(Point flowPoint) {
        super(EntityType.PLANT, flowPoint);
        setImage(plantImage);
        foodValue = 1000;
    }

    public int eatPlant(int needEnergy){
        foodValue -= needEnergy;
        if(foodValue <= 0)
            alive = false;
        return (isAlive())? needEnergy : needEnergy + foodValue;
    }

}
