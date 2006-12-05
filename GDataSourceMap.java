import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;

/** Class defining the characteristics for the map mode data source. It extends the abstract class GDataSource */
public class GDataSourceMap extends GDataSource{

   /** 
    * Constructor for a new GDataSourceMap 
    * @param cacheDirectory  The directory to store cache files for map mode
    */
   public GDataSourceMap(String cacheDirectory){
      super(cacheDirectory);
   }

   /**
    * Method to convert provided paramters into a useable address for the database
    * @param x    The horizontal cooridinate
    * @param y    The vertical coordinate
    * @param zoom The zoom level
    * @return     The path
    */
   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      //System.out.print(" [map]");
      //System.out.print("{http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom+"}");
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom;
   }

}

