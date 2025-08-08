package com.sammc.puppet_application.activities;

import java.awt.AlphaComposite;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;

import com.sammc.puppet_application.activities.scene_edit.screen_objects.Entity;

public class Util {

    public static final String PROJECTS_DIRECTORY = "./Projects";

    public static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

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
        if (files.length == 0) return 0;

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

     /**
     * Render a single entity to the given graphics context
     * @param render_surface
     * @param entity
     * @param base_orientation
     */
    public static void render_entity(Graphics2D g, Entity entity, AffineTransform base_orientation, boolean isGhost) {
        AffineTransform t = entity.getTransform();
        t.preConcatenate(base_orientation);
        
        // if it's a visual entity, draw it
        if (entity.isVisual()) {
            // optionally draw at 50% opacity on the user's view
            if (isGhost) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            // the only thing drawn to the program view is actual images, no bounding boxes and no ghosts
            // while everything, including the image goes on the user view which will actually be drawn to the screen
            g.drawImage(entity.getVisualAsset(), t, null);
        
            // Restore the composite to normal
            if (isGhost) g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // render any child entities
        for (Entity c : entity.getChildren()) {
            render_entity(g, c, t, isGhost);
        }

    }

    public String chooseFile(String path) {
        File selected_file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Image File To Use");
        fileChooser.setCurrentDirectory(new File("./Projects"));
        fileChooser.setLocation(200,200);

        int returnValue = fileChooser.showOpenDialog(fileChooser);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selected_file = fileChooser.getSelectedFile();
        }

        if (selected_file == null) return "";
        return selected_file.getAbsolutePath();

    }
}
