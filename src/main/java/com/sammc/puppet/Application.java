package com.sammc.puppet;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.scene_edit.SceneEditFrame;

public class Application {

    public static void main(String[] args) {
        // Make sure the projects directory exists
        File projectDir = new File(Util.PROJECTS_DIRECTORY);
        if (!projectDir.exists()) {
            projectDir.mkdirs();
        }

        // Make the actual application
        SceneEditFrame frame = new SceneEditFrame();
    }

    // Make a JPopup menu that allows the user to either 
    // choose a new project, open an existing project, create a new entity, or 
    private static void selectActivity() {
        JDialog popup = new JDialog();

        JLabel title = new JLabel("Select an activity to launch:");
        popup.add(title);

        JMenuItem newProj = new JMenuItem("Create a new project");
        newProj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create a new project");
            }
        });
        popup.add(newProj);

        JMenuItem openProj = new JMenuItem("Open an existing project");
        openProj.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Open an existing project");
            }
        });
        popup.add(openProj);

        JMenuItem buildEntity = new JMenuItem("Create a new entity");
        buildEntity.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Create a new entity");
            }

        });
        popup.add(buildEntity);

        JMenuItem compileScene = new JMenuItem("Compile a scene's image directory into video format.");
        compileScene.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Scene compile");
            }
        });
        popup.add(compileScene);
        popup.setVisible(true);

    }
    


}