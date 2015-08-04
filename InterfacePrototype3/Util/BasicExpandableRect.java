/*------------------------------------------------------------------------------
 * BasicExpandableRect.java
 * Author: James McCormick
 * Description: A basic implementation of the expandable rect
 *----------------------------------------------------------------------------*/
package Util;

import java.awt.Color;
import java.awt.Graphics2D;

public class BasicExpandableRect extends IExpandableRect {
    
    public void draw(Graphics2D g, Color c) {
        g.setColor(c);
        g.draw(d_bounds);
    }
    
}
