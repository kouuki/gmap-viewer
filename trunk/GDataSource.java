
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


class GDataSource{
   //dimension of source files
   public static final Dimension sourceSize = new Dimension(256,256);

   //where to put files stored on the server
   private String cacheDirectory;

   //constructor
   public GDataSource(String cacheDirectory){
      this.cacheDirectory = cacheDirectory;
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
   public void cache(int x, int y, int zoom){
      if(isCached(x,y,zoom)) return;
      else getImage(x,y,zoom);
   }

   //image methods
   public BufferedImage getImage(int x, int y, int zoom){
      BufferedImage thumbImage;
      thumbImage = new BufferedImage(sourceSize.width, sourceSize.height, BufferedImage.TYPE_INT_ARGB);
      Graphics2D graphics2D = thumbImage.createGraphics();

      //if we havent cached its next higher zoom level, cache it
      //this makes sure that if a user has downloaded data a closeup zoom level, that user also
      //has the zata for all higher zoom levels
      if(zoom < 15) cache(x/2, y/2, zoom+1);

      //create a bit for knowing if we were successful or not
      boolean loadedImage = false;

      //try accessing local image
      if(!loadedImage){
         try{
            //System.out.println("Load local image ("+x+","+y+") zoom="+zoom);
            //build source string
            String thisFile = makeCachedName(x,y,zoom);
            // load image from INFILE
            Image image = Toolkit.getDefaultToolkit().createImage(thisFile);
            MediaTracker mediaTracker = new MediaTracker(new Container());
            mediaTracker.addImage(image, 0);
            mediaTracker.waitForID(0);
            loadedImage = !(mediaTracker.isErrorAny());

            graphics2D.drawImage(image, 0, 0, sourceSize.width, sourceSize.height, null);
         }catch(Exception e){loadedImage = false;}
      }

      //try accessing remote image
      if(!loadedImage){
         try{
            //build source string
            String thisFile = makeRemoteName(x,y,zoom);
            // load image from INFILE
            Image image = Toolkit.getDefaultToolkit().createImage(new URL(thisFile));
            MediaTracker mediaTracker = new MediaTracker(new Container());
            mediaTracker.addImage(image, 0);
            mediaTracker.waitForID(0);
            loadedImage = !(mediaTracker.isErrorAny());
            graphics2D.drawImage(image, 0, 0, sourceSize.width, sourceSize.height, null);
            //save image to cache
            if(loadedImage) ImageIO.write(thumbImage, "png", new File(cacheDirectory+File.separator+zoom+File.separator+LibString.minimumSize(x,5)+"_"+LibString.minimumSize(y,5)+".png"));
         }catch(Exception e){loadedImage = false;}
      }
      //if still no image, fail to default image
      if(!loadedImage){
         //System.out.println("Fail to default for ("+x+","+y+") zoom="+zoom);
         return null;
      }

      //return
      return thumbImage;

   }

   private String makeCachedName(int x, int y, int zoom){
      return cacheDirectory+File.separator+zoom+File.separator+LibString.minimumSize(x,5)+"_"+LibString.minimumSize(y,5)+".png";
   }

   private String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2.12&x="+x+"&y="+y+"&zoom="+zoom;
   }

   //make sure cache exists
   private void verifyCacheDirectories(){
      for(int i=1;i<=15;i++){
         File thisFile = new File(cacheDirectory+File.separator+i);
         if(!thisFile.exists()) thisFile.mkdirs();
      }
   }

}