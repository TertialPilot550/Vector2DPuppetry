package com.sammc.puppet.application.entity_builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.screen.FileIO;
import com.sammc.puppet.application.screen.Screen;
import com.sammc.puppet.application.screen.SnapshotFrame;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

public class EntityBuilderFrame extends SnapshotFrame {
    
    private Screen screen;
    private Snapshot current;
    private String project_directory = "./Projects";
    

    JPanel controls;

    public EntityBuilderFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Animation Frame");
        setTitle("Animation Frame");
        setSize(1200, 600);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);
        current = new Snapshot(this);

        String grid_image_path = "./grid.jpg";
        current.background = Util.readImage(grid_image_path);
        current.background_path = grid_image_path;

        screen = new Screen(this);
        screen.setPreferredSize(new Dimension(500, 500));
        add(screen, BorderLayout.CENTER);

        controls = new JPanel();
        controls.setPreferredSize(new Dimension(100, 200));
        
        
        JButton newEntity = new JButton("New Entity");
        newEntity.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // clear the current entity
                current.entities.clear();
                
                // get a new entity
                Entity newEntity = quickLoadNewEntity();

                // add the entity
                current.entities.add(newEntity);
                refresh();
            }
            
        });
        controls.add(newEntity);

        JButton saveEntity = new JButton("Save Entity");
        saveEntity.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                Entity ent = current.entities.get(0);
                FileIO f = new FileIO(null);
                f.saveEntity(ent);
                refresh();
            }
            
        });
        controls.add(saveEntity);

        JButton addChildToEntity = new JButton("Add Child");
        addChildToEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entity selected = screen.getSelected();
                Entity child = quickLoadNewEntity();
                child.setX_offset(child.getX_offset() + 200);
                selected.addChild(child);
                refresh();
            }
        });
        controls.add(addChildToEntity);

        JButton deleteEntity = new JButton("Remove Selected");
        deleteEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entity selected = screen.getSelected();
                // if it's a top level entity... remove it
                if (current.entities.contains(selected)) current.entities.remove(selected);
                // other wise it will be a child so it should be removed 
                for (Entity entity : current.entities) {
                    // if it was successfully removed, stop looking
                    if (entity.remove_child(selected)) break;
                }
                refresh();
            }
        });
        controls.add(deleteEntity);

        // should be able to see the project directory, the 
        add(controls, BorderLayout.WEST);
    }

    @Override
    public Snapshot getCurrentSnapshot() {
        return current;
    }

    @Override
    public Snapshot getPreviousSnapshot() {
        return null;
    }

    @Override
    public boolean hasProjectLoaded() {
        return true;
    }

    @Override
    public void updateControls(Entity e) {
    }

    public static void main(String[] args) {
        EntityBuilderFrame b = new EntityBuilderFrame();
    }



    @Override
    public void refresh() {
        repaint();
    }


    public Entity quickLoadNewEntity() {
        Util u = new Util();
        String image_path = u.chooseFile(project_directory + "/Images");
        if (image_path == "") return null;

        Entity newEntity = new Entity();
        newEntity.setVisualAsset(Util.readImage(image_path), image_path);
        String[] path_fields = image_path.split("Projects/")[1].split("/");
        String entity_file_path = path_fields[0] + "/EntityLibrary/" + path_fields[path_fields.length-1].split("\\.")[0] + ".e";
        newEntity.setEntityFilePath(entity_file_path);
        return newEntity;
    }


}

