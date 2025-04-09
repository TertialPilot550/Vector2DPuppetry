package com.sammc.puppet_application.screen_objects;
/**
 * Represent a connection between two components on the screen
 * 
 * relative location can also be interpreted as the vector towards the child,
 * from the parent's orienation and location.
 */
public class Connection {
    
    private Entity parent;
    private Entity child;
   
    public void setParent(Entity component) {
        parent = component;
    }

    public Entity getParent() {
        return parent;
    }

    public void setChild(Entity component) {
        child = component;
    }

    public Entity getChild() {
        return child;
    }
}
