/*------------------------------------------------------------------------------
 * IToolStrategy.java
 * Author: James McCormick
 * Description: The base class for a tool strategy.  Classes will extend this
 * in order to carry about the logic for each tool on mouse operations.  
 * The callback functions return true if they were successfull and the
 * macro should move to the next state.  False if otherwise
 *----------------------------------------------------------------------------*/

package DiagramEditor;


public interface IToolStrategy {
    
    // The CallBack to be performed when the main mouse button is pressed 
    public boolean onMainButtonPress(IMouseData d, DiagramEditor e);
    
    // The Callback to be performed when the secondary mouse button is pressed
    public boolean onSecondaryButtonPress(IMouseData d, DiagramEditor e);
    
    // The Callback to be performed when the mouse is dragged
    public boolean onDrag(IMouseData d, DiagramEditor e);
    
    // The Callback to be performed when the main mouse button is released
    public boolean onMainButtonRelease(IMouseData d, DiagramEditor e);
    
    // The Callback to be performed when the secondary button is released
    public boolean onSecondaryButtonRelease(IMouseData d, DiagramEditor e);
    
    // The Callback to be performed when the mouse moves
    public boolean onMove(IMouseData d, DiagramEditor e);
    
    // The Callback to be performed when the strategy has been canceled
    public void onCancel(DiagramEditor e);
}
