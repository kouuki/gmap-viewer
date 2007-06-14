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
* Class for the Google Map Viewer application. Graphical User Interface that
* uses/implements the following Listener classes:
* <ul>
* <li> ActionListener
* <li> KeyListener
* <li> MouseListener
* <li> MouseMotionListener
* <li> ComponentListener</ul>
*/
public class GUI extends JFrame implements ActionListener, KeyListener, MouseListener, MouseMotionListener, ComponentListener, PaneListener{

   /**
    * The global parameter for the Google Map Viewer title.
    */
   private String title = "Google Map Viewer 1.0";

   /**
    * The global parameter for the initial screen size.
    */
   private Dimension screenSize = new Dimension(500,500);

   /**
    * The global parameter for containers for stuff.
    */
   private Container container = getContentPane();

   /**
    * The global parameter for panes.
    */
   private GTabbedPane pane;

   /**
    * The global parameter for tabs.
    */
   private GPane[] tabs;

   /**
    * The global parameter for popup window object.
    */
   JFrame frame;

   /**
    * The global parameter for map object.
    */
   private GMap gmap;

   /**
    * The global parameter for pane listener notifier.
    */
   private PaneListenerNotifier notifier;

   /**
    * The global parameter for progress meter.
    */
   private EmbeddedProgressMeter embeddedProgressMeter;

   /**
    * The global parameter for tabbed panel for building and setting up the frame.
    */
   private JPanel tabbedPanel;

   /**
    * The global parameter for progress bar panel for building and setting up the frame.
    */
   private JPanel progressBarPanel;

   /**
    * The panel for holding expression text.
    */
   private MessagePanel messagePanel;


   /**
    * The global parameter for progress bar panel for building and setting up the frame.
    */
   private JPanel toolBarPanel;
   transient private BorderLayout toolBarLayout;

   /**
    * The global parameter for the toolBar
    */
   private JToolBar toolBar;

   /**
    * The global parameter for progress panel size for building and setting up the frame.
    */
   private static final int sizeOfProgressBar = 23;
   private static final int sizeOfToolbar = 27;
   private static final int widthOfMessagePanel = 170;

   /**
    * An instance of GZoomSlider.
    */
   private GZoomSlider slider;

   /*
    * A properties dialog box that can be accessed by everyone.
    */

   private GPropertiesDialog gPropertiesDialog;

   /**
    * The main method that creates a new window with the GUI class characteristics.
    *
    * @param args the string array
    */
   public static void main(String[] args){
      // Mac OS X compatibility
      if (System.getProperty("mrj.version") != null) {
         System.setProperty("com.apple.macos.useScreenMenuBar", "true");
         System.setProperty("apple.laf.useScreenMenuBar", "true");
         System.setProperty("apple.awt.textantialiasing", "true");
         System.setProperty("apple.awt.showGrowBox", "false");
      }

      //option to set cache directory from command line
      if ( args.length != 0 && args[0] != null ) {
         GUI newWindow = new GUI( args[0] );
         newWindow.setVisible(true);
      }
      else{
         GUI newWindow = new GUI( "cache" );
         newWindow.setVisible(true);
      }
   }

   /*
    * OBJECT
    */

   /**
    * The GUI constructor.
    *@param cacheDirectory. The directory to store the cached images
    */
   public GUI( String cacheDirectory ){
      //set parameters
      setTitle(title);
      setSize(screenSize.width,screenSize.height);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      //progress meter
      embeddedProgressMeter = new EmbeddedProgressMeter();

      //notifier
      notifier = new PaneListenerNotifier();
      notifier.addPaneListener(this);

      //initialize gmap
      gmap = new GMap( cacheDirectory );

      //run content build
      buildJFrameContents();

      //set up the menubar
      JMenuBar menuBar = new GMenuBar(this);
      setJMenuBar(menuBar);

      //icon
      ImageIcon iconImage = new ImageIcon("images/ico.png");
      Image iconImageObject = iconImage.getImage();
      setIconImage(iconImageObject);

      //properties dialog
      gPropertiesDialog = new GPropertiesDialog(this);

      //add listeners
      addMouseListener(this);
      addKeyListener(this);
      addMouseMotionListener(this);
      addComponentListener(this);
   }

   /**
    * Default constructor uses "cache" as cache directory.
    */
   public GUI(){
      this("cache");
   }


   /**
    * The method for buildFrameContents will set layout to null, add pane,
    * add tabbed panel, add progress bar panel and initializes sizes.
    */
   public void buildJFrameContents(){
      //set layout to null
      container.setLayout(null);

      //add GToolBar
      JToolBar toolBar = new GToolBar(this);
      toolBarLayout = new BorderLayout();
      toolBarPanel = new JPanel(toolBarLayout);
      toolBarPanel.add(toolBar, BorderLayout.PAGE_START);
      container.add(toolBarPanel);

      //add GTabbedPane
      pane = new GTabbedPane(this);
      int location = JTabbedPane.TOP; // or BOTTOM, LEFT, RIGHT

      // add GZoomSlider
      slider = new GZoomSlider(this);
      container.add(slider);

      //add the tabbed panel
      tabbedPanel = new JPanel();
      tabbedPanel.add(pane);
      tabbedPanel.setLayout(null);
      container.add(tabbedPanel);

      //progress bar panel
      progressBarPanel = embeddedProgressMeter.getPanel(); //change this line to get the panel from the meter
      container.add(progressBarPanel);


      //exception panel
      messagePanel = new MessagePanel();
      container.add(messagePanel);

      //initialize sizes
      tabbedPanel.setBounds(0,sizeOfToolbar,screenSize.width,screenSize.height - sizeOfProgressBar - sizeOfToolbar);
             pane.setBounds(0,0,screenSize.width,screenSize.height - sizeOfProgressBar-sizeOfToolbar);

      progressBarPanel.setBounds(0,screenSize.height - sizeOfProgressBar,screenSize.width-widthOfMessagePanel,sizeOfProgressBar);
      messagePanel.setBounds(screenSize.width-widthOfMessagePanel,screenSize.height - sizeOfProgressBar,widthOfMessagePanel,sizeOfProgressBar);

      slider.setBounds(screenSize.width-30, 10+sizeOfToolbar, 50, 50);
      toolBarPanel.setBounds(0,0,screenSize.width,sizeOfToolbar);
      toolBarPanel.setPreferredSize(new Dimension(screenSize.width, sizeOfToolbar));

      //add pane
      addPane(new GPane(this));
   }

   /**
    * It is the method that updates the tabbed panel, progress bar panel and pane.
    */
   public void update(){
      tabbedPanel.setBounds(0,sizeOfToolbar,container.getWidth(),container.getHeight() - sizeOfProgressBar - sizeOfToolbar);
      progressBarPanel.setBounds(0,container.getHeight() - sizeOfProgressBar,container.getWidth()-widthOfMessagePanel,sizeOfProgressBar);
      messagePanel.setBounds(container.getWidth()-widthOfMessagePanel,container.getHeight() - sizeOfProgressBar,widthOfMessagePanel,sizeOfProgressBar);
      pane.setBounds(0,0,container.getWidth(),container.getHeight() - sizeOfProgressBar - sizeOfToolbar);
      slider.setBounds(container.getWidth()-30, 10+sizeOfToolbar, 50, 50);
      toolBarPanel.setPreferredSize(new Dimension(container.getWidth(), sizeOfToolbar));
      toolBarPanel.setBounds(0,0,container.getWidth(),sizeOfToolbar);
      toolBarLayout.layoutContainer(toolBarPanel);
   }

   /**
    * It is used for pane counter, for default title creation.
    */
   private int paneNumber = 1;

   /**
    * It is used for pane addition with title.
    *
    * @param paneToAdd  The pane that is to be added.
    * @param title   The string title of the pane.
    */
   public void addPane(GPane paneToAdd, String title){
      pane.add(paneToAdd, title);
   }

   /**
    * It is the pane addition without title, defaults to "Untitled Pane #".
    *
    * @param paneToAdd  The pane that is to be added.
    */
   public void addPane(GPane paneToAdd){
      addPane(paneToAdd, "Untitled Pane "+paneNumber);
      paneNumber++;
   }

   /**
    * It is the pane removal method.
    *
    *@param paneToRemove   The pane tha is to be removed.
    */
   public void removePane(GPane paneToRemove){
      pane.remove(paneToRemove);
   }


   /*
    * GETTERS
    */

   /**
    * It gets the gmap object.
    *
    *@return gmap is the returned gmap object.
    */
   public GMap getGMap(){
      return gmap;
   }

   /*
    * SETTERS
    */

   /**
    * It sets the gmap object.
    */
   public void setGMap(GMap gmap){
      this.gmap = gmap;
   }


   /**
    * It gets the current size of the JFrame.
    *
    *@return screenSize is the returned screen size of JFrame.
    */
   public Dimension getScreenSize(){
      return screenSize;
   }

   /**
    * It gets the tabbed pane.
    *
    *@return pane a GTabbedPane when method is called.
    */
   public GTabbedPane getTabbedPane(){
      return pane;
   }

   /**
    * Gets the exception panel.
    *
    *@return the GUI's exception panel.
    */
   public MessagePanel getMessagePanel(){
      return messagePanel;
   }


   /**
    * It gets the top pane component.
    *
    *@return null if the selected index equal -1.
    *@return pane if the selected index is not null.
    */
   public GPane getTopPane(){
      int selected = pane.getSelectedIndex();
      if(selected == -1) return null;
      else return (GPane)pane.getComponentAt(selected);
   }

   /**
    * It gets the notifier.
    *
    *@return notifier is the returned PaneListenerNotifier.
    */
   public PaneListenerNotifier getNotifier(){
      return notifier;
   }

   /**
    * It gets the progress meter.
    *
    *@return embeddedProgressMeter is the returned EmbeddedProgressMeter.
    */
   public EmbeddedProgressMeter getProgressMeter(){
      return embeddedProgressMeter;
   }

   /**
    * It gets the zoom slider.
    *
    *@return the zoom slider.
    */

   public GZoomSlider getSlider(){
      return slider;
   }

   /**
    * Provides access to the properties dialog.
    *
    *@return the properties dialog.
    */

   public GPropertiesDialog getGPropertiesDialog(){
      return gPropertiesDialog;
   }


   /*
    * LISTENERS
    */


   //component listener
   /**
    * It hides the Component.
    *
    *@param e  is the component event to be hidden.
    */
   public void componentHidden(ComponentEvent e){update();}

   /**
    * It moves the Component.
    *
    *@param e  is the component event to be moved.
    */
   public void componentMoved(ComponentEvent e){update();}

   /**
    * It resizes the Component.
    *
    *@param e  is the component event to be resized.
    */
   public void componentResized(ComponentEvent e){update();}

   /**
    * It shows the Component.
    *
    *@param e  is the component event to be shown.
    */
   public void componentShown(ComponentEvent e){update();}

   //mouse methods - use e.getX()

   /**
   * It shows the mouse being moved.
   *
   *@param e   is the mouse event action
   */
   public void mouseMoved(MouseEvent e) {}

   /**
   * It shows the mouse being dragged
   *
   *@param e   is the mouse event action
   */
   public void mouseDragged(MouseEvent e){}

   /**
   * It shows the mouse being clicked.
   *
   *@param e   is the mouse event action
   */
   public void mouseClicked(MouseEvent e){}

   /**
   *It shows the mouse being entered.
   *
   *@param e   is the mouse event action
   */
   public void mouseEntered(MouseEvent e){}

   /**
   *It shows the mouse being exited.
   *
   *@param e   is the mouse event action
   */
   public void mouseExited(MouseEvent e){}

   /**
   *It shows the mouse being pressed.
   *
   *@param e   is the mouse event action
   */
   public void mousePressed(MouseEvent e){}

   /**
   *It shows the mouse being released.
   *
   *@param e   is the mouse event action
   */
   public void mouseReleased(MouseEvent e){}

   //keyboard methods - use k.getKeyCode();

   /**
   *It shows the key being typed.
   *
   *@param k   is the key event action
   */
   public void keyTyped(KeyEvent k){}

   /**
   *It shows the key being released.
   *
   *@param k   is the key event action
   */
   public void keyReleased(KeyEvent k){
   }

   /**
   *It shows the key being pressed.
   *
   *@param k   is the key event action
   */
   public void keyPressed(KeyEvent k){}

   //action dispatcher method from menubar

   /**
   *It shows the action performed.
   *
   *@param e   is the action event being deciphered.
   */
   public void actionPerformed(ActionEvent e){
      try{
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
         //dispatch check box actions
         if(sourceObject instanceof JMenuCheckBoxAction){
            JMenuCheckBoxAction sourceMenuAction = (JMenuCheckBoxAction)sourceObject;
            sourceMenuAction.start();
         }
      }catch(Exception exception){
         getMessagePanel().setException(exception);
      }
   }

     /**
   * A method for pane listener events
   *@param o The object given.
   */
   public void paneEvent(Object o){
      repaint();
   }
}





