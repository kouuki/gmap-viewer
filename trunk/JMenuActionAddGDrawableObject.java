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

class JMenuActionAddGDrawableObject extends JMenuAction{
   public JMenuActionAddGDrawableObject(GUI registeredObject) {
      super("Add Object",registeredObject);
   }

   public void run(){
      //create new GDrawableObject
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      gui.getGMap().getGDraw().add(new GMarker((GPhysicalPoint)pane.getCenter().clone()));
      pane.draw();
   }
}
