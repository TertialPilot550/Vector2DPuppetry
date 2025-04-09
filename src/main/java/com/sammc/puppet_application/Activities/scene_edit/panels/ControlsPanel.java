package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.Color;

import javax.swing.JPanel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;

public class ControlsPanel extends JPanel {
    
    private SceneEditFrame parent;

    public ControlsPanel(SceneEditFrame parent) {
        this.parent = parent;
        setBackground(Color.LIGHT_GRAY);
        setVisible(true);
    }



}
