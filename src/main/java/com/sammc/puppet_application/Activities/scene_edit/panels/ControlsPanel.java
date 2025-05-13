package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
import com.sammc.puppet_application.screen_objects.Entity;


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
    
    private JList<String> list;

    public ControlsPanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);

        
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
        JButton save_button = new JButton("Save Entity");
        save_button.addActionListener(e -> {
            parent.file_io.saveEntityFile(entity_path_field.getText(), parent.getSelected());
        });
        JButton save_as_button = new JButton("Save Copy");
        save_as_button.addActionListener(e -> {
            parent.file_io.saveEntityFile(entity_path_field.getText() + ".copy", parent.getSelected());
        });
        JButton delete_button = new JButton("Delete Entity");
        delete_button.addActionListener(e -> {
            parent.deleteSelected();
        });
        save_delete_panel.add(save_button);
        save_delete_panel.add(save_as_button);
        save_delete_panel.add(delete_button);

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
        selected_control.add(save_delete_panel);

        add(selected_control);

        JPanel entity_overview_panel = new JPanel(){
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(250, 220);
            }
        };
        entity_overview_panel.setBackground(Color.BLACK);

        list = new JList<String>(); 
        updateList();
        System.out.println("List: " + list.getModel().getSize());
        System.out.println("List: " + list.getModel().getElementAt(0));

        JScrollPane listScroller = new JScrollPane(list);
        listScroller.setPreferredSize(new Dimension(250, 80));
        listScroller.add(list);
        entity_overview_panel.add(listScroller);
    
        add(entity_overview_panel);
    }
   
    @Override
    public Dimension getPreferredSize() {
        return new java.awt.Dimension(200, 230);
    }

    private void updateList() {
        System.out.println("Updating list");
        Entity[] l = new Entity[parent.getEntities().size()];
        parent.getEntities().toArray(l);
        list.setListData(new String[] {"p", "q", "r"});
        repaint();
    }

    public void updateFor(Entity e) {
        updateList();
        if (e == null) {
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
        entity_path_field.setText(e.getEntityFilePath());
        image_path_field.setText(e.getVisualAssetPath());
        u_scale.assertValues(e.getUni_scale());
        x_scale.assertValues(e.getX_scale());
        y_scale.assertValues(e.getY_scale());
        x_shear.assertValues(e.getX_shear());
        y_shear.assertValues(e.getY_shear());
        rotation.assertValues(e.getRotation());
        depth.assertValues(e.getDepth());
    }
}