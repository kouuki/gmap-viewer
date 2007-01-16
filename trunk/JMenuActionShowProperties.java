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
public class JMenuActionShowProperties extends JMenuAction{

/**
* The constructor.
*@param registeredObject The GUI object
*/
   public JMenuActionShowProperties(GUI registeredObject){super("Properties",registeredObject);}
/**
* The run method simply forces the properties dialog to appear.
*
*/
   public void run(){
      GUI gui = (GUI)registeredObject;
      gui.getGPropertiesDialog().setVisible(true);
   }
}