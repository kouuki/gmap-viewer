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


public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener, ComponentListener{

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

   //progress meter
   private EmbeddedProgressMeter embeddedProgressMeter;


   //build and set up the frame
   private JPanel tabbedPanel;
   private JPanel progressBarPanel;
   private static final int sizeOfProgressBar = 24;


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

      //progress meter
      embeddedProgressMeter = new EmbeddedProgressMeter();

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
      addComponentListener(this);
   }


   public void buildJFrameContents(){
      //set layout to null
      container.setLayout(null);

      //add pane
      pane = new JTabbedPane();
      int location = JTabbedPane.TOP; // or BOTTOM, LEFT, RIGHT
      addPane(new GPane(this));

      //add the tabbed panel
      tabbedPanel = new JPanel();
      tabbedPanel.add(pane);
      tabbedPanel.setLayout(null);
      container.add(tabbedPanel);

      //add the progress bar panel
      //progressBarPanel = new JPanel(); //change this line to get the panel from the meter
      progressBarPanel = embeddedProgressMeter.getPanel(); //change this line to get the panel from the meter
      //JLabel temp = new JLabel("Downloading image #1 of 1");
      //progressBarPanel.add(temp);
      container.add(progressBarPanel);

      //initialize sizes
      tabbedPanel.setBounds(0,0,screenSize.width,screenSize.height - sizeOfProgressBar);
      progressBarPanel.setBounds(0,screenSize.height - sizeOfProgressBar,screenSize.width,sizeOfProgressBar);
      pane.setBounds(0,0,screenSize.width,screenSize.height - sizeOfProgressBar);
   }

   public void update(){
      tabbedPanel.setBounds(0,0,container.getWidth(),container.getHeight() - sizeOfProgressBar);
      progressBarPanel.setBounds(0,container.getHeight() - sizeOfProgressBar,container.getWidth(),sizeOfProgressBar);
      pane.setBounds(0,0,container.getWidth(),container.getHeight() - sizeOfProgressBar);
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

   //get the progress meter
   public EmbeddedProgressMeter getProgressMeter(){
      return embeddedProgressMeter;
   }
   //public ProgressMeter getProgressMeter(){
   //   return globalProgressMeter;
   //}


   /*
    * LISTENERS
    */


   //component listener
   public void componentHidden(ComponentEvent e){update();}
   public void componentMoved(ComponentEvent e){update();}
   public void componentResized(ComponentEvent e){update();}
   public void componentShown(ComponentEvent e){update();}

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
         sourceMenuAction.start();
      }
      //dispatch radio button actions
      if(sourceObject instanceof JMenuRadioButtonAction){
         JMenuRadioButtonAction sourceMenuAction = (JMenuRadioButtonAction)sourceObject;
         sourceMenuAction.start();
      }

   }
}





