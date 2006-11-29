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


/*
*
* Specific JMenuActions - gather user input, exit program, and the like...
*
*
*/

class JMenuActionExport extends JMenuAction{
   public JMenuActionExport(GUI registeredObject){super("Export to PNG",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      ExportDialog dialog = new ExportDialog(gui);
      dialog.setVisible(true);
   }
}