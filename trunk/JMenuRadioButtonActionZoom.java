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
  * Menu action allowing the user to set the current pane's zoom level.
  *@author bill
  */
public class JMenuRadioButtonActionZoom extends JMenuRadioButtonAction{
   //stuff we need to know
   private int zoomLevel;


   /**Constructor for JMenuRadioButtonActionZoom class.
    *@param name The visual representation of this object in a menu.
    *@param registeredObject The object to be registered with this menu action.
    *@param zoomLevel The int value corresponding to this zoom level.
    */
   public JMenuRadioButtonActionZoom(String name, GUI registeredObject, int zoomLevel){
      super(name,registeredObject);
      this.zoomLevel = zoomLevel;
   }

   /** Modify the zoom level. */
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      //if this doesnt turn it off, turn it on and set level
      if(zoomLevel - pane.getShowCachedZoomLevel() > 4) pane.setShowCachedZoom(false);
      pane.setZoom(zoomLevel);
      pane.draw();
   }

   /** On pane events, update the state of this object to reflect the status of the top level pane.
    *@param object The object that fired this pane event.
    */

   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if (pane == null) return;

      int currentZoom = pane.getZoom();
      if(currentZoom == zoomLevel) super.setSelected(true);
      else super.setSelected(false);
   }


}
