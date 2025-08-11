package com.sammc.puppet.application.screen.snapshot;

import java.util.ArrayList;
import java.util.List;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.screen.SnapshotFrame;

import java.awt.image.BufferedImage;

/**
 * Hold all the context information for the current screen state like
 * background, entities and whatnot
 * @author sammc
 */
public class Snapshot {
    
    private SnapshotFrame parent;

    // Session Variables
    public String project_path = ".";
    public BufferedImage background = null;
    public String background_path = "";
    public float zoom = 1;
    public int[] camera_pos = {0, 0};
    public List<Entity> entities = new ArrayList<Entity>();

    /**
     * Default constructor that belongs to a snapshot frame
     * @param parent
     */
    public Snapshot(SnapshotFrame parent) {
        this.parent = parent;
    }

    /**
     * Copy constructor that creates a deep copy.
     * Expensive construction.
     * @param toCopy
     */
    public Snapshot(Snapshot toCopy) {
        parent = toCopy.parent;
        project_path = toCopy.project_path;
        background = Util.deepCopy(toCopy.background);
        background_path = toCopy.background_path;
        this.zoom = toCopy.zoom;
        camera_pos[0] = toCopy.camera_pos[0];
        camera_pos[1] = toCopy.camera_pos[1];

        for (Entity e : toCopy.entities) {
            entities.add(e.clone());
        }
    }

    /**
     * Adds an entity to the snapshot
     */
    public void addEntity(Entity e) {
        entities.add(e);
        parent.refresh();
    }

    /**
     * Clear the current snapshot
     */
    public void clear() {
        project_path = ".";
        background = null;
        background_path = "";
        zoom = 1;
        camera_pos = new int[] {0, 0};
        entities = new ArrayList<Entity>();
    }
 
}
