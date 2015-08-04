
package Diagram;

import java.awt.geom.Point2D;


public class DiagramFactory {
    private static DiagramFactory d_instance = null;
    
    private DiagramFactory() {
       
    } 
   
    public static DiagramFactory getFactory() {
        if(d_instance == null) {
            d_instance = new DiagramFactory();
        }
        return d_instance;
    }
    
    public IDiaObject createElement(int typeID, Point2D p) {
       // return new TestBlock(p);
        return null;
    }
}
