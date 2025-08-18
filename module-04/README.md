# Module 04: Image Processing and Character Art

This module focuses on image processing in Java, specifically converting images to ASCII/character art. You'll learn how to read image files, process pixel data, and create text-based representations of images.

## 📋 Exercises

### ex00: Basic Image to Character Conversion
- **Objective**: Implement basic image to character conversion
- **Concepts**: Image I/O, pixel processing, character mapping
- **Files**: `ImagesToChar/` directory with source code and resources
- **Resources**: `image.bmp`, `README.txt`

### ex01: Enhanced Image Processing
- **Objective**: Improve image processing with better algorithms
- **Concepts**: Enhanced pixel analysis, improved character mapping
- **Files**: `ImagesToChar/` directory with enhanced source code
- **Resources**: `image.bmp`, `manifest.txt`

### ex02: Advanced Image Processing with External Libraries
- **Objective**: Use external libraries for advanced image processing
- **Concepts**: JColor library integration, advanced color analysis
- **Files**: `ImagesToChar/` directory with Maven project
- **Dependencies**: `JColor-5.5.1.jar`, `jcommander-1.82.jar`

## 🚀 How to Run

### Prerequisites
- Java 8 or higher installed
- Understanding of file I/O (Module 02)
- Basic knowledge of image formats (BMP)

### For ex00 and ex01 (Simple Java)
```bash
# Navigate to the exercise directory
cd ex00/ImagesToChar/src/java/fr/school42/

# Compile the Java files
javac *.java

# Run the program
java Program
```

### For ex02 (Maven Project)
```bash
# Navigate to the exercise directory
cd ex02/ImagesToChar

# Compile and run with Maven
mvn clean compile
mvn exec:java -Dexec.mainClass="fr.school42.Program"

# Or run the JAR file directly
java -jar target/images-to-chars-printer.jar
```

## 📚 Learning Objectives

By the end of this module, you should be able to:
- Read and process image files in Java
- Understand pixel data and color information
- Convert images to ASCII/character art
- Use external libraries for image processing
- Handle different image formats
- Optimize image processing algorithms

## 🔍 Key Concepts Covered

- **Image I/O**: Reading image files, understanding image formats
- **Pixel Processing**: Accessing and manipulating individual pixels
- **Color Analysis**: RGB values, brightness, contrast
- **Character Mapping**: Converting pixel values to ASCII characters
- **Image Formats**: BMP file structure and processing
- **External Libraries**: Integrating third-party image processing libraries

## 🖼️ Image Processing Basics

### Reading Image Files
```java
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageProcessor {
    public BufferedImage loadImage(String filename) throws IOException {
        File file = new File(filename);
        if (!file.exists()) {
            throw new IOException("Image file not found: " + filename);
        }
        return ImageIO.read(file);
    }
    
    public void processImage(String filename) {
        try {
            BufferedImage image = loadImage(filename);
            int width = image.getWidth();
            int height = image.getHeight();
            
            System.out.println("Image dimensions: " + width + "x" + height);
            
            // Process each pixel
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    processPixel(rgb, x, y);
                }
            }
        } catch (IOException e) {
            System.err.println("Error processing image: " + e.getMessage());
        }
    }
}
```

### Pixel Analysis
```java
public class PixelAnalyzer {
    
    public static int getRed(int rgb) {
        return (rgb >> 16) & 0xFF;
    }
    
    public static int getGreen(int rgb) {
        return (rgb >> 8) & 0xFF;
    }
    
    public static int getBlue(int rgb) {
        return rgb & 0xFF;
    }
    
    public static double getBrightness(int rgb) {
        int red = getRed(rgb);
        int green = getGreen(rgb);
        int blue = getBlue(rgb);
        
        // Calculate brightness using luminance formula
        return 0.299 * red + 0.587 * green + 0.114 * blue;
    }
    
    public static double getContrast(int rgb1, int rgb2) {
        double brightness1 = getBrightness(rgb1);
        double brightness2 = getBrightness(rgb2);
        return Math.abs(brightness1 - brightness2);
    }
}
```

## 🎨 Character Mapping

### Basic Character Mapping
```java
public class CharacterMapper {
    private static final String CHARS = " .:-=+*#%@";
    
    public static char mapBrightnessToChar(double brightness) {
        // Normalize brightness to 0-1 range
        double normalized = brightness / 255.0;
        
        // Map to character index
        int index = (int) (normalized * (CHARS.length() - 1));
        return CHARS.charAt(index);
    }
    
    public static char mapBrightnessToCharAdvanced(double brightness) {
        // More sophisticated mapping with different character sets
        if (brightness < 30) return '@';
        if (brightness < 60) return '#';
        if (brightness < 90) return '%';
        if (brightness < 120) return '*';
        if (brightness < 150) return '+';
        if (brightness < 180) return '=';
        if (brightness < 210) return '-';
        if (brightness < 240) return ':';
        return '.';
    }
}
```

### Color-Based Mapping
```java
public class ColorMapper {
    
    public static char mapColorToChar(int rgb) {
        int red = PixelAnalyzer.getRed(rgb);
        int green = PixelAnalyzer.getGreen(rgb);
        int blue = PixelAnalyzer.getBlue(rgb);
        
        // Determine dominant color
        if (red > green && red > blue) {
            return mapRedIntensity(red);
        } else if (green > red && green > blue) {
            return mapGreenIntensity(green);
        } else {
            return mapBlueIntensity(blue);
        }
    }
    
    private static char mapRedIntensity(int red) {
        if (red > 200) return 'R';
        if (red > 150) return 'r';
        if (red > 100) return 'o';
        return '.';
    }
    
    private static char mapGreenIntensity(int green) {
        if (green > 200) return 'G';
        if (green > 150) return 'g';
        if (green > 100) return 'v';
        return '.';
    }
    
    private static char mapBlueIntensity(int blue) {
        if (blue > 200) return 'B';
        if (blue > 150) return 'b';
        if (blue > 100) return 'u';
        return '.';
    }
}
```

## 📁 BMP File Format

### BMP Header Structure
```java
public class BMPHeader {
    private byte[] header = new byte[54];
    
    public void readHeader(FileInputStream fis) throws IOException {
        fis.read(header);
    }
    
    public int getWidth() {
        return (header[21] & 0xFF) << 24 | 
               (header[20] & 0xFF) << 16 | 
               (header[19] & 0xFF) << 8 | 
               (header[18] & 0xFF);
    }
    
    public int getHeight() {
        return (header[25] & 0xFF) << 24 | 
               (header[24] & 0xFF) << 16 | 
               (header[23] & 0xFF) << 8 | 
               (header[22] & 0xFF);
    }
    
    public int getBitsPerPixel() {
        return (header[29] & 0xFF) << 8 | (header[28] & 0xFF);
    }
}
```

## 🔧 Image Processing Algorithms

### Grayscale Conversion
```java
public class GrayscaleConverter {
    
    public static BufferedImage convertToGrayscale(BufferedImage colorImage) {
        int width = colorImage.getWidth();
        int height = colorImage.getHeight();
        
        BufferedImage grayscaleImage = new BufferedImage(
            width, height, BufferedImage.TYPE_BYTE_GRAY);
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = colorImage.getRGB(x, y);
                int gray = (int) PixelAnalyzer.getBrightness(rgb);
                int grayRGB = (gray << 16) | (gray << 8) | gray;
                grayscaleImage.setRGB(x, y, grayRGB);
            }
        }
        
        return grayscaleImage;
    }
}
```

### Image Scaling
```java
public class ImageScaler {
    
    public static BufferedImage scaleImage(BufferedImage original, int targetWidth, int targetHeight) {
        BufferedImage scaled = new BufferedImage(targetWidth, targetHeight, original.getType());
        
        double scaleX = (double) original.getWidth() / targetWidth;
        double scaleY = (double) original.getHeight() / targetHeight;
        
        for (int y = 0; y < targetHeight; y++) {
            for (int x = 0; x < targetWidth; x++) {
                int srcX = (int) (x * scaleX);
                int srcY = (int) (y * scaleY);
                
                if (srcX < original.getWidth() && srcY < original.getHeight()) {
                    int rgb = original.getRGB(srcX, srcY);
                    scaled.setRGB(x, y, rgb);
                }
            }
        }
        
        return scaled;
    }
}
```

## 🚨 Common Pitfalls

- **Memory Issues**: Large images can consume significant memory
- **File Format Support**: Not all image formats are supported by default
- **Color Space**: Different color spaces can affect results
- **Performance**: Processing large images can be slow without optimization
- **Character Encoding**: Terminal character encoding affects output display

## 🔧 Best Practices

1. **Use BufferedImage**: More efficient than ImageIcon for processing
2. **Process in Chunks**: For large images, process in smaller sections
3. **Optimize Loops**: Minimize method calls inside nested loops
4. **Handle Exceptions**: Always handle IOExceptions properly
5. **Use Appropriate Data Types**: Use int for RGB values, double for calculations
6. **Test with Different Images**: Ensure your algorithm works with various image types

## 📖 Additional Resources

- [Java Image I/O Tutorial](https://docs.oracle.com/javase/tutorial/2d/images/index.html)
- [BMP File Format Specification](https://en.wikipedia.org/wiki/BMP_file_format)
- [ASCII Art](https://en.wikipedia.org/wiki/ASCII_art)
- [JColor Library Documentation](https://github.com/diogonunes/JColor)

## 🎯 Project Examples

This module includes practical examples like:
- Basic image to character converter
- Enhanced character mapping algorithms
- Advanced image processing with external libraries
- Real-time image conversion tools

---

**Previous Module**: [Module 03: Concurrency](../module-03/README.md)  
**Next Module**: [Module 05: Database Programming](../module-05/README.md)
