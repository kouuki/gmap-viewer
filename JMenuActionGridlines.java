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
class JMenuActionGridlines extends JMenuAction {

   static final long serialVersionUID = 42643948;

   /**
    * Constructor for creation of Radio button
    * @param registeredObject
    */
   public JMenuActionGridlines(GUI registeredObject) {
      super("Add Gridlines",registeredObject);
   }

   /**
    * run method for class
    */
   public void run() {
      GUI gui = (GUI) registeredObject;
      GPane pane = gui.getTopPane();

      int increments = 10;
      for(int i=0;i<increments;i++)
         gui.getGMap().getGDraw().add(new GLine(new GPhysicalPoint((90/increments)*i, -170.0), new GPhysicalPoint((90/increments)*i ,170.0)));

      pane.draw();
   }

}