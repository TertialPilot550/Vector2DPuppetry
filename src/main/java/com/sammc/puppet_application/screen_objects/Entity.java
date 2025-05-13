package com.sammc.puppet_application.screen_objects;
import java.util.ArrayList;
import java.util.List;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Represent any entity to be displayed on the screen
 */
public class Entity {
    
    private int id;
    
    // used to contruct the affine trasform associated with this entity
    private double uni_scale = 1, x_scale = 1, y_scale = 1;
    private double x_shear, y_shear, rotation, x_offset, y_offset, depth = 0;

    // visual asset associated with this entity
    private String entityFilePath = "./proj/Entities/";
    private String visualAssetPath = "";
    private BufferedImage visualAsset = null;
    private List<Connection> connections = new ArrayList<>();
    private List<Animation> definedAnimations = new ArrayList<>(); // ways that this entity can have new arrangements of it's connections

    /*
     * Getters and Setters
     */ 

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    
    public double getUni_scale() {
        return uni_scale;
    }

    public void setUni_scale(double uni_scale) {
        this.uni_scale = uni_scale;
    }

    public double getX_scale() {
        return x_scale;
    }

    public void setX_scale(double x_scale) {
        this.x_scale = x_scale;
    }

    public double getY_scale() {
        return y_scale;
    }

    public void setY_scale(double y_scale) {
        this.y_scale = y_scale;
    }

    public double getX_shear() {
        return x_shear;
    }

    public void setX_shear(double x_shear) {
        this.x_shear = x_shear;
    }

    public double getY_shear() {
        return y_shear;
    }

    public void setY_shear(double y_shear) {
        this.y_shear = y_shear;
    }

    public double getRotation() {
        return rotation;
    }

    public void setRotation(double rotation) {
        this.rotation = rotation;
    }

    public double getX_offset() {
        return x_offset;
    }

    public void setX_offset(double x_offset) {
        this.x_offset = x_offset;
    }

    public double getY_offset() {
        return y_offset;
    }

    public void setY_offset(double y_offset) {
        this.y_offset = y_offset;
    }

    public String getEntityFilePath() {
        return entityFilePath;
    }

    public void setEntityFilePath(String entityFilePath) {
        this.entityFilePath = entityFilePath;
    }

    public void setVisualAssetPath(String visualAssetPath) {
        this.visualAssetPath = visualAssetPath;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public BufferedImage getVisualAsset() {
        return visualAsset;
    }

    public void setVisualAsset(BufferedImage visualAsset, String visualAssetPath) {
        this.visualAssetPath = visualAssetPath;
        this.visualAsset = visualAsset;
    }

    public List<Animation> getDefinedAnimations() {
        return definedAnimations;
    }

    public boolean isVisual() {
        return visualAsset != null;
    }

    /**
     * Returns the affine transform associated with the current values of this entity
     * @return AffineTransform which should be used to render this entity
     */
    public AffineTransform getTransform() {
        double m00 = uni_scale * x_scale;  // x scale
        double m01 = x_shear;              // x shear
        double m10 = y_shear;              // y shear
        double m11 = uni_scale * y_scale;  // y scale
        AffineTransform result = new AffineTransform(m00, m01, m10, m11, x_offset, y_offset);
        AffineTransform rotatation_transform = new AffineTransform(Math.cos(Math.toRadians(rotation)), -Math.sin(Math.toRadians(rotation)), Math.sin(Math.toRadians(rotation)), Math.cos(Math.toRadians(rotation)), 0, 0);
        result.concatenate(rotatation_transform);
        return result;
    
    }

    /**
     * Retuns a list of all children of this entity
     * @return List<Entity>
     */
    public List<Entity> getChildren() {
        List<Entity> acc = new ArrayList<>();
        for (Connection c : connections) {
            Entity child = c.getChild();
            acc.addAll(child.getChildren()); 
        }
        return acc;
    }

    /**
     * Returns the bounding box of this entity, which
     * is based on the currently defined transform 
     * for this entity.
     * @return Polygon which is the final bounding box of this entity
     */
    public Polygon getBoundingBox() {
        // Collect the points
        Point2D[] sp = new Point2D[] { 
            new Point2D.Double(0, 0), 
            new Point2D.Double(getVisualAsset().getWidth(), 0),
            new Point2D.Double(getVisualAsset().getWidth(), getVisualAsset().getHeight()), 
            new Point2D.Double(0, getVisualAsset().getHeight()) 
        };
        Point2D[] ep = new Point2D[4];
        // Transform the points
        getTransform().transform(sp, 0, ep, 0, 4);
        // Transform the polygon and return it
        return new Polygon(
            new int[] {(int) ep[0].getX(), (int) ep[1].getX(), (int) ep[2].getX(), (int) ep[3].getX()}, 
            new int[] {(int) ep[0].getY(), (int) ep[1].getY(), (int) ep[2].getY(), (int) ep[3].getY()}, 
            4
        );
    }

    public String getVisualAssetPath() {
        return visualAssetPath;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

}

