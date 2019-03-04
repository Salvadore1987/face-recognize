package uz.asbt.eldar.face.recognize.model;

import Luxand.FSDK;

public class FacePosition {

    public int xc;
    public int yc;
    public int w;
    public double angle;

    public FacePosition() {
    }

    public FacePosition(FSDK.TFacePosition tFacePosition) {
        this.xc = tFacePosition.xc;
        this.yc = tFacePosition.yc;
        this.w = tFacePosition.w;
        this.angle = tFacePosition.angle;
    }

    public FSDK.TFacePosition convertToTFacePosition() {
        FSDK.TFacePosition tFacePosition = new FSDK.TFacePosition();
        tFacePosition.xc = this.xc;
        tFacePosition.yc = this.yc;
        tFacePosition.w = this.w;
        tFacePosition.angle = this.angle;
        return tFacePosition;
    }

    public int getXc() {
        return xc;
    }

    public void setXc(int xc) {
        this.xc = xc;
    }

    public int getYc() {
        return yc;
    }

    public void setYc(int yc) {
        this.yc = yc;
    }

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }
}
