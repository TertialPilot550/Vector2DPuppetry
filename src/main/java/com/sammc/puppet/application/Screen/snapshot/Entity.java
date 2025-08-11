package com.sammc.puppet.application.screen.snapshot;
import com.sammc.puppet.application.Util;

import java.util.ArrayList;
import java.util.List;
import java.awt.Polygon;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

/**
 * Represent any entity to be displayed on the screen
 * @author sammc
 */
public class Entity {
    
    // used to contruct the affine trasform associated with this entity
    private double uni_scale = 1, x_scale = 1, y_scale = 1;
    private double x_shear, y_shear, rotation, x_offset, y_offset, depth = 0;

    // visual asset associated with this entity
    private String entityFileName = "";
    private String visualAssetName = "";
    private BufferedImage visualAsset = null;
    private Entity parent = null;
  
    private List<Entity> children = new ArrayList<Entity>();

    /**
     * Return a deep copy of this Entity object. Expensive Method.
     */
    public Entity clone() {
        Entity copy = new Entity();
        copy.uni_scale = uni_scale;
        copy.x_scale = x_scale;
        copy.y_scale = y_scale;
        copy.x_shear = x_shear;
        copy.y_shear = y_shear;
        copy.rotation = rotation;
        copy.x_offset = x_offset;
        copy.y_offset = y_offset;
        copy.depth = depth;
        copy.entityFileName = entityFileName;
        copy.visualAssetName = visualAssetName;
        
        copy.visualAsset = Util.deepCopy(visualAsset);
        for (Entity c : children) {
            Entity new_c = c.clone();
            new_c.parent = copy;
            copy.children.add(new_c);
        }
        return copy;
    }

    /**
     * Returns true if the entity was a child and was removed
     * @param toRemove
     * @return
     */
    public boolean remove_child(Entity toRemove) {
        for (Entity e : children) {
            // if the child is the one to remove, remove it and return true
            if (e == toRemove) {
                children.remove(toRemove);
                return true;
            }
            // other wise, check the children of the child. If toRemove is among the child's children, then jon is done return true
            if (e.remove_child(toRemove)) return false;
        }
        return false;
    }

    
    /**
     * Returns null if there is no match, or the child that overlaps with that point. 
     * 
     * @param x
     * @param y
     * @return 
     */
    public Entity getChildAt(int x, int y) {
        // check each child
        for (Entity child : children) {
            // if the point is in the direct child, return the child
            if (child.getBoundingBox().contains(x, y)) return child;
            // otherwise check for a match
            else {
                Entity match = child.getChildAt(x, y);  // check grandchildren
                if (match == null) continue;            // if there's no match, continue to the next iteration to try the next child
                else return match;                      // otherwise return the match
            }
        }
        return null; // fail to find
    }

    public Entity getParent() {
        return parent;
    }

    public void setParent(Entity parent) {
        this.parent = parent;
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

    public String getEntityFileName() {
        return entityFileName;
    }

    public void setEntityFileName(String entity_name_field) {
        this.entityFileName = entity_name_field;
    }

    public void setVisualAssetName(String visualAssetPath) {
        this.visualAssetName = visualAssetPath;
    }

    public BufferedImage getVisualAsset() {
        return visualAsset;
    }

    public void setVisualAsset(BufferedImage visualAsset, String visualAssetPath) {
        this.visualAssetName = visualAssetPath;
        this.visualAsset = visualAsset;
    }


    public boolean isVisual() {
        return visualAsset != null;
    }

    public void addChild(Entity e) {
        children.add(e);
        e.setParent(this);
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
        return children;
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
        AffineTransform t = getTransform();
        if (parent != null) t.preConcatenate(parent.getTransform());
        t.transform(sp, 0, ep, 0, 4);
        // Transform the polygon and return it
        return new Polygon(
            new int[] {(int) ep[0].getX(), (int) ep[1].getX(), (int) ep[2].getX(), (int) ep[3].getX()}, 
            new int[] {(int) ep[0].getY(), (int) ep[1].getY(), (int) ep[2].getY(), (int) ep[3].getY()}, 
            4
        );
    }

    public String getVisualAssetName() {
        return visualAssetName;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }

    public String getName() {
        String[] split = entityFileName.split("/");
        return split[split.length - 1];
    }

    public String toString() {
        return "";
    }

    
}