package com.sammc.puppet_application.activities.scene_edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sammc.puppet_application.Util;
import com.sammc.puppet_application.activities.scene_edit.panels.ControlsPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.ProjectOverviewPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.Screen;
import com.sammc.puppet_application.screen_objects.Entity;

public class SceneEditFrame extends JFrame {
    
    private Screen screen;
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;
    private List<Entity> entities = new ArrayList<>();


    private void buildTestEntities() {
        Entity e = new Entity();
        e.getOrientation().translate(5, 5);
        e.getOrientation().scale(5,5);
        BufferedImage img = Util.readImage("./onion.png");
        e.setVisualAsset(img);
        entities.add(e);
    }

    public SceneEditFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Animation Frame");
        setTitle("Animation Frame");
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);

        buildTestEntities();
        
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

    public List<Entity> getEntities() {
        return entities;
    }
}
