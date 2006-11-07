

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
   BufferedImage defaultImage;

   //transparency
   AlphaComposite googleOverlayComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.75f);

   //google icon
   Image googleImage;

   //keep track of the data object
   GDataSource gDataSource;

   //message
   public static final int MESSAGE_DOWNLOADING = 0;
   public static final int MESSAGE_PAINTING = 1;


   //constructor
   public GMap(GDataSource gDataSource){
      //data source
      this.gDataSource = gDataSource;

      //build default image
      defaultImage = getDefaultImage(GDataSource.sourceSize.width, GDataSource.sourceSize.height);

      //icon
      ImageIcon loadImage = new ImageIcon("images/google.png");
      googleImage = loadImage.getImage();
   }

   public GMap(){
      this(new GDataSource("cache"));
   }

   //getters
   public GDataSource getGDataSource(){
      return gDataSource;
   }


   //build image
   public BufferedImage getImage(GPhysicalPoint center, int width, int height, int zoom, int cachedZoom){
      return getImage(center, width, height, zoom, cachedZoom, null);
   }
   public BufferedImage getImage(GPhysicalPoint center, int width, int height, int zoom, int cachedZoom, GMapListener listener){
      //convert center to pixels
      Point centerPixels = center.getPixelPoint(zoom);
      int x = centerPixels.x - (width/2);
      int y = centerPixels.y - (height/2);

      //load image
      BufferedImage toReturn = getImage(x,y,width,height,zoom,cachedZoom,listener);
      Graphics2D g = toReturn.createGraphics();

      //get composite to restore later
      Composite originalComposite = g.getComposite();
      g.setComposite(googleOverlayComposite);

      //draw the google icon on
      g.drawImage(googleImage, width-googleImage.getWidth(null), height-googleImage.getHeight(null), googleImage.getWidth(null), googleImage.getHeight(null), null);

      //restore composite

      g.setComposite(originalComposite);

      return toReturn;
   }


   //function generates an image from pixel point a, to pixel point b
   /*
   * How it works:
   * Divide the image into 3 parts. (1) That which can be drawn from
   * the coordinate to the edge of an available image (2) everything
   * to the east and southeast of it (3) everything south of it
   *
   * Paint the part that is available, and recursively call getImage
   * on (2) and (3) until they are empty
   */

   public BufferedImage getImage(int x, int y, int w, int h, int zoom, int cachedZoom){
      return getImage(x,y,w,h,zoom,cachedZoom,null);
   }

   public BufferedImage getImage(int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      return getImageFromDS(x,y,w,h,zoom,cachedZoom,listener);
   }

   public BufferedImage getImageFromDS(int x, int y, int w, int h, int zoom, int cachedZoom){
      return getImageFromDS(x,y,w,h,zoom,cachedZoom,null);
   }

   private BufferedImage getImageFromDS(int x, int y, int w, int h, int zoom, int cachedZoom, GMapListener listener){
      //validate
      if(x < 0 || y < 0 || w <= 0 || h <= 0) return getDefaultImage(w,h);

      //find index of point
      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      //find coord of our starting point
      int xCoord = x%GDataSource.sourceSize.width;
      int yCoord = y%GDataSource.sourceSize.height;

      //load this index
      BufferedImage image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom,listener);

      //get info about the image
      Dimension imageSize = new Dimension(image.getWidth(),image.getHeight());

     //Holds number of row and column images needed
     int rowImages;
     int colImages;

     //find the width of what we CAN paint
     int paintWidth = imageSize.width - xCoord;
     int paintHeight = imageSize.height - yCoord;

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

     //create buffered image for return
      BufferedImage toReturn = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = toReturn.createGraphics();

     //Overal Image coordinates
     int xImage = 0;
     int yImage = 0;

     //DEBUG
     //System.out.println(x + " " + y + " " + w + " " + h + " " + rowImages + " " + colImages);
     //System.out.println();

     //set listener
     if(listener != null) listener.updateGMapTaskSize(rowImages*colImages);

     //Iteratively loops through all needed images and paints them
     for(int row = 0; row < rowImages; row++){
        for(int col = 0; col < colImages; col++){
         //draw Image to graphics object

         //DEBUG
         //System.out.println(xIndex + " " + yIndex +  " " + xImage + " " + yImage +  " " + xCoord + " " + yCoord + " " + paintWidth + " " + paintHeight);
         //System.out.println();

         getSpecificImage(x,y,col,row,toReturn,zoom,cachedZoom,listener);
         if(listener != null) listener.updateGMapCompleted(row*colImages + col);
       }
     }

      return toReturn;
   }

   private BufferedImage getSpecificImage(int x, int y, int imgIndexX, int imgIndexY, BufferedImage buffImg, int zoom, int cachedZoom, GMapListener listener){

      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      xIndex += imgIndexX;
      yIndex += imgIndexY;

      BufferedImage image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom,listener);

      int xCoord = x%GDataSource.sourceSize.width;
      int yCoord = y%GDataSource.sourceSize.height;

      int w = buffImg.getWidth();
      int h = buffImg.getHeight();

      //get info about the image
      Dimension imageSize = new Dimension(image.getWidth(),image.getHeight());

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

      Graphics2D g = (Graphics2D)buffImg.getGraphics();

      //DEBUG
      // System.out.println(xIndex + " " + yIndex +  " " + xImage + " " + yImage +  " " + xCoord + " " + yCoord + " " + paintWidth + " " + paintHeight);
      // System.out.println();

      g.drawImage(image.getSubimage(xCoord, yCoord, paintWidth, paintHeight), xImage, yImage, paintWidth, paintHeight, null);

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

   public void cacheImage(int x, int y, int w, int h, int zoom){
      cacheImage(x, y, w, h, zoom, null);
   }

   public void cacheImage(int x, int y, int w, int h, int zoom, GMapListener listener){
      cacheImageFromDS(x,y,w,h,zoom,listener);
   }

   public void cacheImageFromDS(int x, int y, int w, int h, int zoom){
      cacheImageFromDS(x,y,w,h,zoom,null);
   }
   public void cacheImageFromDS(int x, int y, int w, int h, int zoom, GMapListener listener){
      getImageFromDS(x,y,w,h,zoom,-1);
   }

   //this alphacomposite controls transparency
   AlphaComposite cacheGridComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);

   public BufferedImage getIndexedImage(int x, int y, int zoom, int cacheZoom){
      return getIndexedImage(x, y, zoom, cacheZoom, null);
   }
   public BufferedImage getIndexedImage(int x, int y, int zoom, int cacheZoom, GMapListener listener){

      if(listener != null){
         if(!gDataSource.isCached(x,y,zoom)) listener.updateGMapMessage(GMap.MESSAGE_DOWNLOADING);
         else listener.updateGMapMessage(GMap.MESSAGE_PAINTING);
      }
System.out.println("Painting ("+x+","+y+","+zoom+")");
      BufferedImage thumbImage = gDataSource.getImage(x,y,zoom, true);

      if(thumbImage == null) return defaultImage;

      Graphics2D graphics2D = thumbImage.createGraphics();

      //if we dont have to paint cache, return here
      if(cacheZoom == -1 || cacheZoom >= zoom) return thumbImage;

      //now lets move to painting the cache
      double imageNum = Math.pow(2,zoom-cacheZoom);

      //draw cache lines
      int startX = (int)(imageNum * x);
      int startY = (int)(imageNum * y);

      //get composite to restore later, set new transparent composite
      Composite originalComposite = graphics2D.getComposite();
      graphics2D.setComposite(cacheGridComposite);

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
            if(gDataSource.isCached(startX+i,startY+j,cacheZoom)) graphics2D.setColor(Color.RED);
            else graphics2D.setColor(new Color(155,155,155));

            //shade rectangle
            graphics2D.fillRect(upperLeft.x,upperLeft.y,size.width,size.height);
         }
      }

      //restore composite
      graphics2D.setComposite(originalComposite);

      return thumbImage;
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
}

interface GMapListener{
   abstract void updateGMapCompleted(int completed);
   abstract void updateGMapTaskSize(int size);
   abstract void updateGMapMessage(int messageNumber);
}