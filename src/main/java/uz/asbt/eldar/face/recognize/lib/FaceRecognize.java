package uz.asbt.eldar.face.recognize.lib;

import Luxand.FSDK;
import uz.asbt.eldar.face.recognize.exceptions.RecognizeException;
import uz.asbt.eldar.face.recognize.model.FacePosition;
import uz.asbt.eldar.face.recognize.model.FaceTemplate;
import uz.asbt.eldar.face.recognize.model.FacialFeatures;
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
     * Создает копию изображения. Дескриптор целевого изображения должен быть
     * создан с помощью функции FSDK_CreateEmptyImage.
     * @param sourceImage - дескриптор изображения из которого нужно копировать.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage copyImage(FSDK.HImage sourceImage) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.CopyImage(sourceImage, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't copy image");
        return destImage;
    }

    /**
     * Изменяет размер изображения. Дескриптор целевого изображения должен быть создан с
     * функция FSDK_CreateEmptyImage.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param ratio - коэффициент, по которому изменяются размеры x и y исходного изображения. Фактор
     * значение больше 1 соответствует увеличению размера изображения.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage resizeImage(FSDK.HImage sourceImage, double ratio) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.ResizeImage(sourceImage, ratio, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't resize image");
        return destImage;
    }

    /**
     * Поворот изображения на 90 или 180 градусов по часовой стрелке или против часовой стрелки.
     * Дескриптор целевого изображения должен быть создан с помощью функции FSDK_CreateEmptyImage.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param multiplier - целочисленный множитель 90 градусов, определяющий угол поворота.
     *                   Укажите 1 для 90 градусов по часовой стрелке, 2 для 180 градусов по часовой стрелке;
     *                   укажите -1 для 90 градусов против часовой стрелки.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage rotateImage90(FSDK.HImage sourceImage, int multiplier) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.RotateImage90(sourceImage, multiplier, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't rotate image");
        return destImage;
    }

    /**
     * Поворот изображения вокруг его центра. Дескриптор целевого изображения должен быть создан с
     * помощью функции FSDK_CreateEmptyImage.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param angle - угол поворота в градусах.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage rotateImage(FSDK.HImage sourceImage, double angle) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.RotateImage(sourceImage, angle, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't rotate image");
        return destImage;
    }

    /**
     * Поворот изображения вокруг произвольного центра.
     * Дескриптор целевого изображения должен быть создан с помощью функции FSDK_CreateEmptyImage.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param angle  - угол поворота в градусах.
     * @param xCenter - координата X центра вращения.
     * @param yCenter - координата Y центра вращения.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage rotateImageCenter(FSDK.HImage sourceImage,
                                         double angle,
                                         double xCenter,
                                         double yCenter) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.RotateImageCenter(sourceImage, angle, xCenter, yCenter, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't rotate image");
        return destImage;
    }

    /**
     * Создает копию прямоугольной области изображения.
     * Дескриптор целевого изображения должен быть создан с помощью функции FSDK_CreateEmptyImage.
     * Если некоторая вершина прямоугольника находится вне исходного изображения, прямоугольные области,
     * которые не содержат исходное изображение, будут черными.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param x1 - координата X левого нижнего угла скопированного прямоугольника.
     * @param x2 - координата X правого верхнего угла скопированного прямоугольника.
     * @param y1 - координата Y левого нижнего угла скопированного прямоугольника.
     * @param y2 - координата Y правого верхнего угла скопированного прямоугольника.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage copyRect(FSDK.HImage sourceImage,
                                int x1,
                                int x2,
                                int y1,
                                int y2) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.CopyRect(sourceImage, x1, y1, x2, y2, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't copy rectangle");
        return destImage;
    }

    /**
     * Создает копию прямоугольной области изображения и добавляет реплицированные границы пикселей.
     * Дескриптор целевого изображения должен быть создан с помощью функции FSDK_CreateEmptyImage.
     * Эта функция копирует исходное изображение в целевое изображение и заполняет пиксели («рамку»)
     * за пределами скопированной области в целевом изображении значениями ближайших пикселей исходного изображения.
     * @param sourceImage - дескриптор изменяемого изображения.
     * @param x1 - координата X левого нижнего угла скопированного прямоугольника.
     * @param y1 - координата Y левого нижнего угла скопированного прямоугольника.
     * @param x2 - координата X правого верхнего угла скопированного прямоугольника.
     * @param y2 - координата Y правого верхнего угла скопированного прямоугольника.
     * @return измененное изображение.
     * @throws RecognizeException в случае ошибки.
     */
    public FSDK.HImage copyRectReplicateBorder(FSDK.HImage sourceImage,
                                               int x1,
                                               int y1,
                                               int x2,
                                               int y2) throws RecognizeException {
        FSDK.HImage destImage = createEmptyImage();
        int result = FSDK.CopyRectReplicateBorder(sourceImage, x1, y1, x2, y2, destImage);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't copy rectangle");
        return destImage;
    }

    /**
     * Зеркально отображает изображение. Функция может отражать изображения по горизонтали или по вертикали.
     * @param hImage - дескриптор изображения.
     * @param useVerticalMirroringInsteadOfHorizontal - устанавливает направление зеркалирования.
     *                                                true - слева-направо, false - сверху вниз.
     * @throws RecognizeException в случае ошибки.
     */
    public void mirrorImage(FSDK.HImage hImage, boolean useVerticalMirroringInsteadOfHorizontal) throws RecognizeException {
        int result = FSDK.MirrorImage(hImage, useVerticalMirroringInsteadOfHorizontal);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't mirror image");
    }

    /**
     * Возвращает ширину изображения.
     * @param hImage - дескриптор изображения.
     * @return ширина изображения.
     * @throws RecognizeException в случае ошибки.
     */
    public int getImageWidth(FSDK.HImage hImage) throws RecognizeException {
        int[] width = new int[1];
        int result = FSDK.GetImageWidth(hImage, width);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get image width");
        return width[0];
    }

    /**
     * Возвращает высоту изображения.
     * @param hImage - дескриптор изображения.
     * @return высота изображения.
     * @throws RecognizeException в случае ошибки.
     */
    public int getImageHeight(FSDK.HImage hImage) throws RecognizeException {
        int[] height = new int[1];
        int result = FSDK.GetImageHeight(hImage, height);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get image height");
        return height[0];
    }

    /**
     * Эта функция используется для извлечения шаблона из изображения лица. Функция сначала обнаруживает
     * лицо, затем обнаруживает его черты лица и извлекает шаблон.
     * Если на изображении более одного лица, шаблон извлекается для лица с наибольшим
     * четко видимыми деталями. Если нет четко видимого лица, функция возвращает код ошибки. Чтобы
     * установить порог, определяющий приемлемое качество для лица, используйте функцию FSDK_SetFaceDetectionThreshold.
     * Если положение лица или его особенности или глазные центры известны, более эффективно использовать
     * Функцию FSDK_GetFaceTemplateInRegion или FSDK_GetFaceTemplateUsingEyes. Чтобы
     * извлеч шаблон для определенного лица, используйте функцию FSDK_GetFaceTemplateInRegion.
     * @param hImage - дескриптор изображения.
     * @return объект класса @see FaceTemplate, который содержит шаблон лица.
     * @throws RecognizeException в случае ошибки.
     */
    public FaceTemplate getFaceTemplate(FSDK.HImage hImage) throws RecognizeException {
        FSDK.FSDK_FaceTemplate.ByReference byReference = new FSDK.FSDK_FaceTemplate.ByReference();
        int result = FSDK.GetFaceTemplate(hImage, byReference);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't found face");
        return new FaceTemplate(byReference.template);
    }

    /**
     * Сравнивает 2 шаблона лица. Возвращаемое значение определяет сходство.
     * @param template1 - первый шаблон для сверки.
     * @param template2 - второй шаблон для сверки.
     * @return значение указывающее на сходство лица.
     * @throws RecognizeException в случае ошибки.
     */
    public float matchFaces(FaceTemplate template1, FaceTemplate template2) throws RecognizeException {
        float[] similarity = new float[1];
        FSDK.FSDK_FaceTemplate.ByReference faceTemplate1 = new FSDK.FSDK_FaceTemplate.ByReference();
        FSDK.FSDK_FaceTemplate.ByReference faceTemplate2 = new FSDK.FSDK_FaceTemplate.ByReference();
        faceTemplate1.template = template1.getTemplate();
        faceTemplate2.template = template2.getTemplate();
        int result = FSDK.MatchFaces(faceTemplate1, faceTemplate2, similarity);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't match faces");
        return similarity[0];
    }

    /**
     * Извлекает шаблон для лица, расположенного в определенной области, возвращенный методами detectFace или
     * detectMultipleFaces.
     * Функция обнаруживает черты лица в определенной области и извлекает шаблон.
     * Эта функция может быть полезна, если приблизительный размер лица и
     * позиция известна.Функция не возвращает ошибок, если лицо не ясно видно.
     * Это потому, что предполагается, что если функции обнаружения лица возвращают обнаруженное положение лица,
     * лицо достаточно качественное.
     * Если черты лица или глазные центры известны, более эффективно использовать
     * Функцию getFaceTemplateUsingFeatures или getFaceTemplateUsingEyes.
     * @param hImage - дескриптор изображения.
     * @param facePosition - экземпляр объекта @see FacePosition с координатами найденного лица.
     * @return объект класса @see FaceTemplate, который содержит шаблон лица.
     * @throws RecognizeException в случае ошибки.
     */
    public FaceTemplate getFaceTemplateInRegion(FSDK.HImage hImage,
                                                FacePosition facePosition) throws RecognizeException {
        FSDK.TFacePosition tFacePosition = facePosition.convertToTFacePosition();
        FSDK.FSDK_FaceTemplate.ByReference faceTemplate = new FSDK.FSDK_FaceTemplate.ByReference();
        int result = FSDK.GetFaceTemplateInRegion(hImage, tFacePosition, faceTemplate);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't found face");
        return new FaceTemplate(faceTemplate.template);
    }

    /**
     * Извлекает шаблон лица с использованием обнаруженных координат элемента лица.
     * Функция получает координаты черты лица, обнаруженные с помощью методов detectFacialFeatures
     * или detectFacialFeaturesInRegion. Обнаружение лицо, и обнаружение черт лица и глазных центров не выполняются.
     * Эта функция может быть полезна, когда черты лица для определенного лица уже обнаружены. Функция
     * не вызывает ошибок, если лицо не ясно видно, так как предполагается, что если лицо и его
     * черты лица уже обнаружены, лицо достаточно качественное.
     * Функция определяет, равны ли черты лица, начиная со второго, нулю или
     * неинициализированный. В этом случае функции вместо этого вызывают getFaceTemplateUsingEyes.
     * @param hImage - дескриптор изображения.
     * @param facialFeatures - объект с координатами обнаруженных заранее.
     * @return объект класса @see FaceTemplate, который содержит шаблон лица.
     * @throws RecognizeException в случае ошибки.
     */
    public FaceTemplate getFaceTemplateUsingFeatures(FSDK.HImage hImage,
                                                     FacialFeatures facialFeatures) throws RecognizeException {
        FSDK.FSDK_FaceTemplate.ByReference faceTemplate = new FSDK.FSDK_FaceTemplate.ByReference();
        FSDK.FSDK_Features features = facialFeatures.convertToFSDKFeatures();
        int result = FSDK.GetFaceTemplateUsingFeatures(hImage, features, faceTemplate);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't detect face");
        return new FaceTemplate(faceTemplate.template);
    }

    /**
     * Извлекает шаблон лица, используя обнаруженные центры глаз.
     * Функция получает координаты глазных центров, обнаруженных с помошью методов detectFacialFeatures,
     * detectFacialFeaturesInRegion, detectEyes или detectEyesInRegion и извлекает шаблон лица.
     * Обнаружение лица, обнаружение черт лица и глазных центров не производится.
     * Эта функция может быть полезна, когда черты лица или глазные центры
     * для конкретного лица уже обнаружены. Функция не возвращает ошибки, если лицо неясно
     * видимо, поскольку она предполагает, что если лицо и его черты лица или глазные центры уже
     * обнаружены, то лицо достаточно качественное.
     * Обратите внимание, что getFaceTemplate, getFaceTemplateInRegion и
     * функциия getFaceTemplateUsingFeatures возвращают шаблоны, которые могут быть сопоставлены с
     * более высокой точностью, поэтому рекомендуется использовать эти функции.
     * @param hImage - дескриптор изображения.
     * @param eyesCoordinate - объект с координатами центров глаз.
     * @return объект класса @see FaceTemplate, который содержит шаблон лица.
     * @throws RecognizeException в случае ошибки.
     */
    public FaceTemplate getFaceTemplateUsingEyes(FSDK.HImage hImage,
                                                 FacialFeatures eyesCoordinate) throws RecognizeException {
        FSDK.FSDK_FaceTemplate.ByReference faceTemplate = new FSDK.FSDK_FaceTemplate.ByReference();
        FSDK.FSDK_Features features = eyesCoordinate.convertToFSDKFeatures();
        int result = FSDK.GetFaceTemplateUsingEyes(hImage, features, faceTemplate);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't detect face");
        return new FaceTemplate(faceTemplate.template);
    }

    /**
     * Эта функция возвращает пороговое значение для сходства, чтобы определить, соответствуют ли два
     * сопоставленных шаблона и принадлежат ли одному и тому же лицу по заданному значению
     * FAR (коэффициент ложного принятия). FAR определяет допустимую частоту ошибок,
     * когда шаблоны двух разных людей ошибочно признаны тем же лицом.
     * Уменьшение FAR приводит к увеличению FRR - т.е. с низким FAR становится более вероятным, что будут
     * определены два шаблона от одного и того же человека как принадлежность к разным людям.
     * @param value желаемое значение FAR. Варьируется от 0,0 (означает 0%) до 1,0 (означает 100%).
     * @return указатель на переменную типа float для хранения рассчитанного порогового значения
     * @throws RecognizeException в случае ошибки.
     */
    public float getMatchingThresholdAtFAR(float value) throws RecognizeException {
        float[] threshold = new float[1];
        int result = FSDK.GetMatchingThresholdAtFAR(value, threshold);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get matching");
        return threshold[0];
    }

    /**
     * Эта функция возвращает пороговое значение для сходства, чтобы определить, соответствуют ли два сопоставленных шаблона
     * одному лицу с заданным значением FRR (False Rejection Rate). FRR определяет
     * допустимую частоту ошибок, когда два шаблона одного и того же лица определены как принадлежащие к
     * различным людям. Уменьшение FRR приводит к увеличению FAR - то есть с низким FRR становится
     * более вероятно, что шаблоны двух разных людей будут признаны одним и тем же человеком.
     * @param value - желаемое значение FRR. Варьируется от 0,0 (означает 0%) до 1,0 (означает 100%).
     * @return указатель на переменную типа float для хранения рассчитанного порогового значения
     * @throws RecognizeException в случае ошибки.
     */
    public float getMatchingThresholdAtFRR(float value) throws RecognizeException {
        float[] threshold = new float[1];
        int result = FSDK.GetMatchingThresholdAtFRR(value, threshold);
        if (result != FSDK.FSDKE_OK)
            throw new RecognizeException("Can't get matching");
        return threshold[0];
    }

    @Override
    public void close() throws Exception {
        instance = null;
        finalizeLib();
    }
}
