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
 * A clickable menu action that removes a drawable object.
 *@author bill
 */
public class JMenuActionRemoveGDrawableObject extends JMenuAction{
   public JMenuActionRemoveGDrawableObject(GUI registeredObject) {
      super("Remove Object",registeredObject);
   }

   /**
    * Remove the object.
    */
   public void run(){
      //remove the GDrawableObject returned
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();

      int size = gui.getGMap().getGDraw().getSelected().getSize();
      for(int i=0;i<size;i++){
         GDrawableObject removeThisObj = (GDrawableObject)gui.getGMap().getGDraw().getSelected().get(i);
         gui.getGMap().getGDraw().remove(removeThisObj);
      }
      pane.draw();
   }
}

