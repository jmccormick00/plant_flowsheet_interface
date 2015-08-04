/*------------------------------------------------------------------------------
 * AppUtility.java
 * Author: James McCormick
 * Description: Global utility class for the application.  This allows certain
 * bits to be set or settings changed without having to make Application
 * global.
 *----------------------------------------------------------------------------*/
package Application;


public class AppUtility {
    private AppUtility() {}
    
    private static boolean d_captureSpaceBar = false;
    public static void setCaptureSpaceBar(boolean b) {
        d_captureSpaceBar = b;
    }
    
    public static boolean getCaptureSpaceBar() {
        return d_captureSpaceBar;
    }
}
