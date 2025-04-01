package com.sammc.puppet_application.activities.animation_frame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.List;

import javax.swing.JFrame;

import com.sammc.puppet_application.activities.animation_frame.panels.ControlsPanel;
import com.sammc.puppet_application.activities.animation_frame.panels.ProjectOverviewPanel;
import com.sammc.puppet_application.activities.animation_frame.panels.Screen;
import com.sammc.puppet_application.screen_objects.Component;
import com.sammc.puppet_application.screen_objects.Entity;

public class AnimationFrame extends JFrame {
    
    private Screen screen;
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;

    List<Entity> entities;
    List<Component> components;

    public AnimationFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Animation Frame");
        setTitle("Animation Frame");
        setSize(800, 500);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);


        screen = new Screen(this);
        add(screen, BorderLayout.CENTER);

        projectPanel = new ProjectOverviewPanel(this);
        add(projectPanel, BorderLayout.WEST);

        controlsPanel = new ControlsPanel(this);
        add(controlsPanel, BorderLayout.SOUTH);


    }

    public List<Entity> getEntities() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getEntities'");
    }

    public List<Component> getVisualComponents() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getVisualComponents'");
    }
    


}
