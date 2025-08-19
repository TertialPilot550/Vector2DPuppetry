package com.sammc.puppet.application.scene_edit.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.util.List;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import com.sammc.puppet.application.scene_edit.SceneEditFrame;
import com.sammc.puppet.application.screen.snapshot.Entity;



public class ControlsPanel extends JPanel {
    
    private SceneEditFrame parent;
    private AttributeAdjuster u_scale;
    private AttributeAdjuster x_scale;
    private AttributeAdjuster y_scale;
    private AttributeAdjuster x_shear;
    private AttributeAdjuster y_shear;
    private AttributeAdjuster rotation;
    private AttributeAdjuster depth;
    private JTextField image_path_field;
    private JTextField entity_path_field;
    
    private ListPanel list_panel;

    public ControlsPanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);
        setBackground(Color.WHITE);
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        
        /*
         * Selected Entity Controls
         */

        u_scale = new AttributeAdjuster("uni_scale", 100, -100, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setUni_scale(getValue());
                parent.repaint();
            }
        };
        x_scale = new AttributeAdjuster("x_scale", 100, -100, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setX_scale(getValue());
                parent.repaint();
            }
        };
        y_scale = new AttributeAdjuster("y_scale", 100, -100, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setY_scale(getValue());
                parent.repaint();
            }
        };
        x_shear = new AttributeAdjuster("x_shear", 200, -200, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setX_shear(getValue());
                parent.repaint();
            }
        };
        y_shear = new AttributeAdjuster("y_shear", 200, -200, 100) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setY_shear(getValue());
                parent.repaint();
            }
        };
        rotation = new AttributeAdjuster("rotation", 360, -360, 1) {
            @Override
            public void setValue() {
                if (parent.getSelected() != null) parent.getSelected().setRotation(getValue());
                parent.repaint();
            }
        };
        depth = new AttributeAdjuster("depth", 100, -100, 100) {
            @Override
            public void setValue() {
                parent.getSelected().setDepth(getValue());
                parent.repaint();
            }
        };

        JPanel panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 80);
            }
        };
        panel.setBackground(Color.white);
        JLabel image_path_label = new JLabel("Image Path");
        JLabel visual_asset_path_label = new JLabel("         Entity Path");
        image_path_field = new JTextField(10);
        entity_path_field = new JTextField(10);
        panel.add(image_path_label);
        panel.add(image_path_field);
        panel.add(visual_asset_path_label);
        panel.add(entity_path_field);


        JPanel save_delete_panel = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(300, 80);
            }
        };
        save_delete_panel.setBackground(Color.WHITE);
        JButton save_button = new JButton("Save Entity");
        save_button.addActionListener(e -> {
            if (parent.hasProjectLoaded() == false) return;
            parent.file_io.saveEntityFile(entity_path_field.getText(), parent.getSelected());
        });
        JButton save_as_button = new JButton("Save Copy");
        save_as_button.addActionListener(e -> {
            if (parent.hasProjectLoaded() == false) return;
            parent.file_io.saveEntityFile(entity_path_field.getText() + ".copy", parent.getSelected());
        });
        JButton delete_button = new JButton("Delete Entity");
        delete_button.addActionListener(e -> {
            if (parent.hasProjectLoaded() == false) return;
            parent.deleteSelected();
        });
        save_delete_panel.add(save_button);
        save_delete_panel.add(save_as_button);
        save_delete_panel.add(delete_button);

        JPanel selected_control = new JPanel() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(1000, 200);
            }
        };
        selected_control.setBackground(Color.WHITE);
        selected_control.add(u_scale);
        selected_control.add(x_scale);
        selected_control.add(y_scale);
        selected_control.add(x_shear);
        selected_control.add(y_shear);
        selected_control.add(rotation);
        selected_control.add(depth);
        selected_control.add(panel);
        selected_control.add(save_delete_panel);

        add(selected_control);

        

        list_panel = new ListPanel();
        add(list_panel);
        updateList();

    }
   
    @Override
    public Dimension getPreferredSize() {
        return new java.awt.Dimension(200, 230);
    }
    
    public void updateList() {
        List<Entity> entities = parent.getCurrentSnapshot().entities;
        Entity[] l = new Entity[entities.size()];
        String[] names = new String[entities.size()];
        for (int i = 0; i < entities.size(); i++) {
            l[i] = entities.get(i);
            names[i] = l[i].getName();
        }
        list_panel.updateListData(names);
    }

    public void updateFor(Entity e) {
        if (e == null) {
            updateList();
            u_scale.assertValues(0);
            x_scale.assertValues(0);
            y_scale.assertValues(0);
            x_shear.assertValues(0);
            y_shear.assertValues(0);
            rotation.assertValues(0);
            depth.assertValues(0);
            entity_path_field.setText("");
            image_path_field.setText("");
            return;
        }

        List<String> list_data = list_panel.getListData();
        for (int i = 0; i < list_data.size(); i++) {
            if (list_data.get(i).equals(e.getName())) {
                list_panel.list.setSelectedIndex(i);
                break;
            }
        }

        entity_path_field.setText(e.getEntityFileName());
        image_path_field.setText(e.getVisualAssetName());
        u_scale.assertValues(e.getUni_scale());
        x_scale.assertValues(e.getX_scale());
        y_scale.assertValues(e.getY_scale());
        x_shear.assertValues(e.getX_shear());
        y_shear.assertValues(e.getY_shear());
        rotation.assertValues(e.getRotation());
        depth.assertValues(e.getDepth());
    }
}