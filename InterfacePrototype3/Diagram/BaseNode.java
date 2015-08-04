/*------------------------------------------------------------------------------
 * BaseNode.java
 * Author: James McCormick
 * Description: The base class for a node in a diagram
 *----------------------------------------------------------------------------*/

package Diagram;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

// TODO - implement the object system 
public class BaseNode extends IDiaObject {

    private String d_name;
    private VisualModel d_visualModel;
    
    public BaseNode(String name) {
        super(ObjectType.NODETYPE);
        d_name = name;
    }
    
    public void setVisualModel(VisualModel m) {
        
    }
    
    public VisualModel getVisualModel() {
        return d_visualModel;
    }

    @Override
    public Rectangle2D getBounds() {
       return d_visualModel.getEditorBounds();
    }
    
    @Override
    public boolean rectTest(Rectangle2D.Float rect, boolean inclusive) {
        throw new UnsupportedOperationException("Not supported yet.");
    }


    @Override
    public boolean hitTest(Point2D.Float p) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void move(Point2D.Float mvVector) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public IDiaObject copy() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void draw(Graphics2D g, boolean isTempObject) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
   
}
