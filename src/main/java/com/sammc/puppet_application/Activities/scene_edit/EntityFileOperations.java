package com.sammc.puppet_application.activities.scene_edit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import com.sammc.puppet_application.Util;
import com.sammc.puppet_application.screen_objects.Entity;

public class EntityFileOperations {

    private SceneEditFrame parent;

    /*
     * File Operations
     */

    public EntityFileOperations(SceneEditFrame sceneEditFrame) {
        this.parent = sceneEditFrame;
    }

    public void saveEntityFile(String filepath, Entity entity) {
        File selectedFile = new File(filepath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
            StringBuilder sb = new StringBuilder();
            sb.append("transform " + entity.getUni_scale() + " " + entity.getX_scale() + " " + entity.getY_scale() + " " + entity.getX_shear() + " " + entity.getY_shear() + " " + entity.getRotation() + " " + entity.getX_offset() + " " + entity.getY_offset() + "\n");
            sb.append("image " + entity.getVisualAssetPath() + "\n");
            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEntityFile(String filepath) throws FileNotFoundException {
        File selectedFile = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(selectedFile);
        Scanner scan = new Scanner(fileInputStream);
        Entity entity = new Entity();
        while (scan.hasNextLine()) {
            String[] fields = scan.nextLine().split(" ");
            switch (fields[0]) {
                case "transform" -> {
                    // format for the transform section will be:
                    // transform <uni_scale> <x_scale> <y_scale> <x_shear> <y_shear> <rotation> <x_offset> <y_offset>
                    entity.setUni_scale(Double.parseDouble(fields[1]));
                    entity.setX_scale(Double.parseDouble(fields[2]));
                    entity.setY_scale(Double.parseDouble(fields[3]));
                    entity.setX_shear(Double.parseDouble(fields[4]));
                    entity.setY_shear(Double.parseDouble(fields[5]));
                    entity.setRotation(Double.parseDouble(fields[6]));
                    entity.setX_offset(Double.parseDouble(fields[7]));
                    entity.setY_offset(Double.parseDouble(fields[8]));
                }
                case "image" -> {
                    String path = fields[1];
                    BufferedImage img = Util.readImage(path);
                    entity.setVisualAsset(img, path);
                }
                case "animation" -> {
                }
                case "connection" -> {
                }
            }
        }
        scan.close();
        parent.addEntity(entity);
    }
    
}
