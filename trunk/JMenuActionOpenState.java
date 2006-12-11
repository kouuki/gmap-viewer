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
  * Menu action allowing the user to save the current state of the GUI.
  *@author bill
  */
public class JMenuActionOpenState extends JMenuAction{
   /**Constructor for the save state object.
    * @param registeredObject The object to be registered with this menu action.
    */
   public JMenuActionOpenState(GUI registeredObject) {
      super("Open State",registeredObject);
   }
   /** Set the new cache directory and create necessary sub-directories for it */
   public void run(){
      //set the cache directory and verify it
      GUI gui = (GUI)registeredObject;
      //new instance of JFileChooser and a dialog window
      JFileChooser loadFile = new JFileChooser();
      JDialog dialog = new JDialog();
      //allow only directories to be selected
      //loadFile.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
      //show the new instance and declare return value for the instance
      int returnVal = loadFile.showOpenDialog(dialog);
      //declare the file object
      File selectedFile = null;
      //Query the JFileChooser to get the chosen directory from the user
      switch(returnVal) {
         case JFileChooser.APPROVE_OPTION:
            selectedFile = loadFile.getSelectedFile();
            GUI newGui = (GUI)LibGUI.openStateFromFile(selectedFile);
            gui.setVisible(false);
            newGui.setVisible(true);
            break;
         case JFileChooser.CANCEL_OPTION:
            //Cancel button was clicked - do nothing
            break;
         case JFileChooser.ERROR_OPTION:
            //Error was detected - make no changes and output error message
            System.out.println("Error detected!");
            break;
      }
      gui.getTopPane().draw();
   }
}