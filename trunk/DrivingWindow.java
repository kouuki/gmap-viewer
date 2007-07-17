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
import javax.swing.JWindow;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;


public class DrivingWindow extends JWindow{

   private Contents GContentPane;

   /** Creates a DrivingWindow.*/
   public DrivingWindow(){
      this(new JFrame(), new GMap());
   }

   /** Creates a DrivingWindow with window as its owner.
   *@param owner the Window that owns it
   */
   public DrivingWindow(Window owner){
      this(owner, new GMap());
   }

   /** Creates a DrivingWindow with window as its owner.
   *@param owner the Window that owns it
   *@param gmap the GMap to use
   */
   public DrivingWindow(Window owner, GMap gmap){
      super(owner);
      this.setSize(800,1000);
      GContentPane = new Contents(this,gmap);
      setContentPane(GContentPane);
      addKeyListener(GContentPane);
      addMouseListener(GContentPane);
      addMouseWheelListener(GContentPane);
      setFocusable(true);
      requestFocusInWindow();

      //enter full screen mode
      getGraphicsConfiguration().getDevice().setFullScreenWindow(this);
   }


   /**
    * Gets the content pane and returns it casted to a Contents object.
    * @return the center
    */
   public Contents getGContentPane(){
      return GContentPane;
   }

   public void exit(){
      getGraphicsConfiguration().getDevice().setFullScreenWindow(null);
      setVisible(false);
   }


   /**
    * A nested panel that serves as the content pane.
    */
   public class Contents extends JPanel implements GMapListener, MouseListener, KeyListener, MouseWheelListener{
      //things to keep track of
      private Window window;
      private GMap gmap;
      private GPhysicalPoint center;
      private int zoom;
      private BufferedImage image;

      //mode
      private int mode;

      //modes to choose from
      public static final int NORTH_SOUTH_MODE = 0;
      public static final int EAST_WEST_MODE = 1;
      public static final int ZOOM_MODE = 2;

      //properties for display
      private final int marginLeft = 15;
      private final int marginTop = 15;
      private final int marginBottom = 136;
      private final int marginRight = 15;

      //cached images
      private BufferedImage northSouthDark;
      private BufferedImage northSouthLight;
      private BufferedImage eastWestDark;
      private BufferedImage eastWestLight;
      private BufferedImage zoomDark;
      private BufferedImage zoomLight;

      /**
       * Constructor.
       */
      public Contents(Window window, GMap gmap){
         this.gmap = gmap;
         this.window = window;
         addKeyListener(this);
         addMouseListener(this);
         addMouseWheelListener(this);
         mode = ZOOM_MODE;
         loadImages();
      }

      //helper method for constructor
      private void loadImages(){
         northSouthDark = LibGUI.loadImage("images/north_south_dark.gif");
         northSouthLight = LibGUI.loadImage("images/north_south_light.gif");
         eastWestDark = LibGUI.loadImage("images/east_west_dark.gif");
         eastWestLight = LibGUI.loadImage("images/east_west_light.gif");
         zoomDark = LibGUI.loadImage("images/zoom_dark.gif");
         zoomLight = LibGUI.loadImage("images/zoom_light.gif");
      }

      /**
       * Gets the center.
       * @return the center
       */
      public GPhysicalPoint getCenter(){
         return center;
      }

      /**
       * Gets the current zoom level.
       * @return the current zoom level.
       */
      public int getZoom(){
         return zoom;
      }

      /**
       * Gets the current screen image.
       * @return image - the image.
       */
      public BufferedImage getImage(){
         return image;
      }

      /**
       * Sets the center using a physical point.
       * @param center
       */
      public void setCenter(GPhysicalPoint center){
         this.center = center;
      }

      /**
       * Sets the zoom level; checks to make sure it is in range.
       * @param zoom
       */
      public void setZoom(int zoom){
         if(zoom < 1 || zoom > 15) return;
         this.zoom = zoom;
      }

      /**
       * Redraws the map.
       */
      public void draw(){
         drawThreadScheduler();
      }

      DrawThread drawingThread;
      private void drawThreadScheduler(){
         boolean suppressThread = (drawingThread != null);
         if(suppressThread) suppressThread = drawingThread.isAlive();

         if(!suppressThread){
            drawingThread = new DrawThread(this);
            drawingThread.start();
         }
      }

      private class DrawThread extends Thread{
         private Contents parent;
         public DrawThread(Contents parent){
            this.parent = parent;
         }
         public void run(){
            try{

               //set cached zoom level
               int useCachedZoomLevel = GPhysicalPoint.MIN_ZOOM - 1;

               //timer start
               long start = LibGUI.getTime();

               //new dimensions
               int newWidth = Math.max(0,getSize().width - (marginLeft + marginRight));
               int newHeight = Math.max(0,getSize().height - (marginTop + marginBottom));

               //get location
               Point centerPixels = center.getPixelPoint(getZoom());
               int x = centerPixels.x - (newWidth/2);
               int y = centerPixels.y - (newHeight/2);

               //set the bit to determine if we need new image memory
               boolean newImageMemory = (image == null);
               if(!newImageMemory) newImageMemory = (image.getWidth() != newWidth || image.getHeight() != newHeight);

               //make new image if necessary
               if(newImageMemory){
                  image = new BufferedImage(newWidth, newHeight,BufferedImage.TYPE_INT_ARGB);
               }

               //empty data source
               //gmap3d.getGMap().getGDataSource().abortQueue();
               gmap.getGDataSource().abortQueue();

               //send the request to the gmap
               //image = gmap3d.getImage(x, y, newWidth, newHeight, zoom, useCachedZoomLevel, this);
               gmap.paintAsynchronousImage(image, x, y, newWidth, newHeight, getZoom(), useCachedZoomLevel, parent);

               //timer stop
               System.out.println("DM Draw time = " + (LibGUI.getTime() - start));


               //make graphics object
               Graphics2D g2d = (Graphics2D)image.createGraphics();

               //google logo
               //Composite temp = g2d.getComposite();
               //g2d.setComposite(opacity70);
               //g2d.drawImage(googleLogo, image.getWidth()-googleLogo.getWidth(), image.getHeight()-googleLogo.getHeight(), googleLogo.getWidth(), googleLogo.getHeight(),null);
               //g2d.setComposite(temp);
               //

               //download the queue
               //gmap3d.getGMap().getGDataSource().downloadQueue();
               gmap.getGDataSource().downloadQueue();

               //repaint me
               repaint();
            }catch(Exception e){}
         }//end run
      }

      /**
       * Paint the panel
       */
      protected void paintComponent(Graphics g){
         super.paintComponent(g);
         //paint background
         g.setColor(Color.BLACK);
         g.fillRect(0,0,getWidth(), getHeight());

         //paint GMap
         g.drawImage(image, marginLeft, marginTop, getSize().width - (marginRight + marginLeft), getSize().height - (marginBottom + marginTop), null);

         //draw lines
         Point mapCenter = new Point(marginLeft + (getWidth() - marginRight - marginLeft)/2, marginTop + (getHeight() - marginBottom - marginTop)/2);
         g.drawLine(mapCenter.x - 3, mapCenter.y, mapCenter.x + 3, mapCenter.y);
         g.drawLine(mapCenter.x, mapCenter.y - 3, mapCenter.x, mapCenter.y + 3);

         //set up buttons, assume dark
         BufferedImage northSouthImage = northSouthDark;
         BufferedImage eastWestImage = eastWestDark;
         BufferedImage zoomImage = zoomDark;

         //make light button light
         if(mode == NORTH_SOUTH_MODE) northSouthImage = northSouthLight;
         if(mode == EAST_WEST_MODE) eastWestImage = eastWestLight;
         if(mode == ZOOM_MODE) zoomImage = zoomLight;

         //paint the buttons
         g.drawImage(northSouthImage, 100, getHeight() - marginBottom + 10, 42, 115, null);
         g.drawImage(eastWestImage, 50, getHeight() - marginBottom + 48, 140, 41, null);
         g.drawImage(zoomImage, getWidth() - 220, getHeight() - marginBottom + 48, 201, 41, null);
      }

      //gmap listener methods
      public void updateGMapCompleted(int completed){}
      public void updateGMapTaskSize(int size){}
      public void updateGMapMessage(int messageNumber){}
      public void updateGMapPainting(){
         System.out.println("added an image");
         //this.repaint();
         this.paintImmediately(0, 0, getWidth(), getHeight());
      }
      public boolean asynchronousGMapStopFlag(){
         return false;
      }

      //mouse wheel
      private int jumpDistance = 25;
      public void mouseWheelMoved(MouseWheelEvent e) {
         int notches = e.getWheelRotation();
         int direction;
         if(notches < 0) {
            // Mouse wheel moved up
            direction = 1;
         } else {
            // Mouse wheel moved down
            direction = -1;
         }

         //mode control
         if(mode == NORTH_SOUTH_MODE){
            Point p = center.getPixelPoint(getZoom());
            p.y += (direction*jumpDistance*-1);
            center.setPixelPoint(p,getZoom());
         }
         else if(mode == EAST_WEST_MODE){
            Point p = center.getPixelPoint(getZoom());
            p.x += (direction*jumpDistance);
            center.setPixelPoint(p,getZoom());
         }
         else if(mode == ZOOM_MODE){
            setZoom(getZoom() + (direction*-1));
         }
         //redraw
         draw();
      }


      //key listener
      public void keyTyped(KeyEvent k){}
      public void keyReleased(KeyEvent k){}

      public void keyPressed(KeyEvent keyEvent){
         int k = keyEvent.getKeyCode();
         //get center
         Point p = center.getPixelPoint(getZoom());

         if(k == 37){
            //left
            p.x += (jumpDistance*-1);
            center.setPixelPoint(p,getZoom());
            draw();
         }else if(k == 38){
            //up
            p.y += (jumpDistance*-1);
            center.setPixelPoint(p,getZoom());
            draw();
         }else if(k == 39){
            //right
            p.x += (jumpDistance*1);
            center.setPixelPoint(p,getZoom());
            draw();
         }else if(k == 40){
            //down
            p.y += (jumpDistance*1);
            center.setPixelPoint(p,getZoom());
            draw();
         }else if(k == 33 || (k == 10 && keyEvent.getModifiers() == 0)){
            //zoom in
            setZoom(getZoom()-1);
            draw();
         }else if(k == 34 || (k == 10 && keyEvent.getModifiers() == 1)){
            //zoom out
            setZoom(getZoom()+1);
            draw();
         }else if(k == 27){
            exit();
         }

         //print the key code
         System.out.println("key="+k);

      }

      //mouse listener
      public void mouseMoved(MouseEvent e){
      }

      public void mouseDragged(MouseEvent e){
         int m = e.getModifiers();
         if(m == 16){
            //left
         }
      }

      public void mouseClicked(MouseEvent e){
         if(e.getModifiers() == 16){
            //single left click

         }
      }

      public void mouseEntered(MouseEvent e){}

      public void mouseExited(MouseEvent e){}

      public void mousePressed(MouseEvent e){
         //get the focus whenever its clicked
         this.requestFocusInWindow();

         int m = e.getModifiers();
         if(m == 16){
            //left click
            mode++;
            if(mode > 2) mode = 0;
            draw();
         }
         else if(m == 18 || m == 17){
            //left click with control or shift
         }
         else if(m == 4){
            //right click
            mode--;
            if(mode < 0) mode = 2;
            draw();
         }
      }

      public void mouseReleased(MouseEvent e){
         int m = e.getModifiers();
         if(m == 16){
            //left click
         }
      }

   }//end nested Contents class

}//end DrivingWindow