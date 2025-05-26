package com.sammc.puppet_application.activities;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Util {

    public static final String PROJECTS_DIRECTORY = "./Projects";

    public static void buildProjectDirectory(String project_name) {
        String project_path = PROJECTS_DIRECTORY + "/" + project_name;
        File projectDir = new File(project_path);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }
        // Create subdirectories
        String[] subDirs = { "EntityLibrary", "Images", "Sessions", "Output" };
        for (String subDir : subDirs) {
            File dir = new File(project_path + "/" + subDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
    }

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

    public static int getNextAvailableFormattedFileNumber(String directory_path, String file_prefix) {
        // each file in the directory is assumed to be of the form file_prefix + _ + i + ".something"
        File dir = new File(directory_path);
        if (!dir.isDirectory()) return -1;

        String[] files = dir.list();
        if (files == null) return -1;

        // Seperate the file numbers from the file names
        int[] file_numbers = new int[files.length];
        for (int i = 0; i < files.length; i++) {
            if (!files[i].startsWith(file_prefix)) {
                continue; // skip files that don't match the prefix format
            }
            String file_name = files[i].split("\\.")[0];
            int f_num = Integer.parseInt(file_name.substring(file_prefix.length() + 1));
            file_numbers[i] = f_num;
        }

        // Find the highest file number used
        int max_num = -1;
        for (int i = 0; i < file_numbers.length; i++) {
            if (file_numbers[i] > max_num) {
                max_num = file_numbers[i];
            }
        }

        // Return the next available file number
        return max_num + 1;

    }

    // Save a BufferedImage to a file at the specified filepath in png
    public static void saveImage(BufferedImage img, String filepath) {
        File outputFile = new File(filepath);
        try {
            ImageIO.write(img, "png", outputFile);
        } catch (IOException e) {
           System.err.println("Error saving image: " + e.getMessage());
        }
    }
}
