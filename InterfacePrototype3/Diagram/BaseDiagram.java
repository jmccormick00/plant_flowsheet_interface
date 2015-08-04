/*------------------------------------------------------------------------------
 * BaseDiagram.java
 * Author: James McCormick
 * Description: The base class for a diagram.
 *----------------------------------------------------------------------------*/

package Diagram;

import java.awt.Graphics2D;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;

// TODO - implement this class
// TODO - make this abstract
public class BaseDiagram implements IDiagram {
    
    private LinkedList<IDiaObject> d_objectList = new LinkedList<IDiaObject>();
    private String d_name;
    
    public BaseDiagram(String name) {
        d_name = name;
    }
    
    @Override
    public String getName() {
        return d_name;
    }
    
    @Override
    public void addNode(BaseNode node) {
        d_objectList.add(node);
    }
    
//    public void makeConnection(ILink link, BaseNode n1, BaseNode n2); TODO add this later
    
    @Override
    public void draw(Graphics2D g) {
        for(IDiaObject n : d_objectList)
            n.draw(g, false);
    }

    @Override
    public void deleteAllInList(Collection<IDiaObject> l) {
        d_objectList.removeAll(l);
    }
    
    @Override
    public void deleteObject(IDiaObject obj) { // TODO - do i need this.  maybe just need the deleteSelected()
        d_objectList.remove(obj);
    }

    @Override
    public Collection<IDiaObject> getObjectList() {
        return Collections.unmodifiableCollection(d_objectList);
    }
    
//    @Override
//    public Collection<BaseNode> getNodeList() {
//        return d_object; 
//    }
    
    
//    @Override
//    public Collection<BaseNode> getSelectedNodeList() {
//        return d_selectedNodeList;
//    }
    
    @Override
    public Collection<IDiaObject> copySelected() {  // TODO - implement, and move to editor
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Collection<IDiaObject> cutSelected() {   // TODO - implement, and move to the editor
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void pasteSelected(Collection<IDiaObject> c) {   // TODO - implement, and move to the editor
        throw new UnsupportedOperationException("Not supported yet.");
    }
}