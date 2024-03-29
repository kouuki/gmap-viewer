

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

class GMap implements Serializable{

   /**
   This class contains the modifications to the map,
   and is capable of generating an image at any
   zoom level. One Gmap is shared by all the viewing
   panes in the GUI class.
   */


   //defaultImage
   transient private BufferedImage defaultImage;

   //transparency
   transient private AlphaComposite opacity70 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f);
   transient private AlphaComposite opacity40 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);


   //google icon
   Image googleImage;

   //keep track of the data object
   transient private GDataSource gDataSourceMap;
   transient private GDataSource gDataSourceSatellite;
   transient private GDataSource gDataSourceHybrid;
   transient private GDataSource gDataSourceOverlay;

   //GDraw handles the work of painting data NOT in the database
   private GDraw gDraw;

   /**
    * Messages
    */
   public static final int MESSAGE_DOWNLOADING = 0;
   public static final int MESSAGE_PAINTING = 1;

   /**
    * Modes
    */
   public static final int SATELLITE_MODE = 2;
   public static final int MAP_MODE = 3;
   public static final int HYBRID_MODE = 4;
   private int mode;

   /**
   Creates a new GMap object based on a base directory specified in the constructure.
   @param cache - Base directory to search for cached image folders.
   */
   public GMap(String cache){
      //data source
      this.gDataSourceMap = new GDataSourceMap(cache+"/map_cache");
      this.gDataSourceSatellite = new GDataSourceSatellite(cache+"/sat_cache");
      //this.gDataSourceOverlay = new GDataSourceOverlay(cache+"/overlay_cache");
      this.gDataSourceHybrid = new GDataSourceHybrid(cache+"/hybrid_cache",gDataSourceSatellite);

      //build default image
      defaultImage = getDefaultImage(GDataSource.sourceSize.width, GDataSource.sourceSize.height);

      //init gdraw draw object
      this.gDraw = new GDraw();

      //mode
      this.mode = MAP_MODE;

      //icon
      ImageIcon loadImage = new ImageIcon("images/google.png");
      googleImage = loadImage.getImage();
   }
   /**
   Builds a GMap based on a 'cache' sub-directory.
   */
   public GMap(){
      this("cache");
   }

   //getters
   /**
   Returns a GDataSource object used by the GMap object
   @return Returns the GDataSource used to grab the images.
   */
   public GDataSource getGDataSource(){
      return getGDataSource(mode);
   }
   /**
   Returns a GDataSource based on a specific mode using constants:
      MAP_MODE, SATELLITE_MODE, HYBRID_MODE
   @return A GDataSource of the specified mode.
   */
   public GDataSource getGDataSource(int mode){
      if(mode == MAP_MODE) return gDataSourceMap;
      else if(mode == SATELLITE_MODE) return gDataSourceSatellite;
      else if(mode == HYBRID_MODE) return gDataSourceHybrid;
      return null;
   }
   /**

   */
   public GDraw getGDraw(){
      return gDraw;
   }
   /**
   Gets the current used by the GMap object
   @return Current mode: MAP_MODE, SATELLITE_MODE, HYBRID_MODE
   */
   public int getMode(){
      return mode;
   }

   /**
   Sets the current mode of the GMap object
   @param Mode to set:  MAP_MODE, SATELLITE_MODE, HYBRID_MODE
        */
   public void setMode(int mode){
      this.mode = mode;
   }

   /**
   Sets the registered GDraw to gDraw. This method is intended primarily
   for serialization purposes, and should not be used to modify the state
   of the GMap. Instead, use the GDraw's public methods.
   @param gDraw
        */
   public void setGDraw(GDraw gDraw){
      this.gDraw = gDraw;
   }

   /**
    * Method used to build image asynchronously
    * @param image - Image to paint
    * @param x - x Pixel value
    * @param y - y Pixel value
    * @param w - width in pixels
    * @param h - height in pixels
    * @param cachedZoom - zoom level used
    * @param listener - GMapListener object
    */
   public void paintAsynchronousImage(BufferedImage image, int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      buildImage(image, x,y,w,h,zoom,cachedZoom,listener);
   }
   /**
    * Returns image at x and y
    * @param x - x Pixel value
    * @param y - y Pixel value
    * @param w - width in pixels
    * @param h - height in pixels
    * @param cachedZoom - zoom level used
    */
   public BufferedImage getImage(int x, int y, int w, int h, int zoom, int cachedZoom){
     //create buffered image for return
      return getImage(x,y,w,h,zoom,cachedZoom,null);
   }
   /**
    * Returns image at x and y
    * @param x - x Pixel value
    * @param y - y Pixel value
    * @param w - width in pixels
    * @param h - height in pixels
    * @param cachedZoom - zoom level used
    * @param listener - GMapListener to use in getImage
    */
   public BufferedImage getImage(int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      BufferedImage toReturn = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
      buildImage(toReturn,x,y,w,h,zoom,cachedZoom,listener);
      return toReturn;
   }
   /**
    * Method used to cacheImage
    * @param image - Image to paint
    * @param x - x Pixel value
    * @param y - y Pixel value
    * @param w - width in pixels
    * @param h - height in pixels
    * @param zoom - zoom level used
    */
   public void cacheImage(int x, int y, int w, int h, int zoom){
      cacheImage(x,y,w,h,zoom,null);
   }
   /**
    * Method used to cache image
    * @param image - Image to paint
    * @param x - x Pixel value
    * @param y - y Pixel value
    * @param w - width in pixels
    * @param h - height in pixels
    * @param zoom - zoom level used
    */
   public void cacheImage(int x, int y, int w, int h, int zoom, GMapListener listener){
      paintAsynchronousImage(null,x,y,w,h,zoom,(GPhysicalPoint.MIN_ZOOM - 1),listener);
   }

   /**
    * Main method used to build the image based on a large number of tiles.
    */
   public void buildImage(BufferedImage toReturn, int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      //validate
      //if(x < 0 || y < 0 || w <= 0 || h <= 0) return getDefaultImage(w,h);

      //if(toReturn != null) Graphics2D g = toReturn.createGraphics();

      //find index of point
      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      //find coord of our starting point
      int xCoord = x%GDataSource.sourceSize.width;
      int yCoord = y%GDataSource.sourceSize.height;

     //Checks for invalid xCoord and yCoord
     if(xCoord < 0){
      xCoord = 0;
     }
     if(yCoord < 0){
      yCoord = 0;
     }

      //load this index
      BufferedImage image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom,listener);

      //get info about the image
      //Dimension imageSize = new Dimension(image.getWidth(),image.getHeight());

      //Holds number of row and column images needed
      int rowImages;
      int colImages;

      //find the width of what we CAN paint
      int paintWidth = GDataSource.sourceSize.width - xCoord;
      int paintHeight = GDataSource.sourceSize.height - yCoord;

      //Calculate number of row images
      if((h - paintHeight)%256 == 0){
         rowImages = 1 + (h - paintHeight)/256;
      }
      else{
         rowImages = 2 + (h - paintHeight)/256;
      }

      //Calculate number of column images
      if((w - paintWidth)%256 == 0){
         colImages = 1 + (w - paintWidth)/256;
      }
      else{
         colImages = 2 + (w - paintWidth)/256;
      }

      //Overal Image coordinates
      int xImage = 0;
      int yImage = 0;

      //DEBUG
      //System.out.println(x + " " + y + " " + w + " " + h + " " + rowImages + " " + colImages);
      //System.out.println();

      //set listener
      if(listener != null) listener.updateGMapTaskSize(rowImages*colImages);

      //a counter for the listener
      int completed = 0;

      //Iteratively loops through all CACHED images and paints them
      for(int row = 0; row < rowImages; row++){
         for(int col = 0; col < colImages; col++){
            int thisXIndex = x/GDataSource.sourceSize.width + col;
            int thisYIndex = y/GDataSource.sourceSize.height + row;

            getSpecificImage(x,y,w,h,col,row,toReturn,zoom,cachedZoom,listener,true);
            if(getGDataSource().isCached(thisXIndex,thisYIndex,zoom)){
               if(listener != null){
                  listener.updateGMapCompleted(completed);
                  completed++;
               }
            }
            if(listener.asynchronousGMapStopFlag()) return;
         }
      }

      //do the UNCACHED IMAGES NEXT
      for(int row = 0; row < rowImages; row++){
         for(int col = 0; col < colImages; col++){
            int thisXIndex = x/GDataSource.sourceSize.width + col;
            int thisYIndex = y/GDataSource.sourceSize.height + row;

            if(!getGDataSource().isCached(thisXIndex,thisYIndex,zoom)){
               getSpecificImage(x,y,w,h,col,row,toReturn,zoom,cachedZoom,listener,false);
               if(listener != null){
                  listener.updateGMapCompleted(completed);
                  completed++;
                  if(listener.asynchronousGMapStopFlag()) return;
               }
            }
         }
      }

      //the dispatch to GDraw object
      gDraw.draw(toReturn, new GPhysicalPoint(x,y,zoom), zoom);
   }

   private BufferedImage getSpecificImage(int x, int y, int w, int h, int imgIndexX, int imgIndexY, BufferedImage buffImg, int zoom, int cachedZoom, GMapListener listener, boolean localFilesOnly){

      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      xIndex += imgIndexX;
      yIndex += imgIndexY;

      BufferedImage image = null;
      if(!localFilesOnly || getGDataSource().isCached(xIndex,yIndex,zoom)) image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom,listener);

      int xCoord = x%GDataSource.sourceSize.width;
      int yCoord = y%GDataSource.sourceSize.height;

     //Checks for invalid xCoord and yCoord
     if(xCoord < 0){
      xCoord = 0;
     }
     if(yCoord < 0){
      yCoord = 0;
     }

      //get info about the image
      Dimension imageSize = new Dimension(getGDataSource().sourceSize.width,getGDataSource().sourceSize.height);

      //find the width of what we CAN paint
      int initPaintWidth = imageSize.width - xCoord;
      int initPaintHeight = imageSize.height - yCoord;

      int paintWidth = initPaintWidth;
      int paintHeight = initPaintHeight;

      int rowImages = numOfRows(x,y,h,zoom,cachedZoom);
      int colImages = numOfCols(x,y,w,zoom,cachedZoom);

      if(imgIndexX >= colImages || imgIndexY >= rowImages){
         return null;
      }

      int xImage = 0;
      int yImage = 0;

      int xInitCoord = xCoord;
      int yInitCoord = yCoord;

      if(imgIndexX > 0) {
         xImage = initPaintWidth + (imgIndexX - 1)*imageSize.width;
         xCoord = 0;
         if(imgIndexX < (colImages - 1)){
            paintWidth = imageSize.width;
         }
         else{
            paintWidth = w - ((colImages - 2) * imageSize.width) - (imageSize.width - xInitCoord);
         }
      }
      if(imgIndexY > 0){
         yImage = initPaintHeight + (imgIndexY - 1)*imageSize.height;
         yCoord = 0;
         if(imgIndexY < (rowImages - 1)){
            paintHeight = imageSize.height;
         }
         else{
            paintHeight = h - ((rowImages - 2) * imageSize.height) - (imageSize.height - yInitCoord);
         }
      }

      if(buffImg != null){
         Graphics2D g = (Graphics2D)buffImg.getGraphics();
         if(image != null){
            //System.out.println(xCoord + ":" + yCoord + ":" + paintWidth + ":" + paintHeight);
            g.drawImage(image.getSubimage(xCoord, yCoord, paintWidth, paintHeight), xImage, yImage, paintWidth, paintHeight, null);
         }
         else{
            Composite originalComposite = g.getComposite();
            g.setComposite(opacity40);
            g.setColor(Color.BLACK);
            g.fillRect(xImage, yImage, paintWidth, paintHeight);
            g.setComposite(originalComposite);
         }
      }


      return buffImg;
   }

   /**
    * Calculates the number of rows of tiles needed to build current image.
    */
   public int numOfRows(int x, int y, int h, int zoom, int cachedZoom){
      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      //BufferedImage image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom);

      int yCoord = y%GDataSource.sourceSize.height;

      //find the width of what we CAN paint
      int paintHeight = GDataSource.sourceSize.height - yCoord;

      int rowImages;

      //Calculate number of row images
       if((h - paintHeight)%256 == 0){
          rowImages = 1 + (h - paintHeight)/256;
      }
      else{
         rowImages = 2 + (h - paintHeight)/256;
      }

      return rowImages;
   }
   /**
    * Calculates the number of columns of tiles needed to build current image.
    */
   public int numOfCols(int x, int y, int w, int zoom, int cachedZoom){
      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      //BufferedImage image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom);

      int xCoord = x%GDataSource.sourceSize.height;

      //find the width of what we CAN paint
      int paintWidth = GDataSource.sourceSize.width - xCoord;

      int colImages;

      //Calculate number of row images
       if((w - paintWidth)%256 == 0){
          colImages = 1 + (w - paintWidth)/256;
      }
      else{
         colImages = 2 + (w - paintWidth)/256;
      }

      return colImages;
   }


   public BufferedImage getIndexedImage(int x, int y, int zoom, int cacheZoom){
      return getIndexedImage(x, y, zoom, cacheZoom, null);
   }
   /**
    * Get an image based on index numbers
    */
   public BufferedImage getIndexedImage(int x, int y, int zoom, int cacheZoom, GMapListener listener){

      if(listener != null){
         if(!getGDataSource().isCached(x,y,zoom)){
            listener.updateGMapPainting();
            listener.updateGMapMessage(GMap.MESSAGE_DOWNLOADING);
         }
         else{
            listener.updateGMapMessage(GMap.MESSAGE_PAINTING);
         }
      }

      BufferedImage thumbImage = getGDataSource().getImage(x,y,zoom, true);

      if(thumbImage == null) return defaultImage;

      //if we dont have to paint cache, return here
      if(cacheZoom == (GPhysicalPoint.MIN_ZOOM - 1) || cacheZoom >= zoom) return thumbImage;

      BufferedImage paintedImage = new BufferedImage(GDataSource.sourceSize.width, GDataSource.sourceSize.height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics2D = paintedImage.createGraphics();
      graphics2D.drawImage(thumbImage, 0, 0, GDataSource.sourceSize.width, GDataSource.sourceSize.height, null);

      //now lets move to painting the cache
      double imageNum = Math.pow(2,zoom-cacheZoom);

      //draw cache lines
      int startX = (int)(imageNum * x);
      int startY = (int)(imageNum * y);

      //get composite to restore later, set new transparent composite
      Composite originalComposite = graphics2D.getComposite();
      graphics2D.setComposite(opacity40);

      //draw grid
      for(int i=0;i<imageNum;i++){
         for(int j=0;j<imageNum;j++){
            //points
            Point upperLeft = new Point((int)(GDataSource.sourceSize.width/imageNum) * i,(int)(GDataSource.sourceSize.height/imageNum) * j);
            Dimension size = new Dimension((int)(GDataSource.sourceSize.width/imageNum),(int)(GDataSource.sourceSize.height/imageNum));

            //draw lines
            graphics2D.setColor(new Color(100,100,100));
            graphics2D.drawLine(upperLeft.x,upperLeft.y,upperLeft.x+size.width,upperLeft.y);
            graphics2D.drawLine(upperLeft.x,upperLeft.y,upperLeft.x,upperLeft.y+size.height);

            //check if file exists
            if(getGDataSource().isCached(startX+i,startY+j,cacheZoom)) graphics2D.setColor(Color.RED);
            else graphics2D.setColor(new Color(155,155,155));

            //shade rectangle
            graphics2D.fillRect(upperLeft.x,upperLeft.y,size.width,size.height);
         }
      }

      //restore composite
      graphics2D.setComposite(originalComposite);

      return paintedImage;
   }


   //initialize default image
   private BufferedImage getDefaultImage(int w, int h){
      BufferedImage defaultImage = new BufferedImage(w,h, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics2D = defaultImage.createGraphics();
      graphics2D.setColor(new Color(200,200,200));
      graphics2D.fillRect(0,0,w, h);
      graphics2D.setColor(new Color(130,130,130));
      graphics2D.drawRect(0,0,w-1, h-1);
      return defaultImage;
   }

   /** Method to set the <tt>cacheDirectory</tt> property. */
   public void setCacheDirectory()
   {
   }

}
