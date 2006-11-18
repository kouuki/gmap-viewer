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

class GText implements GDrawableObject{

   private GPhysicalPoint point;
   private String text;
   private Rectangle2D textRectangle;
   private int maxDescent;

   public GText(GPhysicalPoint point, String text){
      this.point = point;
      this.text = text;
      this.textRectangle = null;
   }

   public GPhysicalPoint getPoint(){
      return point;
   }

   public void setPoint(GPhysicalPoint point){
      this.point = point;
   }

   public String getText(){
      return text;
   }

   public void setText(String text){
      this.text = text;
   }


   public Rectangle getRectangle(GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null) return null;

      //build a point data element that represents the upper corner of the screen
      Point screen = new Point(p.getPixelX(zoom), p.getPixelY(zoom));

      //get the coordinate of the point on our visible screen
      Point pointOnScreen = new Point(point.getPixelX(zoom) - screen.x, point.getPixelY(zoom) - screen.y);

      return new Rectangle(pointOnScreen.x,(int)(pointOnScreen.y-textRectangle.getHeight()+maxDescent),(int)textRectangle.getWidth(),(int)textRectangle.getHeight()+maxDescent);
   }

   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      //check for nulls to prevent null pointer exceptions
      if(p == null || image == null) return ;

      //build a rectangle data element that represents the visible area of the screen
      Rectangle screen = new Rectangle(p.getPixelX(zoom), p.getPixelY(zoom), image.getWidth(), image.getHeight());

      //create a graphics context
      Graphics2D g = image.createGraphics();

      //text rectangle
      textRectangle = g.getFontMetrics().getStringBounds(text,g);
      maxDescent = g.getFontMetrics().getMaxDescent();

      //if the point is not on the screen return here
      Point pixelLocation = point.getPixelPoint(zoom);
      if(screen.contains(pixelLocation.x, pixelLocation.y)) ;
      else if(screen.contains(pixelLocation.x+textRectangle.getWidth(), pixelLocation.y)) ;
      else if(screen.contains(pixelLocation.x+textRectangle.getWidth(), pixelLocation.y-textRectangle.getHeight()+maxDescent)) ;
      else if(screen.contains(pixelLocation.x, pixelLocation.y-textRectangle.getHeight()+maxDescent)) ;
      else return ;

      //get the coordinate of the point on our visible screen
      Point pointOnScreen = new Point(pixelLocation.x - screen.x, pixelLocation.y - screen.y);

      //draw it
      g.setColor(new Color(0,0,155));
      g.drawString(text,pointOnScreen.x,pointOnScreen.y);
   }


}






