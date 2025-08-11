package com.sammc.puppet.application.scene_edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

import javax.swing.JPanel;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.scene_edit.panels.ControlsPanel;
import com.sammc.puppet.application.scene_edit.panels.ProjectOverviewPanel;
import com.sammc.puppet.application.screen.FileIO;
import com.sammc.puppet.application.screen.Screen;
import com.sammc.puppet.application.screen.SnapshotFrame;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

/**
 * Features that this class takes on
 * 
 * hold the screen, controls, project panel, and have an accessible file_io instance,
 * 
 * keeps track of sessions information using snapshots
 * 
 * master session state changing functions that depend on parts of each component
 */

// could make a session object that takes care of all the session changing data, and then all the child components could just be passed access to the session data
public class SceneEditFrame extends SnapshotFrame {

    private Logger log = Logger.getLogger(SceneEditFrame.class.getName());
    
    // Structural Components
    private Screen screen;
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;
    public FileIO file_io = new FileIO(this);

    // For onion skinning
    private Snapshot current;
    private Snapshot previous;

    public SceneEditFrame() {
        log.info("SceneEditFrame constructor called");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Animation Frame");
        setTitle("Animation Frame");
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);
        current = new Snapshot(this);
        previous = new Snapshot(this);
        
        // Container for screen and controls
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());

        // Screen panel
        screen = new Screen(this);
        screen.setPreferredSize(new Dimension(500, 500));
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

    }

    /*
     * Interacting with entities
     */

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

    public void save_screen() {
        // get the image that is to be saved
        BufferedImage img = screen.getLastRender();

        // Send the current screen state to the onion layer        
        previous = new Snapshot(current);

        // find the next available file number in the output directory
        int f_num = Util.getNextAvailableFormattedFileNumber(current.project_path + "/output", "o");
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
