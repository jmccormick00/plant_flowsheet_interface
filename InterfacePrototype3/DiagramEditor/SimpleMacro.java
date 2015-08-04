/*------------------------------------------------------------------------------
 * SimpleMacro.java
 * Author: James McCormick
 * Description: A basic macro with a single strategy.
 *----------------------------------------------------------------------------*/
package DiagramEditor;


public class SimpleMacro extends IToolMacro {

    private IToolStrategy d_strategy;
    
    final public void setStrategy(IToolStrategy m) {
        d_strategy = m;
    }
        
    @Override
    public IToolStrategy getCurrentStrategy(DiagramEditor d) {
        return d_strategy;
    }

    @Override
    public void onCurrentMacro(DiagramEditor d) {}
}
