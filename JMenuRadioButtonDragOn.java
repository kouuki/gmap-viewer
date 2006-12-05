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

/** Class defining the radio button action for map dragging. It extends JMenuRadioButtonAction */
public class JMenuRadioButtonDragOn extends JMenuRadioButtonAction{
   /** 
    * Constructor for the radio button action
    * @param registeredObject The object to recieve the action
    */
   public JMenuRadioButtonDragOn(GUI registeredObject){
      super("Map Dragging",registeredObject);
   }

   /** Method to perform the necessary tasks for the button action */
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      pane.setMode(GPane.DRAGGING_MODE);
      super.setSelected(true);
      gui.getNotifier().firePaneEvent(this);
      gui.getProgressMeter().getPanel().repaint();
   }

   /** 
    * Method to define a listener for a given object
    * @param object  The object to listen for the event
    */
   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;
      int currentZoom = pane.getShowCachedZoomLevel();
      super.setSelected(pane.getMode() == GPane.DRAGGING_MODE);
   }

}
