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
* The class GText is used in the Google Map Viewer application. It is used for the texts used in the program. It implements the following:
*<ul>
* <li>GDrawableObject</ul>
*/
public class GText extends GCustomObject implements GDrawableObject{

   private GPhysicalPoint point;
   private String text;
   private Rectangle textRectangle;
   private int maxDescent;
   /**
   * This sets the padding between letters to a final number 2.
   *
   */
   private static final int padding = 2;
  /**
   * A constructor for the GText class.
   *@param point The actual point in the map.
   *@param text The String text that is going to be placed in the map.
   */
   public GText(GPhysicalPoint point, String text){
      this.point = point;
      this.text = text;
      this.textRectangle = null;
      setColor(new Color(0,0,0));
      setBackground(new Color(255,255,255));
      setStroke(1);
   }

     /**
   *A method for the return of the physical point on the map.
   *@return The point on the map is given.
   */
   public GPhysicalPoint getPoint(){
      return point;
   }
  /**
   *A method that sets a physical point on the map.
   *@param point  The actual point on the map
   */
   public void setPoint(GPhysicalPoint point){
      this.point = point;
   }
  /**
   *A method for the return of text.
   *@return The text to be written in the map.
   */
   public String getText(){
      return text;
   }
  /**
   *A method that sets text that is given.
   *@param text The text to be written in the map.
   */
   public void setText(String text){
      this.text = text;
   }
  /**
   *A method that returns the rectangle created.
   *@param p The physical point in the map.
   *@param zoom The zoom amoun t/level of the rectangle.
   *@return The rectangle is returned with appropriated values.
   */
   public Rectangle getRectangle(GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null) return null;

      //build a point data element that represents the upper corner of the screen
      Point screen = new Point(p.getPixelX(zoom), p.getPixelY(zoom));

      //get the coordinate of the point on our visible screen
      Point pointOnScreen = new Point(point.getPixelX(zoom) - screen.x, point.getPixelY(zoom) - screen.y);

      return new Rectangle(pointOnScreen.x - padding,(int)(pointOnScreen.y-textRectangle.getHeight()+maxDescent) - padding,(int)textRectangle.getWidth() + 2*padding,(int)textRectangle.getHeight()+maxDescent+2*padding);
   }

  /**
   *A method that enables the drawing of objects to the map.
   *@param image Creates the graphics content.
   *@param p The physical point in the map.
   *@param zoom The zoom amount/level
   */
   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null || image == null) return ;

      //build a rectangle data element that represents the visible area of the screen
      Rectangle screen = new Rectangle(p.getPixelX(zoom), p.getPixelY(zoom), image.getWidth(), image.getHeight());

      //create a graphics context
      Graphics2D g = image.createGraphics();

      //text rectangle
      Rectangle2D textRectangle2D = g.getFontMetrics().getStringBounds(text,g);
      textRectangle = new Rectangle((int)textRectangle2D.getX(), (int)textRectangle2D.getY(), (int)textRectangle2D.getWidth(), (int)textRectangle2D.getHeight());
      maxDescent = g.getFontMetrics().getMaxDescent();

      //if the point is not on the screen return here
      Point pixelLocation = point.getPixelPoint(zoom);
      if(screen.contains(pixelLocation.x, pixelLocation.y)) ;
      else if(screen.contains(pixelLocation.x+textRectangle.getWidth(), pixelLocation.y)) ;
      else if(screen.contains(pixelLocation.x+textRectangle.getWidth(), pixelLocation.y-textRectangle.getHeight()+maxDescent)) ;
      else if(screen.contains(pixelLocation.x, pixelLocation.y-textRectangle.getHeight()+maxDescent)) ;
      else return ;

      //get the coordinate of the point on our visible screen
      Point pointOnScreen = new Point(pixelLocation.x - screen.x, pixelLocation.y - screen.y);

      //draw bg
      Rectangle bgRect = getRectangle(p,zoom);
      g.setColor(getBackground());
      g.fillRect(bgRect.x,bgRect.y,bgRect.width,bgRect.height);
      g.setColor(getColor());
      g.setStroke(new BasicStroke(getStroke()));
      g.drawRect(bgRect.x,bgRect.y,bgRect.width,bgRect.height);

      //draw it
      g.setColor(getColor());
      g.drawString(text,pointOnScreen.x,pointOnScreen.y);

   }

   /**
    * Method moves every item by latitude, longitude.
    * @param lat The amount to add to this items latitude
    * @param lat The amount to add to this items longitude
    */

   public void move(double latitude, double longitude){
      point.setX(point.getX() + longitude);
      point.setY(point.getY() + latitude);
   }


}






