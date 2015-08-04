/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

/**
 *
 * @author jmccormick
 */
public class CreateFSElementStrategy implements IToolStrategy {

    @Override
    public boolean onDrag(IMouseData d, FSViewer v) {
        return false;
    }

    @Override
    public boolean onMove(IMouseData d, FSViewer v) {
        return false;
    }

    @Override
    public boolean onPress(IMouseData d, FSViewer v) {
        v.addElement(ElementFactory.getFactory().createElement(0, d.getCurrentPointInWorld()));
        v.clearMacro();
        return true;
    }

    @Override
    public boolean onRelease(IMouseData d, FSViewer v) {
        return false;
    }

    @Override
    public void onCancel(FSViewer v) {}

    @Override
    public void execute() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
