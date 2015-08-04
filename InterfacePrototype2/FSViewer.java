/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import javax.swing.JComponent;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.awt.Dimension;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelListener;
import java.awt.event.MouseWheelEvent;

import java.awt.Cursor;

import java.util.Collection;
import java.util.LinkedList;


public class FSViewer extends JComponent {
    // Static members for global properties 
    private static Color s_backGroundColor = Color.black;
    
    // Instance Variables
    private LinkedList<FSElement> d_elementList = new LinkedList<FSElement>();
    private LinkedList<FSElement> d_selectionList = new LinkedList<FSElement>();
    private IMacro d_defaultMacro;
    private IMacro d_currentMacro;
    
    private AffineTransform d_zoomMatrix  = new AffineTransform();
    private AffineTransform d_invertViewMatrix  = new AffineTransform();
    
    private MouseData d_mouseData;
    private SelectionStrategy d_selectionStrategy = new SelectionStrategy();
    private MouseListener d_mouse;
    private SelectionBox d_selectionBox = new SelectionBox();
    private boolean d_bToggleSelection = false;
    private boolean d_bPanning = false;
    private Cursor d_cursor;
    
    private float d_fZoom = 1.0f;
    private Point2D.Float d_eCenter = new Point2D.Float();
    private Point2D.Float d_sCenter = new Point2D.Float();
    private Point2D.Float d_origin = new Point2D.Float();
    private BasicExpandableRect d_zoomExtentsRect = new BasicExpandableRect();
    
    public FSViewer() {
        d_zoomMatrix.setToIdentity();
        d_invertViewMatrix.setToIdentity();
        d_mouseData = new MouseData(); 
        
        d_cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        
        d_defaultMacro = new IMacro() {
            @Override
            public IToolStrategy getCurrentStrategy() {
                return d_selectionStrategy;
            }

            @Override
            public void onCurrentMacro(FSViewer v) {} 
        };
        
        d_currentMacro = d_defaultMacro;
        
        // Add the mouse listener
        d_mouse = new MouseListener();
        addMouseMotionListener(d_mouse);
        addMouseListener(d_mouse);
        addMouseWheelListener(d_mouse);
        
        setMinimumSize(new Dimension(300, 300));
    }
    
    
    public void cleanUp() {
        d_currentMacro = null;
        d_elementList = null;
        d_selectionList = null;
        d_zoomMatrix = null;
        d_invertViewMatrix = null;
        d_mouseData = null;
        d_selectionBox = null;
        d_selectionStrategy = null;
    }
    
    public Collection<FSElement> getElementList() {
        return d_elementList;
    }
    
    public Collection<FSElement> getSelectionList() {
        return d_selectionList;
    }
    
    public IToolStrategy getSelectionStrategy() {
        return d_selectionStrategy;
    }
    
    public void addElement(FSElement e) {
        d_elementList.add(e);
        repaint();
    }
    
    public void transformScreenToWorld(Point2D.Float screen, Point2D.Float world) {
        d_invertViewMatrix.transform(screen, world);
    }
    
    public void clearSelection() {
        if(d_bToggleSelection) {
            repaint();
            return;
        }
        
        for(FSElement e : d_selectionList)
            e.unSelect();
        d_selectionList.clear();
        repaint();
    }
    
    public void deleteSelected() {
        d_selectionBox.cancel();
        d_elementList.removeAll(d_selectionList);
        d_selectionList.clear();
        repaint();
    }
    
    public boolean hasSelection() {
        return !d_selectionList.isEmpty();
    }
    
    public void removeSelected() {
        d_elementList.removeAll(d_selectionList);
        d_selectionList.clear();
        repaint();
    }
    
    public void setMacro(IMacro m) {
        if(d_currentMacro != this.d_defaultMacro)
            d_currentMacro.getCurrentStrategy().onCancel(this);
        d_currentMacro = m;
    } 
    
    public void clearMacro() {
        d_currentMacro.getCurrentStrategy().onCancel(this);
        d_currentMacro = null;
        d_currentMacro = d_defaultMacro;
    }
    
    
    private void startPan() {
        d_cursor = Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR);
        d_bPanning = true;
    }
    
    private void setPan(float x, float y) {
        Point2D.Float p = d_mouseData.getLastPointInScreen();
        d_origin.x += ((float)(x - p.x) / d_fZoom);
        d_origin.y += ((float)(y - p.y) / d_fZoom);
    }
    
    private void endPan() {
        d_cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        d_bPanning = false;
    }
    
    private void selectByBox(SelectionBox b) {
        Shape bounds = b.getBounds();
        
        for(FSElement e : d_elementList) {
            if(b.isInclusiveOnly()) {
                if(!bounds.contains(e.getBounds())) continue;
            } else {
                if(!bounds.intersects(e.getBounds())) continue;
            }
            select(e);
        }
    }
    
    private boolean selectByClick() {
        clearSelection();
        for(FSElement e : d_elementList) {
            if(e.getBounds().contains(d_mouseData.getCurrentPointInWorld())) {
                select(e);
                return true;
            }
        }
        return false;
    }
    
    private void select(FSElement e) {
        if(d_bToggleSelection && e.isSelected()) {
            d_selectionList.remove(e);
            e.unSelect();
            return;
        }
        d_selectionList.add(e);
        e.select();
    }
    
    private void changeZoomMatrix() {
        float screenCenterX = this.getWidth() / 2.0f;
        float screenCenterY = this.getHeight() / 2.0f;
        
        d_zoomMatrix.setToTranslation(screenCenterX, screenCenterY);
        d_zoomMatrix.scale(d_fZoom, d_fZoom);
        d_zoomMatrix.translate(-screenCenterX, -screenCenterY);
    }
    
    private void zoomWheel(int steps) {
        final double FACTOR = Math.sqrt(Math.sqrt(2)); 
        for (int i = 1; i <= steps; i++)
            d_fZoom *= FACTOR;
        for (int i = 1; i <= -steps; i++)
            d_fZoom /= FACTOR;
        d_fZoom = (float)Math.max(0.2, d_fZoom); // Lock the scale to a certain value
        d_fZoom = (float)Math.min(8.0, d_fZoom); // Lock the scale to a certain value
        changeZoomMatrix();
        repaint();
    }
    
    private void zoomExtents() {
        LinkedList<FSElement> list;
        
        Rectangle2D.Float bounds = d_zoomExtentsRect.getBounds();
        
        if(hasSelection())
            list = d_selectionList;
        else {
            if(!d_elementList.isEmpty())
                list = d_elementList;
            else {  // Nothing to zoom to, so just zoom to fit the window
                bounds.x = 0.0f;
                bounds.y = 0.0f;
                bounds.height = getHeight();
                bounds.width = getWidth();
                zoomToRect(bounds);
                return;
            }
        }
        
        bounds.height = 0.0f;
        bounds.width = 0.0f;
        bounds.x = (float)list.getFirst().getBounds().getX();
        bounds.y = (float)list.getFirst().getBounds().getY();
        
        for(FSElement e: list) {
            bounds.add(e.getBounds());
        }
        
        Utilities.padRect(bounds);
        zoomToRect(bounds);
    }
    
    void zoomToRect(Rectangle2D.Float rect) {
        final float w = getWidth();
        final float h = getHeight();
        
        d_sCenter.x = w / 2.0f;
        d_sCenter.y = h / 2.0f;
        transformScreenToWorld(d_sCenter, d_sCenter);
        
        float zY = h / rect.height;
        float zX = w / rect.width;
        d_fZoom = ((zX < zY) ? zX : zY);
        
        d_eCenter.x = rect.x + (rect.width / 2.0f);
        d_eCenter.y = rect.y + (rect.height / 2.0f);
        d_origin.x += (d_sCenter.x - d_eCenter.x);
        d_origin.y += (d_sCenter.y - d_eCenter.y);
        
        changeZoomMatrix();
    }
    
    public void startZoomWindow() {
        final ZoomWindowStrategy t = new ZoomWindowStrategy(); 
        IMacro m = new IMacro() {
            final ZoomWindowStrategy d_tool = t;

            @Override
            public IToolStrategy getCurrentStrategy() {
                return d_tool;
            }

            @Override
            public void onCurrentMacro(FSViewer v) {
                throw new UnsupportedOperationException("Not supported yet.");
            }
        };
        setMacro(m);
    }
    
    @Override
    public void paintComponent(Graphics b) {
        setCursor(d_cursor);
        // get a copy of the Graphics
        Graphics2D g = (Graphics2D)b.create();
        
        // Clear the Screen
        g.setColor(s_backGroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        
          // Apply the viewing tranformation matrix
        g.transform(d_zoomMatrix);
        g.translate(d_origin.x, d_origin.y);
        d_invertViewMatrix.setTransform(g.getTransform());
        
        try {
            d_invertViewMatrix.invert();
        } catch(NoninvertibleTransformException e) {
            System.out.println("Non Invertible view matrix");
            d_invertViewMatrix.setToIdentity();
        }
        
        // TODO - DEBUG Remove when not needed
        Point2D.Float p = new Point2D.Float(this.getWidth()/2.0f, this.getHeight() / 2.0f);
        transformScreenToWorld(p, p);
        g.setColor(Color.cyan);
        g.drawLine((int)p.x - 10, (int)p.y, (int)p.x + 10, (int)p.y);
        g.drawLine((int)p.x, (int)p.y - 10, (int)p.x, (int)p.y + 10);
        g.draw(d_zoomExtentsRect.getBounds());
        //------------------------------------------------------------------------------------
        
        for(FSElement e: d_elementList)
            e.draw(g);
        
        if(d_zoomExtentsRect.isActive()) {
            d_zoomExtentsRect.draw(g);
        }
        
        if(d_selectionBox.isActive()) 
            d_selectionBox.draw(g);
        
        g.dispose();
    }
    
    private class MouseListener extends MouseAdapter implements MouseWheelListener {
        static private final int d_iMainButton = MouseEvent.BUTTON1;
        static private final int d_iPanButton = MouseEvent.BUTTON2;
        static private final int d_iZoomExtentsButton = MouseEvent.BUTTON2;

        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == d_iZoomExtentsButton)
                if(e.getClickCount() == 2) 
                    FSViewer.this.zoomExtents();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            FSViewer.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());
            
            if(FSViewer.this.d_currentMacro.getCurrentStrategy().onMove(d_mouseData, FSViewer.this))
                FSViewer.this.repaint();
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            FSViewer.this.d_mouseData.setLastPosScreen(FSViewer.this.d_mouseData.getCurrentPointInScreen());
            FSViewer.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());     
            if(d_bPanning) {
                setPan(e.getX(), e.getY());
                repaint();
            } else {
                if(FSViewer.this.d_currentMacro.getCurrentStrategy().onDrag(d_mouseData, FSViewer.this))
                    FSViewer.this.repaint();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(e.getButton() == d_iPanButton) {
                FSViewer.this.endPan();
                FSViewer.this.repaint();
            } else {
                if(FSViewer.this.d_selectionStrategy.onRelease(d_mouseData, null))
                    FSViewer.this.repaint();
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            FSViewer.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());
            FSViewer.this.d_mouseData.setLastPosScreen(FSViewer.this.d_mouseData.getCurrentPointInScreen());
            
            // Check to see if the ctrl button is down
            if(e.isControlDown())
                FSViewer.this.d_bToggleSelection = true;
            else
                FSViewer.this.d_bToggleSelection = false;
            
            
            if(e.getButton() == d_iPanButton) {
                FSViewer.this.startPan();
                repaint();
            }
            else if(e.getButton() == d_iMainButton) {
                if(FSViewer.this.d_currentMacro.getCurrentStrategy().onPress(d_mouseData, FSViewer.this))
                    FSViewer.this.repaint();
            }
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            FSViewer.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());
            FSViewer.this.zoomWheel(-e.getWheelRotation());
        }
    }
    
    private final class MouseData implements IMouseData {
        private Point2D.Float d_currentPosWorld;
        private Point2D.Float d_currentPosScreen;
        private Point2D.Float d_lastPosWorld;
        private Point2D.Float d_lastPosScreen;

        public MouseData() {
            d_currentPosScreen = new Point2D.Float();
            d_currentPosWorld = new Point2D.Float();
            d_lastPosWorld = new Point2D.Float();
            d_lastPosScreen = new Point2D.Float();
        }

        public void setLastPosScreen(Point2D.Float p) {
            d_lastPosScreen.x = p.x;
            d_lastPosScreen.y = p.y;
        }

        public void setCurrentPosScreen(int x, int y) {
            d_currentPosScreen.x = x;
            d_currentPosScreen.y = y;
        }

        // Returns the last place the mouse was clicked in screen coords
        @Override
        public Point2D.Float getLastPointInScreen() {
            return d_lastPosScreen;
        }

        // Returns the last place the mouse was clicked in world coords
        @Override
        public Point2D.Float getLastPointInWorld() {
            FSViewer.this.transformScreenToWorld(d_lastPosScreen, d_lastPosWorld);
            return d_lastPosWorld;
        }

        @Override
        public Point2D.Float getCurrentPointInScreen() {
            return d_currentPosScreen;
        }

        @Override
        public Point2D.Float getCurrentPointInWorld() {
            FSViewer.this.transformScreenToWorld(d_currentPosScreen, d_currentPosWorld);
            return d_currentPosWorld;
        }
    }
    
    private class SelectionStrategy implements IToolStrategy {

       @Override
       public boolean onPress(IMouseData d, FSViewer v) {
           if(FSViewer.this.d_selectionBox.isActive()) {
               FSViewer.this.selectByBox(FSViewer.this.d_selectionBox);
               FSViewer.this.d_selectionBox.cancel();
               return true;
           }
           else {
               FSViewer.this.clearSelection();
               if(!FSViewer.this.selectByClick())
                FSViewer.this.d_selectionBox.setFirstCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
               return true;
           }
       }

        @Override
        public void onCancel(FSViewer v) {}


       @Override
       public void execute() { // TODO - figure out if i need this
           throw new UnsupportedOperationException("Not supported yet.");
       }

       @Override
       public boolean onDrag(IMouseData d, FSViewer v) {
          return onMove(d, v);
       }

       @Override
       public boolean onMove(IMouseData d, FSViewer v) {
           if(FSViewer.this.d_selectionBox.isActive()) {
                FSViewer.this.d_selectionBox.setSecondCoords((int)d.getCurrentPointInWorld().x, (int)d.getCurrentPointInWorld().y);
                FSViewer.this.clearSelection();
                FSViewer.this.selectByBox(FSViewer.this.d_selectionBox);
                return true;
            }
           return false;
       }

       @Override
       public boolean onRelease(IMouseData d, FSViewer v) {
           return true; // TODO - add something here
       }
    }
    
    private class ZoomWindowStrategy implements IToolStrategy {
        
       @Override
       public boolean onPress(IMouseData d, FSViewer v) {
           
           // TODO - Finish this
           if(!FSViewer.this.d_zoomExtentsRect.isActive()) {// Start the extents window
               FSViewer.this.d_zoomExtentsRect.setFirstCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
           } else { // finish the zoom window
               FSViewer.this.d_zoomExtentsRect.setSecondCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
               FSViewer.this.zoomToRect(FSViewer.this.d_zoomExtentsRect.getBounds());
               FSViewer.this.d_zoomExtentsRect.cancel();
               FSViewer.this.clearMacro();
           }
           return true;
       }

        @Override
        public void onCancel(FSViewer v) {
            FSViewer.this.d_zoomExtentsRect.cancel();
        }


       @Override
       public void execute() {} // TODO - figure out if i need this

       @Override
       public boolean onDrag(IMouseData d, FSViewer v) {
           return false;
       }

       @Override
       public boolean onMove(IMouseData d, FSViewer v) {
           if(FSViewer.this.d_zoomExtentsRect.isActive()) {
                FSViewer.this.d_zoomExtentsRect.setSecondCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
                return true;
           } else {
                return false;
           }
       }

       @Override
       public boolean onRelease(IMouseData d, FSViewer v) {
           return false;
       }
    }
}
