package com.sammc.puppet.application.scene_edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.scene_edit.panels.ControlsPanel;
import com.sammc.puppet.application.scene_edit.panels.ProjectOverviewPanel;
import com.sammc.puppet.application.screen.FileIOManager;
import com.sammc.puppet.application.screen.Screen;
import com.sammc.puppet.application.screen.SnapshotFrame;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

/**
 * This class is the JFrame that handles the main application activity, scene editing
 * 
 * @author sammc
 */
public class SceneEditFrame extends SnapshotFrame {
    
    // Structural Components
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;
    public FileIOManager file_io = new FileIOManager(this);

    private Snapshot current;
    // For onion skinning
    private Snapshot previous;

    /**
     * Structure
     */
    public SceneEditFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Snappy");
        setTitle("Snappy");
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.WHITE);
        current = new Snapshot(this);
        previous = new Snapshot(this);
        
        // Container for screen and controls
        JPanel container = new JPanel();
        container.setBackground(getBackground());
        container.setLayout(new BorderLayout());

        // Screen panel
        screen = new Screen(this, Screen.DEFAULT_ASPECT_RATIO);
        container.add(screen, BorderLayout.CENTER);

        // Control panel
        controlsPanel = new ControlsPanel(this);
        controlsPanel.setPreferredSize(new Dimension(400,150));
        container.add(controlsPanel, BorderLayout.SOUTH);

        // Add container
        add(container, BorderLayout.CENTER);

        // Project view
        projectPanel = new ProjectOverviewPanel(this);
        add(projectPanel, BorderLayout.WEST);
        refresh();
    }

    public Snapshot getCurrentSnapshot() {
        return current;
    }

    public Snapshot getPreviousSnapshot() {
        return previous;
    }

    public Entity getSelected() {
        return screen.getSelected();
    }

    public String getBackground_path() {
        return current.background_path;
    }

    public void setBackground_path(String background_path) {
        current.background_path = background_path;
    }

    public void deleteSelected() {
        Entity selected = screen.getSelected();
        if (selected != null) {
            file_io.saveEntityFile("" + getName(), selected);
            current.entities.remove(selected);
            controlsPanel.updateList();
            screen.repaint();
        }
    }

    @Override
    public void refresh() {
        if (controlsPanel != null) controlsPanel.updateList();
        if (projectPanel != null) projectPanel.getTreePanel().refresh();
        if (screen != null) screen.repaint();
    }

    public ControlsPanel getControls() {
        return controlsPanel;
    }

    public ProjectOverviewPanel getProjectPanel() {
        return projectPanel;
    }

    public void setBackgroundImage(BufferedImage image, String filepath) {
        current.background = image;
        current.background_path = filepath;
        screen.repaint();
    }

    /**
     * Method that saves the current screen contents to an image file in the output folder.
     */
    public void save_screen() {
        // get the image that is to be saved
        BufferedImage img = screen.getLastRender();

        // Send the current screen state to the onion layer        
        previous = new Snapshot(current);

        // find the next available file number in the output directory
        int f_num = Util.getNextAvailableFormattedFileNumber(current.project_path + "/Output", "o");
        Util.saveImage(img, current.project_path + "/Output/o_" +  f_num + ".png");
        refresh();
    }

    public void clear_session_data() {
        setBackgroundImage(null, "");
        current.entities.clear();
    }

    public String getProjectRootPath() {
        return current.project_path;
    }

    public void setProjectRootPath(String project_path) {
        current.project_path = project_path;
    }

    public boolean hasProjectLoaded() {
        return !current.project_path.equals(".");
    }

    @Override
    public void updateControls(Entity e) {
        controlsPanel.updateFor(e);
    }

}
