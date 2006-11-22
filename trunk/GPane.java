

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
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GPane extends JPanel implements ActionListener, ComponentListener, MouseListener, MouseMotionListener, Cloneable, GMapListener, MouseWheelListener{

   //available JFrame object
   private JFrame popup;

   //GMap object
   private GUI gui;
   private GMap gmap;

   //image AND icon object
   private BufferedImage image;
   private JLabel label;

   //stuff to keep track of
   private GPhysicalPoint center;
   private int zoom;

   //mouse rectangle parameters
   private Rectangle mouseRectanglePosition;

   //display cache variables
   private boolean showCachedZoom;
   private int showCachedZoomLevel;

   //this alphacomposite is controls transparency
   private AlphaComposite selectionOverlayTransparency = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f);

   //a thread for drawing stuff
   private DrawThread drawingThread;

   //boolean mode
   public static final int SELECTION_MODE = 0;
   public static final int DRAGGING_MODE = 1;
   public static final int DRAW_LINE_MODE = 2;
   public static final int DRAW_MARKER_MODE = 3;
   public static final int DRAW_STRING_MODE = 4;
   public static final int DISTANCE_MODE = 5;
   private int mode;

   //GZoomslider object
   private GZoomSlider slider;



   //constructor
   public GPane(GUI gui, GPhysicalPoint center, int zoom, boolean showCachedZoom, int showCachedZoomLevel, int mode){
      //get gmap and registered objects
      this.gui = gui;
      this.gmap = gui.getGMap();

      //zoom
      this.zoom = zoom;
      this.center = center;

      //add this stuff to pane
      label = new JLabel();
      setLayout(null);
      add(label);

      //set mode
      this.mode = mode;

      //draw it
      draw();

      //showCachedZoom
      this.showCachedZoomLevel = showCachedZoomLevel;
      this.showCachedZoom = showCachedZoom;

      //start pane listener
      //initializePaneListener();

      //add component listener
      addComponentListener(this);
      addMouseListener(this);
      addMouseMotionListener(this);
      addMouseWheelListener(this);


      //GZoomSlider
      slider = new GZoomSlider(gui);


      //initialize draw thread to null
      drawingThread = null;


      //fire pane listener event
      gui.getNotifier().firePaneEvent(this);


   }

   public GPane(GUI gui){
      this(gui, new GPhysicalPoint(29.8265419861086, -82.35763549804688), 13, false, -1, SELECTION_MODE);
   }

   public void draw(){
      drawThreadScheduler();
   }

   private void drawThreadScheduler(){
      boolean suppressThread = (drawingThread != null);
      if(suppressThread) suppressThread = drawingThread.isAlive();

      if(!suppressThread){
         drawingThread = new DrawThread(this);
         drawingThread.start();
      }
   }

   private class DrawThread extends Thread{
      private GPane parent;
      public DrawThread(GPane parent){
         this.parent = parent;
      }
      public void run(){
         //set cursor to hourglass
         setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

         //System.out.println("fired - "+getSize().width+", "+getSize().height);
         //check to make sure size is valid
         if(getSize().width == 0 || getSize().height == 0) return;

         //set cached zoom level
         int useCachedZoomLevel = showCachedZoomLevel;
         if(!showCachedZoom) useCachedZoomLevel = -1;

         //timer start
         long start = LibGUI.getTime();

         //package data and send to scheduler
         Point centerPixels = center.getPixelPoint(zoom);
         int x = centerPixels.x - (getSize().width/2);
         int y = centerPixels.y - (getSize().height/2);

         //set the bit to determine if we need new image memory

         boolean newImageMemory = (image == null);
         if(!newImageMemory) newImageMemory = (image.getWidth() != getSize().width || image.getHeight() != getSize().height);


         //make new image if necessary
         if(newImageMemory){
            image = new BufferedImage(getSize().width, getSize().height,BufferedImage.TYPE_INT_ARGB);
         }
         //else paint an overlay
         else{
            Graphics2D g2d = (Graphics2D)image.createGraphics();
            Composite temp = g2d.getComposite();
            g2d.setComposite(selectionOverlayTransparency);
            g2d.setColor(Color.BLACK);
            g2d.fillRect(0,0,image.getWidth(), image.getHeight());
            g2d.setComposite(temp);

         }


         gmap.paintAsynchronousImage(image, x, y, getSize().width, getSize().height, zoom, useCachedZoomLevel, parent);
         gui.getProgressMeter().release(parent);

         //timer stop
         System.out.println("Draw time = " + (LibGUI.getTime() - start));


         //TEMP - DRAW TICK LINES
         //BufferedImage toDraw = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
         //Graphics2D g = toDraw.createGraphics();

         //.drawImage(image, 0, 0, image.getWidth(), getHeight(), null);
         //g.drawLine(getSize().width/2, 0, getSize().width/2, getSize().height);
         //g.drawLine(0, getSize().height/2, getSize().width, getSize().height/2);

         //update icon bounds
         //label.setBounds(0,0,getSize().width, getSize().height);

         //image = toDraw;
         updateScreen();

         //set cursor to hourglass
         setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));

         //update flag
         parent.drawingThread = null;

         //download adjacent
         gmap.getGDataSource().downloadQueue();

         //notify listener
         gui.getNotifier().firePaneEvent(this);
      }
   }


   public void updateScreen(){
      this.paintImmediately(0,0,this.getWidth(), this.getHeight());
      gui.repaint();
   }

   protected void paintComponent(Graphics g) {
      //call superclass, although its not necessary cause this is not transparent
      super.paintComponent(g);

      Graphics2D g2d = (Graphics2D)g;

      //draw overlay
      g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);

      //rectangle
      if(mouseRectanglePosition != null){
         //draw transparent white
         g2d.setColor(Color.WHITE);
         Composite temp = g2d.getComposite();
         g2d.setComposite(selectionOverlayTransparency);
         g2d.fillRect(mouseRectanglePosition.x,mouseRectanglePosition.y,mouseRectanglePosition.width,mouseRectanglePosition.height);
         g2d.setComposite(temp);
         //draw border
         g2d.setColor(new Color(110,110,110));
         g2d.drawRect(mouseRectanglePosition.x,mouseRectanglePosition.y,mouseRectanglePosition.width,mouseRectanglePosition.height);
      }
   }



   //getters

   //returns center as a GPhysicalPoint
   public GPhysicalPoint getCenter(){
      return center;
   }

   //returns upper left pixel of map
   public Point getUpperLeftPixel(){
      Point centerPixel = center.getPixelPoint(zoom);
      int x = centerPixel.x - (getSize().width/2);
      int y = centerPixel.y - (getSize().height/2);
      return new Point(x,y);
   }

   //returns center as a pixel at this zoom level
   public Point getCenterPixel(){
      return center.getPixelPoint(zoom);
   }

   //returns Rectangle corresponding to selected area
   public Rectangle getMouseRectanglePosition(){
      return mouseRectanglePosition;
   }

   //returns centered rectangle for selected area
   public CenteredRectangle getMouseRectanglePositionCentered(){
      if(mouseRectanglePosition == null) return null;
      Point ul = getUpperLeftPixel();
      int x = ul.x + mouseRectanglePosition.x + mouseRectanglePosition.width/2;
      int y = ul.y + mouseRectanglePosition.y + mouseRectanglePosition.height/2;
      return new CenteredRectangle(new GPhysicalPoint(x,y,zoom),mouseRectanglePosition.width,mouseRectanglePosition.height);
   }

   //returns centered rectangle for screen
   public CenteredRectangle getScreenDimensionsCentered(){
      return new CenteredRectangle((GPhysicalPoint)center.clone(),getSize().width,getSize().height);
   }

   //returns zoom
   public int getZoom(){
      return zoom;
   }

   //returns cache display zoom
   public int getShowCachedZoomLevel(){
      return showCachedZoomLevel;
   }

   //returns whether or not cache zoom is on
   public boolean getShowCachedZoom(){
      return showCachedZoom;
   }

   public int getMode(){
      return mode;
   }

   //setters
   public void setCenter(GPhysicalPoint center){
      this.center = center;
      draw();
   }

   public void setCenterPixel(Point center){
      this.center.setPixelPoint(center, zoom);
      draw();
   }

   public void setZoom(int zoom){
      if (zoom >= GDataImage.ZOOM_MIN && zoom <= GDataImage.ZOOM_MAX) {
         this.zoom = zoom;
         draw();
      }
   }

   public void setShowCachedZoomLevel(int showCachedZoomLevel){
      this.showCachedZoomLevel = showCachedZoomLevel;
      draw();
   }

   public void setShowCachedZoom(boolean showCachedZoom){
      this.showCachedZoom = showCachedZoom;
      draw();
   }

   public void setMode(int mode){
      //clean up tracking variables for mouse actions
      clickCount = 0;
      gui.getGMap().getGDraw().remove(tempMarkerA);
      mouseRectanglePosition = null;
      this.mode=mode;
      draw();
   }


   //component listener
   public void componentHidden(ComponentEvent e){}
   public void componentMoved(ComponentEvent e){}
   public void componentResized(ComponentEvent e){draw();}
   public void componentShown(ComponentEvent e){gui.getNotifier().firePaneEvent(this);}


   //mouse methods - use e.getX()
   private Point mouseLocation = new Point(0,0);
   private Point clickLocation = new Point(0,0);
   private Dimension mouseOffset = new Dimension(0,0);

   public void mouseMoved(MouseEvent e){
      int mouseX = e.getX() + mouseOffset.width;
      int mouseY = e.getY() + mouseOffset.height;

      mouseLocation.x = mouseX;
      mouseLocation.y = mouseY;
   }

   private boolean mouseDraggedThisClick = false;

   public void mouseDragged(MouseEvent e){
      int m = e.getModifiers();
      if(m == 16){
         int mouseX = e.getX() + mouseOffset.width;
         int mouseY = e.getY() + mouseOffset.height;

         //center
         Point original = getCenterPixel();

         if(mode == SELECTION_MODE){
            //update dragged boolean
            mouseDraggedThisClick = true;
            //do computations relating to selection
            mouseRectanglePosition.x = Math.min(clickLocation.x,mouseX);
            mouseRectanglePosition.y = Math.min(clickLocation.y,mouseY);
            mouseRectanglePosition.width = Math.max(clickLocation.x,mouseX) - mouseRectanglePosition.x;
            mouseRectanglePosition.height = Math.max(clickLocation.y,mouseY) - mouseRectanglePosition.y;
            updateScreen();
         }else if(mode == DRAGGING_MODE){
            //do computations relating to dragging the center
            center.setPixelPoint(new Point(original.x + (clickLocation.x - e.getX()), original.y + (clickLocation.y - e.getY())), zoom);
            clickLocation.x = e.getX();
            clickLocation.y = e.getY();
            draw();
         }
      }
   }

   public void mouseClicked(MouseEvent e){
      //single left click
      if(e.getModifiers() == 16){
         //single left click

         //get upper left coord
         Point centerPixels = center.getPixelPoint(zoom);
         int x = centerPixels.x - (getSize().width/2);
         int y = centerPixels.y - (getSize().height/2);

         if(mode == SELECTION_MODE || mode == DRAGGING_MODE){
            int clicked = gmap.getGDraw().inside(new Point(e.getX(),e.getY()), new GPhysicalPoint(x,y,zoom), zoom);
            if(clicked != gmap.getGDraw().getSelected()){
               gmap.getGDraw().setSelected(clicked);
               draw();
            }
         }
      }
   }
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}

   private int clickCount = 0;
   private GPhysicalPoint tempA;
   private GPhysicalPoint tempB;
   private GMarker tempMarkerA;
   private GMarker tempMarkerB;

   public void mousePressed(MouseEvent e){

            System.out.println("mode="+mode);


      //update mouse dragged
      mouseDraggedThisClick = false;

      int mouseX = e.getX() + mouseOffset.width;
      int mouseY = e.getY() + mouseOffset.height;

      clickLocation.x = mouseX;
      clickLocation.y = mouseY;
      int m = e.getModifiers();
      if(m == 16){

         //left click
         if(e.getClickCount() == 1){
            //get this click location
            Point c = getCenterPixel();
            c.x += clickLocation.x - (getSize().width/2);
            c.y += clickLocation.y - (getSize().height/2);

            //single left click
            if(mode == SELECTION_MODE)
               mouseRectanglePosition = new Rectangle(clickLocation.x,clickLocation.y,clickLocation.x,clickLocation.y);
            else if(mode == DRAW_LINE_MODE && clickCount == 0){
               clickCount = 1;
               tempA = new GPhysicalPoint(c.x, c.y,zoom);
               tempMarkerA = new GMarker(tempA);
               gui.getGMap().getGDraw().add(tempMarkerA);
               draw();
            }
            else if(mode == DRAW_LINE_MODE && clickCount == 1){
               clickCount = 0;
               tempB = new GPhysicalPoint(c.x, c.y,zoom);
               gui.getGMap().getGDraw().remove(tempMarkerA);
               gui.getGMap().getGDraw().add(new GLine(tempA, tempB));
               draw();
            }
            else if(mode == DISTANCE_MODE && clickCount == 0){
               clickCount = 1;
               tempA = new GPhysicalPoint(c.x, c.y,zoom);
               tempMarkerA = new GMarker(tempA);
               gui.getGMap().getGDraw().add(tempMarkerA);
               draw();
            }
            else if(mode == DISTANCE_MODE && clickCount == 1){
               clickCount = 0;
               tempB = new GPhysicalPoint(c.x, c.y,zoom);
               gui.getGMap().getGDraw().remove(tempMarkerA);
               gui.getGMap().getGDraw().add(new GLine(tempA, tempB));
               GText distance = new GText(tempB, ""+Math.round(GLib.computeDistance(tempA, tempB)*1000)/1000);
               gui.getGMap().getGDraw().add(distance);
               draw();
            }
            else if(mode == DRAW_MARKER_MODE){
               clickCount = 0;
               tempB = new GPhysicalPoint(c.x, c.y,zoom);
               gui.getGMap().getGDraw().add(new GMarker(tempB));
               draw();
            }
            else if(mode == DRAW_STRING_MODE){
               clickCount = 0;
               tempA = new GPhysicalPoint(c.x, c.y,zoom);
               tempMarkerA = new GMarker(tempA);
               gui.getGMap().getGDraw().add(tempMarkerA);
               draw();

               String in = (String)JOptionPane.showInputDialog(gui.frame, "Enter the text to place on the map.","Add Text",JOptionPane.PLAIN_MESSAGE,null,null,"");
               gui.getGMap().getGDraw().remove(tempMarkerA);

               if(in != null){
                  tempB = new GPhysicalPoint(c.x, c.y,zoom);
                  gui.getGMap().getGDraw().add(new GText(tempB, in));
               }
               draw();
            }


         }
         else if(e.getClickCount() == 2){
            //double left click
            Point c = getCenterPixel();
            c.x += clickLocation.x - (getSize().width/2);
            c.y += clickLocation.y - (getSize().height/2);
            center.setPixelPoint(c,zoom);
            draw();
         }
      }
      else if(m == 18 || m == 17){
         //left click with control or shift
      }
      else if(m == 4){
         //right click
         gui.getTabbedPane().showPopupMenu(e.getX(), e.getY()+25);
      }


   }
   public void mouseReleased(MouseEvent e){
      if(!mouseDraggedThisClick){
         mouseRectanglePosition = null;
         gmap.getGDataSource().downloadQueue();
      }
      updateScreen();
  }

   //keyboard methods - use k.getKeyCode();
   public void keyTyped(KeyEvent k){}
   public void keyReleased(KeyEvent k){}
   public void keyPressed(KeyEvent k){}


   //clone
   public Object clone(){
      return new GPane(gui, (GPhysicalPoint)center.clone(), zoom, showCachedZoom, showCachedZoomLevel, mode);
   }

   //gmap listener
   private int gmapCompleted;
   private int gmapTaskSize;
   private int messageNumber;

   public void updateGMapCompleted(int completed){
      this.gmapCompleted = completed;
      gui.getProgressMeter().setPercent(ProgressMeter.computePercent(gmapCompleted,gmapTaskSize),this);
   }

   public void updateGMapTaskSize(int size){
      this.gmapTaskSize = size;
      gui.getProgressMeter().setPercent(ProgressMeter.computePercent(gmapCompleted,gmapTaskSize),this);
   }

   public void updateGMapMessage(int messageNumber){
      if(this.messageNumber != messageNumber){
         this.messageNumber = messageNumber;
         String message;
         if(messageNumber == GMap.MESSAGE_DOWNLOADING){
            gui.getProgressMeter().grab(this);
            gui.getProgressMeter().registerThread(drawingThread, this);
            message = "Downloading data...";
         }
         else if(messageNumber == GMap.MESSAGE_PAINTING){
            message = "Painting image...";
         }
         else{
            message = "Working...";
         }
         gui.getProgressMeter().setMessage(message,this);
      }
   }

   public void updateGMapPainting(){
      updateScreen();
   }

   public boolean asynchronousGMapStopFlag(){
      return gui.getProgressMeter().getStopFlag();
   }


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


   /*
    * (non-Javadoc)
    * @see java.awt.event.MouseWheelListener#mouseWheelMoved(java.awt.event.MouseWheelEvent)
    */
   public void mouseWheelMoved(MouseWheelEvent e) {
      // TODO Auto-generated method stub
      int notches = e.getWheelRotation();

      if(notches < 0) {
         // Mouse wheel moved up
         this.gui.getTopPane().setZoom(this.gui.getTopPane().getZoom() - 1);
      } else {
         // Mouse wheel moved down
         this.gui.getTopPane().setZoom(this.gui.getTopPane().getZoom() + 1);
      }
   }


}
