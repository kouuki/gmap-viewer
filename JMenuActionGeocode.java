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
* A JMenuAction that geocodes a location and returns its latitude/longitude.
*/
public class JMenuActionGeocode extends JMenuAction{

/**
* Constructor for this action.
*/
   public JMenuActionGeocode(GUI registeredObject){super("Geocode",registeredObject);}
/**
*The run method gets the string to geocode and passes it on to the geocoding method.
*
*/
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      String in = "";
      String message = "";
      GPhysicalPoint newCenter = null;
      while(in.equals("")){
         in = (String)JOptionPane.showInputDialog(gui.frame, "Enter a location to geocode."+message,"Geocode",JOptionPane.PLAIN_MESSAGE,null,null,in);
         if(in == null) break;
         GPhysicalPoint gp = Geocoder.geocode(in);
         if(gp != null) pane.setCenter(gp);
         else JOptionPane.showMessageDialog(gui, "This location could not be found.");
      }
   }
}