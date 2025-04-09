package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.io.File;

import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;


public class ProjectOverviewPanel extends JPanel {

    public static final String FILE_ROOT_PATH = "./";

    private SceneEditFrame parent;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private JTree tree;

    public ProjectOverviewPanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(200, 800));
        
        ScrollPane scroll = new ScrollPane();
        scroll.setLocation(0,0);
        scroll.setPreferredSize(getPreferredSize());
        add(scroll);

        File file_root = new File(FILE_ROOT_PATH);
        root = new DefaultMutableTreeNode(file_root);
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        tree.setShowsRootHandles(true);
        createChildren(file_root, root);
        scroll.add(tree);
    }

    private void createChildren(File fileRoot, DefaultMutableTreeNode node) {
        File[] files = fileRoot.listFiles();
        if (files == null) return;

        for (File file : files) {
            DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(new FileNode(file));
            node.add(childNode);
            if (file.isDirectory()) {
                createChildren(file, childNode);
            }
        }
    }

    public class FileNode {

        private File file;

        public FileNode(File file) {
            this.file = file;
        }

        @Override
        public String toString() {
            String name = file.getName();
            if (name.equals("")) {
                return file.getAbsolutePath();
            } else {
                return name;
            }
        }
    }


}