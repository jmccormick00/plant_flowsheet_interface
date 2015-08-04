/*------------------------------------------------------------------------------
 * IDiaObject.java
 * Author: James McCormick
 * Description: The base class for a all objects in a diagram.
 *----------------------------------------------------------------------------*/
package Diagram;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

public abstract class IDiaObject {
    
    public enum ObjectType {
        NODETYPE, LINKTYPE, 
        PRINTTYPE, LABELTYPE
    }
    
    private boolean d_bSelected = false; 
    private ObjectType d_eType;
    
    IDiaObject(ObjectType t) {
        d_eType = t;
    }
    
    public ObjectType getType() {
        return d_eType;
    }
    
    public void select() {
        d_bSelected = true;
    }
    
    public void deSelect() {
        d_bSelected = false;
    }
    
    public boolean isSelected() {
        return d_bSelected;
    }
    
    public abstract Rectangle2D getBounds();
    
    // Make a copy of this object
    public abstract IDiaObject copy();
    
    public abstract void draw(Graphics2D g, boolean isTempObject);
    
    public abstract boolean hitTest(Point2D.Float p);
    
    public abstract boolean rectTest(Rectangle2D.Float rect, boolean inclusive);
    
    // Move by the vector passed to the function
    public abstract void move(Point2D.Float mvVector);
}
