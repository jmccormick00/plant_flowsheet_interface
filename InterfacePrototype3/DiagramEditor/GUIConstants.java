/*------------------------------------------------------------------------------
 * GUIConstants.java
 * Author: James McCormick
 * Description: The set of common constatns for the graphical interface
 *----------------------------------------------------------------------------*/
package DiagramEditor;

import java.awt.BasicStroke;

public class GUIConstants {
    private GUIConstants() {}
    private static final float[] dashed = { 2f, 0f, 2f};
    public static final BasicStroke DASHEDLINE = new BasicStroke(1, 
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dashed, 2f);
    public static final BasicStroke SOLIDLINE = new BasicStroke();
}
