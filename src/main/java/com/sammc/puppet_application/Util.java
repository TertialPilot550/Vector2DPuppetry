package com.sammc.puppet_application;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Util {
    public static BufferedImage readImage(String filePath) {
        BufferedImage image = null;
        try {
            File file = new File(filePath);
            image = ImageIO.read(file);
        } catch (IOException e) {
            System.err.println("Error reading image file: " + e.getMessage());
        }
        return image;
    }
}
