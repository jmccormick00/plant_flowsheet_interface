/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import java.awt.geom.Rectangle2D;
import java.awt.Graphics2D;
import java.awt.AlphaComposite;
import java.awt.Color;

public abstract class FSElement {
    private static final float OFFSETX = -5;
    private static final float OFFSETY = -5;
    private static final float HANDLEWIDTH = 10;
    private static final float HANDLEHEIGHT = 10;
    
    private boolean d_bIsSelected = false;
    private final Rectangle2D.Float[] d_handles;
    
    public FSElement() {
        d_handles = new Rectangle2D.Float[8];
        for(int i = 0; i < 8; ++i)
            d_handles[i] = new Rectangle2D.Float();
    }
    
    public final void select() {
        d_bIsSelected = true;
    }

    public final void unSelect() {
        d_bIsSelected = false;
    }

    public final boolean isSelected() {
        return d_bIsSelected;
    }

    protected final void drawHandles(Graphics2D g, Rectangle2D r) {
        // Top Left
        d_handles[0].setRect((float)r.getX() + OFFSETX, (float)r.getY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Top Middle
        d_handles[1].setRect((float)r.getCenterX() + OFFSETX, (float)r.getY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Top Right
        d_handles[2].setRect((float)r.getMaxX() + OFFSETX, (float)r.getY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Middle Right
        d_handles[3].setRect((float)r.getMaxX() + OFFSETX, (float)r.getCenterY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Bottom Right
        d_handles[4].setRect((float)r.getMaxX() + OFFSETX, (float)r.getMaxY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Bottom Middle
        d_handles[5].setRect((float)r.getCenterX() + OFFSETX, (float)r.getMaxY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Bottom Left
        d_handles[6].setRect((float)r.getX() + OFFSETX, (float)r.getMaxY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        // Middle Left
        d_handles[7].setRect((float)r.getX() + OFFSETX, (float)r.getCenterY() + OFFSETY, HANDLEWIDTH, HANDLEHEIGHT);
        
        g.setColor(Color.blue);
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f));

        for(int i = 0; i < 8; ++i)
            g.fill(d_handles[i]);

        g.setComposite(AlphaComposite.SrcOver);
    }
    
    public final void draw(Graphics2D g) {
        if(d_bIsSelected) {
            g.setStroke(GUIConstants.DASHEDLINE);
            g.setColor(Color.white);
            drawShape(g);
            g.setStroke(GUIConstants.SOLIDLINE);
            drawHandles(g, this.getBounds());
        } else {
            g.setColor(Color.red);
            drawShape(g);
        }
    }
    
    abstract protected void drawShape(Graphics2D g);
    abstract public Rectangle2D getBounds();
}
