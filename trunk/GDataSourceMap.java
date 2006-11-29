import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;

class GDataSourceMap extends GDataSource{

   public GDataSourceMap(String cacheDirectory){
      super(cacheDirectory);
   }

   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      //System.out.print(" [map]");
      //System.out.print("{http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom+"}");
      return "http://mt"+serverNumber+".google.com/mt?n=404&v=w2.79&x="+x+"&y="+y+"&zoom="+zoom;
   }

}

