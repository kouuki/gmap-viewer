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
 * Creates a radio button for setting the cache zoom level.  It also the events associated with it.
 * @author aaday
 *
 */
public class JMenuRadioButtonActionCache extends JMenuRadioButtonAction{
   //stuff we need to know
   /**
    * The current zoom level.
    */
   int zoomLevel;

   /**
    * Creates a radio button for setting the cache zoom level.
    *
    * @param name Name of radio button
    * @param registeredObject GUI
    * @param zoomLevel The current zoom level
    */
   public JMenuRadioButtonActionCache(String name, GUI registeredObject, int zoomLevel){
      super(name,registeredObject);
      this.zoomLevel = zoomLevel;
   }

   /**
    * Handles the event for the radio button.
    */
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      //if this doesnt turn it off, turn it on and set level
      if(zoomLevel != (GPhysicalPoint.MIN_ZOOM - 1)){
         pane.setShowCachedZoom(true);
         pane.setShowCachedZoomLevel(zoomLevel);
      }
      else{
         pane.setShowCachedZoom(false);
      }
   }

   /**
    * When a pane event is fired, this method is run.
    */
   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if (pane == null) return;

      int currentZoom = pane.getShowCachedZoomLevel();
      int paneZoom = pane.getZoom();

      //if the boolean is false, and this is NONE, its checked
      //System.out.println("("+zoomLevel+" == ("+(GPhysicalPoint.MIN_ZOOM - 1)+") && "+!pane.getShowCachedZoom()+")");

      if(zoomLevel == (GPhysicalPoint.MIN_ZOOM - 1) && !pane.getShowCachedZoom()) super.setSelected(true);
      //if the current pane zoom is this one, this is checked
      else if(currentZoom == zoomLevel && pane.getShowCachedZoom()) super.setSelected(true);
      //all other cases, its false
      else super.setSelected(false);

      //if we've zoomed out too far, and the zoom is now > 4 away, and this is not the
      //NONE, this is disabled
      if((zoomLevel >= paneZoom || paneZoom - zoomLevel > 4) && zoomLevel != (GPhysicalPoint.MIN_ZOOM - 1))
         this.setEnabled(false);
      else
         this.setEnabled(true);


   }

}
