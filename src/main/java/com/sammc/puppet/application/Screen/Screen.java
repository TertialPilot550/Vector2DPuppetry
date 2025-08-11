package com.sammc.puppet.application.screen;

import javax.swing.JPanel;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;


/**
 * TO DO :
 * change aspect ratio of the screen to always be 1.85x1
 * /


/**
 * JPanel that displays the current state of all objects held in the current scene,
 * based on their associated visual assets and affine transforms. Mouse Handling
 * is also handled here, including selecting and moving entities.
 * @param parent
 * @author sammc
 */

public class Screen extends JPanel implements MouseListener, MouseMotionListener {
    
    private SnapshotFrame parent;
    private int[] mouse_pos;
    private Entity selected;
    private BufferedImage last_render = null;

    /**
     * Constructor for the screen
     * @param parent
     */
    public Screen(SnapshotFrame parent) {
        this.parent = parent;
        mouse_pos = new int[] {-1, -1};
        selected = null;
        setVisible(true);
        setBackground(Color.CYAN);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        parent.refresh();
    }
    
    /**
     * Main behavior, draw all foreground and background objects
     */
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g; 

        // Create the view that the direct output (screenshot) of a saved view
        BufferedImage program_buffer    = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage user_buffer       = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Render program information to both buffers
        render_to_views(program_buffer.createGraphics(), user_buffer.createGraphics());
        
        // Use the buffers
        g2.drawImage(user_buffer, 0, 0, this);          // Draw the final buffer to the screen
        last_render = program_buffer;                       // Keep the final program version saved away

        boolean project_loaded = !parent.getCurrentSnapshot().project_path.equals("");
        UIManager.paintUI(g2, project_loaded);
        g2.dispose();  
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

    /**
     * Render both types of views, the program view and the user view
     * 
     * @param render_surface
     * @param entities
     */
    private void render_to_views(Graphics2D g_program_view, Graphics2D g_user_view) {
        Snapshot curr = parent.getCurrentSnapshot();
        Snapshot prev = parent.getPreviousSnapshot();
        // must have a valid snapshot object
        if (curr == null) return;
        
        if (curr.background != null) {
            g_program_view.drawImage(curr.background, -curr.camera_pos[0], -curr.camera_pos[1], (int) (getWidth() * curr.zoom), (int) (getHeight() * curr.zoom), this);
            g_user_view.drawImage(curr.background, -curr.camera_pos[0], -curr.camera_pos[1], (int) (getWidth() * curr.zoom), (int) (getHeight() * curr.zoom), this);
        }

        if (getSelected() != null) {
            g_user_view.setColor(Color.CYAN);
            g_user_view.draw(getSelected().getBoundingBox());
        }

        // Draw entities from current snapshot to both the user and program view.
        for (Entity e : curr.entities) {
            Util.render_entity(g_program_view, e, new AffineTransform(), false);
            Util.render_entity(g_user_view, e, new AffineTransform(), false);
        }
        // Render entites from previous snapshot as ghosts on only the user view
        if (prev == null) return;
        for (Entity e : prev.entities) {
            Util.render_entity(g_user_view, e, new AffineTransform(), true);
        }
    }

    public Entity getSelected() {
        return selected;
    }

    public BufferedImage getLastRender() {
        return last_render;
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
