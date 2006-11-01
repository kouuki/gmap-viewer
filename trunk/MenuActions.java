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


/*
*
* Specific JMenuActions - gather user input, exit program, and the like...
*
*
*/

class JMenuActionExport extends JMenuAction{
   public JMenuActionExport(GUI registeredObject){super("Export to PNG",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      ExportDialog dialog = new ExportDialog(gui);
      dialog.setVisible(true);
   }
}

class JMenuActionSetCenter extends JMenuAction{
   public JMenuActionSetCenter(GUI registeredObject){super("Set Center",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      String in = pane.getCenter()+"";
      String message = "";
      GPhysicalPoint newCenter = null;
      while(newCenter == null){
         in = (String)JOptionPane.showInputDialog(gui.frame, "Enter new center as Lat, Long"+message,"Recenter",JOptionPane.PLAIN_MESSAGE,null,null,in);
         if(in == null) break;
         newCenter = LibString.makeGPhysicalPoint(in);
         if(newCenter == null) message = "\n\nParse Error: Bad lat/long format.";
      }
      if(newCenter != null) pane.setCenter(newCenter);
   }
}

class JMenuActionSetCenterPixel extends JMenuAction{
   public JMenuActionSetCenterPixel(GUI registeredObject){super("Set Center Pixel",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;


      Point inPoint = pane.getCenterPixel();
      String in = inPoint.x+", "+inPoint.y;
      String message = "";
      Point newCenter = null;
      while(newCenter == null){
         in = (String)JOptionPane.showInputDialog(gui.frame, "Enter new center as X px, Y px"+message,"Recenter to Pixel",JOptionPane.PLAIN_MESSAGE,null,null,in);
         if(in == null) break;
         newCenter = LibString.makePoint(in);
         if(newCenter == null) message = "\n\nParse Error: Bad coordinate format.";
      }
      if(newCenter != null) pane.setCenterPixel(newCenter);
   }
}


class JMenuActionSetZoom extends JMenuAction{
   public JMenuActionSetZoom(GUI registeredObject){super("Set Zoom",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;


      int inPoint = pane.getZoom();
      String in = inPoint+"";
      String message = "";
      Integer newZoom = null;
      while(newZoom == null){
         in = (String)JOptionPane.showInputDialog(gui.frame, "Enter Zoom "+message,"Zoom",JOptionPane.PLAIN_MESSAGE,null,null,in);
         if(in == null) break;
         try{newZoom = new Integer(Integer.parseInt(in));}
         catch(Exception e){message = "\n\nParse Error: Zoom must be an integer.";newZoom = null;}
      }
      if(newZoom != null) pane.setZoom(newZoom.intValue());
   }
}


class JMenuActionExit extends JMenuAction{
   public JMenuActionExit(GUI registeredObject){super("Exit",registeredObject);}

   public void run(){
      System.exit(1);
   }
}


class JMenuActionNewPane extends JMenuAction{
   public JMenuActionNewPane(GUI registeredObject){super("New Pane",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane != null) gui.addPane((GPane)pane.clone());
      else gui.addPane(new GPane(gui));
   }
}

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

class JMenuActionRemovePane extends JMenuAction{
   public JMenuActionRemovePane(GUI registeredObject){super("Remove Pane",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
//      String in = (String)JOptionPane.showInputDialog(gui.frame, "Remove Pane","Are you sure you want to remove this pane?",JOptionPane.PLAIN_MESSAGE,null,null,in);

      gui.removePane(gui.getTopPane());
//      gui.addPane();
   }
}



class JMenuActionCacheSelected extends JMenuAction{
   //stuff we need to know
   int zoomLevel;

   public JMenuActionCacheSelected(String name, GUI registeredObject, int zoomLevel){
      super(name,registeredObject);
      this.zoomLevel = zoomLevel;
   }

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
      gui.getGMap().cacheImage(toCache.x, toCache.y, toCache.width, toCache.height, zoomLevel);
      pane.draw();
   }

   public void paneEvent(Object object){
      GPane pane = (GPane)object;
      int paneZoom = pane.getZoom();
      if(zoomLevel >= paneZoom) this.setEnabled(false);
      else this.setEnabled(true);
   }

}



/*
*
* Specific JMenuRadioButtonActions - selected or not
*
*
*/

class JMenuRadioButtonActionCache extends JMenuRadioButtonAction{
   //stuff we need to know
   int zoomLevel;

   public JMenuRadioButtonActionCache(String name, GUI registeredObject, int zoomLevel){
      super(name,registeredObject);
      this.zoomLevel = zoomLevel;
   }

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      //if this doesnt turn it off, turn it on and set level
      if(zoomLevel != -1){
         pane.setShowCachedZoom(true);
         pane.setShowCachedZoomLevel(zoomLevel);
      }
      else{
         pane.setShowCachedZoom(false);
      }
   }

   public void paneEvent(Object object){
      GPane pane = (GPane)object;
      int currentZoom = pane.getShowCachedZoomLevel();
      if(zoomLevel == -1 && !pane.getShowCachedZoom()) super.setSelected(true);
      else if(currentZoom == zoomLevel) super.setSelected(true);
      else super.setSelected(false);

      int paneZoom = pane.getZoom();
      if(zoomLevel >= paneZoom || paneZoom - zoomLevel > 4 && zoomLevel != -1) this.setEnabled(false);
      else this.setEnabled(true);
   }

}

class JMenuRadioButtonActionZoom extends JMenuRadioButtonAction{
   //stuff we need to know
   int zoomLevel;

   public JMenuRadioButtonActionZoom(String name, GUI registeredObject, int zoomLevel){
      super(name,registeredObject);
      this.zoomLevel = zoomLevel;
   }

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      //if this doesnt turn it off, turn it on and set level
      if(zoomLevel - pane.getShowCachedZoomLevel() > 4) pane.setShowCachedZoom(false);
      pane.setZoom(zoomLevel);
      pane.draw();
   }

   public void paneEvent(Object object){
      GPane pane = (GPane)object;
      int currentZoom = pane.getZoom();
      if(currentZoom == zoomLevel) super.setSelected(true);
      else super.setSelected(false);
   }


}


class JMenuActionPOC extends JMenuAction{
   public JMenuActionPOC(GUI registeredObject){super("Progress Meter Test",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      try{
         String newTitle = "";
         String message = "";
         newTitle = (String)JOptionPane.showInputDialog(gui.frame, "Enter a task size. "+message,"Set Task Size",JOptionPane.PLAIN_MESSAGE,null,null,newTitle);
         //newTitle = "1000";
         if(newTitle != null){
            int taskSize = Integer.parseInt(newTitle);
            ProgressMeter meter = gui.getProgressMeter();
            meter.grab(this);
            for(int i=0;i<taskSize;i++){
               meter.setPercent(ProgressMeter.computePercent(i,taskSize), this);
               meter.setMessage(""+meter.getRoundedPercent(3), this);
               System.out.println("Completed "+meter.getRoundedPercent(3));

            }
            meter.release(this);
         }
      }
      catch(Exception e){System.out.println(e);}
   }
}


class JMenuActionClearRAM extends JMenuAction{
   public JMenuActionClearRAM(GUI registeredObject){super("Clear RAM",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      try{
         for(int i=0;i<40;i++)
            gui.getGMap().getGDataSource().addImageToRAM(-1,-1,-1,null);
      }
      catch(Exception e){System.out.println(e);}
   }
}
