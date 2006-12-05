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

/**Class for the Google Map Viewer application.  JMenuRadioButtonSelectionOn
*involves the radio button controls over the visible image.
*/
public class JMenuRadioButtonSelectionOn extends JMenuRadioButtonAction{
   //stuff we need to know

/**
* JMenuRadioButtonSelectionOn constructor.
*@param registeredObject   The selection pane.
*/
   public JMenuRadioButtonSelectionOn(GUI registeredObject){
      super("Selection Rectangle",registeredObject);
   }

/** This method listens for a mouse click and drag to
*begin making a selection pane.
*/
   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      pane.setMode(GPane.SELECTION_MODE);
      super.setSelected(true);
      gui.getNotifier().firePaneEvent(this);
      gui.getProgressMeter().getPanel().repaint();
   }
/** This method creates the pane that covers a selected area.
*@param object  This parameter is not used in the method.
*/
   public void paneEvent(Object object){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;
      int currentZoom = pane.getShowCachedZoomLevel();
      super.setSelected(pane.getMode() == GPane.SELECTION_MODE);
   }

}
