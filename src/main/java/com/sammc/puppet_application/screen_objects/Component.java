package com.sammc.puppet_application.screen_objects;

/**
 * 
 * @author sammc
 */

public interface Component {
    
    public int[] getLocation();
    public boolean isEntity();
    public boolean isAnimated();
    public boolean isVisual();

}
