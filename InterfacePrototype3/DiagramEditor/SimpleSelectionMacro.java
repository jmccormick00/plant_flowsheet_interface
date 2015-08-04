/*------------------------------------------------------------------------------
 * SimpleSelectionMacro.java
 * Author: James McCormick
 * Description: A basic macro that checks for selections if none, then loads
 * the selection strategy before the real strategy.
 *----------------------------------------------------------------------------*/
package DiagramEditor;

import Application.Console;

// TODO - implement
public class SimpleSelectionMacro extends IToolMacro {

    private IToolStrategy d_strategy;
    private IToolStrategy d_selectionStrategy;
    private String d_sVerb;
    
    public void setManipulatorStrategy(IToolStrategy m) {
        d_strategy = m;
    }
    
    public void setSelectionStrategy(IToolStrategy m) {
        d_selectionStrategy = m;
    }
    
    // Sets the verb that describes the manipulation for Console output
    public void setManipulatingVerb(String s){
        d_sVerb = s;
    }
    
    @Override
    public IToolStrategy getCurrentStrategy(DiagramEditor d) {
        if(d.hasSelectedObjects())
            return d_strategy;
        else 
            return d_selectionStrategy;
    }

    @Override
    public void onCurrentMacro(DiagramEditor d) {
        if(!d.hasSelectedObjects())
            Console.getInstance().printLine("Please select the item(s) to " + d_sVerb + ".");
    }
    
}
