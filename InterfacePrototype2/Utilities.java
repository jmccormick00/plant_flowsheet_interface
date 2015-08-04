
package interfaceprototype2;

import java.awt.geom.Rectangle2D;

public class Utilities {
    public static void padRect(Rectangle2D.Float rect) {
        final float padPerc = 0.05f;
        final float paddingX = rect.width * padPerc;
        final float paddingY = rect.height * padPerc;
        rect.x = rect.x - paddingX;
        rect.y = rect.y - paddingY;
        rect.width += (2 * paddingX);
        rect.height += (2 * paddingY);
    }
}
