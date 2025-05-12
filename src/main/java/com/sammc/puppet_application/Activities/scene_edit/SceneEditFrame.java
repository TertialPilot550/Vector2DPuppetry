package com.sammc.puppet_application.activities.scene_edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sammc.puppet_application.activities.scene_edit.panels.ControlsPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.ProjectOverviewPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.Screen;
import com.sammc.puppet_application.screen_objects.Entity;

public class SceneEditFrame extends JFrame {

    private Logger log = Logger.getLogger(SceneEditFrame.class.getName());
    
    private Screen screen;
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;
    private List<Entity> entities;
    public EntityFileOperations file_io = new EntityFileOperations(this);
    private int idCounter = 1000;

    public SceneEditFrame() {
        log.info("SceneEditFrame constructor called");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Animation Frame");
        setTitle("Animation Frame");
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);
        entities = new ArrayList<>();
        
        // Internal Container for the screen and control panel
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        screen = new Screen(this);
        screen.setPreferredSize(new Dimension(500, 500));
        container.add(screen, BorderLayout.CENTER);

        controlsPanel = new ControlsPanel(this);
        controlsPanel.setPreferredSize(new Dimension(400,150));
        container.add(controlsPanel, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);

        // project view
        projectPanel = new ProjectOverviewPanel(this);
        add(projectPanel, BorderLayout.WEST);

    }

    /*
     * Interacting with entities
     */

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getSelected() {
        return screen.getSelected();
    }

    public void deleteSelected() {
        Entity selected = screen.getSelected();
        if (selected != null) {
            file_io.saveEntityFile("" + getName(), selected);
            entities.remove(selected);
            screen.repaint();
        }
    }

    public void addEntity(Entity entity) {
        entity.setId(idCounter++);
        entities.add(entity);
        screen.repaint();
    }    
}
