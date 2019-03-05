package uz.asbt.eldar.face.recognize.model;

import Luxand.FSDK;

public class FaceTemplate {

    private byte[] template;

    public FaceTemplate() {
        this.template = new byte[13324];
    }

    public FaceTemplate(byte[] template) {
        this.template = template;
    }

    public FaceTemplate(FSDK.FSDK_FaceTemplate fsdkFaceTemplate) {
        this.template = fsdkFaceTemplate.template;
    }

    public byte[] getTemplate() {
        return template;
    }

    public void setTemplate(byte[] template) {
        this.template = template;
    }
}
