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

/** Class defining the instance for a line object. A line is simply two GMarkers. */
public class GLine extends GCustomObject implements GDrawableObject{
   /** Two GMarkers */
   private GMarker point1;
   private GMarker point2;
   /**
    * Constructor for GLine.
    * @param point1, point2
    */
   public GLine(GMarker point1, GMarker point2){
      this.point1 = point1;
      this.point2 = point2;
   }
   /**
    * Constructor for GLine using GPhysicalPoints.
    * @param point1, point2
    */
   public GLine(GPhysicalPoint point1, GPhysicalPoint point2){
      this.point1 = new GMarker(point1);
      this.point2 = new GMarker(point2);
   }
   /**
    * Default constructor for GMarker. Creates a line with an empty GMarker
    */
   public GLine(){
      this(new GMarker(), new GMarker());
   }

   /**
    * Gets the first GMarker.
    * @return        The first GMarker.
    */
   public GMarker getPoint1(){
      return point1;
   }

   /**
    * Gets the 2nd GMarker.
    * @return        The 2nd GMarker.
    */
   public GMarker getPoint2(){
      return point2;
   }


   /** Method to set the point on the map to place the 1st marker */
   public void setPoint1(GMarker point){
      this.point1 = point;
   }

   /** Method to set the point on the map to place the 1st marker. Uses a GPhysicalPoint */
   public void setPoint1(GPhysicalPoint point){
      this.point1 = new GMarker(point);
   }

   /** Method to set the point on the map to place the 2nd marker */
   public void setPoint2(GMarker point){
      this.point2 = point;
   }

   /** Method to set the point on the map to place the 2nd marker. Uses a GPhysicalPoint */
   public void setPoint2(GPhysicalPoint point){
      this.point2 = new GMarker(point);
   }


   /**
    * Method defining a rectangle object for the current map view
    * @param p       The point to place the marker
    * @param zoom    The current zoom level for the map
    * @return        The rectangle object defined for the current map view
    */
   public Rectangle getRectangle(GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null) return null;

      //build a point data element that represents the upper-left corner of the screen
      Point screen = new Point(p.getPixelX(zoom), p.getPixelY(zoom));

      //get the coordinate of the point on our visible screen
      Point pointOnScreen1 = new Point(point1.getPoint().getPixelX(zoom) - screen.x, point1.getPoint().getPixelY(zoom) - screen.y);
      Point pointOnScreen2 = new Point(point2.getPoint().getPixelX(zoom) - screen.x, point2.getPoint().getPixelY(zoom) - screen.y);

      //compute x, y, w, h
      int x = Math.min(pointOnScreen1.x, pointOnScreen2.x);
      int y = Math.min(pointOnScreen1.y, pointOnScreen2.y);
      int w = Math.max(pointOnScreen1.x, pointOnScreen2.x) - x;
      int h = Math.max(pointOnScreen1.y, pointOnScreen2.y) - y;

      //rectangle
      return new Rectangle(x-5,y-5,w+10,h+10);
   }
   /**
    * Method to draw the marker object to the screen
    * @param image   The map image to be rendered
    * @param p       The point to place the marker
    * @param zoom    The current zoom level for the map
    */
   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null || image == null) return ;

      //build a rectangle data element that represents the visible area of the screen
      Rectangle screen = new Rectangle(p.getPixelX(zoom), p.getPixelY(zoom), image.getWidth(), image.getHeight());

      //if the point is not on the screen return here
      if(!screen.contains(point1.getPoint().getPixelX(zoom), point1.getPoint().getPixelY(zoom)) && !screen.contains(point2.getPoint().getPixelX(zoom), point2.getPoint().getPixelY(zoom))) return ;

      //create a graphics context
      Graphics2D g = image.createGraphics();

      //get the coordinate of the point on our visible screen
      Point pointOnScreen1 = new Point(point1.getPoint().getPixelX(zoom) - screen.x, point1.getPoint().getPixelY(zoom) - screen.y);
      Point pointOnScreen2 = new Point(point2.getPoint().getPixelX(zoom) - screen.x, point2.getPoint().getPixelY(zoom) - screen.y);

      //draw it
      g.setColor(new Color(0,0,155));
      g.setStroke(new BasicStroke(3));
      g.drawLine(pointOnScreen1.x, pointOnScreen1.y, pointOnScreen2.x, pointOnScreen2.y);
   }

   /**
    * Method moves every item by latitude, longitude.
    * @param lat The amount to add to this items latitude
    * @param lat The amount to add to this items longitude
    */

   public void move(double latitude, double longitude){
      point1.move(latitude, longitude);
      point2.move(latitude, longitude);
   }


   /**
   * Prints out GLine{ GPhysicalPoint}
   */

   public String toString(){
      return "GLine{ "+point1+","+point2+" }";
   }


}