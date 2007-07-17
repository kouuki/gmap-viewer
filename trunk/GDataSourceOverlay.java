import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;


/*Class for the Google Map Viewer application.  GDataSourceOverlay
*retrieves the map images from the database again for an overlay.
*/
public class GDataSourceOverlay extends GDataSource{

/**
*GDataSourceOverlay constructor
*@param cacheDirectory The directory that the retrived images are stored to.
*/
   public GDataSourceOverlay(String cacheDirectory){
      super(cacheDirectory);
   }

/**
* This method calls the URL address for the requested image.
*@param x    The x coordinate of the topleft corner of the image being retrieved.
*@param y    The y coordinate of the topleft corner of the image being retrieved.
*@param zoom    The zoom level of the image being retrieved.
*@return   String   The URL address for the requested image.
*/
   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      //System.out.print(" [map]");
      //System.out.print("{http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom+"}");
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2t.57&x="+x+"&y="+y+"&zoom="+zoom;
   }

}

