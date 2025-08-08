package com.sammc.puppet_application.activities.scene_edit;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import com.sammc.puppet_application.activities.Util;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Entity;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Snapshot;

/**
 * Compartmentalized file I/O operations for the SceneEditFrame.
 * 
 * @author sammc
 */
public class FileIO {

    private SceneEditFrame parent;

    /*
     * File Operations
     */

    public FileIO(SceneEditFrame sceneEditFrame) {
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

            sb.append("children ");
            for (Entity c : entity.getChildren()) {
                sb.append(c.getEntityFilePath() + " ");
                saveEntity(c);
            }

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
                case "children" -> {
                    // each field here is going to be the name of another entity in the same project. 
                    // need to load each entity and then add them as a child once that's something relatively easy to do
                    for (int i = 1; i < fields.length; i++) {
                        Entity child = loadEntityFile(fields[i]);
                    }
                }
            }
        }
        scan.close();
        parent.getCurrentSnapshot().addEntity(entity);
    }

    /**
     * Load a file that contains the data for a 'session', with a particular name,
     * set of entities, and optionally a background image.
     * 
     * The file format looks like this:
     * 
     * """
     * background <path to image>
     * entities <entity1> <entity2> <entity3> ...
     * """
     * 
     * @param filepath
     * @throws FileNotFoundException
     */
    public void loadSessionFile(String filepath) throws FileNotFoundException {
        // Build tools
        File selected_file = new File(filepath);
        FileInputStream fs = new FileInputStream(selected_file);
        Scanner scan = new Scanner(fs);

        // Prepare parent to load the new scene by clearing current session data
        parent.clear_session_data();

        /*
         * Read the file data, setting the parent frames session data as needed
         */
        while (scan.hasNextLine()) { // while the are still lines to be read
            // Read the line and split it into fields
            String line = scan.nextLine();
            String[] fields = line.split(" ");

            // Check the first field (command) to determine what to do
            if (fields[0].equals("background")) { // load the background image
                // load the background image
                String path = line.substring(fields[0].length()).trim();
                BufferedImage img = Util.readImage(path);
                parent.setBackgroundImage(img, path);

            } else if (fields[0].equals("entities")) { // load the entities
                /*
                 * Read all the entity names from the file, build those entity objects, and 
                 * add them to the parent frame.
                 */
                StringBuilder sb = new StringBuilder();
                sb.append(line.substring(fields[0].length()));

                // Read the rest of the file
                while (scan.hasNextLine()) {
                    sb.append(scan.nextLine());
                }
                
                // Now sb has the whole list of entities, seperated by whitespace
                String[] entity_names = sb.toString().trim().split(" ");

                for (String e_name : entity_names) {
                    System.out.println(e_name);
                    // For each entity, load the entity.
                    String entity_path = parent.getProjectRootPath() + "/EntityLibrary/" + e_name.trim();
                    System.out.println(entity_path);
                    loadEntityFile(entity_path);
                }
            }
        }
        scan.close();
    }
    
    /**
     * Save the parent frame's current session data to a file, with the given filepath.
     * 
     * @param filepath
     */
    public void saveSessionFile(String filepath) {
        File selected_file = new File(filepath);
        try {
            FileOutputStream fs = new FileOutputStream(selected_file);
            // Construct the information to write to the file
            StringBuilder sb = new StringBuilder();
            Snapshot current = parent.getCurrentSnapshot();
            // Conditionally set the background image
            BufferedImage background_img = current.background;
            if (background_img != null) {
                sb.append("background " + parent.getBackground_path() + "\n");
            }

            // Add all the entities to the session file
            sb.append("entities ");
            for (Entity entity : current.entities) {
                sb.append(entity.getName() + " ");
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
