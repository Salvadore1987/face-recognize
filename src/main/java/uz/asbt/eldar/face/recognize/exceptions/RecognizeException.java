package uz.asbt.eldar.face.recognize.exceptions;

public class RecognizeException extends Exception {

    private String message;

    public RecognizeException(String message) {
        super(message);
    }
}
