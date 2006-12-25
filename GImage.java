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
public class GImage extends GCustomObject implements GDrawableObject{
   /** Two GMarkers */
   private GMarker point1;
   private GMarker point2;
   private Dimension dimension;
   private boolean lossless;
   transient private BufferedImage image;
   /**
    * Creates a GImage at (point1,point2) using pixel data from the BufferedImage image.
    * @param point1
    * @param point2
    * @param image
    * @param lossless
    */
   public GImage(GMarker point1, GMarker point2, BufferedImage image, Boolean lossless){
      this.point1 = point1;
      this.point2 = point2;
      this.image = image;
      this.lossless = lossless;
      if(image != null) this.dimension = new Dimension(image.getWidth(), image.getHeight());
   }

   /**
    * Creates a GImage at (point1,point2) using pixel data from the BufferedImage image. Assumes a lossy image (ie. JPEG compression).
    * @param point1
    * @param point2
    * @param image
    */
   public GImage(GMarker point1, GMarker point2, BufferedImage image){
      this(point1, point2, image, false);
   }

   /**
    * Creates a GImage at (point1,point2) using pixel data from the BufferedImage image. Converts GPhysicalPoints to GMarkers for convenience.
    * @param point1
    * @param point2
    * @param image
    */
   public GImage(GPhysicalPoint point1, GPhysicalPoint point2, BufferedImage image){
      this(new GMarker(point1), new GMarker(point2), image);
   }
   /**
    * Creates a GImage at (0,0) using pixel data from the BufferedImage image.
    * @param image
    */
   public GImage(BufferedImage image){
      this(new GMarker(), new GMarker(), image);
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

   /**
    * Gets the image as a BufferedImage.
    * @return        The image.
    */
   public BufferedImage getImage(){
      return image;
   }

   /**
    * Gets the dimension of this image.
    * @return        The dimension.
    */
   public Dimension getDimension(){
      return dimension;
   }

   /** Method to get lossless status of this image.
    * @return        The status - true if it is lossless.
    */
   public boolean getLossless(){
      return lossless;
   }




   /** Method to set the point on the map to place the 1st marker */
   public void setPoint1(GMarker point){
      this.point1 = point;
   }

   /** Method to set the point on the map to place the 1st marker. Uses a GPhysicalPoint */
   public void setPoint1(GPhysicalPoint point){
      if(point == null) this.point1 = null;
      this.point1 = new GMarker(point);
   }

   /** Method to set the point on the map to place the 2nd marker. If this point is null, the image is a constant width and height (controlled by dimension) at all zoom levels. */
   public void setPoint2(GMarker point){
      this.point2 = point;
   }

   /** Method to set the point on the map to place the 2nd marker. Uses a GPhysicalPoint. If this point is null, the image is a constant width and height (controlled by dimension) at all zoom levels. */
   public void setPoint2(GPhysicalPoint point){
      if(point == null) this.point2 = null;
      this.point2 = new GMarker(point);
   }

   /** Method to set the file of the image. Automatically updates the image. */
   public void setImage(BufferedImage image){
      this.image = image;
   }

   /** Method to set the width and height of the image. This is width and height are used to draw the image a constant width and height (regardless of zoom level) if point2 is null. If point2 is not null, this data is ignored.*/
   public void setDimension(Dimension dimension){
      this.dimension = dimension;
   }

   /** Method to set lossless status of this image. If this value is true, the image is serialized using a lossless compression algorithm (PNG). If it is false, the image is serialized using medium quality JPEG compression. Resulting file sizes are much smaller. */
   public void setLossless(boolean lossless){
      this.lossless = lossless;
   }

   /**
    * Method defining a rectangle object for the current map view
    * @param p       The point to place the marker
    * @param zoom    The current zoom level for the map
    * @return        The rectangle object defined for the current map view
    */
   public Rectangle getRectangle(GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null || this.image == null) return null;

      //build a point data element that represents the upper-left corner of the screen
      Point screen = new Point(p.getPixelX(zoom), p.getPixelY(zoom));

      //get the coordinate of point1 on our visible screen
      Point pointOnScreen1 = new Point(point1.getPoint().getPixelX(zoom) - screen.x, point1.getPoint().getPixelY(zoom) - screen.y);

      //handle the case of point2 being null first
      if(point2 == null){
         return new Rectangle(pointOnScreen1.x, pointOnScreen1.y, dimension.width, dimension.height);
      }

      //get the coordinate of point2 on our visible screen
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
      if(p == null || image == null || this.image == null) return ;

      //build a rectangle data element that represents the visible area of the screen
      Rectangle screen = new Rectangle(p.getPixelX(zoom), p.getPixelY(zoom), image.getWidth(), image.getHeight());

      //if the point is not on the screen return here
      //if(!screen.contains(point1.getPoint().getPixelX(zoom), point1.getPoint().getPixelY(zoom)) && !screen.contains(point2.getPoint().getPixelX(zoom), point2.getPoint().getPixelY(zoom))) return ;

      //create a graphics context
      Graphics2D g = image.createGraphics();

      //set up graphics properties
      g.setColor(getColor());
      g.setStroke(new BasicStroke(getStroke()));

      //get the coordinate of point1 on our visible screen
      Point pointOnScreen1 = new Point(point1.getPoint().getPixelX(zoom) - screen.x, point1.getPoint().getPixelY(zoom) - screen.y);

      //handle the case of point2 being null first
      if(point2 == null){
         //draw
         g.drawImage(this.image, pointOnScreen1.x, pointOnScreen1.y, dimension.width, dimension.height, null);
         g.drawRect(pointOnScreen1.x, pointOnScreen1.y, dimension.width, dimension.height);
      }else{
         //get the coordinate of point2 on our visible screen
         Point pointOnScreen2 = new Point(point2.getPoint().getPixelX(zoom) - screen.x, point2.getPoint().getPixelY(zoom) - screen.y);
         //draw
         int x = Math.min(pointOnScreen1.x, pointOnScreen2.x);
         int y = Math.min(pointOnScreen1.y, pointOnScreen2.y);
         int w = Math.max(pointOnScreen1.x, pointOnScreen2.x) - x;
         int h = Math.max(pointOnScreen1.y, pointOnScreen2.y) - y;
         g.drawImage(this.image, x, y, w, h, null);
         g.drawRect(x, y, w, h);
      }
   }

   /**
    * Method moves every item by latitude, longitude.
    * @param lat The amount to add to this items latitude
    * @param lat The amount to add to this items longitude
    */

   public void move(double latitude, double longitude){
      if(point1 != null) point1.move(latitude, longitude);
      if(point2 != null) point2.move(latitude, longitude);
   }

   /**
   * Method invoked when this object is written to a persistent state.
   */
   //private int[] imagePixels;
   //private Dimension savedSize;
   private byte[] pngImageData;

   private void writeObject(ObjectOutputStream out) throws IOException{
      //save image as a png file to an internal byte array
      if(image != null){
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         if(lossless) ImageIO.write(image, "png", baos);
         else ImageIO.write(image, "jpg", baos);
         pngImageData = baos.toByteArray();
      }
      //write this object normally
      out.defaultWriteObject();
   }

   /**
   * Method invoked when this object is read from its persistent state.
   */

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException{
      //read object normally
      in.defaultReadObject();

      //read image from an internal byte array
      ByteArrayInputStream bais = new ByteArrayInputStream(pngImageData);
      image = ImageIO.read(bais);
   }

   /**
   * Prints out GLine{ GPhysicalPoint}
   */

   public String toString(){
      return "GImage{ "+point1+","+point2+" }";
   }


}