package com.sammc.puppet.application.screen;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Scanner;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

/**
 * Compartmentalized file I/O operations for a Snapshot Frame.
 * 
 * @author sammc
 */
public class FileIO {

    private SnapshotFrame parent;

    /*
     * File Operations
     */

    public FileIO(SnapshotFrame parent) {
        this.parent = parent;
    }

    /*
     * Entity File layout (The order of the lines technically doesn't matter)
     * 
     * transform 1.0 1.0 1.0 0.0 0.0 0.0 202.0 44.0
     * image name.png
     * children patrick.e 
     */

    /**
     * Shortcut for saveEntityFile()
     * 
     * @param selected
     */
    public void saveEntity(Entity selected) {
        saveEntityFile(parent.getCurrentSnapshot().project_path + "/EntityLibrary/" +  selected.getEntityFileName(), selected);
    }

    /**
     * Saves the given entity to the given filepath
     * @param filepath
     * @param entity
     */
    public void saveEntityFile(String filepath, Entity entity) {
        File selectedFile = new File(filepath);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(selectedFile);
            StringBuilder sb = new StringBuilder();
            sb.append("transform " + entity.getUni_scale() + " " + entity.getX_scale() + " " + entity.getY_scale() + " " + entity.getX_shear() + " " + entity.getY_shear() + " " + entity.getRotation() + " " + entity.getX_offset() + " " + entity.getY_offset() + "\n");
            String[] fields = entity.getVisualAssetName().split("/");
            if (fields.length != 0) sb.append("image " + fields[fields.length - 1] + "\n");

            sb.append("children ");
            for (Entity c : entity.getChildren()) {
                sb.append(c.getName() + " ");
                saveEntity(c);
            }

            fileOutputStream.write(sb.toString().getBytes());
            fileOutputStream.close();
            if (parent != null) parent.refresh();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Entity loadEntityFile(String filepath) throws FileNotFoundException {
        File selectedFile = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(selectedFile);
        Scanner scan = new Scanner(fileInputStream);
        Entity entity = new Entity();
        entity.setEntityFileName(filepath);

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
                    BufferedImage img = Util.readImage( parent.getCurrentSnapshot().project_path + "/Images/" + path);
                    entity.setVisualAsset(img, path);
                }
                case "children" -> {
                    // each field here is going to be the name of another entity in the same project. 
                    // need to load each entity and then add them as a child once that's something relatively easy to do
                    for (int i = 1; i < fields.length; i++) {
                        Entity child = loadEntityFile(parent.getCurrentSnapshot().project_path + "/EntityLibrary/" + fields[i]);
                        entity.addChild(child);
                    }
                }
            }
        }
        scan.close();
        return entity;
    }

    /**
     * Load a file that contains the data for a 'session', with a particular name,
     * set of entities, and optionally a background image.
     * 
     * The file format looks like this: (the order of backgrounds vs entities technically doesn't matter)
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
        parent.getCurrentSnapshot().clear();

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
                String path = parent.getCurrentSnapshot().project_path + "/Images/" + line.substring(fields[0].length()).trim();
                BufferedImage img = Util.readImage(path);
                parent.getCurrentSnapshot().background = img;
                parent.getCurrentSnapshot().background_path = path;

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
                    // For each entity, load the entity.
                    String entity_path = parent.getCurrentSnapshot().project_path + "/EntityLibrary/" + e_name.trim();
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
                sb.append("background " + parent.getCurrentSnapshot().background_path + "\n");
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
            parent.refresh();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
