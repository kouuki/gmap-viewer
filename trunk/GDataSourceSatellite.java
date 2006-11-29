import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.awt.image.*;
import java.net.*;
import javax.imageio.ImageIO;


class GDataSourceSatellite extends GDataSource{

   public GDataSourceSatellite(String cacheDirectory){
      super(cacheDirectory);
   }

   protected String makeRemoteName(int x, int y, int zoom){
      int serverNumber = (int)Math.round(Math.random()*3.0);
      //System.out.print(" [satellite]");
      String pathToNode = makeRemoteSatName(x,y,zoom);

      System.out.println(pathToNode);
      return "http://kh"+serverNumber+".google.com/kh?n=404&v=11&t="+pathToNode;
   }

}

