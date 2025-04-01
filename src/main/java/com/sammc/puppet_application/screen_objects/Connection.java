package com.sammc.puppet_application.screen_objects;
/**
 * Represent a connection between two components on the screen
 * 
 * relative location can also be interpreted as the vector towards the child,
 * from the parent's orienation and location.
 */
public class Connection {
    
    private Component parent;
    private Component child;
    private float[] relativeLocation;

    public float[] getRelativeLocation() {
        return relativeLocation;
    }

    public void setRelativeLocation(float[] relativeLocation) {
        this.relativeLocation = relativeLocation;
    }

    public void setParent(Component component) {
        parent = component;
    }

    public Component getParent() {
        return parent;
    }

    public void setChild(Component component) {
        child = component;
    }

    public Component getChild() {
        return child;
    }
}
