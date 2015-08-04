/*------------------------------------------------------------------------------
 * DiagramEditor.java
 * Author: James McCormick
 * Description: The base class for an editor.  Handles zooming and paning and
 * displaying the diagram.
 *----------------------------------------------------------------------------*/
package DiagramEditor;

import Application.ClipBoard;
import Diagram.IDiaObject;
import Diagram.IDiagram;
import Util.BasicExpandableRect;
import Util.GeometricUtil;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.JComponent;

public class DiagramEditor extends JComponent {
    private static ClipBoard d_clipBoard = new ClipBoard();
    private LinkedList<IDiaObject> d_selectionList = new LinkedList<>();
    private LinkedList<IDiaObject> d_tempObjectList = new LinkedList<>();
    private Collection<IDiaObject> d_objectList;
    private Point2D.Float d_tempObjectPosition = new Point2D.Float();
    
    private IToolMacro d_currentMacro;
    private MacroRegistry.MacroID d_previousMacro;
    private final SimpleMacro d_defaultMacro = new SimpleMacro();
    private final SelectionStrategy d_selectionStrategy = new SelectionStrategy();
    private final ZoomWindowStrategy d_zoomStrategy = new ZoomWindowStrategy();
    
    private final AffineTransform d_invertViewMatrix  = new AffineTransform();
    
    private Cursor d_cursor;
    private final MouseData d_mouseData = new MouseData();
    private final MouseListener d_mouse;
    private boolean d_bPanning = false;
    private boolean d_bToggleSelection = false;
    private final SelectionBox d_selectionBox = new SelectionBox();
    
    // Used for zoom calculations
    private float d_fZoom = 1.0f;
    private Point2D.Float d_eCenter = new Point2D.Float();
    private Point2D.Float d_sCenter = new Point2D.Float();
    private Point2D.Float d_origin = new Point2D.Float();
    private BasicExpandableRect d_zoomExtentsRect = new BasicExpandableRect();
    
    private IDiagram d_diagram;
    
    public DiagramEditor(IDiagram dia) {
        d_invertViewMatrix.setToIdentity();
        
        d_cursor = Cursor.getPredefinedCursor(Cursor.CROSSHAIR_CURSOR);
        
        d_defaultMacro.setStrategy(d_selectionStrategy);
        
        d_currentMacro = (IToolMacro)d_defaultMacro;
        
        d_diagram = dia;
        
        // Add the mouse listener
        d_mouse = new MouseListener();
        addMouseMotionListener(d_mouse);
        addMouseListener(d_mouse);
        addMouseWheelListener(d_mouse);        
    }
    
    public static ClipBoard getClipBoard() {
        return d_clipBoard;
    }
    
    public void cleanUp() {
        d_diagram = null;
        d_currentMacro = null;
//        d_invertViewMatrix = null;
//        d_mouseData = null;
//        d_selectionBox = null;
//        d_selectionStrategy = null;
        d_objectList = null;
        d_tempObjectList = null;
    }

    public IToolStrategy getSelectionStrategy() {
        return d_selectionStrategy;
    }
    
    
    public IToolStrategy getZoomWindowStrategy() {
        return d_zoomStrategy;
    }
    
    public void transformScreenToWorld(Point2D.Float screen, Point2D.Float world) {
        d_invertViewMatrix.transform(screen, world);
    }
    
    public IDiagram getDiagram() {
        return d_diagram;
    }
    
    public void clearSelection() {
        if(!d_bToggleSelection) {
            for(IDiaObject o : d_selectionList) {
                o.deSelect();
            }
            d_selectionList.clear();
        }
        repaint();
    }
    
    public void deleteSelected() {
        d_selectionBox.cancel();
        d_diagram.deleteAllInList(d_selectionList);
        d_selectionList.clear();
        repaint();
    }
    
    public void invertSelection() {
        d_objectList = d_diagram.getObjectList();
        for(IDiaObject o : d_objectList) {
            select(o, true);
        }
    }
    
    public void selectAll() {
        d_objectList = d_diagram.getObjectList();
        for(IDiaObject o : d_objectList) {
            select(o, false);
        }
    }
    
    public void pasteSelected() {
        // TODO - Add the paste code
    }
    
    public void copySelected() {
        // TODO - Add the copy code
    } 
    
    private void selectByBox() {
        clearSelection();
        d_objectList = d_diagram.getObjectList();
        for(IDiaObject o : d_objectList) {
            if(o.rectTest(d_selectionBox.getBounds(), d_selectionBox.isInclusiveOnly())) {
                select(o, d_bToggleSelection);
            }
        }
    }
    
    private boolean selectByClick() {
        clearSelection();
        d_objectList = d_diagram.getObjectList();
        for(IDiaObject o : d_objectList) {
            if(o.hitTest(d_mouseData.getCurrentPointInWorld())) {
                select(o, d_bToggleSelection);
                return true;
            }
        }
        return false;
    }
    
    public boolean hasSelectedObjects() {
        return !d_selectionList.isEmpty();
    }
    
    private void select(IDiaObject o, boolean toggleSelection) {
        if(toggleSelection && o.isSelected()) {
            d_selectionList.remove(o);
            o.deSelect();
            return;
        }
        d_selectionList.add(o);
        o.select();
    }
 
    public void setMacro(IToolMacro m) {
        if(d_currentMacro != d_defaultMacro) {
            d_previousMacro = d_currentMacro.getID();
        }
        d_currentMacro.getCurrentStrategy(null).onCancel(this);
        d_currentMacro = m;
    }
    
    public MacroRegistry.MacroID getPrevMacroID() {
        return d_previousMacro;
    }
    
    public void clearMacro() {
        if(d_currentMacro != d_defaultMacro) {
            d_previousMacro = d_currentMacro.getID();
        }
        d_currentMacro.getCurrentStrategy(null).onCancel(this);
        d_currentMacro = d_defaultMacro;
        repaint();
    }
    
    public boolean isDefualtMacroCurrent() {
        return d_currentMacro == d_defaultMacro;
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
    
    private void zoomWheel(int steps) {
        final double FACTOR = Math.sqrt(Math.sqrt(2)); 
        for (int i = 1; i <= steps; i++) {
            d_fZoom *= FACTOR;
        }
        for (int i = 1; i <= -steps; i++) {
            d_fZoom /= FACTOR;
        }
        d_fZoom = (float)Math.max(0.2, d_fZoom); // Lock the scale to a certain value
        d_fZoom = (float)Math.min(8.0, d_fZoom); // Lock the scale to a certain value
        repaint();
    }
    
    private void zoomExtents() {
        Rectangle2D.Float bounds = d_zoomExtentsRect.getBounds();
        
        if(hasSelectedObjects()) {
            d_objectList = d_selectionList;
        } else {
            d_objectList = d_diagram.getObjectList();
            if(d_objectList.isEmpty()) {  // Nothing to zoom to, so just zoom to fit the window
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
        
        boolean first = true;
        for(IDiaObject o: d_objectList) {
            if(first) {
                bounds.x = (float)o.getBounds().getX();
                bounds.y = (float)o.getBounds().getY();
                first = false;
            }
            bounds.add(o.getBounds());
        }
        
        GeometricUtil.padRect(bounds);
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
        
        repaint();
    }
    
    @Override
    public void paintComponent(Graphics b) {
        setCursor(d_cursor);
        // get a copy of the Graphics
        Graphics2D g = (Graphics2D)b.create();
        
        // Clear the Screen
        g.setColor(EditorProperties.s_backGroundColor);
        g.fillRect(0, 0, getWidth(), getHeight());
        
          // Apply the viewing tranformation matrix
        g.translate(getWidth() / 2.0f, getHeight() / 2.0f);
        g.scale(d_fZoom, d_fZoom);
        g.translate(-getWidth() / 2.0f, -getHeight() / 2.0f);
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
        
        if(d_diagram != null) {
            d_diagram.draw(g);
        }
        
        if(d_zoomExtentsRect.isActive()) {
            d_zoomExtentsRect.draw(g, Color.white);
        }
        
        if(d_selectionBox.isActive()) {
            d_selectionBox.draw(g);
        }
        
        // TODO - Test!!
        g.translate(d_tempObjectPosition.x, d_tempObjectPosition.y);
        for(IDiaObject o : d_tempObjectList) {
            o.draw(g, true);
        }
        
        g.dispose();
    }
    
    // TODO - Clean up and set up the ToolStrategy for left and right mouse button.
    private class MouseListener extends MouseAdapter implements MouseWheelListener {
        @Override
        public void mouseClicked(MouseEvent e) {
            if(e.getButton() == EditorProperties.d_iZoomExtentsButton) {
                if(e.getClickCount() == 2) {
                    DiagramEditor.this.zoomExtents();
                }
            }
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            DiagramEditor.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());
            
            if(DiagramEditor.this.d_currentMacro.getCurrentStrategy(DiagramEditor.this).onMove(d_mouseData, DiagramEditor.this)) {
                DiagramEditor.this.repaint();
            }
        }
        
        @Override
        public void mouseDragged(MouseEvent e) {
            DiagramEditor.this.d_mouseData.setLastPosScreen(DiagramEditor.this.d_mouseData.getCurrentPointInScreen());
            DiagramEditor.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());     
            if(d_bPanning) {
                DiagramEditor.this.setPan(e.getX(), e.getY());
                repaint();
            } else {
                if(DiagramEditor.this.d_currentMacro.getCurrentStrategy(DiagramEditor.this).onDrag(d_mouseData, DiagramEditor.this)) {
                    DiagramEditor.this.repaint();
                }
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            DiagramEditor.this.d_mouseData.setLastPosScreen(DiagramEditor.this.d_mouseData.getCurrentPointInScreen());
            DiagramEditor.this.d_mouseData.setCurrentDownButton(0);
            
            if(e.getButton() == EditorProperties.d_iPanButton) {
                DiagramEditor.this.endPan();
                DiagramEditor.this.repaint();
            } else if(e.getButton() == EditorProperties.d_iMainButton) {
                if(DiagramEditor.this.d_currentMacro.getCurrentStrategy(null).onMainButtonRelease(d_mouseData, DiagramEditor.this)) {
                    DiagramEditor.this.repaint();
                }
            } else if(e.getButton() == EditorProperties.d_iSecondaryButton) {
                if(DiagramEditor.this.d_currentMacro.getCurrentStrategy(null).onSecondaryButtonRelease(d_mouseData, DiagramEditor.this)) {
                    DiagramEditor.this.repaint();
                }
            }
        }

        @Override
        public void mousePressed(MouseEvent e) {
            DiagramEditor.this.d_mouseData.setCurrentDownButton(e.getButton());
            DiagramEditor.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY()); // TODO - Fix
//            DiagramEditor.this.d_mouseData.setLastPosScreen(DiagramEditor.this.d_mouseData.getCurrentPointInScreen());
            
            // Check to see if the ctrl button is down
            if(e.isControlDown()) {
                DiagramEditor.this.d_bToggleSelection = true;
            } else {
                DiagramEditor.this.d_bToggleSelection = false;
            }
            
            if(e.getButton() == EditorProperties.d_iPanButton) {
                DiagramEditor.this.startPan();
                DiagramEditor.this.repaint();
            } else if(e.getButton() == EditorProperties.d_iMainButton) {
                if(DiagramEditor.this.d_currentMacro.getCurrentStrategy(null).onMainButtonPress(d_mouseData, DiagramEditor.this)) {
                    DiagramEditor.this.repaint();
                }
            } else if(e.getButton() == EditorProperties.d_iSecondaryButton) {
                if(DiagramEditor.this.d_currentMacro.getCurrentStrategy(null).onSecondaryButtonPress(d_mouseData, DiagramEditor.this)) {
                    DiagramEditor.this.repaint();
                }
            }
        }
        
        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            DiagramEditor.this.d_mouseData.setCurrentPosScreen(e.getX(), e.getY());
            DiagramEditor.this.zoomWheel(-e.getWheelRotation());
        }
    }
    
    private class SelectionStrategy implements IToolStrategy {
        @Override
        public boolean onMainButtonPress(IMouseData d, DiagramEditor e) {
            if(DiagramEditor.this.d_selectionBox.isActive()) {
                DiagramEditor.this.selectByBox();
                DiagramEditor.this.d_selectionBox.cancel();
                return true;
            }
            else {
                DiagramEditor.this.clearSelection();
                if(!DiagramEditor.this.selectByClick()) {
                    DiagramEditor.this.d_selectionBox.setFirstCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
                }
                return true;
            }
        }

        @Override
        public boolean onSecondaryButtonPress(IMouseData d, DiagramEditor e) {
            if(DiagramEditor.this.d_selectionBox.isActive()) {
                DiagramEditor.this.clearSelection();
                DiagramEditor.this.d_selectionBox.cancel();
                return true;
            }
            return false;
        }

        @Override
        public void onCancel(DiagramEditor e) {
            DiagramEditor.this.d_selectionBox.cancel();
        }


        @Override
        public boolean onDrag(IMouseData d, DiagramEditor e) {
            if(d.getCurrentDownButton() == EditorProperties.d_iMainButton) {
                return onMove(d, e);
            }
            return false;
        }

        @Override
        public boolean onMove(IMouseData d, DiagramEditor e) {
            if(DiagramEditor.this.d_selectionBox.isActive()) {
                DiagramEditor.this.d_selectionBox.setSecondCoords((int)d.getCurrentPointInWorld().x, (int)d.getCurrentPointInWorld().y);
                DiagramEditor.this.selectByBox();
                return true;
            }
            return false;
        }

        @Override
        public boolean onMainButtonRelease(IMouseData d, DiagramEditor e) {
            return false;
        }

        @Override
        public boolean onSecondaryButtonRelease(IMouseData d, DiagramEditor e) {
            return false;
        }
    }
    
    private class ZoomWindowStrategy implements IToolStrategy {
        @Override
        public boolean onMainButtonPress(IMouseData d, DiagramEditor v) {
            if(!DiagramEditor.this.d_zoomExtentsRect.isActive()) {// Start the extents window
                DiagramEditor.this.d_zoomExtentsRect.setFirstCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
            } else { // finish the zoom window
                DiagramEditor.this.d_zoomExtentsRect.setSecondCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
                DiagramEditor.this.zoomToRect(DiagramEditor.this.d_zoomExtentsRect.getBounds());
                DiagramEditor.this.d_zoomExtentsRect.cancel();
                DiagramEditor.this.clearMacro();
            }
            return true;
        }
        
        @Override
        public boolean onSecondaryButtonPress(IMouseData d, DiagramEditor v) {
            if(DiagramEditor.this.d_zoomExtentsRect.isActive()) {
               DiagramEditor.this.d_zoomExtentsRect.cancel(); 
            }
            return true;
        }

        @Override
        public void onCancel(DiagramEditor v) {
            DiagramEditor.this.d_zoomExtentsRect.cancel();
        }

        @Override
        public boolean onDrag(IMouseData d, DiagramEditor v) {
            return false;
        }

        @Override
        public boolean onMove(IMouseData d, DiagramEditor v) {
            if(DiagramEditor.this.d_zoomExtentsRect.isActive()) {
                DiagramEditor.this.d_zoomExtentsRect.setSecondCoords(d.getCurrentPointInWorld().x, d.getCurrentPointInWorld().y);
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean onSecondaryButtonRelease(IMouseData d, DiagramEditor e) {
            return false;
        }


        @Override
        public boolean onMainButtonRelease(IMouseData d, DiagramEditor e) {
            return false;
        }
    }
    
    private final class MouseData implements IMouseData {
        private Point2D.Float d_currentPosWorld = new Point2D.Float();
        private Point2D.Float d_currentPosScreen = new Point2D.Float();
        private Point2D.Float d_lastPosWorld = new Point2D.Float();
        private Point2D.Float d_lastPosScreen = new Point2D.Float();
        private int d_currentButtonDown;
        
        public void setCurrentDownButton(int b) {
            d_currentButtonDown = b;
        }
                
        @Override
        public int getCurrentDownButton() {
            return d_currentButtonDown;
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
            DiagramEditor.this.transformScreenToWorld(d_lastPosScreen, d_lastPosWorld);
            return d_lastPosWorld;
        }

        @Override
        public Point2D.Float getCurrentPointInScreen() {
            return d_currentPosScreen;
        }

        @Override
        public Point2D.Float getCurrentPointInWorld() {
            DiagramEditor.this.transformScreenToWorld(d_currentPosScreen, d_currentPosWorld);
            return d_currentPosWorld;
        }
    }
}
