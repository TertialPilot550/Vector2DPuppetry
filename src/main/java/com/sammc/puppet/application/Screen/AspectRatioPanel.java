package com.sammc.puppet.application.screen;

import javax.swing.JPanel;
import java.awt.Dimension;

public class AspectRatioPanel extends JPanel {
    
    private double aspectRatio; // e.g., 1.0 for 1:1, 16.0/9.0 for 16:9

    public AspectRatioPanel(double aspectRatio) {
        this.aspectRatio = aspectRatio;
    }

    // ChatGPT Code
    @Override
    public Dimension getPreferredSize() {
        Dimension size = super.getPreferredSize();
        int width = size.width;
        int height = (int) (width / aspectRatio);
        return new Dimension(width, height);
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        // adjust width or height to maintain aspect ratio
        double newAspect = (double) width / (double) height;

        if (newAspect > aspectRatio) {
            // too wide, reduce width
            width = (int) (height * aspectRatio);
        } else {
            // too tall, reduce height
            height = (int) (width / aspectRatio);
        }

        super.setBounds(x, y, width, height);
    }
}