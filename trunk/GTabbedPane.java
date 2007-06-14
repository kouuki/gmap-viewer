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

class GTabbedPane extends JTabbedPane implements MouseListener, ActionListener{

   //gui
   private GUI gui;

   //menuGroup
   private GPopupMenu popupMenu;


   //default
   public GTabbedPane(GUI gui){
      super(JTabbedPane.BOTTOM);

      //init gui
      this.gui = gui;

      popupMenu = new GPopupMenu(gui);
      //jmenugroup init
      //JMenuAction[] array = {new JMenuActionRemovePane(gui)};
      //popupMenu = new JPopupMenu();
      //array[0].addActionListener(this);
      //for(int i=0;i<array.length;i++) popupMenu.add(array[i]);
      //this.add(popupMenu);
      addMouseListener(this);

   }


   public void showPopupMenu(int x, int y){
      popupMenu.show(this,x+5,y+5);
//      popupMenu.setBounds(x,y,200,300);
   }


   //mouse methods - use e.getX()
   public void mouseMoved(MouseEvent e) {}
   public void mouseDragged(MouseEvent e){}
   public void mouseClicked(MouseEvent e){

   }
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){
      int m = e.getModifiers();
      if(m == 4 && gui.getTopPane() != null){
         //right click
         showPopupMenu(e.getX(), e.getY());
      }
   }
   public void mouseReleased(MouseEvent e){}


   //action dispatcher method from menubar
   public void actionPerformed(ActionEvent e){
      Object sourceObject = e.getSource();
      //dispatch actions
      if(sourceObject instanceof JMenuAction){
         JMenuAction sourceMenuAction = (JMenuAction)sourceObject;
         sourceMenuAction.start();
      }
      //dispatch radio button actions
      if(sourceObject instanceof JMenuRadioButtonAction){
         JMenuRadioButtonAction sourceMenuAction = (JMenuRadioButtonAction)sourceObject;
         sourceMenuAction.start();
      }

   }


}