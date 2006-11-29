import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;



class GDataSourceHybrid extends GDataSource{
   private GDataSource satellite;

   public GDataSourceHybrid(String cacheDirectory, GDataSource satellite){
      super(cacheDirectory);
      this.satellite = satellite;
   }


   public BufferedImage getImage(int x, int y, int zoom, boolean findAdjacent) {
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



   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      //System.out.print(" [map]");
      //System.out.print("{http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom+"}");
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2t.30&x="+x+"&y="+y+"&zoom="+zoom;
   }

}

