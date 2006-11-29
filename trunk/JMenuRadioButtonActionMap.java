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


/** It is the radio button action when map type data is needed.*/
class JMenuRadioButtonActionMap extends JMenuRadioButtonAction{

   /** Constructor for JMenuRadioButtonActionMap class.*/
   public JMenuRadioButtonActionMap(String name, GUI registeredObject){
      super(name,registeredObject);
   }
   /**create new GDrawableObject for the map type images*/
   public void run(){
      GUI gui = (GUI)registeredObject;
      int maptype = gui.getGMap().MAP_MODE;
      gui.getGMap().setMode(maptype);
      GPane pane = gui.getTopPane();
      pane.draw();
   }

   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      super.setSelected(gui.getGMap().getMode() == GMap.MAP_MODE);
   }
}
