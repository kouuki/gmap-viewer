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
* The class JMenuActionSetCenter is a type of JMenuAction that sets the View menu to Set Center or Set Center Pixel. 
*/
class JMenuActionSetCenter extends JMenuAction{

/**
* The method allows the user to set the view of the map towards the center of the given latitude and longitude
*@param registeredObject The GUI object 
*/
   public JMenuActionSetCenter(GUI registeredObject){super("Set Center",registeredObject);}
/**
*The method run sets and creates the GUI object with the new dialog box assigning the new center.
*
*/
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