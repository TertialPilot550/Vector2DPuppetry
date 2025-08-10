package com.sammc.puppet.application.scene_edit.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListModel;

public class ListPanel extends JPanel {
    private static final long serialVersionUID = 1L;

    public JList<String> list;

    public ListPanel() {
        super();

        list = new JList<String>() {
            @Override
            public Dimension getPreferredSize() {
                return new Dimension(200, 200);
            }
        };
        ListModel<String> model = new DefaultListModel<String>();
        list.setModel(model);

        list.setVisible(true);
        list.setSize(200, 200);
        list.setBackground(Color.LIGHT_GRAY);
        list.setForeground(Color.BLACK);
        add(list);
    }

    public List<String> getListData() {
        ListModel<String> model = list.getModel();
        List<String> data = new ArrayList<String>();
        for (int i = 0; i < model.getSize(); i++) {
            data.add(model.getElementAt(i));
        }
        return data;
    }

    public void updateListData(String[] newData) {
        DefaultListModel<String> model = (DefaultListModel<String>) list.getModel();
        model.clear();
        for (String data : newData) {
            model.addElement(data);
        }
    }

    public void updateListData(ArrayList<String> newData) {
        String[] dataArray = new String[newData.size()];
        newData.toArray(dataArray);
        updateListData(dataArray);
    }
    
}
