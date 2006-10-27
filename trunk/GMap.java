
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
      //convert center to pixels
      Point centerPixels = center.getPixelPoint(zoom);
      int x = centerPixels.x - (width/2);
      int y = centerPixels.y - (height/2);

      //load image
      BufferedImage toReturn = getImage(x,y,width,height,zoom,cachedZoom);
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
      return getImageFromDS(x,y,w,h,zoom,cachedZoom);
   }

   private BufferedImage getImageFromDS(int x, int y, int w, int h, int zoom, int cachedZoom){
      //validate
      if(x < 0 || y < 0 || w <= 0 || h <= 0) return getDefaultImage(w,h);

      //find index of point
      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      //find coord of our starting point
      int xCoord = x%GDataSource.sourceSize.width;
      int yCoord = y%GDataSource.sourceSize.height;

      //load this index
      BufferedImage image = getIndexedImage(xIndex,yIndex,zoom,cachedZoom);

      //get info about the image
      Dimension imageSize = new Dimension(image.getWidth(),image.getHeight());

      //find the width of what we CAN paint
      int paintWidth = imageSize.width - xCoord;
      int paintHeight = imageSize.height - yCoord;

      //initialize the variables that will tell us if we are done
      boolean xDone = (paintWidth >= w);
      boolean yDone = (paintHeight >= h);

      //create buffered image for return
      BufferedImage toReturn = new BufferedImage(w,h,BufferedImage.TYPE_INT_ARGB);
      Graphics2D g = toReturn.createGraphics();

      //handles the possible cases - whether or not we have reached the base
      //case for width and height
      if(xDone && yDone){
         g.drawImage(image.getSubimage(xCoord, yCoord, w, h), 0, 0, w, h, null);
      }
      else if(!xDone && yDone){
         g.drawImage(image.getSubimage(xCoord, yCoord, paintWidth, h), 0, 0, paintWidth, h, null);
         g.drawImage(getImageFromDS(x+paintWidth,y,w-paintWidth,h,zoom,cachedZoom), paintWidth, 0, w-paintWidth, h, null);

      }
      else if(xDone && !yDone){
         g.drawImage(image.getSubimage(xCoord, yCoord, w, paintHeight), 0, 0, w, paintHeight, null);
         g.drawImage(getImageFromDS(x,y+paintHeight,w,h-paintHeight,zoom,cachedZoom), 0, paintHeight, w, h-paintHeight, null);
      }
      else if(!xDone && !yDone){
         g.drawImage(image.getSubimage(xCoord, yCoord, paintWidth, paintHeight), 0, 0, paintWidth, paintHeight, null);
         g.drawImage(getImageFromDS(x+paintWidth,y,w-paintWidth,h,zoom,cachedZoom), paintWidth, 0, w-paintWidth, h, null);
         g.drawImage(getImageFromDS(x,y+paintHeight,paintWidth,h-paintHeight,zoom,cachedZoom), 0, paintHeight, paintWidth, h-paintHeight, null);
      }
      return toReturn;
   }

   public void cacheImage(int x, int y, int w, int h, int zoom){
      cacheImageFromDS(x,y,w,h,zoom);
   }

   private void cacheImageFromDS(int x, int y, int w, int h, int zoom){
      //find index of point
      int xIndex = x/GDataSource.sourceSize.width;
      int yIndex = y/GDataSource.sourceSize.height;

      //find coord of our starting point
      int xCoord = x%GDataSource.sourceSize.width;
      int yCoord = y%GDataSource.sourceSize.height;

      //actually download this thing
      gDataSource.cache(xIndex,yIndex,zoom);

      //get info about the image
      Dimension imageSize = new Dimension(GDataSource.sourceSize.width,GDataSource.sourceSize.height);

      //find the width of what we CAN paint
      int paintWidth = imageSize.width - xCoord;
      int paintHeight = imageSize.height - yCoord;

      //initialize the variables that will tell us if we are done
      boolean xDone = (paintWidth >= w);
      boolean yDone = (paintHeight >= h);

      //handles the possible cases - whether or not we have reached the base
      //case for width and height
      if(xDone && yDone){

      }
      else if(!xDone && yDone){
         cacheImageFromDS(x+paintWidth,y,w-paintWidth,h,zoom);

      }
      else if(xDone && !yDone){
         cacheImageFromDS(x,y+paintHeight,w,h-paintHeight,zoom);
      }
      else if(!xDone && !yDone){
         cacheImageFromDS(x+paintWidth,y,w-paintWidth,h,zoom);
         cacheImageFromDS(x,y+paintHeight,paintWidth,h-paintHeight,zoom);
      }
   }

   //this alphacomposite controls transparency
   AlphaComposite cacheGridComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.4f);

   public BufferedImage getIndexedImage(int x, int y, int zoom, int cacheZoom){
      BufferedImage thumbImage = gDataSource.getImage(x,y,zoom);

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
