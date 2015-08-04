/*------------------------------------------------------------------------------
 * IToolMacro.java
 * Author: James McCormick
 * Description: The base class for a tool macro.  Performs the logic to get the
 * current strategy to use.
 *----------------------------------------------------------------------------*/

package DiagramEditor;


public abstract class IToolMacro {
    private MacroRegistry.MacroID d_ID;
    final public MacroRegistry.MacroID getID() {
        return d_ID;
    }
    
    final public void setID(MacroRegistry.MacroID id) {
        d_ID = id;
    }
    
    public abstract IToolStrategy getCurrentStrategy(DiagramEditor d);
    public abstract void onCurrentMacro(DiagramEditor d);
}
