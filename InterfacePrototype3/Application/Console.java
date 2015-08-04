/*------------------------------------------------------------------------------
 * Console.java
 * Author: James McCormick
 * Description: A singleton for handling the console.  A mechanism for showing
 * messages to the user.
 *----------------------------------------------------------------------------*/
package Application;

import javax.swing.JTextArea;

public class Console {
    private JTextArea d_textArea;
    private boolean d_first = true;
    
    private Console() {}
    
    private static class SingletonHolder {
        public static final Console d_instance = new Console();
    }
    
    public static Console getInstance() {
        return SingletonHolder.d_instance;
    }
    
    public void setTextArea(JTextArea t) {
        d_textArea = t;
    }
    
    public void printLine(String line) {
        if(d_first){
            d_textArea.append(line);
            d_first = false;
        } else {
           d_textArea.append("\n" + line); 
        }
    }
    
    public void addToCurrentLine(String s) {
        d_textArea.append(" " + s);
    }
}
