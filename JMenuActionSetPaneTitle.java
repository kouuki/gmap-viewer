import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.awt.image.*;
import javax.imageio.*;
import java.beans.*; //Property change stuff
import javax.swing.table.*;
import com.sun.image.codec.jpeg.*;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.geom.*;

class JMenuActionSetPaneTitle extends JMenuAction{
   public JMenuActionSetPaneTitle(GUI registeredObject){super("Set Pane Title",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      String newTitle = gui.getTabbedPane().getTitleAt(gui.getTabbedPane().getSelectedIndex());
      String message = "";
      newTitle = (String)JOptionPane.showInputDialog(gui.frame, "Enter new title. "+message,"Set Pane Title",JOptionPane.PLAIN_MESSAGE,null,null,newTitle);
      if(newTitle != null) gui.getTabbedPane().setTitleAt(gui.getTabbedPane().getSelectedIndex(), newTitle);
   }
}
