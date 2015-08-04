/*------------------------------------------------------------------------------
 * ToolRegistry.java
 * Author: James McCormick
 * Description: A registry to get a macro from.  Creates and sets up a macro.
 *----------------------------------------------------------------------------*/
package DiagramEditor;


public class MacroRegistry {
    public enum MacroID {
        CREATE, MOVE, ZOOMWINDOW, DELETE, COPY, PASTE, CUT
    }
    
    private SimpleMacro d_simpleMacro = new SimpleMacro();
    private SimpleSelectionMacro d_simpleSelectionMacro = new SimpleSelectionMacro();
    
    public IToolMacro getMacro(MacroID id, DiagramEditor d) throws Exception {
        switch(id) {
            case CREATE: {
                
            }
            case MOVE: {
                
            }
            case ZOOMWINDOW: {
                d_simpleMacro.setStrategy(d.getZoomWindowStrategy());
                d_simpleMacro.setID(id);
                return d_simpleMacro;
            }
            case DELETE: {
                d_simpleSelectionMacro.setManipulatingVerb("delete");
                d_simpleSelectionMacro.setSelectionStrategy(d.getSelectionStrategy());
                d_simpleSelectionMacro.setID(id);
                //d_simpleSelectionMacro.setManipulatorStrategy(d); // TODO - make a delete strategy
            }
            case COPY: {
                
            }
            case PASTE: {
                
            }
            case CUT: {
                
            }
            default: {
                throw new Exception("MacroID Undefined.");
            }
        }// end switch
    }
}
