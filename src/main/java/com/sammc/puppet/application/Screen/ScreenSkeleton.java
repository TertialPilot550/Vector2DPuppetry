package com.sammc.puppet.application.screen;

import com.sammc.puppet.application.screen.snapshot.Entity;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.List;

/**
 * Compartmentalize the control aspects of the screen so that the Screen.java class is only
 * graphics related. This handles mouse motions and selecting entities.
 * 
 * @author sammc
 */
public class ScreenSkeleton extends AspectRatioPanel implements MouseListener, MouseMotionListener {

    private int[] mouse_pos;
    private Entity selected;
    protected SnapshotFrame parent;

    public ScreenSkeleton(SnapshotFrame parent, double aspect_ratio) {
        super(aspect_ratio);
        this.parent = parent;
        setBackground(Color.DARK_GRAY);
        mouse_pos = new int[] {-1, -1};
        selected = null;
    }

    public Entity getSelected() {
        return selected;
    }

    /**
     * Select an entity at the given coordinates on the screen.
     * 
     * @param x
     * @param y
     */
    private void selectEntityAt(int x, int y) {
        List<Entity> entities = parent.getCurrentSnapshot().entities;
        for (Entity e : entities) {
            // check each entity
            if (e.getBoundingBox().contains(x, y)) {
                selected = e;
                parent.updateControls(e);
                parent.repaint();
                return;
            } 
            // if it doesn't match, check it's children recursively for a match
            else {
                Entity match = e.getChildAt(x, y);
                // if no match found continue
                if (match == null) continue;
                // otherwise select the new match
                selected = match;
                parent.updateControls(match);
                parent.repaint();
                return;
            }
        }
        // also update the control panel for the selected entity. should have a method in controls panel
        selected = null;
        parent.updateControls(null);
        repaint();
    }
    
    /*
     * Mouse - katools
     */

    @Override
    public void mouseDragged(MouseEvent e) {
        boolean was_previous_call = (mouse_pos.length == 2) && (mouse_pos[0] != -1 & mouse_pos[1] != -1);
        if (was_previous_call) {
            handleLaterMouseDrag(e);
        } else {
            mouse_pos[0] = e.getX();
            mouse_pos[1] = e.getY();
            selectEntityAt(e.getX(), e.getY());
        }
    }

    /**
     * Helper method to handle mouse drag events
     * Handle mouse drag events that occur after the initial click
     * @param e
     */
    private void handleLaterMouseDrag(MouseEvent e) {
        if (selected == null) {
            return;
        }
    
        // update mouse position 
        mouse_pos[0] = e.getX();
        mouse_pos[1] = e.getY();

        int new_x = e.getX();
        int new_y = e.getY();

        int scaled_image_width = (int) (selected.getVisualAsset().getWidth()  * selected.getX_scale() * selected.getUni_scale());
        int scaled_image_height = (int) (selected.getVisualAsset().getHeight() * selected.getY_scale() * selected.getUni_scale());

        new_x -= scaled_image_width / 2;
        new_y -= scaled_image_height / 2;
        if (selected.getParent() != null) { 
            new_x -= scaled_image_height * .7;
            new_y -= scaled_image_height * .3;
        }

        selected.setX_offset((int) new_x);
        selected.setY_offset((int) new_y);

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        selectEntityAt(e.getX(), e.getY());
        UIManager.handle_mouse_press(e, parent);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        selectEntityAt(e.getX(), e.getY());
    }


    @Override
    public void mouseReleased(MouseEvent e) {
        mouse_pos[0] = -1;
        mouse_pos[1] = -1;
    }

    // UNUSED
    @Override
    public void mouseEntered(MouseEvent e) {
    }


    @Override
    public void mouseExited(MouseEvent e) {
    }


    @Override
    public void mouseMoved(MouseEvent e) {
    }
    
}
