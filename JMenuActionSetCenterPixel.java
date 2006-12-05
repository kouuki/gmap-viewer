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

/*Class for the Google Map Viewer application.  JMenuActionSetCenterPixel
*takes in an x and y value and displays a map image with that pixel as its center.
*/
public class JMenuActionSetCenterPixel extends JMenuAction{

/**
* The JMenuActionSetCenterPixel constructor.
*@param registeredObject    The object representing the visible map image.
*/
   public JMenuActionSetCenterPixel(GUI registeredObject){super("Set Center Pixel",registeredObject);}

/**
* The method for setting the new Centerd image as the top
*pane if it is valid, otherwise it asks for new coordinates.
*/
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