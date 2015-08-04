package DiagramEditor;

import Util.IExpandableRect;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;

public class SelectionBox extends IExpandableRect {
    private boolean d_bIsCrossingBox;
    
    @Override
    public void setSecondCoords(float x, float y) {
        super.setSecondCoords(x, y);
        if(d_fFirstPoint[0] < x) d_bIsCrossingBox = false;
        else d_bIsCrossingBox = true;
    }
    
    public SelectionBox() {
        d_bIsCrossingBox = true;
    }
    
    public void draw(Graphics2D g) {
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
        if(d_bIsCrossingBox) {
            g.setColor(Color.green);
            g.setStroke(GUIConstants.DASHEDLINE);
        }
        else g.setColor(Color.blue);
        g.fill(d_bounds);
        // Set the composite back to normal
        g.setComposite(AlphaComposite.SrcOver);
        g.setColor(Color.LIGHT_GRAY);
        g.draw(d_bounds);
    }
    
    public boolean isInclusiveOnly() {
        return !d_bIsCrossingBox;
    }
}
