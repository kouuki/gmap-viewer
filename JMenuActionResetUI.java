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
 * Class which defines the reset UI action.
 */
class JMenuActionResetUI extends JMenuAction {
   /**
    * Create new instance of JMenuActionResetUI
    *
    * @param registeredObject
    */
   public JMenuActionResetUI(GUI registeredObject) {
      super("Reset UI", registeredObject);
   }

   /**
    * run method which causes the program to exit when called.
    */
   public void run() {
      GUI gui = (GUI)registeredObject;
      //state
      try{
         GApplicationState defaultState = new GApplicationState(gui, "defaultstate.gmv");
         defaultState.restoreState();
         gui.getApplicationState().saveState();
      }catch(Exception e2){
         System.out.println("Problem: default application state could not be loaded from defaultstate.gmv.");
      }
      //gdraw
      try{
         GDraw newGDrawDefault = (GDraw)LibGUI.openStateFromFile(new File("defaultgdraw.gmv"));
         newGDrawDefault.getSize();
         gui.getGMap().setGDraw(newGDrawDefault);
      }catch(Exception e2){
         System.out.println("Problem: default application state could not be loaded from defaultdgdaw.gmv.");
      }
   }
}
