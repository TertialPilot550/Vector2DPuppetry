package com.sammc.puppet_application.screen_objects;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sammc.puppet_application.Util;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Represent some sort of complex entity to be displayed on the screen
 */
public class Entity implements Component {
    
    private int id;

    private float[][] orientation_matrix;
    private float[] location;

    private List<Connection> connections;
    private BufferedImage visualAsset;
    private List<Animation> definedAnimations; // ways that this entity can have new arrangements of it's connections

    public Entity() {
        orientation_matrix = new float[][] {{1, 0}, {0, 1}};        // init to I
        location = new float[] {0, 0};                              // init to 0, 0
       
        connections = new ArrayList<>();                            
        visualAsset = null;
        definedAnimations = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }

    public BufferedImage getVisualAsset() {
        return visualAsset;
    }

    public void setVisualAsset(BufferedImage visualAsset) {
        this.visualAsset = visualAsset;
    }

    public List<Animation> getDefinedAnimations() {
        return definedAnimations;
    }

    public void setDefinedAnimations(List<Animation> definedAnimations) {
        this.definedAnimations = definedAnimations;
    }

    public void setLocation(float[] location) {
        this.location = location;
    }

    public int[] getLocation() {
        return new int[] {(int) location[0], (int) location[1]};
    }

    public float[] getExactLocation() {
        return location;
    }

    public void applyOrientation(float[][] o_matrix) {
        orientation_matrix = Util.matrixProduct(o_matrix, orientation_matrix);
    }

    public void applyTranslation(float[] translation_vector) {

    }

    public void setOrientation(float[][] o_matrix) {
        orientation_matrix = o_matrix;
    }

    public float[][] getOrientation() {
        return orientation_matrix;
    }

    private List<Component> getChildComponents() {
        List<Component> acc = new ArrayList<>();
        for (Connection c : connections) {
            Component child = c.getChild();
            if (child.isEntity()) {
                Entity e = (Entity) child;
                acc.addAll(e.getChildComponents());
            } else {
                acc.add(child);
            }
        }
        
        return acc;
    }

    private List<Entity> getChildrenEntities() {
        List<Entity> acc = new ArrayList<>();
        for (Connection c : connections) {
            Component child = c.getChild();
            if (child.isEntity()) {
                Entity e = (Entity) child;
                acc.addAll(e.getChildrenEntities());
            } 
        }
        return acc;
    }




    @Override
    public boolean isAnimated() {
       return definedAnimations.size() > 0;
    }

    @Override
    public boolean isVisual() {
        return visualAsset != null;
    }

    @Override
    public boolean isEntity() {
       return true;
    }

    public void fromString(String string) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper obj = new ObjectMapper();
        Entity e = obj.readValue(string, Entity.class);
        this.location = e.location.clone();
        this.orientation_matrix = e.orientation_matrix.clone();

        this.visualAsset = e.visualAsset;
        this.connections = e.connections;
        this.definedAnimations = e.definedAnimations;
    }

    @Override
    public String toString() {
        ObjectMapper Obj = new ObjectMapper(); 
        try {
            return Obj.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

    public static void main(String[] args) {

        Entity e = new Entity();
        System.out.println (e);

    }

}

