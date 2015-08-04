/*------------------------------------------------------------------------------
 * IExpandableRect.java
 * Author: James McCormick
 * Description: An expandable rectangle abstract object
 *----------------------------------------------------------------------------*/
package Util;

import java.awt.geom.Rectangle2D;

public abstract class IExpandableRect {
    private boolean d_bIsActive = false;
    protected Rectangle2D.Float d_bounds = new Rectangle2D.Float();
    protected float d_fFirstPoint[] = new float[2];
    
    final public boolean isActive() {
        return d_bIsActive;
    }
    
    final public void cancel() {
        d_bIsActive = false;
    }
    
    public void setFirstCoords(float x, float y) {
        d_bIsActive = true;
        d_fFirstPoint[0] = x;
        d_fFirstPoint[1] = y;
        d_bounds.setFrameFromDiagonal(x, y, x, y);
    }
    
    public void setSecondCoords(float x, float y) {
        d_bounds.setFrameFromDiagonal(d_fFirstPoint[0], d_fFirstPoint[1], x, y);
    }
    
    final public Rectangle2D.Float getBounds() {
        return d_bounds;
    }
}
