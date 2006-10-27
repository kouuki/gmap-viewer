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


public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener{

   /*
    * DATA  ELEMENTS
    */

   //global parameters
   private String title = "Google Map Viewer 1.0";

   //screen size
   private Dimension screenSize = new Dimension(500,500);

   //containers for stuff
   private Container container = getContentPane();

   //keep track of panes
   private JTabbedPane pane;
   private GPane[] tabs;

   //popup window object
   JFrame frame;

   //map object
   private GMap gmap;

   //pane listener notifier
   private PaneListenerNotifier notifier;

   /*
    * EXECUTABLE
    */

   //executable code
   public static void main(String[] args){
      GUI newWindow = new GUI();
      newWindow.setVisible(true);
   }

   /*
    * OBJECT
    */

   //constructor
   public GUI(){
      //set parameters
      setTitle(title);
      setSize(screenSize.width,screenSize.height);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //notifier
      notifier = new PaneListenerNotifier();

      //initialize gmap
      gmap = new GMap();

      //run content builde
      buildJFrameContents();

      //set up the menubar
      JMenuBar menuBar = new GMenuBar(this);
      setJMenuBar(menuBar);

      //icon
      ImageIcon iconImage = new ImageIcon("images/ico.png");
      Image iconImageObject = iconImage.getImage();
      setIconImage(iconImageObject);

      //add listeners
      addMouseListener(this);
      addKeyListener(this);
      addMouseMotionListener(this);
   }


   //build and set up the frame
   public void buildJFrameContents(){
      pane = new JTabbedPane();
      int location = JTabbedPane.TOP; // or BOTTOM, LEFT, RIGHT
      addPane(new GPane(this));
      container.add(pane);
   }

   //pane counter, for default title creation
   private int paneNumber = 1;

   //pane addition with title
   public void addPane(GPane paneToAdd, String title){
      pane.add(paneToAdd, title);
   }

   //pane addition without title, defaults to "Untitled Pane #"
   public void addPane(GPane paneToAdd){
      addPane(paneToAdd, "Untitled Pane "+paneNumber);
      paneNumber++;
   }

   //pane removal methods
   public void removePane(GPane paneToRemove){
      pane.remove(paneToRemove);
   }


   /*
    * GETTERS
    */

   //get the gmap object
   public GMap getGMap(){
      return gmap;
   }

   //get the current size of the JFrame
   public Dimension getScreenSize(){
      return screenSize;
   }

   //get the pane
   public JTabbedPane getTabbedPane(){
      return pane;
   }

   //get top pane component
   public GPane getTopPane(){
      int selected = pane.getSelectedIndex();
      if(selected == -1) return null;
      else return (GPane)pane.getComponentAt(selected);
   }

   //get the notifier
   public PaneListenerNotifier getNotifier(){
      return notifier;
   }


   /*
    * LISTENERS
    */

   //mouse methods - use e.getX()
   public void mouseMoved(MouseEvent e) {}
   public void mouseDragged(MouseEvent e){}
   public void mouseClicked(MouseEvent e){}
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){}
   public void mouseReleased(MouseEvent e){}

   //keyboard methods - use k.getKeyCode();
   public void keyTyped(KeyEvent k){}
   public void keyReleased(KeyEvent k){}
   public void keyPressed(KeyEvent k){}

   //action dispatcher method from menubar
   public void actionPerformed(ActionEvent e){
      Object sourceObject = e.getSource();
      //dispatch actions
      if(sourceObject instanceof JMenuAction){
         JMenuAction sourceMenuAction = (JMenuAction)sourceObject;
         sourceMenuAction.run();
      }
      //dispatch radio button actions
      if(sourceObject instanceof JMenuRadioButtonAction){
         JMenuRadioButtonAction sourceMenuAction = (JMenuRadioButtonAction)sourceObject;
         sourceMenuAction.run();
      }

   }
}





