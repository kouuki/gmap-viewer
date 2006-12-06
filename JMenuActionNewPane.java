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
/**this is the class that creates a new pane for the menu bar
 * @param registeredObject the GUI frame name
 */
class JMenuActionNewPane extends JMenuAction{
   public JMenuActionNewPane(GUI registeredObject){super("New Pane",registeredObject);}
/**this is where the gui is set to the pane menu bar
 */
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane != null) gui.addPane((GPane)pane.clone());
      else gui.addPane(new GPane(gui));
   }
}
