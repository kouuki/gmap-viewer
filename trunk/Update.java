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


class Update{

   public static void main(String[] args){

      int zoom = -1;
      if(args.length == 1) zoom = Integer.parseInt(args[0]);

      GMap map = new GMap();

      for(int i=15;i>=0;i--){
         if(i != zoom && zoom != -1) continue;
         File file = new File("cache/"+i);
         String[] list = file.list();
         for(int j=0;j<list.length;j++){
            try{
               String[] split1 = LibString.split(list[j],'_');
               String[] split2 = LibString.split(split1[1],'.');
               int x = Integer.parseInt(split1[0]);
               int y = Integer.parseInt(split2[0]);
               map.getIndexedImage(x,y,i,-1);
               System.out.println("Done with Zoom "+i+" ("+x+")("+y+")");
            }catch(Exception e){System.out.println("Skipped: cache/"+i+"/"+list[j]);}
         }
      }


   }
}