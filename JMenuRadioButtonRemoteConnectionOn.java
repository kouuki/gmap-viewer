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
 * This class handles allowing the user to draw text on the map.
 *
 */

public class JMenuRadioButtonRemoteConnectionOn extends JMenuRadioButtonAction {

   /**
    * Constructor for creation of Radio button
    * @param registeredObject
    */
   public JMenuRadioButtonRemoteConnectionOn(GUI registeredObject) {
      super("Online",registeredObject);
   }

   /**
    * Run method for this class
    */
   public void run() {
      GUI gui = (GUI) registeredObject;
      GPane pane = gui.getTopPane();
      gui.getGMap().getGDataSource().setRemoteConnection(true);
   }

   /**
    * Pane event method for this class.
    */
   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;
      super.setSelected(gui.getGMap().getGDataSource().getRemoteConnection() == true);
   }

}
