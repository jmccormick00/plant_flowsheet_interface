package interfaceprototype2;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;


public class SelectionBox extends IExpandableRect {
//    private int minx , miny, maxx , maxy;
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



//public class SelectionBox {
//    private final Rectangle2D.Float d_bounds;
//    private final int[] d_iCoords;
//    private int minx , miny, maxx , maxy;
//    private boolean d_bIsCrossingBox;
//    private boolean d_bActive;
//    
//    public void setFirstCoords(int x, int y) {
//        d_bActive = true;
//        d_iCoords[0] = x;
//        d_iCoords[1] = y;
//        d_bIsCrossingBox = true;
//        d_bounds.x = x;
//        d_bounds.y = y;
//        d_bounds.height = 0.0f;
//        d_bounds.width = 0.0f;
//    }
//    
//    public void setSecondCoords(int x, int y) {
//        minx = Math.min(d_iCoords[0], x);
//        miny = Math.min(d_iCoords[1], y);
//        maxx = Math.max(d_iCoords[0], x);
//        maxy = Math.max(d_iCoords[1], y);
//        if(d_iCoords[0] < x) d_bIsCrossingBox = false;
//        else d_bIsCrossingBox = true;
//        d_bounds.x = minx;
//        d_bounds.y = miny;
//        d_bounds.height = maxy - miny;
//        d_bounds.width = maxx - minx;
//    }
//    
//    public SelectionBox() {
//        d_bActive = false;
//        d_bIsCrossingBox = true;
//        d_bounds = new Rectangle2D.Float();
//        d_iCoords = new int[2];
//    }
//    
//    public void cancel() {
//        d_bActive = false;
//    }
//    
//    boolean isActive() {
//        return d_bActive;
//    }
//    
//    public void draw(Graphics g) {
//        Graphics2D g2d = (Graphics2D)g;
//        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.2f));
//        if(d_bIsCrossingBox) {
//            g2d.setColor(Color.green);
//            g2d.setStroke(GUIConstants.DASHEDLINE);
//        }
//        else g2d.setColor(Color.blue);
//        g2d.fill(d_bounds);
//        // Set the composite back to normal
//        g2d.setComposite(AlphaComposite.SrcOver);
//        g2d.setColor(Color.LIGHT_GRAY);
//        g2d.draw(d_bounds);
//    }
//    
//    public Rectangle2D getBounds() {
//        return d_bounds.getBounds();
//    }
//    
//    public boolean isInclusiveOnly() {
//        return !d_bIsCrossingBox;
//    }
//}
