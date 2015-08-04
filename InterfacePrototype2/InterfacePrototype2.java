/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaceprototype2;

import javax.swing.JFrame;
import java.awt.BorderLayout;
import javax.swing.JTabbedPane;
import javax.swing.JMenuBar;
import javax.swing.JToolBar;
import javax.swing.JButton;
import javax.swing.Action;
import java.awt.event.ActionEvent;
import javax.swing.JOptionPane;
import javax.swing.AbstractAction;
import javax.swing.JMenu;

import java.awt.RenderingHints;
import java.awt.Graphics2D;

import java.awt.event.KeyEvent;
import java.awt.KeyboardFocusManager;
import java.awt.KeyEventDispatcher;

public class InterfacePrototype2 extends JFrame {
    
    private JTabbedPane d_tabbedPane = new JTabbedPane();
    
    public InterfacePrototype2() {
        // Set up the window
        super("InterfacePrototype2");
        setSize(600, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventDispatcher(new KeyBoardDispatcher());
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(createFileMenu());
        setJMenuBar(menuBar);
        
        add(createToolBar(), BorderLayout.NORTH);
        
        getContentPane().add(d_tabbedPane);
        
        setVisible(true);
        Graphics2D g = (Graphics2D)getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
    }
    
    private JMenu createFileMenu() {
        Action createTabAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addNewTab();
            }  
        };
        createTabAction.putValue(Action.NAME, "New Tab");
        createTabAction.putValue(Action.SHORT_DESCRIPTION, "Creates a new tab");
        createTabAction.putValue(Action.MNEMONIC_KEY, new Integer('n'));
        
        Action deleteTabAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteNewTab();
            }  
        };
        deleteTabAction.putValue(Action.NAME, "Delete Tab");
        deleteTabAction.putValue(Action.SHORT_DESCRIPTION, "Deletes current tab");
        deleteTabAction.putValue(Action.MNEMONIC_KEY, new Integer('d'));
        
        JMenu fileMenu = new JMenu("File");
        fileMenu.add(createTabAction);
        fileMenu.add(deleteTabAction);
        fileMenu.setMnemonic('F');
        
        return fileMenu;
    }
    
    private FSViewer getCurrentTab() {
        return (FSViewer)d_tabbedPane.getSelectedComponent();
    }
    
    private JToolBar createToolBar() {
        Action moveAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //((FSViewer)d_tabbedPane.getSelectedComponent()).setMacro();
            }  
        };
        moveAction.putValue(Action.NAME, "Move");
        moveAction.putValue(Action.SHORT_DESCRIPTION, "Moves the selected items");
        
        Action deleteAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getCurrentTab() != null)
                    getCurrentTab().deleteSelected();
            }  
        };
        deleteAction.putValue(Action.NAME, "Delete");
        deleteAction.putValue(Action.SHORT_DESCRIPTION, "Deletes the selected items");
        
        Action createAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final CreateFSElementStrategy t = new CreateFSElementStrategy();  // TODO - Make this a singleton flywheel so it doesnt have to be created all the time 
                IMacro m = new IMacro() {
                    CreateFSElementStrategy d_tool = t;

                    @Override
                    public IToolStrategy getCurrentStrategy() {
                        return d_tool;
                    }

                    
                    @Override
                    public void onCurrentMacro(FSViewer v) {}
                };
                if(InterfacePrototype2.this.assignMacro(m))
                    InterfacePrototype2.this.getCurrentTab().clearSelection();
            }  
        };
        createAction.putValue(Action.NAME, "Create");
        createAction.putValue(Action.SHORT_DESCRIPTION, "Creates a block");
        
        Action copyAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }  
        };
        copyAction.putValue(Action.NAME, "Copy");
        copyAction.putValue(Action.SHORT_DESCRIPTION, "Copies the selected items");
        
        Action printAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                
            }  
        };
        printAction.putValue(Action.NAME, "Print");
        printAction.putValue(Action.SHORT_DESCRIPTION, "Prints current Flowsheet");
        
        Action zoomWindowAction = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(getCurrentTab() != null)
                    getCurrentTab().startZoomWindow();
            }  
        };
        zoomWindowAction.putValue(Action.NAME, "Zoom Window");
        zoomWindowAction.putValue(Action.SHORT_DESCRIPTION, "Zooms to a user defined window");
        
        
        JButton button1 = new JButton(moveAction);
        JButton button2 = new JButton(deleteAction);
        JButton button3 = new JButton(createAction);
        JButton button4 = new JButton(copyAction);
        JButton button5 = new JButton(printAction);
        JButton button6 = new JButton(zoomWindowAction);
        
        JToolBar toolBar = new JToolBar("Interface");
        toolBar.setRollover(true);
        toolBar.add(button1);
        toolBar.add(button2);
        toolBar.add(button3);
        toolBar.add(button4);
        toolBar.add(button5);
        toolBar.add(button6);
        
        return toolBar;
    }
    
    private void addNewTab() {
        String name = JOptionPane.showInputDialog(this, "Enter the name of the new flowsheet:", null, JOptionPane.PLAIN_MESSAGE);
        if(name == null) return;
        while(name.isEmpty()) {
            name = JOptionPane.showInputDialog(this, "Enter the name of the new flowsheet:", "Name cannot be empty", JOptionPane.ERROR_MESSAGE);
            if(name == null) return;
        }
        d_tabbedPane.add(name, new FSViewer());
    }
    
    private void deleteNewTab() {  
        FSViewer v = getCurrentTab();
        if(v == null) {
            JOptionPane.showMessageDialog(this, "No tab to delete.");
            return;
        }
        int b = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete the current tab?", "" ,JOptionPane.YES_NO_OPTION);
        if(b != JOptionPane.YES_OPTION) return;
        int i = d_tabbedPane.getSelectedIndex();
        v.cleanUp();
        d_tabbedPane.remove(i);
    }
    
    private boolean assignMacro(IMacro m) {
        FSViewer v = (FSViewer)d_tabbedPane.getSelectedComponent();
        if(v != null) {
            v.setMacro(m);
            return true;
        }
        return false;
    }
    
    private class KeyBoardDispatcher implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            FSViewer v = (FSViewer)d_tabbedPane.getSelectedComponent();
            switch(e.getKeyCode()) {
                case KeyEvent.VK_ESCAPE: {
                    v.clearSelection();
                    v.clearMacro();
                    return true;  // Dont forward to focused compenent
                }
                case KeyEvent.VK_DELETE: {
                    v.deleteSelected();
                    return true;
                }
            }
            return false;
        }
    }
    
    public static void main(String[] args) {
        new InterfacePrototype2();
    }
}
