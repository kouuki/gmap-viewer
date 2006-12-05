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
 * ActionListner that clears RAM when activated.
 */
class JMenuActionClearRAM extends JMenuAction{
   public JMenuActionClearRAM(GUI registeredObject){super("Clear RAM",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      try{
         for(int i=0;i<40;i++)
            gui.getGMap().getGDataSource().addImageToRAM(-1,-1,-1,null);
      }
      catch(Exception e){System.out.println(e);}
   }
}
