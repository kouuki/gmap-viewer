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
* JMenuAction
*
* This abstract JMenuItem makes its own name, and guarantees a run method
* that is invoked by the GUI when it is fired
*
*/
abstract class JMenuAction extends JMenuItem implements PaneListener{
   Object registeredObject;
   public JMenuAction(String string, Object registeredObject){
      super(string);
      this.registeredObject = registeredObject;
      if(registeredObject instanceof GUI){
         GUI gui = (GUI)registeredObject;
         this.addActionListener(gui);
         //GPane pane = gui.getTopPane();
         //if(pane != null) pane.addPaneListener(this);
         gui.getNotifier().addPaneListener(this);
      }
   }

   public void start(){
      thisThread tt = new thisThread(this);
      tt.start();
   }

   class thisThread extends Thread{
      private JMenuAction codeSource;
      public thisThread(JMenuAction codeSource){
         this.codeSource = codeSource;
      }
      public void run(){
         codeSource.run();
      }
   }

   public abstract void run();
   public void paneEvent(Object object){
      //do nothing on pane event unless this is overidden
   };
}
