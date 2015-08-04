
package interfaceprototype2;

import java.awt.geom.Rectangle2D;

public abstract class IExpandableRect {
    private boolean d_bIsActive = false;
//    private float minX, minY, maxX, maxY;
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
//        d_bounds.x = x;
//        d_bounds.y = y;
//        d_bounds.height = 0.0f;
//        d_bounds.width = 0.0f;
        d_bounds.setFrameFromDiagonal(x, y, x, y);
    }
    
    public void setSecondCoords(float x, float y) {
//        minX = Math.min(d_fTemp[0], x);
//        minY = Math.min(d_fTemp[1], y);
//        maxX = Math.max(d_fTemp[0], x);
//        maxY = Math.max(d_fTemp[1], y);
//        d_bounds.x = minX;
//        d_bounds.y = minY;
//        d_bounds.height = maxY - minY;
//        d_bounds.width = maxX - minX;
        
        d_bounds.setFrameFromDiagonal(d_fFirstPoint[0], d_fFirstPoint[1], x, y);
    }
    
    final public Rectangle2D.Float getBounds() {
        return d_bounds;
    }
}
