package com.sammc.puppet_application.activities.animation_frame.panels;

import javax.swing.JPanel;

import com.sammc.puppet_application.activities.animation_frame.AnimationFrame;
import com.sammc.puppet_application.screen_objects.Component;
import com.sammc.puppet_application.screen_objects.Entity;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class Screen extends JPanel {
    
    AnimationFrame parent;

    public Screen(AnimationFrame parent) {
        this.parent = parent;
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        BufferedImage buffer = new BufferedImage(parent.getWidth(), parent.getHeight(), BufferedImage.TYPE_INT_ARGB);

        // add all entities
        //List<Entity> entities = parent.getEntities();
        //List<Component> components = parent.getVisualComponents();

        //renderEntities(buffer, entities);
        //renderVisualComponents(buffer, components);

        // add all entities

        g2.drawImage(buffer, 0, 0, null);


    }

    private void renderEntities(BufferedImage image, List<Entity> entities) {

        

    }



}
