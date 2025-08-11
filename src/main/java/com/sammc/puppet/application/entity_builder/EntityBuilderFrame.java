package com.sammc.puppet.application.entity_builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.screen.FileIO;
import com.sammc.puppet.application.screen.Screen;
import com.sammc.puppet.application.screen.SnapshotFrame;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

public class EntityBuilderFrame extends SnapshotFrame {
    
    private Screen screen;
    private Snapshot current;
    private SnapshotFrame parent;

    private JTextField entity_name_field;

    private JPanel controls;

    public EntityBuilderFrame(SnapshotFrame parent) {
        setFocusable(true);
        setName("Entity Wizard");
        setTitle("Entity Wizard");
        setSize(1200, 600);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);
        current = new Snapshot(this);
        this.parent = parent;

        JLabel entity_name_label = new JLabel("Entity Filename:");
        entity_name_field = new JTextField("something.e");

        String grid_image_path = "./res/grid.jpg";
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
                Entity ent = screen.getSelected();
                ent.setEntityFileName(entity_name_field.getText());
                FileIO f = new FileIO(parent);
                f.saveEntity(ent);
                refresh();
                parent.refresh();
            }
            
        });
        controls.add(saveEntity);

        JButton addChildToEntity = new JButton("Add Child");
        addChildToEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Entity selected = screen.getSelected();
                Entity child = quickLoadNewEntity();
                child.setEntityFileName(selected.getEntityFileName().split("\\.")[0] + "-" + child.getEntityFileName());
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

        
        controls.add(entity_name_label);
        controls.add(entity_name_field);

        // should be able to see the project directory, the 
        add(controls, BorderLayout.WEST);
        refresh();
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
    public void updateControls(Entity e) {
    }

    @Override
    public void refresh() {
        repaint();
    }

    /**
     * Use the util file chooser function to load a quick entity from a file and return it
     * @return
     */
    public Entity quickLoadNewEntity() {
        Util u = new Util();
        String image_path = u.chooseFile(parent.getCurrentSnapshot().project_path + "/Images");
        if (image_path == "") return null;

        Entity newEntity = new Entity();
        newEntity.setVisualAsset(Util.readImage(image_path), image_path);
        String[] path_fields = image_path.split("/");
        String entity_file_name = path_fields[path_fields.length-1].split("\\.")[0] + ".e";
        newEntity.setEntityFileName(entity_file_name);
        return newEntity;
    }


}

