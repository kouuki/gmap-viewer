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

import java.math.BigInteger;
import java.net.*;
import javax.imageio.ImageIO;
import java.awt.geom.*;

class GDataImage {

   public static int ZOOM_MIN = 1;
   public static int ZOOM_MAX = 15;
	
   private BufferedImage image;
   private int x;
   private int y;
   private int zoom;

   public GDataImage(BufferedImage image, int x, int y, int zoom){
      this.image = image;
      this.x = x;
      this.y = y;
      this.zoom = zoom;
   }

   public BufferedImage getImage(){
      return image;
   }

   public int getX(){
      return x;
   }

   public int getY(){
      return y;
   }

   public int getZoom(){
      return zoom;
   }

   public void setImage(BufferedImage image){
      this.image = image;
   }

   public String toString() {
	   return "("+x+","+y+","+zoom+")";
   }

   public boolean equals(Object obj) {
      if (!(obj instanceof GDataImage)) {
         return false;
      }
      
      GDataImage o = (GDataImage) obj;
      return (this.zoom == o.zoom && this.y == o.y && this.x == o.x);
   }
}

