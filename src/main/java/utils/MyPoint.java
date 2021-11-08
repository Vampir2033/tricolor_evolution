package utils;

import java.awt.*;

public class MyPoint extends Point {
    public static int getDistance(Point from, Point to){
        return Math.abs(to.x - from.x) + Math.abs(to.y - from.y);
    }
}
