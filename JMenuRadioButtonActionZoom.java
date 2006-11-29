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
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if (pane == null) return;

      int currentZoom = pane.getZoom();
      if(currentZoom == zoomLevel) super.setSelected(true);
      else super.setSelected(false);
   }


}
