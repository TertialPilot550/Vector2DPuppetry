package com.sammc.puppet_application.activities.scene_edit;

import javax.swing.JFrame;

import com.sammc.puppet_application.activities.scene_edit.screen_objects.Entity;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Snapshot;

public abstract class SnapshotFrame extends JFrame {
    
    public abstract Snapshot getCurrentSnapshot();
    public abstract Snapshot getPreviousSnapshot();
    public abstract boolean hasProjectLoaded();
    public abstract void updateControls(Entity e);
}
