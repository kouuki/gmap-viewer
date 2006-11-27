

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

class GMap{

   /*
   This class contains the modifications to the map,
   and is capable of generating an image at any
   zoom level. One Gmap is shared by all the viewing
   panes in the GUI class.
   */


   //defaultImage
   private BufferedImage defaultImage;

   //transparency
   private AlphaComposite opacity70 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.70f);
   private AlphaComposite opacity40 = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);


   //google icon
   Image googleImage;

   //keep track of the data object
   private GDataSource gDataSourceMap;
   private GDataSource gDataSourceSatellite;
   private GDataSource gDataSourceHybrid;
   private GDataSource gDataSourceOverlay;

   //GDraw handles the work of painting data NOT in the database
   private GDraw gDraw;

   //message
   public static final int MESSAGE_DOWNLOADING = 0;
   public static final int MESSAGE_PAINTING = 1;

   //modes
   public static final int SATELLITE_MODE = 2;
   public static final int MAP_MODE = 3;
   public static final int HYBRID_MODE = 4;
   private int mode;

   //constructor
   public GMap(){
      //data source
      this.gDataSourceMap = new GDataSourceMap("map_cache");
      this.gDataSourceSatellite = new GDataSourceSatellite("sat_cache");
      this.gDataSourceOverlay = new GDataSourceOverlay("overlay_cache");
      this.gDataSourceHybrid = new GDataSourceHybrid("hybrid_cache",gDataSourceSatellite);

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

   //getters
   public GDataSource getGDataSource(){
      return getGDataSource(mode);
   }

   public GDataSource getGDataSource(int mode){
      if(mode == MAP_MODE) return gDataSourceMap;
      else if(mode == SATELLITE_MODE) return gDataSourceSatellite;
      else if(mode == HYBRID_MODE) return gDataSourceHybrid;
      return null;
   }

   public GDraw getGDraw(){
      return gDraw;
   }

   public int getMode(){
      return mode;
   }

   //setters
   public void setMode(int mode){
      this.mode = mode;
   }

   //actual build image method
   public void paintAsynchronousImage(BufferedImage image, int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      buildImage(image, x,y,w,h,zoom,cachedZoom,listener);
   }

   public BufferedImage getImage(int x, int y, int w, int h, int zoom, int cachedZoom){
     //create buffered image for return
      return getImage(x,y,w,h,zoom,cachedZoom,null);
   }

   public BufferedImage getImage(int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      BufferedImage toReturn = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
      buildImage(toReturn,x,y,w,h,zoom,cachedZoom,listener);
      return toReturn;
   }

   public void cacheImage(int x, int y, int w, int h, int zoom){
      cacheImage(x,y,w,h,zoom,null);
   }
   public void cacheImage(int x, int y, int w, int h, int zoom, GMapListener listener){
      paintAsynchronousImage(null,x,y,w,h,zoom,-1,listener);
   }


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
      if(cacheZoom == -1 || cacheZoom >= zoom) return thumbImage;

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
      //new instance of JFileChooser and a dialog window
      JFileChooser loadFile = new JFileChooser();
      JDialog cacheDialog = new JDialog();
      //allow only directories to be selected
      loadFile.setFileSelectionMode( JFileChooser.DIRECTORIES_ONLY );
      //show the new instance and declare return value for the instance
      int returnVal = loadFile.showOpenDialog(cacheDialog);
      //declare the file object
      File selectedFile = null;
      //Query the JFileChooser to get the chosen directory from the user
      switch(returnVal) {
         case JFileChooser.APPROVE_OPTION:
            selectedFile = loadFile.getSelectedFile();
            String cacheDirectory = selectedFile.toString();            
         //set the appropriate directories for each gDataSource instance and verify they exist, otherwise create them
            gDataSourceMap.cacheDirectory = cacheDirectory+File.separator+"map_cache";
            gDataSourceMap.verifyCacheDirectories();
            gDataSourceSatellite.cacheDirectory = cacheDirectory+File.separator+"sat_cache";
            gDataSourceSatellite.verifyCacheDirectories();
            gDataSourceHybrid.cacheDirectory = cacheDirectory+File.separator+"hybrid_cache";
            gDataSourceHybrid.verifyCacheDirectories();
            gDataSourceOverlay.cacheDirectory = cacheDirectory+File.separator+"overlay_cache";      
            gDataSourceOverlay.verifyCacheDirectories();
            
            JOptionPane.showMessageDialog(null, "Cache directory set successfully!",
                    "Set Cache Directory", JOptionPane.PLAIN_MESSAGE);                 
            break;
         case JFileChooser.CANCEL_OPTION:
            //Cancel button was clicked - do nothing   
            break;
         case JFileChooser.ERROR_OPTION:
            //Error was detected - make no changes and output error message
            System.out.println("Error detected!");
            break;
      }
   }
   
}

interface GMapListener{
   abstract void updateGMapCompleted(int completed);
   abstract void updateGMapTaskSize(int size);
   abstract void updateGMapMessage(int messageNumber);
   abstract void updateGMapPainting();
   abstract boolean asynchronousGMapStopFlag();

}