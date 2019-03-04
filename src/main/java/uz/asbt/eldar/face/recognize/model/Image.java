package uz.asbt.eldar.face.recognize.model;

import com.sun.jna.ptr.IntByReference;

public class Image {

    private IntByReference imageRef;

    public IntByReference getImageRef() {
        return imageRef;
    }

    public void setImageRef(IntByReference imageRef) {
        this.imageRef = imageRef;
    }
}
