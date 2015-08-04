/*------------------------------------------------------------------------------
 * IDGenerator.java
 * Author: James McCormick
 * Description: Generates new ID's
 *----------------------------------------------------------------------------*/

package Util;

import java.util.UUID;

public class IDGenerator {
    public static String getNewID() {
        return UUID.randomUUID().toString();
    }
}