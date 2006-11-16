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

class GMarker implements GDrawableObject{

   private GPhysicalPoint point;

   public GMarker(GPhysicalPoint point){
      this.point = point;
   }

   public GPhysicalPoint getPoint(){
      return point;
   }

   public void setPoint(GPhysicalPoint point){
      this.point = point;
   }

   public Rectangle getRectangle(GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null) return null;

      //build a rectangle data element that represents the visible area of the screen
      Point screen = new Point(p.getPixelX(zoom), p.getPixelY(zoom));

      //get the coordinate of the point on our visible screen
      Point pointOnScreen = new Point(point.getPixelX(zoom) - screen.x, point.getPixelY(zoom) - screen.y);

      return new Rectangle(pointOnScreen.x - 5,pointOnScreen.y - 5,10,10);
   }

   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null || image == null) return ;

      //build a rectangle data element that represents the visible area of the screen
      Rectangle screen = new Rectangle(p.getPixelX(zoom), p.getPixelY(zoom), image.getWidth(), image.getHeight());

      //if the point is not on the screen return here
      if(!screen.contains(point.getPixelX(zoom), point.getPixelY(zoom))) return ;

      //create a graphics context
      Graphics2D g = image.createGraphics();

      //get the coordinate of the point on our visible screen
      Point pointOnScreen = new Point(point.getPixelX(zoom) - screen.x, point.getPixelY(zoom) - screen.y);

      //draw it
      g.setColor(new Color(0,0,155));
      g.fillOval(pointOnScreen.x - 5,pointOnScreen.y - 5,10,10);
   }


}