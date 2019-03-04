package uz.asbt.eldar.face.recognize.model;

import Luxand.FSDK;

public class Point {

    private int x;
    private int y;

    public Point() {}

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point(FSDK.TPoint tPoint) {
        this.x = tPoint.x;
        this.y = tPoint.y;
    }

    protected FSDK.TPoint convertToTPoint(Point point) {
        FSDK.TPoint tPoint = new FSDK.TPoint();
        tPoint.x = point.x;
        tPoint.y = point.y;
        return tPoint;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
