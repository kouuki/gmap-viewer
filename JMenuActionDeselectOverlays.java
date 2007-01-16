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
 * Class which handles allowing the user to draw points on the map.
 *
 */
class JMenuActionDeselectOverlays extends JMenuAction {

   /**
    * Constructor for creation of paste button.
    * @param registeredObject
    */
   public JMenuActionDeselectOverlays(GUI registeredObject) {
      super("Deselect Overlays",registeredObject);
   }

   /**
    * Run method for class.
    */
   public void run(){
      GUI gui = (GUI) registeredObject;
      GPane pane = gui.getTopPane();
      //clear the selection
      gui.getGMap().getGDraw().getSelected().removeAll();
      pane.draw();
   }

}