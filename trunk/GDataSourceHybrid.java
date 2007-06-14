import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;


/**
*Class for the Google Map Viewer application.  GDataSourceHybrid combines the
*satellite imagery with Mapped Streets and Labels.
*/
public class GDataSourceHybrid extends GDataSource{
   private GDataSource satellite;

/**This method gets the satellite imagery from the cache.
*@param cacheDirectory   The directory location for the cached images.
*@param satellite    An attribute for satellite imagery.
*/
   public GDataSourceHybrid(String cacheDirectory, GDataSource satellite){
      super(cacheDirectory);
      this.satellite = satellite;
   }

/**This method retrieves the hybrid images from the RAM to display in the pane.
*@param x    The x coordinate of the topleft corner of the image being retrieved.
*@param y    The y coordinate of the topleft corner of the image being retrieved.
*@param zoom    The zoom level of the image being retrieved.
*@param findAdjacent  Boolean for whether the bordering images are already cached in RAM.
*@return   BufferedImage The image to be retrieved.
*/
   public BufferedImage getImage(int x, int y, int zoom, boolean findAdjacent) {
      //try to determine if index is invalid
      if(!isValidIndex(x,y,zoom)) return null;

      /* try getting image from RAM */
      BufferedImage ramImage = getImageFromRAM(x,y,zoom);
      if (ramImage != null) {
         if (findAdjacent) {
          queueAdjacent(x,y,zoom);
         }
         queueHigherLevels(x, y, zoom);
         return ramImage;
      }

      //allocate space for the return
      BufferedImage thumbImage = new BufferedImage(sourceSize.width, sourceSize.height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics2D = thumbImage.createGraphics();

      /* try accessing local image */
      try {
         //System.out.println("Load local image ("+x+","+y+") zoom="+zoom);
         //build source string
         String thisFile = makeCachedName(x,y,zoom);
         // load image from INFILE
         Image image = Toolkit.getDefaultToolkit().createImage(thisFile);
         MediaTracker mediaTracker = new MediaTracker(new Container());
         mediaTracker.addImage(image, 0);
         mediaTracker.waitForID(0);

         if (!(mediaTracker.isErrorAny()))
         {
            graphics2D.drawImage(image, 0, 0, sourceSize.width, sourceSize.height, null);
            addImageToRAM(x,y,zoom,thumbImage);
            if (findAdjacent) {
               queueAdjacent(x,y,zoom);
            }
            queueHigherLevels(x, y, zoom);
            return thumbImage;
         }
      } catch(Exception e) {
      }

      //if we're offline, return here and do not connect
      if(!remoteConnection) return null;

      /* try accessing remote image */
      try{
         System.out.print("Load remote image ("+x+","+y+") zoom="+zoom);
         //build source string
         String thisFile = makeRemoteName(x,y,zoom);
         // load image from INFILE
         Image image = Toolkit.getDefaultToolkit().createImage(new URL(thisFile));
         MediaTracker mediaTracker = new MediaTracker(new Container());
         mediaTracker.addImage(image, 0);
         mediaTracker.waitForID(0);

         if (!(mediaTracker.isErrorAny()))
         {
            graphics2D.drawImage(satellite.getImage(x,y,zoom), 0, 0, sourceSize.width, sourceSize.height, null);
            graphics2D.drawImage(image, 0, 0, sourceSize.width, sourceSize.height, null);
            addImageToRAM(x,y,zoom,thumbImage);
            //save image to cache
            ImageIO.write(thumbImage, "png", new File(cacheDirectory+File.separator+zoom+File.separator+LibString.minimumSize(x,5)+"_"+LibString.minimumSize(y,5)+".png"));
            System.out.println(" [done!]");
            if (findAdjacent) {
               queueAdjacent(x,y,zoom);
            }
            queueHigherLevels(x, y, zoom);
            return thumbImage;
         }
         else
         {
          System.out.println(" [error!]");
         }
      } catch(Exception e) {

          System.out.println("{"+e+"}");

      }

      return null;
   }

/**
* This method creates the URL address to retrieve the hybrid image from the
*online Google database.
*@param x    The x coordinate of the topleft corner of the image being retrieved.
*@param y    The y coordinate of the topleft corner of the image being retrieved.
*@param zoom    The zoom level of the image being retrieved.
*@return String   The URL address for the requested image.
*/

   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      //System.out.print(" [map]");
      //System.out.print("{http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom+"}");
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2t.53&x="+x+"&y="+y+"&zoom="+zoom;
   }

}

