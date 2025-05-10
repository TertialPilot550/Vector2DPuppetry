package com.sammc.puppet_application.activities.scene_edit;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.sammc.puppet_application.Util;
import com.sammc.puppet_application.activities.scene_edit.panels.ControlsPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.ProjectOverviewPanel;
import com.sammc.puppet_application.activities.scene_edit.panels.Screen;
import com.sammc.puppet_application.screen_objects.Entity;

public class SceneEditFrame extends JFrame {
    
    private Screen screen;
    private ProjectOverviewPanel projectPanel;
    private ControlsPanel controlsPanel;
    private List<Entity> entities;
    private int idCounter = 1000;

    private void buildTestEntities() {
        try {
            loadEntityFile("./test_entity.e");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public SceneEditFrame() {
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

        buildTestEntities();

    }

    public List<Entity> getEntities() {
        return entities;
    }

    public Entity getSelected() {
        return screen.getSelected();
    }

    public void loadEntityFile(String filepath) throws FileNotFoundException {
        File selectedFile = new File(filepath);
        FileInputStream fileInputStream = new FileInputStream(selectedFile);
        Scanner scan = new Scanner(fileInputStream);
        Entity entity = new Entity();
        while (scan.hasNextLine()) {
            String[] fields = scan.nextLine().split(" ");
            switch (fields[0]) {
                case "transform" -> {
                    AffineTransform transform = new AffineTransform(Double.parseDouble(fields[1]), Double.parseDouble(fields[2]), Double.parseDouble(fields[3]), Double.parseDouble(fields[4]), Double.parseDouble(fields[5]), Double.parseDouble(fields[6]));
                    entity.setOrientation(transform);
                }
                case "image" -> {
                    String path = fields[1];
                    BufferedImage img = Util.readImage(path);
                    entity.setVisualAsset(img);
                }
                case "animation" -> {
                }
                case "connection" -> {
                }
            }
        }
        scan.close();
        entity.setId(idCounter++);
        entities.add(entity);
        screen.repaint();
    }
}
