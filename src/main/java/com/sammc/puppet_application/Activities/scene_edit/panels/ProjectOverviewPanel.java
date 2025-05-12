package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.ScrollPane;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;


public class ProjectOverviewPanel extends JPanel {

    public static final String FILE_ROOT_PATH = "./proj";

    private SceneEditFrame parent;
    private DefaultMutableTreeNode root;
    private DefaultTreeModel model;
    private JTree tree;

    public ProjectOverviewPanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(200, 800));
        
        // Set up control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(200, 400));
        add(controlPanel, BorderLayout.NORTH);

        // Set up the scroll pane
        ScrollPane scroll = new ScrollPane();
        scroll.setBounds(0, 0, 200, 1600);
        add(scroll, BorderLayout.CENTER);

        // add the file root to the tree
        File file_root = new File(FILE_ROOT_PATH);
        root = new DefaultMutableTreeNode(file_root);
        model = new DefaultTreeModel(root);
        tree = new JTree(model);
        tree.setShowsRootHandles(true);
        createChildren(file_root, root);
        scroll.add(tree);

        // Create the control panel for the project overview
        JButton addSelectedEntityFileButton = new JButton("Load Entity File");
        addSelectedEntityFileButton.setPreferredSize(new Dimension(200, 20));
        addSelectedEntityFileButton.addActionListener(e -> {
            // Load the selected entity file
            if (tree.getSelectionPath() == null) {
                return;
            }

            String selected_file_path = tree.getSelectionPath().toString();
            selected_file_path = selected_file_path.substring(1, selected_file_path.length() - 1);
            selected_file_path = selected_file_path.replace(", ", "/");
            selected_file_path = selected_file_path.replace(" ", "");
            // Now the file path is correctly formatted
            try {
                parent.file_io.loadEntityFile(selected_file_path);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(addSelectedEntityFileButton);

        JPanel filepathEntryPanel = new JPanel();
        filepathEntryPanel.setPreferredSize(new Dimension(200, 26));
        controlPanel.add(filepathEntryPanel);

        JTextField fileRootField = new JTextField(FILE_ROOT_PATH);
        filepathEntryPanel.add(fileRootField);
        fileRootField.setPreferredSize(new Dimension(150, 20));

        JButton refreshButton = new JButton("R");
        refreshButton.setPreferredSize(new Dimension(20, 20));
        refreshButton.addActionListener(e -> {
            // Refresh the tree
            root.removeAllChildren();
            createChildren(new File(fileRootField.getText()), root);
            model.reload();
        });
        filepathEntryPanel.add(refreshButton);

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