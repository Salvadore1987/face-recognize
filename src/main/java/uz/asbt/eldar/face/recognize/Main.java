package uz.asbt.eldar.face.recognize;


import uz.asbt.eldar.face.recognize.lib.FaceRecognize;

public class Main {
    public static void main(String[] args) throws Exception {
        String licenseKey = "bSB3NdbTnv/0eW/uhypSe6hDMtjZ76Sisw5NwcN+0sfahxOtoUW22el54e/M6cSG5" +
                "/xsdVIorPgugbTIfoIIn7ltyw1QMSleNebVx/Xe8aRA8bP+aVDybjoWdW/0rDP9Pv7yqBzNXyuwjgsVhPB53VGP8oTirTSUP7PTzSwOEe0=";
        try (FaceRecognize faceRecognize = FaceRecognize.getInstance(licenseKey)) {

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }
}
