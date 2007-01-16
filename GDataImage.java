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

import java.math.BigInteger;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.geom.*;
/**This class gets the image for GData to display on the screen
 */
class GDataImage implements Serializable{

   transient private BufferedImage image;
   private int x;
   private int y;
   private int zoom;
/**This is the constructor for the Image to be passed in with required parameters
 * @param image the buffered image of the map
 * @param x the x coord.
 * @param y the y coord.
 * @param zoom the zoom level
 */
   public GDataImage(BufferedImage image, int x, int y, int zoom){
      this.image = image;
      this.x = x;
      this.y = y;
      this.zoom = zoom;
   }
/**this gets the image that is buffered
 * @return the image is returned
 */
   public BufferedImage getImage(){

      return image;
   }
/**this gets the x coord.
 * @return x coord
 */
   public int getX(){
      return x;
   }
/**this gets the y coord.
 * @return y coord
 */
   public int getY(){
      return y;
   }
/**this gets the zoom level
 * @return the zoom level
 */
   public int getZoom(){
      return zoom;
   }
/**this gets the buffered image and sets it current
 */
   public void setImage(BufferedImage image){
      this.image = image;
   }
/**this gets the coord and puts them into string from
 * @return the string of coords x and y
 */
   public String toString() {
      return "("+x+","+y+","+zoom+")";
   }
/**this checks to see if the object is set to thr current object
 * @return false if it isn't
 * @param obj the image object
 */
   public boolean equals(Object obj) {
      if (!(obj instanceof GDataImage)) {
         return false;
      }

      GDataImage o = (GDataImage) obj;
      return (this.zoom == o.zoom && this.y == o.y && this.x == o.x);
   }
}

