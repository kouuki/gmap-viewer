import java.awt.*;
import java.awt.image.*;

/** GDrawableObject
  * An abstract class defining the structure for classes
  * to represent objects drawn to the map
  *
  * @author Alfred
  *
  */

interface GDrawableObject{

   /**draw method that will draw the physical object to the map
    * @param image Image to be drawn
    * @param p GPhysicalPoint to draw at
    * @param zoom Current zoom level
    */
   public abstract void draw(BufferedImage image, GPhysicalPoint p, int zoom);

   /**getRectangle returns a rectangle containing the drawn object
    * @param p GPhysicalPoint to draw at
    * @param zoom Current zoom level
    */
   public abstract Rectangle getRectangle(GPhysicalPoint p, int zoom);

}

