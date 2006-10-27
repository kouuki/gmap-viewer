

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


class GPane extends JPanel implements ComponentListener, MouseListener, MouseMotionListener, Cloneable{

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


   //constructor
   public GPane(GUI gui, GPhysicalPoint center, int zoom, boolean showCachedZoom, int showCachedZoomLevel){
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

      //fire pane listener event
      gui.getNotifier().firePaneEvent(this);

   }

   public GPane(GUI gui){
      this(gui, new GPhysicalPoint(-13.123524592036453, -105.44062302343556), 13, false, -1);
   }


   public void draw(){
      //System.out.println("fired - "+getSize().width+", "+getSize().height);
      //check to make sure size is valid
      if(getSize().width == 0 || getSize().height == 0) return;

      //set cached zoom level
      int useCachedZoomLevel = showCachedZoomLevel;
      if(!showCachedZoom) useCachedZoomLevel = -1;

      //get the image
      image = gmap.getImage(center, getSize().width, getSize().height, zoom, useCachedZoomLevel);

      //TEMP - DRAW TICK LINES
      BufferedImage toDraw = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
      Graphics2D g = toDraw.createGraphics();

      g.drawImage(image, 0, 0, image.getWidth(), getHeight(), null);
      //g.drawLine(getSize().width/2, 0, getSize().width/2, getSize().height);
      //g.drawLine(0, getSize().height/2, getSize().width, getSize().height/2);

      //update icon bounds
      label.setBounds(0,0,getSize().width, getSize().height);

      image = toDraw;
      update();
   }

   public void update(){
      //prepare output
      BufferedImage toDraw = new BufferedImage(image.getWidth(),image.getHeight(),BufferedImage.TYPE_INT_RGB);
      Graphics2D g = toDraw.createGraphics();

      //draw overlay
      g.drawImage(image, 0, 0, image.getWidth(), getHeight(), null);

      //rectangle
      if(mouseRectanglePosition != null){
         //draw transparent white
         g.setColor(Color.WHITE);
         Composite temp = g.getComposite();
         g.setComposite(selectionOverlayTransparency);
         g.fillRect(mouseRectanglePosition.x,mouseRectanglePosition.y,mouseRectanglePosition.width,mouseRectanglePosition.height);
         g.setComposite(temp);
         //draw border
         g.setColor(new Color(100,100,100));
         g.drawRect(mouseRectanglePosition.x,mouseRectanglePosition.y,mouseRectanglePosition.width,mouseRectanglePosition.height);
      }

      //notify listener
      gui.getNotifier().firePaneEvent(this);

      //repaint
      label.setIcon(new ImageIcon(toDraw));
      repaint();
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
      this.zoom = zoom;
      draw();
   }

   public void setShowCachedZoomLevel(int showCachedZoomLevel){
      this.showCachedZoomLevel = showCachedZoomLevel;
      draw();
   }

   public void setShowCachedZoom(boolean showCachedZoom){
      this.showCachedZoom = showCachedZoom;
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
      int mouseX = e.getX() + mouseOffset.width;
      int mouseY = e.getY() + mouseOffset.height;

      //update dragged boolean
      mouseDraggedThisClick = true;

      //center
      Point original = getCenterPixel();
      mouseRectanglePosition.x = Math.min(clickLocation.x,mouseX);
      mouseRectanglePosition.y = Math.min(clickLocation.y,mouseY);
      mouseRectanglePosition.width = Math.max(clickLocation.x,mouseX) - mouseRectanglePosition.x;
      mouseRectanglePosition.height = Math.max(clickLocation.y,mouseY) - mouseRectanglePosition.y;
      update();
//      center.setPixelPoint(new Point(original.x + (clickLocation.x - e.getX()), original.y + (clickLocation.y - e.getY())), zoom);
//      clickLocation.x = e.getX();
//      clickLocation.y = e.getY();
//      draw();
   }

   public void mouseClicked(MouseEvent e){
   }
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){
      int mouseX = e.getX() + mouseOffset.width;
      int mouseY = e.getY() + mouseOffset.height;

      //update mouse dragged
      mouseDraggedThisClick = false;

      clickLocation.x = mouseX;
      clickLocation.y = mouseY;
      int m = e.getModifiers();
      if(m == 16){
         //left click
         if(e.getClickCount() == 1){
            //single left click
            mouseRectanglePosition = new Rectangle(clickLocation.x,clickLocation.y,clickLocation.x,clickLocation.y);
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
      }


   }
   public void mouseReleased(MouseEvent e){
      if(!mouseDraggedThisClick) mouseRectanglePosition = null;
      update();
   }

   //keyboard methods - use k.getKeyCode();
   public void keyTyped(KeyEvent k){}
   public void keyReleased(KeyEvent k){}
   public void keyPressed(KeyEvent k){}


   //clone
   public Object clone(){
      return new GPane(gui, (GPhysicalPoint)center.clone(), zoom, showCachedZoom, showCachedZoomLevel);
   }

}
