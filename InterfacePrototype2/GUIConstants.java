/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import java.awt.BasicStroke;

public class GUIConstants {
    private static final float[] dashed = { 2f, 0f, 2f};
    public static final BasicStroke DASHEDLINE = new BasicStroke(1, 
            BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 1.0f, dashed, 2f);
    
    public static final BasicStroke SOLIDLINE = new BasicStroke();
}
