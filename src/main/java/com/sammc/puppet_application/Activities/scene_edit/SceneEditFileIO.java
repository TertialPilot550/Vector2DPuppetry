package com.sammc.puppet_application.activities.scene_edit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import com.sammc.puppet_application.Util;
import com.sammc.puppet_application.screen_objects.Entity;

public class SceneEditFileIO {

    private SceneEditFrame parent;

    /*
     * File Operations
     */

    public SceneEditFileIO(SceneEditFrame sceneEditFrame) {
        this.parent = sceneEditFrame;
    }

    public void saveEntity(Entity selected) {
        saveEntityFile(selected.getEntityFilePath(), selected);
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
            parent.getProjectPanel().getTreePanel().refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadEntityFile(String filepath) throws FileNotFoundException {
        File selectedFile = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(selectedFile);
        Scanner scan = new Scanner(fileInputStream);
        Entity entity = new Entity();
        entity.setEntityFilePath(filepath);

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

    /*
     * File Format:
"""
id_counter {value}
background {image path}
entities {
    name,
    name,
    name,
    name,
    name(,)
}
"""
     */

    public void loadSessionFile(String filepath) throws FileNotFoundException {
        File selected_file = new File(filepath);
        FileInputStream fs = new FileInputStream(selected_file);
        Scanner scan = new Scanner(fs);

        // Prepare parent by clearing 
        parent.clear_session_data();

        // Read the file data, setting the parent frames session data as needed
        while (scan.hasNextLine()) {
            String line = scan.nextLine();
            String[] fields = line.split(" ");
            if (fields[0].equals("background")) {

            } else if (fields[0].equals("entities")) {
                StringBuilder sb = new StringBuilder();
                sb.append(line.substring(fields[0].length()));

                while (scan.hasNextLine()) {
                    sb.append(scan.nextLine());
                }
                
                // Now sb has the whole list of entities, seperated by whitespace
                String[] entity_names = sb.toString().split(" ");
                for (String e_name : entity_names) {
                    // For each entity, load the entity.
                    String entity_path = parent.getProjectRootPath() + "/EntityLibrary/" + e_name.trim();
                    loadEntityFile(entity_path);
                }
            }
        }
        scan.close();
    }
    
    public void saveSessionFile(String filepath) {
        File selected_file = new File(filepath);
        try {
            FileOutputStream fs = new FileOutputStream(selected_file);

            // Construct the information to write to the file
            StringBuilder sb = new StringBuilder();
            // TODO!

            // TODO: If there is a background, then add that line conditionally
            // get the background. If the background is not null, add it
            // also need to add some way to store the path to the background image. 


            // Maybe the background image, as well as the path to the background image, as well as the project directory should be moved to the parent object to make access cleaner.
            BufferedImage background_img = null;
            if (background_img != null) {
                //TODO!!!!
            }


            // Write and Close the file
            fs.write(sb.toString().getBytes());
            fs.close();

            // Refresh parent project view panel so the user can see the new file right away
            parent.getProjectPanel().getTreePanel().refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
