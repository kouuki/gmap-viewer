import java.awt.*;
import java.awt.image.*;
import java.io.Serializable;


/**
  * An interface defining the structure for classes
  * to represent objects drawn to the map.
  *
  * @author Alfred
  *
  */

public interface GDrawableObject extends Serializable{

   /**Should draw the physical object to the map
    * @param image Image to be drawn
    * @param p GPhysicalPoint to draw at
    * @param zoom Current zoom level
    */
   public abstract void draw(BufferedImage image, GPhysicalPoint p, int zoom);

   /**Should return a rectangle containing the drawn object
    * @param p GPhysicalPoint to draw at
    * @param zoom Current zoom level
    */
   public abstract Rectangle getRectangle(GPhysicalPoint p, int zoom);

   /**Should mobe this object the necessary lat, long
    * @param lat The amount to add to this items latitude
    * @param lat The amount to add to this items longitude
    */
   public abstract void move(double latitude, double longitude);


}

