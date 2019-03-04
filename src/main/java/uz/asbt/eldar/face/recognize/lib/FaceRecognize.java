package uz.asbt.eldar.face.recognize.lib;

import Luxand.FSDK;
import uz.asbt.eldar.face.recognize.exceptions.RecognizeException;
import uz.asbt.eldar.face.recognize.model.FacePosition;
import uz.asbt.eldar.face.recognize.model.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс предназначен для обработки фотоизображений, и для распознавания лиц.
 * Включает в себя все методы необходимые для работы с изображениями и для распознования лиц.
 * Использует интерфейс AutoCloseable для автоматического закрытия объекта.
 */
public class FaceRecognize implements AutoCloseable {

    // Поле содержащее лицензионный ключ для активации библиотеки.
    private final String licenseKey;

    // Статическое поле содержит экземпляр объекта данного класса.
    private static FaceRecognize instance;

    /**
     * Приватный конструктор класса, служит для инициализации объекта данного класса.
     * @param licenseKey - ключ активации.
     * @throws RecognizeException в случае ошибки.
     */
    private FaceRecognize(String licenseKey) throws RecognizeException {
        this.licenseKey = licenseKey;
        activateLibrary();
        initializeLib();
    }

    /**
     * Статический метод служит для создания единственного экземпляра данного класса.
     * @param licenseKey - ключ активации.
     * @return возвращает экземпляр данного класса в единственном экземпляре.
     * @throws RecognizeException в случае ошибки.
     */
    public static FaceRecognize getInstance(String licenseKey) throws RecognizeException {
        if (instance == null)
            instance = new FaceRecognize(licenseKey);
        return instance;
    }

    /**
     * Метод для инициализации библиотеки.
     * @throws RecognizeException в случае ошибки.
     */
    private void initializeLib() throws RecognizeException {
        int result = FSDK.Initialize();
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't initialize library");
    }

    /**
     * Метод для удаления всех ссылок на библиотеку.
     * @throws RecognizeException в случае ошибки.
     */
    private void finalizeLib() throws RecognizeException {
        int result = FSDK.Finalize();
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't finalize library");
    }

    /**
     * Метод для активации библиотеки.
     * @throws RecognizeException в случае ошибки.
     */
    private void activateLibrary() throws RecognizeException {
        int result = FSDK.ActivateLibrary(licenseKey);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't activate library");
    }

    /**
     * Метод для установки кол-ва потоков процессора.
     * @param number кол-во потоков.
     * @throws RecognizeException в случае ошибки.
     */
    public void setNumberOfThreads(int number) throws RecognizeException {
        int result = FSDK.SetNumThreads(number);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't set number of threads");
    }

    /**
     * Метод возвращает уникальный идентификатор оборудования.
     * @return строка содержащая идентификатор оборудования.
     * @throws RecognizeException в случае ошибки.
     */
    public String getHardwareId() throws RecognizeException {
        String[] hardwareId = new String[1];
        int result = FSDK.GetHardware_ID(hardwareId);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get hardware id");
        return hardwareId[0];
    }

    /**
     * Метод возвращает информацию о лицензии.
     * @return строка содержащая информацию об лицензионном соглашении.
     * @throws RecognizeException в случае ошибки.
     */
    public String getLicenseInfo() throws RecognizeException {
        String[] info = new String[1];
        int result = FSDK.GetLicenseInfo(info);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get license info");
        return info[0];
    }

    /**
     * Метод возвращает объект экземпляра класса @see FacePosition.
     * В котором храняться данные по найденым координатам.
     * @param hImage - экземпляр класса @see FSDK.HImage, содержащий ссылку на фото изображение.
     * @return объект с координатами найденного лица.
     * @throws RecognizeException в случае ошибки.
     */
    public FacePosition detectFace(FSDK.HImage hImage) throws RecognizeException {
        FSDK.TFacePosition.ByReference tFacePosition = new FSDK.TFacePosition.ByReference();
        int result = FSDK.DetectFace(hImage, tFacePosition);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't detect face");
        return new FacePosition(tFacePosition);
    }

    /**
     * Метод возвращает список объектов экземпляра класса @see FacePosition.
     * В случае если на фотоизображении имеется более одного лица.
     * @param hImage - экземпляр класса @see FSDK.HImage, содержащий ссылку на фото изображение.
     * @return список объектов с координатами найденных лиц.
     * @throws RecognizeException в случае ошибки.
     */
    public List<FacePosition> detectMultipleFaces(FSDK.HImage hImage) throws RecognizeException {
        List<FacePosition> list = new ArrayList<>();
        FSDK.TFaces tFaces = new FSDK.TFaces();
        int result = FSDK.DetectMultipleFaces(hImage, tFaces);
        if (result != FSDK.FSDKE_OK && tFaces.faces.length == 0)
            throw new RecognizeException("Can't detect faces");
        for (FSDK.TFacePosition tFacePosition : tFaces.faces) {
            list.add(new FacePosition(tFacePosition));
        }
        return list;
    }

    /**
     * Метод возвращает координаты найденных глаз.
     * @param hImage - экземпляр класса @see FSDK.HImage, содержащий ссылку на фото изображение.
     * @return список объектов @see Point содержащие координаты глаз.
     * @throws RecognizeException в случае ошибки.
     */
    public List<Point> detectEyes(FSDK.HImage hImage) throws RecognizeException {
        List<Point> list = new ArrayList<>();
        FSDK.FSDK_Features.ByReference eyes = new FSDK.FSDK_Features.ByReference();
        int result = FSDK.DetectEyes(hImage, eyes);
        if (result != FSDK.FSDKE_OK && eyes.features.length == 0)
            throw new RecognizeException("Can't detect eyes");
        for (FSDK.TPoint tPoint : eyes.features) {
            list.add(new Point(tPoint));
        }
        return list;
    }

    /**
     * Метод возвращает координаты найденных глаз, по переданным координатам ранее найденного лица.
     * @param hImage - экземпляр класса @see FSDK.HImage, содержащий ссылку на фото изображение.
     * @param facePosition - экземпляр объекта @see FacePosition с координатами найденного лица.
     * @return список объектов @see Point содержащие координаты глаз.
     * @throws RecognizeException в случае ошибки.
     */
    public List<Point> detectEyesInRegion(FSDK.HImage hImage, FacePosition facePosition) throws RecognizeException {
        List<Point> list = new ArrayList<>();
        FSDK.FSDK_Features.ByReference eyes = new FSDK.FSDK_Features.ByReference();
        int result = FSDK.DetectEyesInRegion(hImage, facePosition.convertToTFacePosition(), eyes);
        if (result != FSDK.FSDKE_OK && eyes.features.length == 0)
            throw new RecognizeException("Can't detect eyes in region");
        for (FSDK.TPoint tPoint : eyes.features) {
            list.add(new Point(tPoint));
        }
        return list;
    }

    /**
     * Метод возвращает список координат, найденных по лицу.
     * @param hImage - экземпляр класса @see FSDK.HImage, содержащий ссылку на фото изображение.
     * @return список объектов @see Point содержащие координаты точек лица.
     * @throws RecognizeException в случае ошибки.
     */
    public List<Point> detectFacialFeatures(FSDK.HImage hImage) throws RecognizeException {
        List<Point> list = new ArrayList<>();
        FSDK.FSDK_Features.ByReference features = new FSDK.FSDK_Features.ByReference();
        int result = FSDK.DetectFacialFeatures(hImage, features);
        if (result != FSDK.FSDKE_OK && features.features.length == 0)
            throw new RecognizeException("Can't detect facial features");
        for (FSDK.TPoint tPoint : features.features) {
            list.add(new Point(tPoint));
        }
        return list;
    }

    /**
     * Метод возвращает список координат, найденных по лицу, по переданным координатам ранее найденного лица.
     * @param hImage - экземпляр класса @see FSDK.HImage, содержащий ссылку на фото изображение.
     * @param facePosition - экземпляр объекта @see FacePosition с координатами найденного лица.
     * @return список объектов @see Point содержащие координаты точек лица.
     * @throws RecognizeException в случае ошибки.
     */
    public List<Point> detectFacialFeaturesInRegion(FSDK.HImage hImage, FacePosition facePosition) throws RecognizeException {
        List<Point> list = new ArrayList<>();
        FSDK.FSDK_Features.ByReference features = new FSDK.FSDK_Features.ByReference();
        int result = FSDK.DetectFacialFeaturesInRegion(hImage, facePosition.convertToTFacePosition(), features);
        if (result != FSDK.FSDKE_OK && features.features.length == 0)
            throw new RecognizeException("Can't detect facial features in region");
        for (FSDK.TPoint tPoint : features.features) {
            list.add(new Point(tPoint));
        }
        return list;
    }

    /**
     * Метод позволяет установить ряд параметров распознавания лиц для контроля производительности и
     * надежность детектора лица.
     * @param handleArbitraryRotations - увеличивает угол поворота грани в плоскости по умолчанию с -15..15
     * градусов до -30..30 градусов. Значение true - расширенная поддержка вращения в плоскости включена за счет
     * скорости обнаружения (в 3 раза хит производительности). Значение false - по умолчанию быстрое
     * обнаружение -15..15 градусов.
     * @param determineFaceRotationAngle - включает или отключает обнаружение лица в плоскости
     * угол поворота. Значение true - определяет угол поворота в плоскости при обнаружении лиц. Угол записывается в
     * поле структуры TFacePosition (TFacePosition - это структура, возвращаемая FSDK_DetectFace
     * и FSDK_DetectMultipleFaces. Значение false - отключает определение угла поворота.
     * @param internalResizeWidth - контролирует скорость обнаружения, устанавливая размер изображения
     * функции обнаружения будут работать с. Выберите более высокое значение, чтобы повысить качество обнаружения, или
     * меньшее значение для улучшения производительности.
     * @throws RecognizeException в случае ошибки.
     */
    public void setFaceDetectionParameters(boolean handleArbitraryRotations,
                                           boolean determineFaceRotationAngle,
                                           int internalResizeWidth) throws RecognizeException {
        int result = FSDK.SetFaceDetectionParameters(
                handleArbitraryRotations,
                determineFaceRotationAngle,
                internalResizeWidth);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't setting face detection parameters");
    }

    /**
     * Метод устанавливает пороговое значение для обнаружения лица. Значение по умолчанию 5.
     * Наименьшее возможное значение 1.
     * Функция позволяет настроить чувствительность обнаружения. Если пороговое значение установлено в
     * чем выше значение, детектор распознает только лица с четкими, четко определенными деталями, таким образом
     * уменьшение количества ложноположительных обнаружений. Установка нижнего порога позволяет обнаружить
     * больше лиц с менее четко определенными чертами за счет увеличения числа ложных
     * позитивы.
     * @param threshold - значение порога.
     * @throws RecognizeException в случае ошибки.
     */
    public void setFaceDetectionThreshold(int threshold) throws RecognizeException {
        int result = FSDK.SetFaceDetectionThreshold(threshold);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't setting face detection threshold");
    }

    /**
     * Метод создает дескриптор пустого изображения. Вам не нужно вызывать эту функцию перед вызовом
     * FSDK_LoadImageFromXXXX, так как эти функции уже создают дескриптор HImage.
     * Должны быть вызваны перед использованием FSDK_CopyImage, FSDK_ResizeImage,
     * FSDK_RotateImage, FSDK_RotateImageCenter, FSDK_RotateImage90,
     * FSDK_MirrorImage, FSDK_CopyRect, FSDK_CopyRectReplicateBorder функции для создания
     * дескриптор целевого изображения.
     * @return дескриптор пустого изображения
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage createEmptyImage() throws RecognizeException {
        FSDK.HImage hImage = new FSDK.HImage();
        int result = FSDK.CreateEmptyImage(hImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't create empty image");
        return hImage;
    }

    /**
     * Загружает изображение из файла и предоставляет внутренний дескриптор этого изображения.
     * @param fileName - путь к файлу.
     * @return дескриптор файла.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage loadImageFromFile(String fileName) throws RecognizeException {
        FSDK.HImage hImage = new FSDK.HImage();
        int result = FSDK.LoadImageFromFile(hImage, fileName);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't load image from file");
        return hImage;
    }

    /**
     * Загружает изображение из пути к файлу в наборе символов Unicode и обеспечивает внутренний дескриптор
     * этого изображения. Функция доступна только на платформах Windows.
     * @param fileName - путь к файлу.
     * @return дескриптор файла.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage loadImageFromFileW(String fileName) throws RecognizeException {
        FSDK.HImage hImage = new FSDK.HImage();
        int result = FSDK.LoadImageFromFileW(hImage, fileName);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't load image from file");
        return hImage;
    }

    /**
     * Загружает изображение из буфера и предоставляет внутренний дескриптор этого изображения. Функция
     * предполагает, что данные изображения организованы в порядке сверху вниз, а расстояние между
     * соседние строки - это байты ScanLine (например, в 24-битном изображении значение ScanLine может
     * быть 3 * ширина байта, если между соседними строками нет промежутка). Следующие режимы изображения
     * поддерживаются: ImageMode.IMAGE_GRAYSCALE_8BIT, ImageMode.IMAGE_COLOR_24BIT, ImageMode.IMAGE_COLOR_32BIT
     * @param buffer буфер содержащий данные изображения в байтах.
     * @param width - ширина изображения в пикселях
     * @param height - высота изображения в пикселях
     * @param scanLine - расстояние между соседними строками в байтах
     * @param imageMode - режим изображения.
     * @return указатель на HImage для получения дескриптора загруженного изображения
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage loadImageFromBuffer(byte[] buffer,
                                           int width,
                                           int height,
                                           int scanLine,
                                           int imageMode) throws RecognizeException {
        FSDK.HImage hImage = new FSDK.HImage();
        int result = FSDK.LoadImageFromBuffer(hImage, buffer, width, height, scanLine, imageMode);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't load image from buffer");
        return hImage;
    }

    /**
     * Метод очищает диструктор изображения.
     * @param hImage - деструктор изображения, который необъодимо очистить.
     * @throws RecognizeException в случае ошибки.
     */
    public void freeImage(FSDK.HImage hImage) throws RecognizeException {
        int result = FSDK.FreeImage(hImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't free an image");
    }

    /**
     * Метод сохраняет изображение в файл.
     * @param hImage - дескриптор изображения.
     * @param fileName - имя файла для сохранения.
     * @throws RecognizeException в случае ошибки.
     */
    public void saveImageToFile(FSDK.HImage hImage, String fileName) throws RecognizeException {
        int result = FSDK.SaveImageToFile(hImage, fileName);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't save image to file");
    }

    /**
     * Метод сохраняет изображение в файл только для платформы Windows.
     * @param hImage - дескриптор изображения.
     * @param fileName - имя файла для сохранения.
     * @throws RecognizeException в случае ошибки.
     */
    public void saveImageToFileW(FSDK.HImage hImage, String fileName) throws RecognizeException {
        int result = FSDK.SaveImageToFileW(hImage, fileName);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't save image to file");
    }

    /**
     * Возвращает размер буфера изображения.
     * @param hImage - дескриптор изображения.
     * @param imageMode - режим изображения.
     * @return размер буфера.
     * @throws RecognizeException в случае ошибки.
     */
    public int getImageBufferSize(FSDK.HImage hImage, int imageMode) throws RecognizeException {
        int[] bufSize = new int[1];
        int result = FSDK.GetImageBufferSize(hImage, bufSize, imageMode);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get image buffer size");
        return bufSize[0];
    }

    /**
     * Метод сохраняет изображение в буфер.
     * @param hImage - дескриптор изображения.
     * @param imageMode - режим изображения.
     * @return буфер.
     * @throws RecognizeException в случае ошибки.
     */
    public byte[] saveImageToBuffer(FSDK.HImage hImage, int imageMode) throws RecognizeException {
        int bufSize = getImageBufferSize(hImage, imageMode);
        byte[] buffer = new byte[bufSize];
        int result = FSDK.SaveImageToBuffer(hImage, buffer, imageMode);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't save image to buffer");
        return buffer;
    }

    /**
     * Устанавливает качество сжатия JPEG для использования в функции FSDK_SaveImageToFile.
     * @param quality - качество сжатия JPEG. Варьируется от 0 до 100.
     * @throws RecognizeException в случае ошибки.
     */
    public void setJpegCompressionQuality(int quality) throws RecognizeException {
        int result = FSDK.SetJpegCompressionQuality(quality);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't setting jpeg quality");
    }

    /**
     * Копирует изображения из одного дескриптора в другой.
     * @param sourceImage - дескриптор изображения из которого нужно копировать.
     * @param destImage - дескриптор изображения в который нужно копировать.
     * @throws RecognizeException в случае ошибки.
     */
    public void copyImage(FSDK.HImage sourceImage, FSDK.HImage destImage) throws RecognizeException {
        int result = FSDK.CopyImage(sourceImage, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't copy image");
    }

    /**
     * Изменяет размер изображения. Дескриптор целевого изображения должен быть создан с
     * функция FSDK_CreateEmptyImage.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param ratio - коэффициент, по которому изменяются размеры x и y исходного изображения. Фактор
     * значение больше 1 соответствует увеличению размера изображения.
     * @param destImage - дескриптор целевого изображения.
     * @throws RecognizeException в случае ошибки.
     */
    public void resizeImage(FSDK.HImage sourceImage,
                            double ratio,
                            FSDK.HImage destImage) throws RecognizeException {
        int result = FSDK.ResizeImage(sourceImage, ratio, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't resize image");
    }

    /**
     *
     * @param sourceImage
     * @param multiplier
     * @param destImage
     * @throws RecognizeException
     */
    public void rotateImage90(FSDK.HImage sourceImage,
                              int multiplier,
                              FSDK.HImage destImage) throws RecognizeException {
        int result = FSDK.RotateImage90(sourceImage, multiplier, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't rotate image");
    }

    @Override
    public void close() throws Exception {
        instance = null;
        finalizeLib();
    }
}
