package com.sammc.puppet.application.screen;

import com.sammc.puppet.application.Util;
import com.sammc.puppet.application.screen.snapshot.Entity;
import com.sammc.puppet.application.screen.snapshot.Snapshot;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.logging.Logger;

/**
 * JPanel that displays the current state of all objects held in the current scene,
 * based on their associated visual assets and affine transforms. Mouse Handling
 * is also handled here, including selecting and moving entities.
 * @param parent
 * @author sammc
 */

public class Screen extends ScreenSkeleton {

    private Logger logger = Logger.getLogger("Screen.java");

    public static final double DEFAULT_ASPECT_RATIO = 1.85 / 1.0;
    
    private BufferedImage last_render = null;

    /**
     * Constructor for the screen
     * @param parent
     */
    public Screen(SnapshotFrame parent, double aspect_ratio) {
        super(parent, aspect_ratio);
        logger.info("Screen Instance Created");
        setVisible(true);
        setFocusable(true);
        addMouseListener(this);
        addMouseMotionListener(this);
        setBackground(Color.BLACK);
        parent.refresh();
    }
    
    /**
     * Main behavior, draw all foreground and background objects
     */
    @Override
    public void paintComponent(Graphics g) {
        if (parent == null) {
            logger.warning("No parent program window associated with this screen instance!");
            return;
        }
        Graphics2D g2 = (Graphics2D) g; 

        // Create the view that the direct output (screenshot) of a saved view
        BufferedImage program_buffer    = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);
        BufferedImage user_buffer       = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_RGB);

        // Render program information to both buffers
        render_to_views(program_buffer.createGraphics(), user_buffer.createGraphics());
        
        // Use the buffers
        g2.drawImage(user_buffer, 0, 0, this);          // Draw the final buffer to the screen
        last_render = program_buffer;                       // Keep the final program version saved away

        boolean project_loaded = !parent.getCurrentSnapshot().project_path.equals(".");
        UIManager.paintUI(g2, project_loaded);
        g2.dispose();  
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

    public BufferedImage getLastRender() {
        return last_render;
    } 

}
