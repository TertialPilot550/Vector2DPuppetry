package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;

/*
 * TODO update this to take a selected entities values WHEN it is selected.
 */

public class ControlsPanel extends JPanel {
    
    private SceneEditFrame parent;

    public ControlsPanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);

        




        /*
         * Selected Entity Controls
         */

        AttributeAdjuster u_scale = new AttributeAdjuster("uni_scale", 100, -100, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setUni_scale(getValue());
                parent.repaint();
            }
        };
        AttributeAdjuster x_scale = new AttributeAdjuster("x_scale", 100, -100, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setX_scale(getValue());
                parent.repaint();
            }
        };
        AttributeAdjuster y_scale = new AttributeAdjuster("y_scale", 100, -100, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setY_scale(getValue());
                parent.repaint();
            }
        };
        AttributeAdjuster x_shear = new AttributeAdjuster("x_shear", 200, -200, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setX_shear(getValue());
                parent.repaint();
            }
        };
        AttributeAdjuster y_shear = new AttributeAdjuster("y_shear", 200, -200, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setY_shear(getValue());
                parent.repaint();
            }
        };
        AttributeAdjuster rotation = new AttributeAdjuster("rotation", 360, -360, 1) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setRotation(getValue());
                parent.repaint();
            }
        };
        AttributeAdjuster depth = new AttributeAdjuster("depth", 100, -100, 100) {
            @Override
            public void setValue() {
                //parent.getSelected().setDepth(getValue()); TODO!!
                parent.repaint();
            }
        };

        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 80);
            }
        };
        JLabel image_path_label = new JLabel("Image Path");
        JLabel visual_asset_path_label = new JLabel("Visual Asset Path");
        JTextField image_path_field = new JTextField(10);
        JTextField entity_path_field = new JTextField(10);
        panel.add(image_path_label);
        panel.add(image_path_field);
        panel.add(visual_asset_path_label);
        panel.add(entity_path_field);

        JPanel selected_control = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1200, 220);
            }
        };
        selected_control.add(u_scale);
        selected_control.add(x_scale);
        selected_control.add(y_scale);
        selected_control.add(x_shear);
        selected_control.add(y_shear);
        selected_control.add(rotation);
        selected_control.add(depth);
        selected_control.add(panel);


        add(selected_control);

        JPanel entity_overview_panel = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(250, 220);
            }
        };
        entity_overview_panel.setBackground(Color.BLACK);


        /*
         * TODO: add the rest of the entity overview panel
         */



        add(entity_overview_panel);
    }
   
    @Override
    public Dimension getPreferredSize() {
        return new java.awt.Dimension(200, 230);
    }
}