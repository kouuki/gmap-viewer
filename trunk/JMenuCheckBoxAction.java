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
* This is the super class to all JMenuCheckBoxActions. JMenuCheckBoxAction is no different
* than its parent JMenuItem, except that it (1) has a run method for actually performing
* some task, (2) can be registered to an object on which it acts, (3) implements PaneListener, so
* it can update its state in response to certain events.
*/
public abstract class JMenuCheckBoxAction extends JCheckBoxMenuItem implements PaneListener{
   protected Object registeredObject;

   /**
   * Set up the JMenuCheckBoxAction.
   *@param string - the representation of the object in a menu
   *@param object - the object on which this action executes
   */

   public JMenuCheckBoxAction(String string, Object registeredObject){
      super(string);
      this.registeredObject = registeredObject;
      if(registeredObject instanceof GUI){
         GUI gui = (GUI)registeredObject;
         this.addActionListener(gui);
         //GPane pane = gui.getTopPane();
         gui.getNotifier().addPaneListener(this);
      }
   }

   /**
   * Launch the run method in a new thread.
   */
   public void start(){
      thisThread tt = new thisThread(this);
      tt.start();
   }

   /**
   * A new thread for the run method.
   */
   class thisThread extends Thread{
      private JMenuCheckBoxAction codeSource;
      public thisThread(JMenuCheckBoxAction codeSource){
         this.codeSource = codeSource;
      }
      public void run(){
         codeSource.run();
      }
   }

   /**
   * The run method. This must be overridden, as all implementations of this class will do something different.
   */
   public abstract void run();

   /**
   * Receive and handle a pane event. By default, this method does nothing, so subclassers do not have
   * to override paneEvent unless they want to use its functionality.
   *@param object - the object that fired this paneEvent
   */
   public void paneEvent(Object object){
      //do nothing on pane event unless this is overidden
   };
}
