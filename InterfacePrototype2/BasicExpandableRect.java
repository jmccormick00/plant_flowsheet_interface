
package interfaceprototype2;

import java.awt.Color;
import java.awt.Graphics2D;

public class BasicExpandableRect extends IExpandableRect {
    
    public void draw(Graphics2D g) {
        g.setColor(Color.white);
        g.draw(d_bounds);
    }
    
}
