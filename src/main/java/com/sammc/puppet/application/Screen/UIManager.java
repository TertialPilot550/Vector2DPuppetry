package com.sammc.puppet.application.screen;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import com.sammc.puppet.application.screen.snapshot.Snapshot;

/**
 * Layered User interface manager
 * @author sammc
 */
public class UIManager {
    
    /**
     * Definitions for the UI. Changing these will change the program's appearance/function, 
     * but you should also edit execute_ui_button().
     */
    private final static String[] SYMBOLS = {"+", "-", "<", ">", "^", "v"};
    private final static int NUM_BUTTONS = SYMBOLS.length;

    /**
     * Predefined formula for mapping a natural number to a corresponding 'button position' on the screen
     * @param n
     * @return
     */
    public static Rectangle boundsFormula(int n) {
        return new Rectangle(10, 10 + (60 * n), 50, 50);
    }

    /**
     * Paints the user interface on top of the provided graphics context, and provides a warning if there is no project currently loaded by the program.
     * @param g
     * @param project_loaded
     */
    public static void paintUI(Graphics2D g, boolean project_loaded) {
        g.setColor(Color.RED);
        // Project not loaded warning
        if (!project_loaded) g.drawString("No Project Currently Loaded", 100, 50);

        for (int i = 0; i < NUM_BUTTONS; i++) {
            paintButton(g, i);
        }
    }

    /**
     * Paints a button in position n according to the boundsFormula() onto 
     * the graphics context
     * @param g
     * @param n
     */
    public static void paintButton(Graphics2D g, int n) {
        Rectangle bounds = boundsFormula(n);
        g.setColor(Color.WHITE);
        g.fill(bounds);
        g.setColor(Color.BLACK);
        g.draw(bounds);
        
        g.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        g.drawString("" + SYMBOLS[n], bounds.x + 15, bounds.y + 30);
    }       

    /**
     * Handle a button event from button in position n
     */
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

    /**
     * Check the mouse events of the screen for interactions with the ui.
     */
    public static void handle_mouse_press(MouseEvent e, SnapshotFrame parent) {
        for (int i = 0; i < NUM_BUTTONS; i++) {
            if (UIManager.boundsFormula(i).contains(e.getX(), e.getY())) {
                execute_ui_button(i, parent.getCurrentSnapshot());
            }
        }
    }

   
}
