package com.sammc.puppet.application.Screen;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.sammc.puppet.application.Screen.SnapshotFrame.Snapshot;
import com.sammc.puppet.application.Screen.SnapshotFrame.SnapshotFrame;


public class UIManager {
    
    private final static String[] SYMBOLS = {"+", "-", "<", ">", "^", "v"};
    private final static int NUM_BUTTONS = SYMBOLS.length;

    public static Rectangle boundsFormula(int n) {
        return new Rectangle(10, 10 + (60 * n), 50, 50);
    }

    public static void paintUI(Graphics2D g, boolean project_loaded) {
        g.setColor(Color.RED);
        // Project not loaded warning
        if (!project_loaded) g.drawString("No Project Currently Loaded", 100, 50);

        for (int i = 0; i < NUM_BUTTONS; i++) {
            paintButton(g, i);
        }
    }

    public static void paintButton(Graphics2D g, int n) {
        Rectangle bounds = boundsFormula(n);
        g.setColor(Color.WHITE);
        g.fill(bounds);
        g.setColor(Color.BLACK);
        g.draw(bounds);
        
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g.drawString("" + SYMBOLS[n], bounds.x + 15, bounds.y + 30);
    }       

    private static void execute_ui_button(int n, Snapshot curr) {
        switch (n) {
            case 0:
                curr.zoom += 0.1;
                break;
            case 1:
                curr.zoom = (float) Math.max(1.0, curr.zoom - 0.1);
                break;
            case 2:
                curr.camera_pos[0] = Math.max(0, curr.camera_pos[0] - 10);
                break;
            case 3:
                curr.camera_pos[0] += 10;
                break;
            case 4:
                curr.camera_pos[1] = Math.max(0, curr.camera_pos[1] - 10);
                break;
            case 5: 
                curr.camera_pos[1] += 10;
                break;
        }
    }

    public static void handle_mouse_press(MouseEvent e, SnapshotFrame parent) {
        for (int i = 0; i < NUM_BUTTONS; i++) {
            if (UIManager.boundsFormula(i).contains(e.getX(), e.getY())) {
                execute_ui_button(i, parent.getCurrentSnapshot());
            }
        }
    }

   
}
