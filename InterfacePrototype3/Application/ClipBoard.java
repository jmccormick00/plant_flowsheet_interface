/*------------------------------------------------------------------------------
 * ClipBoard.java
 * Author: James McCormick
 * Description: Handles the cut, past, and copying operations between the 
 * editors.
 *----------------------------------------------------------------------------*/
package Application;

import Diagram.IDiaObject;
import java.awt.geom.Point2D;
import java.util.Collection;

public class ClipBoard {
    private Collection<IDiaObject> d_currentCollection = null;
    private Point2D.Float d_basePoint = null;
    
    public ClipBoard() {
        
    }
    
    public void postCollection(Collection<IDiaObject> c, Point2D.Float v) {
        d_currentCollection = c;
        d_basePoint = v;
    }
    
    public boolean hasPost() {
        return d_currentCollection != null;
    }
    
    public Collection<IDiaObject> getCurrentCollectionPost() {
        return d_currentCollection;
    }
    
    public Point2D.Float getCurrentBasePoint() {
        return d_basePoint;
    }
    
    public void clearPost() {
        d_currentCollection = null;
        d_basePoint = null;
    }
}
