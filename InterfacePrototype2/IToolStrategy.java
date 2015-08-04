/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;


public interface IToolStrategy {
    public boolean onPress(IMouseData d, FSViewer v);
    public boolean onDrag(IMouseData d, FSViewer v);
    public boolean onRelease(IMouseData d, FSViewer v);
    public boolean onMove(IMouseData d, FSViewer v);
    public void onCancel(FSViewer v);
    public void execute(); // TODO - DO i need this?
}
