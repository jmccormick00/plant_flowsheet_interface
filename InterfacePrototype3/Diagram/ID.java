/*------------------------------------------------------------------------------
 * ID.java
 * Author: James McCormick
 * Description: A class representing an objects ID.
 *----------------------------------------------------------------------------*/

package Diagram;

import Util.IDGenerator;

public class ID {
    private String d_id;
    
    public ID() {
        d_id = IDGenerator.getNewID();
    }
    
    public ID(String id) {
        d_id = id;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;
        if(d_id != null) return d_id.equals(((ID)obj).d_id);
        return false;
    }

    @Override
    public int hashCode() {
        if(d_id != null) {
            return d_id.hashCode();
        }
        return super.hashCode();
    }

    @Override
    public String toString() {
        if(d_id != null)
            return d_id;
        return super.toString();
    }
    
}