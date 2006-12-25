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
class JMenuActionPaste extends JMenuAction {

   /**
    * Constructor for creation of paste button.
    * @param registeredObject
    */
   public JMenuActionPaste(GUI registeredObject) {
      super("Paste",registeredObject);
   }

   /**
    * Run method for class.
    */
   public void run(){
      GUI gui = (GUI) registeredObject;
      GPane pane = gui.getTopPane();
      //if an area is selected, assume we want to place an image in that area
      if(pane.getMouseRectanglePosition() != null){
         //compute corners
         Rectangle selection = pane.getMouseRectanglePosition();
         Point upperLeftScreenPixel = pane.getUpperLeftPixel();
         Point upperLeft = new Point(upperLeftScreenPixel.x + selection.x, upperLeftScreenPixel.y + selection.y);
         Point lowerRight = new Point(upperLeft.x + selection.width, upperLeft.y + selection.height);

         //load the image
         BufferedImage loadedImage = LibGUI.paste();

         //add image to gdraw
         if(loadedImage != null) gui.getGMap().getGDraw().add(new GImage(new GPhysicalPoint(upperLeft, pane.getZoom()), new GPhysicalPoint(lowerRight, pane.getZoom()), loadedImage));

      }else{
         //compute corners
         Point upperLeftScreenPixel = pane.getUpperLeftPixel();
         Point upperLeft = new Point(upperLeftScreenPixel.x, upperLeftScreenPixel.y);

         //load the image
         BufferedImage loadedImage = LibGUI.paste();

         //add image to gdraw
         if(loadedImage != null) gui.getGMap().getGDraw().add(new GImage(new GMarker(new GPhysicalPoint(upperLeft, pane.getZoom())), null, loadedImage));
      }

      gui.getNotifier().firePaneEvent(this);
      gui.getProgressMeter().getPanel().repaint();
      pane.draw();
   }

}