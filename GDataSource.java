import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;

abstract class GDataSource {
   //dimension of source files
   public static final Dimension sourceSize = new Dimension(256,256);
   /**
    * This constant restricts the size of the download queue.
    */
   public static int QUEUE_MAX_SIZE = 32;

   //where to put files stored on the server
   private String cacheDirectory;

   /* The Queue for pending image downloads. */
   private Queue<GDataImage> downloadQueue;

   /* The size of the download queue; needed because Queue.size() is not O(1). */
   private int queueSize;

   //private
   private Hashtable<String,GDataImage> ramCache;
   private int lastPointer;

   //constructor
   public GDataSource(String cacheDirectory){
      this.cacheDirectory = cacheDirectory;
      ramCache = new Hashtable<String, GDataImage>();
      lastPointer = 0;
      this.downloadQueue = new ConcurrentLinkedQueue<GDataImage>();
      this.queueSize = 0;
      verifyCacheDirectories();
   }

   //get methods
   public String getCacheDirectory(){
      return cacheDirectory;
   }

   //cached or not methods
   public boolean isCached(int x, int y, int zoom){
      File file = new File(makeCachedName(x,y,zoom));
      return file.exists();
   }

   //cache method
   public void cache(int x, int y, int zoom) {
      if (!isCached(x,y,zoom)) {
         getImage(x, y, zoom, false);
      }
   }

   /**
    * This method will return the image corresponding to the specified point
    * and zoom level. The image could be in any of three places: RAM, memory,
    * or the Google servers. This method checks the RAM before the memory and
    * only checks remotely if it was not found locally.
    * <p>
    * Furthermore, this method will queue for download all adjacent images as
    * well as images at all higher zoom levels.
    *
    * @param x The horizontal point
    * @param y The vertical point
    * @param zoom The zoom level (1-15)
    * @return A BufferedImage containing the Google Map Image or null if the
    *         image could not be loaded.
    */
   public BufferedImage getImage(int x, int y, int zoom) {
     return getImage(x, y, zoom, false);
   }

   /**
    * This method does the real work of getImage(). The findAdjacent
    * parameter is necessary because we want to have the option to not
    * download the adjacent/bordering images. In other words, a call to
    * the public getImage() will find the bordering images, but all calls to
    * getImage() made within that initial call will not download the adjacent
    * images.
    */
   public BufferedImage getImage(int x, int y, int zoom, boolean findAdjacent) {
      /* try getting image from RAM */
      BufferedImage ramImage = getImageFromRAM(x,y,zoom);
      if (ramImage != null) {
         if (findAdjacent) {
          queueAdjacent(x,y,zoom);
         }
         cacheHigherLevels(x, y, zoom);
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
            cacheHigherLevels(x, y, zoom);
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
            graphics2D.drawImage(image, 0, 0, sourceSize.width, sourceSize.height, null);
            addImageToRAM(x,y,zoom,thumbImage);
            //save image to cache
            ImageIO.write(thumbImage, "png", new File(cacheDirectory+File.separator+zoom+File.separator+LibString.minimumSize(x,5)+"_"+LibString.minimumSize(y,5)+".png"));
            System.out.println(" [done!]");
            if (findAdjacent) {
               queueAdjacent(x,y,zoom);
            }
            cacheHigherLevels(x, y, zoom);
            return thumbImage;
         }
         else
         {
          System.out.println("oh we had errors");
         }
      } catch(Exception e) {
      }

      return null;
   }

   private void cacheHigherLevels(int x, int y, int zoom)
   {
     /* cache higher zoom levels */
     if (zoom < GDataImage.ZOOM_MAX && !isCached(x, y, zoom)) {
         queue(new GDataImage(null, x/2, y/2, zoom+1));
      }
   }

   /**
    * Checks to see if the following squares are in the cache. If not, then
    * they are added to the download queue.
    * <p>
    * +----------+----------+----------+
    * | x-1, y-1 |  x, y-1  | x+1, y-1 |
    * +----------+----------+----------+
    * | x-1, y   |  x, y    | x+1, y   |
    * +----------+----------+----------+
    * | x-1, y+1 |  x, y+1  | x+1, y+1 |
    * +----------+----------+----------+
    * <p>
    * @param x
    * @param y
    * @param zoom
    */
   private void queueAdjacent(int x, int y, int zoom) {
      for (int m = x-1, M = x+1; m <= M; m++) {
       if (m < 0) continue;

         for (int n = y-1, N = y+1; n <= N; n++) {
         if (n >= 0) {
            this.queue(new GDataImage(null, m, n, zoom));
         }
         }
      }
      return;
   }

   private void queue(GDataImage img) {
      synchronized (downloadQueue) {
         if (!isCached(img.getX(), img.getY(), img.getZoom()) && !downloadQueue.contains(img)) {
            while (this.queueSize >= QUEUE_MAX_SIZE) {
            downloadQueue.poll();
            queueSize--;
            }

           downloadQueue.offer(img);
           queueSize++;
           System.out.println("Added " + img + " to download queue.");
         }
      }
   }

   /**
    * Iterates through the download queue and caches every image. Then, it
    * removes that image from the queue.
    */
   public void downloadQueue() {
      GDataImage img;
      synchronized (downloadQueue) {
         while ((img = downloadQueue.poll()) != null) {
         System.out.println("   from Q: " +img);
            cache(img.getX(), img.getY(), img.getZoom());
            queueSize--;
         }
      }
   }

   private String makeCachedName(int x, int y, int zoom){
      return cacheDirectory+File.separator+zoom+File.separator+LibString.minimumSize(x,5)+"_"+LibString.minimumSize(y,5)+".png";
   }

   //abstract makeRemoteNameMethod
   protected abstract String makeRemoteName(int x, int y, int zoom);

   //RAM Methods
   public BufferedImage getImageFromRAM(int x, int y, int zoom){
      /*for(int i=0;i<ramCache.length;i++)
         if(ramCache[i] != null)
            if(x == ramCache[i].getX() && y == ramCache[i].getY() && zoom == ramCache[i].getZoom())
               return ramCache[i].getImage();
      return null;*/
      BufferedImage image = null;
      String key = x + " " + y + " " + zoom;
      GDataImage imageHolder = ramCache.get(key);
      if (imageHolder != null){
         image = imageHolder.getImage();
      }
      return image;
   }

   public void addImageToRAM(int x, int y, int zoom, BufferedImage image){
      /*ramCache[lastPointer] = new GDataImage(image,x,y,zoom);
      lastPointer++;
      if(lastPointer >= ramCache.length) lastPointer = 0;*/
     if(ramCache.size() > 100){
        ramCache.clear();
        System.gc();
     }
     String key = x + " " + y + " " + zoom;
     ramCache.put(key, new GDataImage(image,x,y,zoom));
   }

   /*
    * make sure cache exists
    */
   private void verifyCacheDirectories(){
      for (int i = GDataImage.ZOOM_MIN; i <= GDataImage.ZOOM_MAX; i++) {
         File thisFile = new File(cacheDirectory+File.separator+i);
         if(!thisFile.exists()) thisFile.mkdirs();
      }
   }
}

class GDataSourceSatellite extends GDataSource{

   public GDataSourceSatellite(String cacheDirectory){
      super(cacheDirectory);
   }

   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      String pathToNode = "tqtsr";
      return "http://kh"+serverNumber+".google.com/kh?n=404&v=11&t="+pathToNode;
   }

}


class GDataSourceMap extends GDataSource{

   public GDataSourceMap(String cacheDirectory){
      super(cacheDirectory);
   }

   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2.12&x="+x+"&y="+y+"&zoom="+zoom;
   }

}
