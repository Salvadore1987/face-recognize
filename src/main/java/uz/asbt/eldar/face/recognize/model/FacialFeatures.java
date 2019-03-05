package uz.asbt.eldar.face.recognize.model;

import Luxand.FSDK;

import java.util.List;

public class FacialFeatures {

    List<Point> points;

    public FSDK.FSDK_Features convertToFSDKFeatures() {
        FSDK.TPoint[] tPoints = new FSDK.TPoint[points.size()];
        for (int i = 0; i < points.size(); i++) {
            tPoints[i] = points.get(i).convertToTPoint();
        }
        FSDK.FSDK_Features features = new FSDK.FSDK_Features();
        features.features = tPoints;
        return features;
    }

    public FacialFeatures() {
    }

    public FacialFeatures(List<Point> points) {
        this.points = points;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}
