/*------------------------------------------------------------------------------
 * IDiagram.java
 * Author: James McCormick
 * Description: The interface for a diagram.
 *----------------------------------------------------------------------------*/

package Diagram;

import java.awt.Graphics2D;
import java.util.Collection;


public interface IDiagram {
    
    public String getName();
    
    public void addNode(BaseNode node);
    
//    public void addLink(ILink link);
    
//    public void makeConnection(ILink link, BaseNode n1, BaseNode n2); TODO add this later
    
    public void draw(Graphics2D g);
    
    public void deleteAllInList(Collection<IDiaObject> l);
    
//    public void deleteLink(ILink link);
    
    public void deleteObject(IDiaObject obj);
    
    public Collection<IDiaObject> getObjectList();
    
//    public Collection<BaseNode> createNodeList();
    
//    public Collection<ILink> createLinkList();
    
//    public Collection<BaseNode> createSelectedNodeList();
    
//    public Collection<ILink> createSelectedLinkList();
    
    // TODO - move these to the editor
    // Returns a list of objects to copy
    public Collection<IDiaObject> cutSelected();
    
    public Collection<IDiaObject> copySelected();
    
    public void pasteSelected(Collection<IDiaObject> c);
}
