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
* Listeners and adding its own item array. Puts items in a single ButtonGroup.
*
*/
class JMenuGroup extends JMenu{
   private ButtonGroup buttonGroup;

   /*
   * Construct this JMenu group.
   */
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


