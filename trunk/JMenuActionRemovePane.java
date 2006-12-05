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

/**
 * Removes a pane from the menu.
 */
public class JMenuActionRemovePane extends JMenuAction{
   /**
    * Creates a JMenuAction.
    * @param registeredObject GUI
    */
   public JMenuActionRemovePane(GUI registeredObject){super("Remove Pane",registeredObject);}

   /**
    * Removes the pane from the menu.
    */
   public void run(){
      GUI gui = (GUI)registeredObject;
//      String in = (String)JOptionPane.showInputDialog(gui.frame, "Remove Pane","Are you sure you want to remove this pane?",JOptionPane.PLAIN_MESSAGE,null,null,in);

      gui.removePane(gui.getTopPane());
//      gui.addPane();
   }
}
