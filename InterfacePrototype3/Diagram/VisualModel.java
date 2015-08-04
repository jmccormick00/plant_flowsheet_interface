/*------------------------------------------------------------------------------
 * VisualModel.java
 * Author: James McCormick
 * Description: The visual model for any object to be displayed in the editor
 *----------------------------------------------------------------------------*/
package Diagram;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;

public class VisualModel {
    
    private Path2D.Float d_shape;
    static private AffineTransform s_transform = new AffineTransform();
    
    public void setLibraryShape(Path2D.Float s) {
        d_shape = s;
    }
    
    public void setEditorShape(Path2D.Float s) {
        d_shape = s;
    }
    
    public void translateEditorShape(float x, float y) {
        s_transform.setToTranslation(x, y);
        d_shape.transform(s_transform);
    }
    
    public Rectangle2D getEditorBounds() {
        return d_shape.getBounds2D();
    }
    
    public void drawEditorShape(Graphics2D g) {
        g.draw(d_shape);
    }
}
