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
 * Class which defines the exit action for the menu.
 *
 * Exits the program when activated.
 *
 */
class JMenuActionExit extends JMenuAction {
   /**
    * Create new instance of JMenuActionExit
    *
    * @param registeredObject
    */
   public JMenuActionExit(GUI registeredObject) {
      super("Exit", registeredObject);
   }

   /**
    * run method which causes the program to exit when called.
    */
   public void run() {
      GUI gui = (GUI)registeredObject;
      gui.getApplicationState().saveState();
      LibGUI.saveStateToFile(new File("usergdraw.gmv"), gui.getGMap().getGDraw());
      System.exit(0);
   }
}
