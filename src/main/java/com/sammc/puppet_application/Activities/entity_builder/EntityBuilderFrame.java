package com.sammc.puppet_application.activities.entity_builder;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JFrame;

import com.sammc.puppet_application.activities.scene_edit.SnapshotFrame;
import com.sammc.puppet_application.activities.scene_edit.panels.Screen;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Entity;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Snapshot;

public class EntityBuilderFrame extends SnapshotFrame {
    
    private Screen screen;

    public EntityBuilderFrame() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setFocusable(true);
        setName("Animation Frame");
        setTitle("Animation Frame");
        setSize(1200, 800);
        setLayout(new BorderLayout());
        setVisible(true);
        setBackground(Color.GRAY);
        screen = new Screen(this);
        screen.setPreferredSize(new Dimension(500, 500));
        add(screen);
    }



    @Override
    public Snapshot getCurrentSnapshot() {
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentSnapshot'");
    }

    @Override
    public Snapshot getPreviousSnapshot() {
        return null;
    }

    @Override
    public boolean hasProjectLoaded() {
        return true;
    }

    @Override
    public void updateControls(Entity e) {
        
    }

    public static void main(String[] args) {
        EntityBuilderFrame b = new EntityBuilderFrame();
    }

}

