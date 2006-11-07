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
* JMenuGroup
*
* A JMenu that handles the work involved in registering all the necessary
* Listeners and adding its own item array
*
*/
class JMenuGroup extends JMenu{
   ButtonGroup buttonGroup;

   public JMenuGroup(String name, ActionListener registeredObject, JMenuItem[] items){
      super(name);
      //set up the button group
      buttonGroup = new ButtonGroup();

      //initialize and add
      for(int i=0;i<items.length;i++){
         this.add(items[i]);
         //if its a button, add it to this group
         if(items[i] instanceof JRadioButtonMenuItem){
            JRadioButtonMenuItem thisButton = (JRadioButtonMenuItem)items[i];
            buttonGroup.add(thisButton);
         }
      }
   }
}



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

abstract class JMenuRadioButtonAction extends JRadioButtonMenuItem implements PaneListener{
   Object registeredObject;
   public JMenuRadioButtonAction(String string, Object registeredObject){
      super(string);
      this.registeredObject = registeredObject;
      if(registeredObject instanceof GUI){
         GUI gui = (GUI)registeredObject;
         this.addActionListener(gui);
         //GPane pane = gui.getTopPane();
         gui.getNotifier().addPaneListener(this);
      }
   }

   public void start(){
      thisThread tt = new thisThread(this);
      tt.start();
   }

   class thisThread extends Thread{
      private JMenuRadioButtonAction codeSource;
      public thisThread(JMenuRadioButtonAction codeSource){
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
