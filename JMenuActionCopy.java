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
* This JMenuAction copies the current selected area to the system clipboard.
*/
public class JMenuActionCopy extends JMenuAction{

/**
* The constructor.
*@param registeredObject The GUI object
*/
   public JMenuActionCopy(GUI registeredObject){super("Copy",registeredObject);}
/**
* The run method simply gets the image from the GPane and calls LibGUI's copy method.
*
*/
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      //get clipping rectangle
      Rectangle clipping = pane.getMouseRectanglePosition();
      //if we have null selection or image, return and do nothing
      if(clipping == null || pane.getImage() == null) return;
      //reserve output image memory
      //BufferedImage output = new BufferedImage(clipping.getWidth(),clipping.getHeight(),BufferedImage.TYPE_INT_RGB);
      //Graphics2D g = output.createGraphics();
      //clip the image
      BufferedImage clipped = pane.getImage().getSubimage(clipping.x, clipping.y, clipping.width, clipping.height);
      //g.drawImage(clipped, 0, 0, null);
      //put the image in the clipboard
      LibGUI.copy(clipped);
   }
}