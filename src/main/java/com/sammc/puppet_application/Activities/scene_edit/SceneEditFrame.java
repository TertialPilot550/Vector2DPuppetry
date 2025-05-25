package com.sammc.puppet_application.activities.scene_edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sammc.puppet_application.Util;
import com.sammc.puppet_application.activities.scene_edit.panels.ControlsPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.ProjectOverviewPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.Screen;
import com.sammc.puppet_application.screen_objects.Entity;
// Maybe the background image, as well as the path to the background image, as well as the project directory should be moved to the parent object to make access cleaner.

public class SceneEditFrame extends JFrame {

    private Logger log = Logger.getLogger(SceneEditFrame.class.getName());
    
    // Structural Components
    private Screen screen;
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;
    public SceneEditFileIO file_io = new SceneEditFileIO(this);

    // Session Variables
    private String project_path = ".";
    private BufferedImage background = null;
    private String background_path = "";
    private List<Entity> entities;
    private int idCounter = 1000;

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
        entities = new ArrayList<>();
        
        // Internal Container for the screen and control panel
        JPanel container = new JPanel();
        container.setLayout(new BorderLayout());
        screen = new Screen(this);
        screen.setPreferredSize(new Dimension(500, 500));
        container.add(screen, BorderLayout.CENTER);

        controlsPanel = new ControlsPanel(this);
        controlsPanel.setPreferredSize(new Dimension(400,150));
        container.add(controlsPanel, BorderLayout.SOUTH);
        add(container, BorderLayout.CENTER);

        // project view
        projectPanel = new ProjectOverviewPanel(this);
        add(projectPanel, BorderLayout.WEST);

    }

    /*
     * Interacting with entities
     */

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getSelected() {
        return screen.getSelected();
    }

    

    public BufferedImage getBackgroundImage() {
        return background;
    }

    public String getBackground_path() {
        return background_path;
    }

    public void setBackground_path(String background_path) {
        this.background_path = background_path;
    }

    public void deleteSelected() {
        Entity selected = screen.getSelected();
        if (selected != null) {
            file_io.saveEntityFile("" + getName(), selected);
            entities.remove(selected);
            controlsPanel.updateList();
            screen.repaint();
        }
    }

    public void addEntity(Entity entity) {
        entity.setId(idCounter++);
        entities.add(entity);
        controlsPanel.updateList();
        screen.repaint();
    }

    public ControlsPanel getControls() {
        return controlsPanel;
    }

    public ProjectOverviewPanel getProjectPanel() {
        return projectPanel;
    }

    public void setBackgroundImage(BufferedImage image, String filepath) {
        this.background = image;
        this.background_path = filepath;
        screen.repaint();
    }

    public void save_screen() {
        BufferedImage img = screen.getLastRender();

        // find the next available file number in the output directory
        int f_num = Util.getNextAvailableFormattedFileNumber(project_path + "/output", "o");

        Util.saveImage(img, project_path + "/o_" +  f_num + ".png");
    }

    public void clear_session_data() {
        setBackgroundImage(null, "");
        entities.clear();
        idCounter = 1000;
    }

    public String getProjectRootPath() {
        return project_path;
    }

    public void setProjectRootPath(String project_path) {
        this.project_path = project_path;
    }
}
