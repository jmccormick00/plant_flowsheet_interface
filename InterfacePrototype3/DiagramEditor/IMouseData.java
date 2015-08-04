/*------------------------------------------------------------------------------
 * IMouseData.java
 * Author: James McCormick
 * Description: The base class for mouse data.  The interface for accessing 
 * mouse data.
 *----------------------------------------------------------------------------*/

package DiagramEditor;

import java.awt.geom.Point2D;

public interface IMouseData {
    public Point2D.Float getLastPointInScreen();
    public Point2D.Float getLastPointInWorld();
    public Point2D.Float getCurrentPointInScreen();
    public Point2D.Float getCurrentPointInWorld();
    public int getCurrentDownButton();
}
