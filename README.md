# Face Recognition Java Library

A Java wrapper library for face recognition functionality using the Luxand FaceSDK. This project provides a comprehensive interface for facial detection, recognition, and tracking capabilities.

## Overview

This library wraps the Luxand FaceSDK to provide face recognition capabilities in Java applications. It includes features for face detection, facial feature extraction, face template generation, and face matching with high accuracy.

## Project Structure

```
face-recognize/
├── src/
│   └── main/
│       ├── java/
│       │   └── uz/asbt/eldar/face/recognize/
│       │       ├── Main.java                    # Application entry point
│       │       ├── Test.java                    # Test utilities
│       │       ├── common/
│       │       │   └── ImageMode.java           # Image processing modes
│       │       ├── exceptions/
│       │       │   └── RecognizeException.java  # Custom exception handling
│       │       ├── lib/
│       │       │   └── FaceRecognize.java       # Main face recognition API
│       │       └── model/
│       │           ├── FacePosition.java        # Face position coordinates
│       │           ├── FaceTemplate.java        # Face template data
│       │           ├── FacialFeatures.java      # Facial feature points
│       │           ├── Point.java               # 2D coordinate point
│       │           └── TrackerParameter.java    # Face tracking parameters
│       └── resources/
│           ├── lib/
│           │   ├── FaceSDK.jar                  # Luxand FaceSDK library
│           │   └── jna.jar                      # JNA (Java Native Access)
│           ├── facesdk.dll                      # Windows native library
│           └── libfsdk.so                       # Linux native library
├── facesdk.dll                                  # Windows native library (root)
├── libfsdk.so                                   # Linux native library (root)
└── pom.xml                                      # Maven build configuration
```

## Features

### Core Face Recognition
- **Face Detection**: Detect single or multiple faces in images
- **Facial Feature Detection**: Identify key facial landmarks (eyes, nose, mouth)
- **Face Template Generation**: Create unique face templates for comparison
- **Face Matching**: Compare face templates with configurable similarity thresholds

### Image Processing
- **Multiple Format Support**: Load images from files or memory buffers
- **Image Manipulation**: Resize, rotate, copy, and mirror images
- **Quality Control**: Configurable JPEG compression and detection thresholds

### Face Tracking
- **Real-time Tracking**: Track faces across multiple frames
- **ID Management**: Assign and manage unique identifiers for tracked faces
- **Parameter Configuration**: Customizable tracking parameters for different scenarios

### Advanced Features
- **Multi-threading Support**: Configurable CPU thread usage for performance optimization
- **Hardware ID**: Unique hardware identification for licensing
- **False Accept/Reject Rate Control**: Statistical matching thresholds for accuracy tuning

## Dependencies

The project uses Maven for build management and includes the following dependencies:

- **Luxand FaceSDK**: Commercial face recognition SDK (requires license key)
- **JNA (Java Native Access)**: Interface for calling native libraries
- **Java 8+**: Minimum Java version requirement

## License Requirements

This project requires a valid Luxand FaceSDK license key to function. The license key must be provided when initializing the `FaceRecognize` class.

## Usage Example

```java
// Initialize with license key
String licenseKey = "your-license-key-here";
try (FaceRecognize faceRecognize = FaceRecognize.getInstance(licenseKey)) {
    
    // Load image
    FSDK.HImage image = faceRecognize.loadImageFromFile("path/to/image.jpg");
    
    // Detect face
    FacePosition facePosition = faceRecognize.detectFace(image);
    
    // Extract face template
    FaceTemplate template = faceRecognize.getFaceTemplate(image);
    
    // Clean up
    faceRecognize.freeImage(image);
    
} catch (RecognizeException e) {
    System.err.println("Face recognition error: " + e.getMessage());
}
```

## Build Instructions

1. Ensure you have Maven installed
2. Place valid Luxand FaceSDK license key in your code
3. Run the build:

```bash
mvn clean compile
```

To package the application:

```bash
mvn clean package
```

## Platform Support

The library includes native libraries for:
- **Windows**: `facesdk.dll`
- **Linux**: `libfsdk.so`

## Threading and Performance

The library supports multi-threading for improved performance:

```java
// Set number of CPU threads to use
faceRecognize.setNumberOfThreads(4);

// Configure face detection parameters
faceRecognize.setFaceDetectionParameters(
    true,    // Handle arbitrary rotations
    true,    // Determine face rotation angle
    512      // Internal resize width
);
```

## Error Handling

All methods throw `RecognizeException` for error conditions. The library provides detailed error messages for troubleshooting face recognition operations.

## Author

Developed by Eldar Sagitov (uz.asbt.eldar)

## Notes

- This is a wrapper library around the commercial Luxand FaceSDK
- Requires a valid license key for operation
- Native libraries must be properly deployed with the application
- Thread-safe operations with synchronized methods
- Implements AutoCloseable for proper resource management