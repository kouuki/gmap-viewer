import java.awt.*;
import java.awt.geom.*;
import java.awt.image.*;

public class GLine implements GDrawableObject{

   Line2D line;
   int zoom;

   public GLine(Point2D p1, Point2D p2, int zoom){
      line = new Line2D.Double(p1, p2);
      this.zoom = zoom;
   }

   public GLine(GPhysicalPoint p1, GPhysicalPoint p2){
     this.zoom = 1;
     //System.out.println("(" + p1.getPixelPoint(zoom).x + ", " + p1.getPixelPoint(zoom).y + ") " + "(" + p2.getPixelPoint(zoom).x + ", " + p2.getPixelPoint(zoom).y);
     line = new Line2D.Double(p1.getPixelPoint(zoom), p2.getPixelPoint(zoom));
   }

   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      Dimension imageDim = new Dimension(image.getWidth(), image.getHeight());
     //imageRect is the currently viewable image window.
      Rectangle imageRect = new Rectangle(p.getPixelX(zoom) - 1, p.getPixelY(zoom) - 1, imageDim.width - 2, imageDim.height - 2);
      Graphics g = image.getGraphics();

      int power = this.zoom - zoom;
      Line2D cLine = new Line2D.Double(line.getX1()*Math.pow(2, power), line.getY1()*Math.pow(2,power), line.getX2()*Math.pow(2,power), line.getY2()*Math.pow(2,power));

      //DEBUG
      //System.out.println("Method Running");
      //System.out.println(p.getPixelX(zoom) + " "  + p.getPixelY(zoom) + " " + imageDim.width + " " + imageDim.height);
      System.out.println("(" + cLine.getX1() + "," + cLine.getY1() + ") (" + cLine.getX2() + "," + cLine.getY2() + ")");

      //If line does not intersect the image, return image w/o modification
      if(cLine.intersects(imageRect)){

         //will contain drawing coordinates for image (will take p as point (0,0)
         int x1 = -1;
         int y1 = -1;
         int x2 = -1;
         int y2 = -1;

         double slope;

   /*
         try{
            slope = (-(int)cLine.getY2() + (int)cLine.getY1())/((int)cLine.getX2() - (int)cLine.getX1());
         }catch(java.lang.ArithmeticException e){
            if((-(int)cLine.getY2() + (int)cLine.getY1()) > 0){
               slope = 999999999.0;
            }else{
               slope = -999999999.0;
            }
         }
         double yIntercept = -(int)cLine.getY2() - slope*(int)cLine.getX2();
   */
      //Represents the lines of the edges of the imageRect object
      Line2D upLine =  new Line2D.Double(imageRect.x, imageRect.y, imageRect.x + imageRect.width, imageRect.y);
      Line2D rightLine = new Line2D.Double(imageRect.x + imageRect.width, imageRect.y, imageRect.x + imageRect.width, imageRect.y + imageRect.height);
      Line2D downLine = new Line2D.Double(imageRect.x + imageRect.width, imageRect.y + imageRect.height, imageRect.x, imageRect.y + imageRect.height);
      Line2D leftLine = new Line2D.Double(imageRect.x, imageRect.y + imageRect.height, imageRect.x, imageRect.y);


         if(imageRect.contains(cLine.getX1(), cLine.getY1())){
            x1 = (int)cLine.getX1();
            y1 = (int)cLine.getY1();
            if(imageRect.contains(cLine.getX2(), cLine.getY2())){
               x2 = (int)cLine.getX2();
               y2 = (int)cLine.getY2();
            }
            else{
               if(upLine.intersectsLine(cLine)){
                  y2 = imageRect.y;
              x2 = (int)MathLib.intersectPointX(upLine, cLine);
               }else if(rightLine.intersectsLine(cLine)){
                  x2 = imageRect.x + imageRect.width;
              y2 = (int)MathLib.intersectPointY(rightLine, cLine);
               }else if(downLine.intersectsLine(cLine)){
                  y2 = imageRect.y + imageRect.height;
                  x2 = (int)MathLib.intersectPointX(downLine, cLine);
               }else if(leftLine.intersectsLine(cLine)){
                  x2 = imageRect.x;
                  y2 = (int)MathLib.intersectPointY(leftLine, cLine);
               }
            }
         }
         else{
            if(imageRect.contains(cLine.getX2(), cLine.getY2())){
               x2 = (int)cLine.getX2();
               y2 = (int)cLine.getY2();
            if(upLine.intersectsLine(cLine)){
                  y1 = imageRect.y;
              x1 = (int)MathLib.intersectPointX(upLine, cLine);
               }else if(rightLine.intersectsLine(cLine)){
                  x1 = imageRect.x + imageRect.width;
              y1 = (int)MathLib.intersectPointY(rightLine, cLine);
               }else if(downLine.intersectsLine(cLine)){
                  y1 = imageRect.y + imageRect.height;
                  x1 = (int)MathLib.intersectPointX(downLine, cLine);
               }else if(leftLine.intersectsLine(cLine)){
                  x1 = imageRect.x;
                  y1 = (int)MathLib.intersectPointY(leftLine, cLine);
               }
            }
            else{
               if(upLine.intersectsLine(cLine)){
                  if(x1 < 0 && y1 < 0){
                     y1 = imageRect.y;
                     x1 = (int)MathLib.intersectPointX(upLine, cLine);
                  }
                  else{
                     y2 = imageRect.y;
                     x2 = (int)MathLib.intersectPointX(upLine, cLine);
                  }
               }
               if(rightLine.intersectsLine(cLine)){
                  if(x1 < 0 && y1 < 0){
                     x1 = imageRect.x + imageRect.width;
                     y1 = (int)MathLib.intersectPointY(rightLine, cLine);
                  }
                  else{
                     x2 = imageRect.x + imageRect.width;
                     y2 = (int)MathLib.intersectPointY(rightLine, cLine);
                  }
               }
               if(downLine.intersectsLine(cLine)){
                  if(x1 < 0 && y1 < 0){
                     y1 = imageRect.y + imageRect.height;
                     x1 = (int)MathLib.intersectPointX(downLine, cLine);
                  }
                  else{
                     y2 = imageRect.y + imageRect.height;
                     x2 = (int)MathLib.intersectPointX(downLine, cLine);
                  }
               }
               if(leftLine.intersectsLine(cLine)){
                  if(x1 < 0 && y1 < 0){
                     x1 = imageRect.x;
                     y1 = (int)MathLib.intersectPointY(leftLine, cLine);
                  }
                  else{
                     x2 = imageRect.x;
                     y2 = (int)MathLib.intersectPointY(leftLine, cLine);
                  }
               }
            }
         }

         x1 -= (int)p.getPixelX(zoom);
         y1 -= (int)p.getPixelY(zoom);

         x2 -= (int)p.getPixelX(zoom);
         y2 -= (int)p.getPixelY(zoom);

         //DEBUG
         System.out.println(x1 + " " + y1);
         System.out.println(x2 + " " + y2);

         //int [] xPoints = {x1 - 2, x1 + 2, x2 + 2, x2 + 2, x2 - 2};
         //int [] yPoints = {y1 - 2, y1 - 2, y2 - 2, y2 + 2, y2 + 2};
         //int nPoints = 5;
         //Polygon polyLine = new Polygon(xPoints, yPoints, nPoints);

       g.setColor(new Color(0,0,155));

       //Draws a line b/w each pixel of a 3 by 3 square surrounding the center pixel.
         g.drawLine(x1,y1,x2,y2);
       g.drawLine(x1-1,y1,x2-1,y2);
       g.drawLine(x1,y1-1,x2,y2-1);
       g.drawLine(x1-1,y1-1,x2-1,y2-1);
       g.drawLine(x1+1,y1,x2-1,y2);
       g.drawLine(x1,y1+1,x2,y2+1);
       g.drawLine(x1+1,y1+1,x2+1,y2+1);
       g.drawLine(x1+1,y1-1,x2+1,y2-1);
       g.drawLine(x1-1,y1+1,x2-1,y2+1);
      }
   }

   public Rectangle getRectangle(GPhysicalPoint p, int zoom){

      //implementation???

      return new Rectangle(10,10,50,50);
   }

}