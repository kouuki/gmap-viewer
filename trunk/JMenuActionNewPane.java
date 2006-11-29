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

class JMenuActionNewPane extends JMenuAction{
   public JMenuActionNewPane(GUI registeredObject){super("New Pane",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane != null) gui.addPane((GPane)pane.clone());
      else gui.addPane(new GPane(gui));
   }
}
