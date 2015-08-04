/*------------------------------------------------------------------------------
 * EditorProperties.java
 * Author: James McCormick
 * Description: The static properties that each editor uses.
 *----------------------------------------------------------------------------*/
package DiagramEditor;

import java.awt.Color;
import java.awt.event.MouseEvent;

public class EditorProperties {
    private EditorProperties() {}
    
    public static int d_iMainButton = MouseEvent.BUTTON1;
    public static int d_iPanButton = MouseEvent.BUTTON2;
    public static int d_iZoomExtentsButton = MouseEvent.BUTTON2;
    public static int d_iSecondaryButton = MouseEvent.BUTTON3;
    public static Color s_backGroundColor = Color.black;
}
