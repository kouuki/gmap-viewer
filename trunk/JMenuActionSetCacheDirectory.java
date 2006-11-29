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


/** Menu action allowing the user to set the cache directory */
class JMenuActionSetCacheDirectory extends JMenuAction{
   /**Constructor for JMenuActionSetCacheDirectory class.
    * @param registeredObject The object to be registered with this menu action.
    */
   public JMenuActionSetCacheDirectory(GUI registeredObject) {
      super("Set Cache Directory",registeredObject);
   }
   /** Set the new cache directory and create necessary sub-directories for it */
   public void run(){
      //set the cache directory and verify it
      GUI gui = (GUI)registeredObject;
      //new instance of JFileChooser and a dialog window
      JFileChooser loadFile = new JFileChooser();
      JDialog cacheDialog = new JDialog();
      //allow only directories to be selected
      loadFile.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
      //show the new instance and declare return value for the instance
      int returnVal = loadFile.showOpenDialog(cacheDialog);
      //declare the file object
      File selectedFile = null;
      //Query the JFileChooser to get the chosen directory from the user
      switch(returnVal) {
         case JFileChooser.APPROVE_OPTION:
            selectedFile = loadFile.getSelectedFile();
            String cacheDirectory = selectedFile.toString();
         //set the appropriate directories for each gDataSource instance and verify they exist, otherwise create them
            gui.setGMap(new GMap(cacheDirectory));

            gui.getGMap().getGDataSource().cacheDirectory = cacheDirectory;
            gui.getGMap().getGDataSource().verifyCacheDirectories();
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