package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.ScrollPane;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
/*
 * buttons:

import a file
quick make a simple entity
 */

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
        controlPanel.setPreferredSize(new Dimension(200, 300));
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




        /*
         * Buttons
         */

        JLabel label0 = new JLabel("Project Overview", JLabel.CENTER);
        label0.setPreferredSize(new Dimension(200, 20));
        label0.setFont(new Font("Arial", Font.BOLD, 16));
        controlPanel.add(label0);

        JButton newProject = new JButton("New Project");
        newProject.setPreferredSize(new Dimension(200, 20));
        newProject.addActionListener(e -> {
            // Create a new project
            // TODO!! FIX THIS
        });
        controlPanel.add(newProject);

        JButton openProject = new JButton("Open Project");
        openProject.setPreferredSize(new Dimension(200, 20));
        openProject.addActionListener(e -> {
            // Open the selected project
            if (tree.getSelectionPath() == null) {
                return;
            }

            String selected_file_path = tree.getSelectionPath().toString();
            selected_file_path = selected_file_path.substring(1, selected_file_path.length() - 1);
            selected_file_path = selected_file_path.replace(", ", "/");
            selected_file_path = selected_file_path.replace(" ", "");
            // Now the file path is correctly formatted
            try {
                // TODO!! FIX THIS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(openProject);

        JLabel label = new JLabel("Session Control", JLabel.CENTER);
        label.setPreferredSize(new Dimension(200, 20));
        controlPanel.add(label);

        JButton loadSession = new JButton("Load Session");
        loadSession.setPreferredSize(new Dimension(200, 20));
        loadSession.addActionListener(e -> {
            // Load the selected session file
            if (tree.getSelectionPath() == null) {
                return;
            }

            String selected_file_path = tree.getSelectionPath().toString();
            selected_file_path = selected_file_path.substring(1, selected_file_path.length() - 1);
            selected_file_path = selected_file_path.replace(", ", "/");
            selected_file_path = selected_file_path.replace(" ", "");
            // Now the file path is correctly formatted
            try {
                // TODO!! FIX THIS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(loadSession);

        JButton saveSession = new JButton("Save Session");
        saveSession.setPreferredSize(new Dimension(200, 20));
        saveSession.addActionListener(e -> {
            // Save the current session
            try {
                // TODO!! FIX THIS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(saveSession);

        // Manipulate the scene
        JLabel label2 = new JLabel("Scene Control", JLabel.CENTER);
        label2.setPreferredSize(new Dimension(200, 20));
        controlPanel.add(label2);

        JButton saveScreen = new JButton("Save Screen");
        saveScreen.setPreferredSize(new Dimension(200, 20));
        saveScreen.addActionListener(e -> {
            // Save the current screen
            try {
                // TODO!! FIX THIS
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(saveScreen);

        // Create the control panel for the project overview
        JButton loadEntity = new JButton("Load Entity File");
        loadEntity.setPreferredSize(new Dimension(200, 20));
        loadEntity.addActionListener(e -> {
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
        controlPanel.add(loadEntity);

        JButton loadBackground = new JButton("Load Background");
        loadBackground.setPreferredSize(new Dimension(200, 20));
        loadBackground.addActionListener(e -> {
            // Load the selected background file
            if (tree.getSelectionPath() == null) {
                return;
            }

            String selected_file_path = tree.getSelectionPath().toString();
            selected_file_path = selected_file_path.substring(1, selected_file_path.length() - 1);
            selected_file_path = selected_file_path.replace(", ", "/");
            selected_file_path = selected_file_path.replace(" ", "");
            // Now the file path is correctly formatted
            try {
                // TODO!! Set the background image to the selected file
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(loadBackground);

        JButton quickEntity = new JButton("Quick Entity");
        quickEntity.setPreferredSize(new Dimension(200, 20));
        quickEntity.addActionListener(e -> {
            // Create a new entity with default values
            // TODO!!
        });
        controlPanel.add(quickEntity);

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