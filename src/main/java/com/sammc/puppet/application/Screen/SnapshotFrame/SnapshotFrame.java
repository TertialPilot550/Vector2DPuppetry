package com.sammc.puppet.application.Screen.SnapshotFrame;

import javax.swing.JFrame;


public abstract class SnapshotFrame extends JFrame {
    
    public abstract Snapshot getCurrentSnapshot();
    public abstract Snapshot getPreviousSnapshot();
    public abstract boolean hasProjectLoaded();
    public abstract void updateControls(Entity e);
    public abstract void refresh();
}
