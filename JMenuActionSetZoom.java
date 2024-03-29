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

/*Class for the Google Map Viewer application.  JMenuActionSetZoom
*refreshes the Map Image when a new zoom level is set in the Menu Bar.
*/
public class JMenuActionSetZoom extends JMenuAction{

   private int modeSpecific;

/**
* The JMenuActionSetZoom constructor.
*@param registeredObject    The object representing the visible map image.
*/
   public JMenuActionSetZoom(GUI registeredObject){super("Set Zoom", registeredObject);}

/**
* The method for setting the new Zoom Level image as the top
*pane if it is valid, otherwise it asks for a new integer value.
*/
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

/**
* The paneEvent method decides whether or not to disable/enable this item based
* on the current mode.
*/

   public void paneEvent(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
   }

}