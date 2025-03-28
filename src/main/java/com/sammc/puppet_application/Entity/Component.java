package com.sammc.puppet_application.Entity;

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
