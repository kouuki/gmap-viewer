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
  * Menu action allowing the user to cache currently selected area
  * @author bill
  */
class JMenuActionCacheSelected extends JMenuAction{
   //stuff we need to know
   int zoomLevel;
   /**Constructor for JMenuActionCacheSelected class.
    * @param name The name of the desired action
    * @param registeredObject The object to be registered with this menu action.
    * @param zoomLevel The current system zoom level
    */
   public JMenuActionCacheSelected(String name, GUI registeredObject, int zoomLevel){
      super(name,registeredObject);
      this.zoomLevel = zoomLevel;
   }

   /**Run method that performs actual caching
    * 
    */
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      //convert the rectangle to physical points at current zoom, return if rect is null
      Point upperLeft = pane.getUpperLeftPixel();
      Rectangle selectedRect = pane.getMouseRectanglePosition();
      if(selectedRect == null) return ;
      GPhysicalPoint physicalPoint1 = new GPhysicalPoint(upperLeft.x + selectedRect.x,upperLeft.y + selectedRect.y, pane.getZoom());
      GPhysicalPoint physicalPoint2 = new GPhysicalPoint(upperLeft.x + selectedRect.x + selectedRect.width,upperLeft.y + selectedRect.y + selectedRect.height, pane.getZoom());

      //use physical points to get the dimensions of the rectangle we need to cache
      Rectangle toCache = new Rectangle();
      toCache.x = physicalPoint1.getPixelX(zoomLevel);
      toCache.y = physicalPoint1.getPixelY(zoomLevel);
      toCache.width = physicalPoint2.getPixelX(zoomLevel) - toCache.x;
      toCache.height = physicalPoint2.getPixelY(zoomLevel) - toCache.y;

      //cache it
      gui.getGMap().cacheImage(toCache.x, toCache.y, toCache.width, toCache.height, zoomLevel, pane);
      gui.getGMap().getGDataSource().downloadQueue();
      pane.draw();

   }

   /**paneEvent method determines when to enable this thread
    * @param Object
    */
   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if (pane == null) return;
      int paneZoom = pane.getZoom();
      if(zoomLevel >= paneZoom) this.setEnabled(false);
      else this.setEnabled(true);
   }

}
