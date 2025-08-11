package com.sammc.puppet.application.screen;

import javax.swing.JFrame;

import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;


public abstract class SnapshotFrame extends JFrame {
    
    public abstract Snapshot getCurrentSnapshot();
    public abstract Snapshot getPreviousSnapshot();
    public abstract void updateControls(Entity e);
    public abstract void refresh();
}
