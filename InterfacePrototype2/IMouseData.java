/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import java.awt.geom.Point2D;

public interface IMouseData {
    public Point2D.Float getLastPointInScreen();
    public Point2D.Float getLastPointInWorld();
    public Point2D.Float getCurrentPointInScreen();
    public Point2D.Float getCurrentPointInWorld();
}
