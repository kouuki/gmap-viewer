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



class JMenuActionRemoveGDrawableObject extends JMenuAction{
   public JMenuActionRemoveGDrawableObject(GUI registeredObject) {
      super("Remove Object",registeredObject);
   }

   public void run(){
      //remove the GDrawableObject returned
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      gui.getGMap().getGDraw().remove(gui.getGMap().getGDraw().getSelected());
      pane.draw();
   }
}

