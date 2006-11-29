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