package com.sammc.puppet_application.activities.scene_edit.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.sammc.puppet_application.activities.Util;
import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Entity;

public class ProjectOverviewPanel extends JPanel {

    private SceneEditFrame parent;
    private TreePanel treePanel;

    public ProjectOverviewPanel(SceneEditFrame parent) {
        this.parent = parent;
        setVisible(true);
        setBackground(Color.GRAY);
        setPreferredSize(new Dimension(300, 1000));
        
        // Set up control panel
        JPanel controlPanel = new JPanel();
        controlPanel.setPreferredSize(new Dimension(300, 400));
        add(controlPanel, BorderLayout.NORTH);

        // Set up and build tree panel
        treePanel = new TreePanel(parent);
        add(treePanel);

        /*
         * Build Control Panel
         */

        JLabel label0 = new JLabel("Project Overview", JLabel.CENTER);
        label0.setPreferredSize(new Dimension(200, 20));
        label0.setFont(new Font("Arial", Font.BOLD, 16));
        controlPanel.add(label0);

        JButton newProject = new JButton("New Project");
        newProject.setPreferredSize(new Dimension(200, 20));
        newProject.addActionListener(e -> {
            // Create a new project
            new_project();
        });
        controlPanel.add(newProject);

        JButton openProject = new JButton("Open Project");
        openProject.setPreferredSize(new Dimension(200, 20));
        openProject.addActionListener(e -> {
            open_project();
        });
        controlPanel.add(openProject);

        JLabel label = new JLabel("Session Control", JLabel.CENTER);
        label.setPreferredSize(new Dimension(200, 20));
        controlPanel.add(label);

        JButton loadSession = new JButton("Load Session");
        loadSession.setPreferredSize(new Dimension(200, 20));
        loadSession.addActionListener(e -> {
            // Load the selected session file
            try {
                String selected_file_path = treePanel.getSelectedPath();
                if (selected_file_path == null) return;
                load_session(selected_file_path);
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
                save_session();
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
                save_screen();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(saveScreen);

        // Create the control panel for the project overview
        JButton loadEntity = new JButton("Load Entity File");
        loadEntity.setPreferredSize(new Dimension(200, 20));
        loadEntity.addActionListener(e -> {
            String selected_file_path = treePanel.getSelectedPath();
            if (selected_file_path == null) return;
            // Now the file path is correctly formatted
            try {
                if (parent.hasProjectLoaded() == false) return;
                parent.file_io.loadEntityFile(selected_file_path);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(loadEntity);

        JButton loadBackground = new JButton("Load Background");
        loadBackground.setPreferredSize(new Dimension(200, 20));
        loadBackground.addActionListener(e -> {
            String selected_file_path = treePanel.getSelectedPath();
            if (selected_file_path == null) return;
            // Now the file path is correctly formatted
            try {
                if (parent.hasProjectLoaded() == false) return;
                BufferedImage image = Util.readImage(selected_file_path);
                System.out.println(selected_file_path);
                parent.setBackgroundImage(image, selected_file_path);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        controlPanel.add(loadBackground);

        JButton clearBackground = new JButton("Clear Background");
        clearBackground.setPreferredSize(new Dimension(200, 20));
        clearBackground.addActionListener(e -> {
            parent.setBackgroundImage(null, "");
        });
        controlPanel.add(clearBackground);

        JButton quickEntity = new JButton("Quick Entity");
        quickEntity.setPreferredSize(new Dimension(200, 20));
        quickEntity.addActionListener(e -> {
            String selected_file_path = treePanel.getSelectedPath();
            if (selected_file_path == null) return;
            quick_entity(selected_file_path);
        });
        controlPanel.add(quickEntity);

        JButton deleteSelected = new JButton("Delete File");
        deleteSelected.setPreferredSize(new Dimension(200, 20));
        deleteSelected.addActionListener(e -> {
            if (!parent.hasProjectLoaded()) return;
            String selected_file_path = treePanel.getSelectedPath();
            if (selected_file_path == null) return;
            File selected = new File(selected_file_path);
            if (selected.exists() && !selected.isDirectory()) {
                selected.delete();
                treePanel.refresh();
            } 
        });
        controlPanel.add(deleteSelected);
    }

    // get the project directory path
    private void quick_entity(String image_path) {
        if (parent.hasProjectLoaded() == false) return;
        BufferedImage image = Util.readImage(image_path);
        if (image == null) return;
        Entity entity = new Entity();
        String entity_dir_path = parent.getProjectRootPath() + "/EntityLibrary";
        int f_num = Util.getNextAvailableFormattedFileNumber(entity_dir_path, "entity");
        if (f_num == -1) {
            System.err.println("Error: Could not find next available file number.");
            return;
        }

        entity.setEntityFilePath(entity_dir_path + "/entity_" + f_num + ".e");
        entity.setVisualAsset(image, image_path);
        entity.setUni_scale(0.25);
        parent.getCurrentSnapshot().addEntity(entity);
    }

    private void save_screen() {
        if (parent.hasProjectLoaded() == false) return;
        parent.save_screen();
        
    }

    private void save_session() {
        if (parent.hasProjectLoaded() == false) return;
        String sessions_path = parent.getProjectRootPath() + "/Sessions";
        parent.file_io.saveSessionFile(sessions_path + "/session_" + Util.getNextAvailableFormattedFileNumber(sessions_path, "session") + ".s");
        for (Entity e : parent.getCurrentSnapshot().entities) {
            parent.file_io.saveEntityFile(e.getEntityFilePath(), e);
        }
    }

    private void load_session(String selected_file_path) {
        if (parent.hasProjectLoaded() == false) return;
        try {
            parent.file_io.loadSessionFile(selected_file_path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void open_project() {
        File selected_file = null;
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Project Directory");
        fileChooser.setCurrentDirectory(new File("./Projects"));
        fileChooser.setLocation(200,200);
        // Set the file chooser to only allow directories

        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnValue = fileChooser.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            selected_file = fileChooser.getSelectedFile();
        }
        if (selected_file == null) return;
        set_current_project_path("./Projects/" + selected_file.getName());
    }

    private void new_project() {
        String name = JOptionPane.showInputDialog("Enter project name: ");
        Util.buildProjectDirectory(name);
        set_current_project_path(Util.PROJECTS_DIRECTORY + "/" + name);
    }

    private void set_current_project_path(String project_path) {    
        // cleanup
        parent.clear_session_data();
        parent.getControls().updateList();
        parent.repaint();

        // build 
        Util.buildProjectDirectory(project_path);
        parent.setProjectRootPath(project_path);
        treePanel.refresh();
    }

    public TreePanel getTreePanel() {
        return treePanel;
    }

}