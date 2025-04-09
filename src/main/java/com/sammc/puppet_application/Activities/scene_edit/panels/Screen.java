package com.sammc.puppet_application.activities.scene_edit.panels;

import javax.swing.JPanel;

import com.sammc.puppet_application.activities.scene_edit.SceneEditFrame;
import com.sammc.puppet_application.screen_objects.Connection;
import com.sammc.puppet_application.screen_objects.Entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.util.List;

public class Screen extends JPanel {
    
    SceneEditFrame parent;

    public Screen(SceneEditFrame parent) {
        setVisible(true);
        setBackground(Color.CYAN);
        this.parent = parent;
        
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
 
        g2.fillRect(0, 30, 30, 30);
        BufferedImage buffer = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // add all entities and components
        List<Entity> entities = parent.getEntities();

        // render all entities and components
        renderEntities(buffer, entities);

        // draw the buffer to fill the screen
        g2.drawImage(buffer, 0, 0, this);

    }

    // Render all entities associated with the parent activity
    private void renderEntities(BufferedImage render_surface, List<Entity> entities) {
        for (Entity e : entities) {
            renderEntity(render_surface, e, new AffineTransform());
        }
    }

    // private method used as a higher level recursive method
    private void renderEntity(BufferedImage render_surface, Entity entity, AffineTransform base_orientation) {
        entity.getOrientation().concatenate(base_orientation);
        Graphics2D g = render_surface.createGraphics();
        if (entity.isVisual()) {
            g.drawImage(entity.getVisualAsset(), entity.getOrientation(), this);
        }

        for (Connection c : entity.getConnections()) {
            renderEntity(render_surface, c.getChild(), entity.getOrientation());
        }
    }





}
