package com.sammc.puppet_application.activities.scene_edit.panels;

import javax.swing.JPanel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Entity;
import com.sammc.puppet_application.activities.scene_edit.screen_objects.Snapshot;

import java.awt.AlphaComposite;
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
 * one big issue: add zooming and panning somehow
 * 
 * update the mouse drag listener. if the first mouse click hits an object, select it, and enter move mode. 
 * Otherwise, enter camera bound mode. Small bounding boxes will not be valid and will default to large screen, 
 * grabs the first and last mouse points and makes a horizontal rectangle between them 
 */


/**
 * JPanel that displays the current state of all objects held in the current scene,
 * based on their associated visual assets and affine transforms. Mouse Handling
 * is also handled here, including selecting and moving entities.
 * @param parent
 * @author sammc
 */

public class Screen extends JPanel implements MouseListener, MouseMotionListener {
    
    private SceneEditFrame parent;
    private int[] mouse_pos;
    private Entity selected;
    private BufferedImage last_render = null;

    /**
     * Constructor for the screen
     * @param parent
     */
    public Screen(SceneEditFrame parent) {
        this.parent = parent;
        mouse_pos = new int[] {-1, -1};
        selected = null;
        setVisible(true);
        setBackground(Color.CYAN);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
    }

    public Entity getSelected() {
        return selected;
    }

    public BufferedImage getLastRender() {
        return last_render;
    }
    
    /**
     * Main behavior, draw all foreground and background objects
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Create the view that the direct output (screenshot) of a saved view
        BufferedImage program_buffer = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage user_buffer = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);


        render_snapshot(program_buffer, user_buffer, parent.getCurrentSnapshot(), false);     // Render only the foreground objects to the buffer
        render_snapshot(program_buffer, user_buffer, parent.getPreviousSnapshot(), true);     // Render the background objects at partial opacity
        
        g2.drawImage(user_buffer, 0, 0, this);          // Draw the final buffer to the screen
        last_render = program_buffer;                       // Keep the final program version saved away
        // Project not loaded warning, close graphics context
        if (!parent.hasProjectLoaded()) g2.drawString("No Project Currently Loaded", 50, 50);
        g2.dispose();  
    }

    /**
     * Select an entity at the given coordinates.
     * 
     * @param x
     * @param y
     */
    private void selectEntityAt(int x, int y) {
        List<Entity> entities = parent.getCurrentSnapshot().entities;
        for (Entity e : entities) {
            if (e.getBoundingBox().contains(x, y)) {
                selected = e;
                parent.getControls().updateFor(e);
                parent.repaint();
                return;
            }
            
        }
        // also update the control panel for the selected entity. should have a method in controls panel
        selected = null;
        parent.getControls().updateFor(null);
        repaint();
    }

    /*
     * Renderering methods
     */

    /**
     * Render all entities to the given BufferedImage
     * @param render_surface
     * @param entities
     */
    private void render_snapshot(Graphics2D g_program, Graphics2D g_user, Snapshot s, boolean isGhosts) {
        if (s == null) return;
        // Draw background image
        BufferedImage background_image = null;
        if (!isGhosts) background_image = s.background;

        if (background_image != null) {
            g_program.drawImage(background_image, 0, 0, getWidth(), getHeight(), this);
            g_user.drawImage(background_image, 0, 0, getWidth(), getHeight(), this);

        }

        // Draw current components
        for (Entity e : s.entities) {
            render_entity(g_program, g_user, e, new AffineTransform(), isGhosts);
        }
    }

    private void render_snapshot(BufferedImage b, BufferedImage b1, Snapshot s, boolean isGhosts) {
        render_snapshot(b.createGraphics(), b1.createGraphics(), s, isGhosts);
    }

    /**
     * Render a single entity to the given BufferedImage
     * @param render_surface
     * @param entity
     * @param base_orientation
     */
    private void render_entity(Graphics2D g_program, Graphics2D g_user, Entity entity, AffineTransform base_orientation, boolean isGhost) {
        AffineTransform t = entity.getTransform();
        t.preConcatenate(base_orientation);
        
        // if it's a visual entity, draw it
        if (entity.isVisual()) {
            // optionally draw at 50% opacity on the user's view
            if (isGhost) g_user.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f));

            // if it's selected, draw the bounding box
            if (selected == entity) {
                g_user.setColor(Color.RED);
                g_user.drawPolygon(entity.getBoundingBox());
            } 
            // the only thing drawn to the program view is actual images, no bounding boxes and no ghosts
            // while everything, including the image goes on the user view which will actually be drawn to the screen
            g_program.drawImage(entity.getVisualAsset(), t, this);
            g_user.drawImage(entity.getVisualAsset(), t, this);
        
            // Restore the composite to normal
            if (isGhost) g_user.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        }

        // render any child entities
        for (Entity c : entity.getChildren()) {
            render_entity(g_program, g_user, c, t, isGhost);
        }

    }





    /*
     * Mouse - katools
     */

    @Override
    public void mouseDragged(MouseEvent e) {
        boolean was_previous_call = mouse_pos.length == 2 & (mouse_pos[0] != -1 & mouse_pos[1] != -1);
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

        selected.setX_offset((int) (e.getX() - (selected.getVisualAsset().getWidth()  * selected.getX_scale() * selected.getUni_scale()) / 2));
        selected.setY_offset((int) (e.getY() - (selected.getVisualAsset().getHeight() * selected.getY_scale() * selected.getUni_scale()) / 2));

        repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        selectEntityAt(e.getX(), e.getY());
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
