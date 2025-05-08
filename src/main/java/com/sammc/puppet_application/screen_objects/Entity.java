package com.sammc.puppet_application.screen_objects;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Represent some sort of complex entity to be displayed on the screen
 */
public class Entity {
    
    private int id;

    private AffineTransform orientation = new AffineTransform();
    private BufferedImage visualAsset = null;
    private List<Connection> connections = new ArrayList<>();
    private List<Animation> definedAnimations = new ArrayList<>(); // ways that this entity can have new arrangements of it's connections


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public AffineTransform getOrientation() {
        return orientation;
    }

    public void setOrientation(AffineTransform orientation) {
        this.orientation = orientation;
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

    public boolean isVisual() {
        return visualAsset != null;
    }


    public List<Entity> getChildren() {
        List<Entity> acc = new ArrayList<>();
        for (Connection c : connections) {
            Entity child = c.getChild();
            acc.addAll(child.getChildren()); 
        }
        return acc;
    }

    public Polygon getBoundingBox() {
        Point2D origin = new Point2D.Double(0, 0);
        Point2D t_origin = new Point2D.Double();
        Point2D width = new Point2D.Double(getVisualAsset().getWidth(),0);
        Point2D t_width = new Point2D.Double();
        Point2D height = new Point2D.Double(0,getVisualAsset().getHeight());
        Point2D t_height = new Point2D.Double();
        Point2D r_corner = new Point2D.Double(getVisualAsset().getWidth(), getVisualAsset().getHeight());
        Point2D t_r_corner = new Point2D.Double();

        getOrientation().transform(origin, t_origin);
        getOrientation().transform(width, t_width);
        getOrientation().transform(height, t_height);
        getOrientation().transform(r_corner, t_r_corner);

        return new Polygon(
            new int[] {(int) t_origin.getX(), (int) t_width.getX(), (int) t_r_corner.getX(), (int) t_height.getX()}, 
            new int[] {(int) t_origin.getY(), (int) t_width.getY(), (int) t_r_corner.getY(), (int) t_height.getY()},
            4
        );
    }

    public static Entity fromString(String string) throws JsonParseException, JsonMappingException, IOException {
        ObjectMapper obj = new ObjectMapper();
        return obj.readValue(string, Entity.class);
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

}

