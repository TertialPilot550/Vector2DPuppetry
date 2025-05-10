package com.sammc.puppet_application.activities.scene_edit.panels;

import javax.swing.JPanel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
import com.sammc.puppet_application.screen_objects.Connection;
import com.sammc.puppet_application.screen_objects.Entity;

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
    

    // Main behavior
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
 
        // Make a buffer to draw to
        BufferedImage buffer = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);

        // add all entities and components, then render them
        List<Entity> entities = parent.getEntities();
        renderEntities(buffer, entities);

        // draw the buffer to fill the screen
        g2.drawImage(buffer, 0, 0, this);

    }

    /**
     * Select an entity at the given coordinates.
     * 
     * @param x
     * @param y
     */
    private void selectEntityAt(int x, int y) {
        List<Entity> entities = parent.getEntities();
        for (Entity e : entities) {
            if (e.getBoundingBox().contains(x, y)) {
                selected = e;
                return;
            }
            
        }
        selected = null;
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
    private void renderEntities(BufferedImage render_surface, List<Entity> entities) {
        for (Entity e : entities) {
            renderEntity(render_surface, e, new AffineTransform());
        }
    }

    /**
     * Render a single entity to the given BufferedImage
     * @param render_surface
     * @param entity
     * @param base_orientation
     */
    private void renderEntity(BufferedImage render_surface, Entity entity, AffineTransform base_orientation) {
        entity.getOrientation().concatenate(base_orientation);
        Graphics2D g = render_surface.createGraphics();
        if (entity.isVisual()) {

            if (selected != null && selected.getId() == entity.getId()) {
                g.setColor(Color.RED);
                g.drawPolygon(entity.getBoundingBox());
            } 

            g.drawImage(entity.getVisualAsset(), entity.getOrientation(), this);
        }

        for (Connection c : entity.getConnections()) {
            renderEntity(render_surface, c.getChild(), entity.getOrientation());
        }
    }



    /*
     * MouseListener methods
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
     * Handle mouse drag events that occur after the initial click
     * @param e
     */
    private void handleLaterMouseDrag(MouseEvent e) {
        if (selected == null) {
            return;
        }
        // get the change in position since the last call
        int dx = e.getX() - mouse_pos[0];
        int dy = e.getY() - mouse_pos[1];
        
        // update mouse position
        mouse_pos[0] = e.getX();
        mouse_pos[1] = e.getY();
        
        selected.getOrientation().getTranslateX();
        selected.getOrientation().getTranslateY();

        // find the change in position needed to move the entity to the mouse position
        int corrected_dx, corrected_dy, new_x, new_y;
        new_x = ((int)e.getX() - (int)(selected.getVisualAsset().getWidth() * selected.getOrientation().getScaleX())/2); // set the new location relative to the mouse activity
        new_y = ((int)e.getY() - (int) (selected.getVisualAsset().getHeight() * selected.getOrientation().getScaleY())/2); 
        corrected_dx = (int)(new_x - selected.getOrientation().getTranslateX()); // combine the new position with negative of the current position
        corrected_dy = (int)(new_y - selected.getOrientation().getTranslateY());


        selected.getOrientation().translate(corrected_dx, corrected_dy);
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
