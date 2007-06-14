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
 * Action Class which allows for quick selection of all the drawable
 * objects on the map.
 *
 */
class JMenuActionSelectAllOverlays extends JMenuAction {

   /**
    * Constructor for creation of paste button.
    * @param registeredObject
    */
   public JMenuActionSelectAllOverlays(GUI registeredObject) {
      super("Select All",registeredObject);
   }

   /**
    * Run method for class.
    */
   public void run(){
      GUI gui = (GUI) registeredObject;
      GPane pane = gui.getTopPane();
      //clear the selection
      for(int i=0;i<gui.getGMap().getGDraw().getSize();i++)
         gui.getGMap().getGDraw().getSelected().add(
            gui.getGMap().getGDraw().get(i)
         );
      pane.draw();
   }

}