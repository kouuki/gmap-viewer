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
  * Menu action allowing the user to save the current state of the GUI.
  *@author bill
  */
public class JMenuActionDrivingWindow extends JMenuAction{
   /**Constructor for the save state object.
    * @param registeredObject The object to be registered with this menu action.
    */
   public JMenuActionDrivingWindow(GUI registeredObject) {
      super("Driving Window",registeredObject);
   }
   /** Set the new cache directory and create necessary sub-directories for it */
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      DrivingWindow dw = new DrivingWindow(gui, gui.getGMap());
      dw.setSize(gui.getWidth(), gui.getHeight());
      dw.setLocation(gui.getX(), gui.getY());
      dw.setVisible(true);

      dw.getGContentPane().setCenter(pane.getCenter());
      dw.getGContentPane().setZoom(pane.getZoom());
      dw.getGContentPane().draw();
   }
}