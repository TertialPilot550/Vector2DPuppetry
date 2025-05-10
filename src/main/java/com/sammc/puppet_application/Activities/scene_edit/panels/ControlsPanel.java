package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.Color;
import java.awt.geom.AffineTransform;

import javax.swing.JPanel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
import com.sammc.puppet_application.screen_objects.Entity;

/*
 * maybe i could have this have a spot for x scale, y scale, collective scale, and rotation, and just set the values of the 
 * selected entity to the right multiplications of values
 * and flip checkboxes
 * 
 * maybe just let you view the transform matrix and edit it directly, 
 * or set a completely new one
 */

public class ControlsPanel extends JPanel {
    
    private SceneEditFrame parent;

    public ControlsPanel(SceneEditFrame parent) {
        this.parent = parent;
        setBackground(Color.LIGHT_GRAY);
        setVisible(true);
    }
   
}
