package utils;

import java.util.Random;

public class Range {
    public int minValue;
    public int maxValue;

    public Range(int minValue, int maxValue) {
        this.minValue = minValue;
        this.maxValue = maxValue;
    }

    public int inRange(int value){
        if(value < minValue)
            return minValue;
        else if(value > maxValue)
            return maxValue;
        else
            return value;
    }

    public int getRandValue(){
        return new Random().nextInt(maxValue - minValue + 1) + minValue;
    }

    public int transformToRange(Range r2, int value){
        return (r2.maxValue - r2.minValue) * (value - minValue) / (maxValue - minValue) + r2.minValue;
    }
}
