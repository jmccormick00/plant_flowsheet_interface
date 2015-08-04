/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import java.awt.geom.Point2D;

/**
 *
 * @author jmccormick
 */
public class ElementFactory {
    private static ElementFactory d_instance = null;
    
    private ElementFactory() {
       
    } 
   
    public static ElementFactory getFactory() {
        if(d_instance == null) {
            d_instance = new ElementFactory();
        }
        return d_instance;
    }
    
    public FSElement createElement(int typeID, Point2D p) {
        return new TestBlock(p);
    }
            
   
}
