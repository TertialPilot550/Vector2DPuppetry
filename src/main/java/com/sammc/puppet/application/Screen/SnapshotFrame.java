package com.sammc.puppet.application.screen;


import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.JFrame;

import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

/**
 * Template for a JFrame that contains a screen panel
 * @author sammc
 */
public abstract class SnapshotFrame extends JFrame {

    protected Screen screen;

    public SnapshotFrame() {
        // Component listener to enforce aspect ratio for the screen
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (screen == null) return;
                screen.revalidate(); // Revalidate the panel to apply new preferred size
                screen.repaint();    // Repaint to ensure visual update
            }
        });
    }

    public abstract Snapshot getCurrentSnapshot();
    public abstract Snapshot getPreviousSnapshot();
    public abstract void updateControls(Entity e);
    public abstract void refresh();
}
