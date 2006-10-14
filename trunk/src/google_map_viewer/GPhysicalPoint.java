/*
GPoint is a point in space that can be represented in pixels
or as a physical location


*/



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


public class GPhysicalPoint implements Cloneable{

   //parameters
   private Point2D.Double point;

   //calibration NEED POINTS
/*
   public static final GCalibration[] calibrationPoints = {
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,17),(int)Math.pow(2,17))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,16),(int)Math.pow(2,16))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,15),(int)Math.pow(2,15))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,14),(int)Math.pow(2,14))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,13),(int)Math.pow(2,13))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,12),(int)Math.pow(2,12))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,11),(int)Math.pow(2,11))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,10),(int)Math.pow(2,10))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,9),(int)Math.pow(2,9))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,8),(int)Math.pow(2,8))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,7),(int)Math.pow(2,7))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,6),(int)Math.pow(2,6))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,5),(int)Math.pow(2,5))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,4),(int)Math.pow(2,4))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,3),(int)Math.pow(2,3))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,2),(int)Math.pow(2,2))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,1),(int)Math.pow(2,1))),
      new GCalibration(new Point2D.Double(0,0), new Point(0,0), new Point2D.Double(1,1), new Point((int)Math.pow(2,0),(int)Math.pow(2,0)))
      };

// 5        new GCalibration(new Point2D.Double(47.606389,-122.330833), new Point(167974,366207), new Point2D.Double(25.773889,-80.193889), new Point(290706,446548)),
*/
/*
   public static final GCalibration[] calibrationPoints = {
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,7)),(int)(65792*Math.pow(2,7))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,7)),(int)(49621*Math.pow(2,7)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,6)),(int)(65792*Math.pow(2,6))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,6)),(int)(49621*Math.pow(2,6)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,5)),(int)(65792*Math.pow(2,5))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,5)),(int)(49621*Math.pow(2,5)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,4)),(int)(65792*Math.pow(2,4))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,4)),(int)(49621*Math.pow(2,4)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,3)),(int)(65792*Math.pow(2,3))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,3)),(int)(49621*Math.pow(2,3)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,2)),(int)(65792*Math.pow(2,2))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,2)),(int)(49621*Math.pow(2,2)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,1)),(int)(65792*Math.pow(2,1))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,1)),(int)(49621*Math.pow(2,1)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,0)),(int)(65792*Math.pow(2,0))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,0)),(int)(49621*Math.pow(2,0)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-1)),(int)(65792*Math.pow(2,-1))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-1)),(int)(49621*Math.pow(2,-1)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-2)),(int)(65792*Math.pow(2,-2))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-2)),(int)(49621*Math.pow(2,-2)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-3)),(int)(65792*Math.pow(2,-3))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-3)),(int)(49621*Math.pow(2,-3)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-4)),(int)(65792*Math.pow(2,-4))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-4)),(int)(49621*Math.pow(2,-4)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-5)),(int)(65792*Math.pow(2,-5))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-5)),(int)(49621*Math.pow(2,-5)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-6)),(int)(65792*Math.pow(2,-6))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-6)),(int)(49621*Math.pow(2,-6)))),
      new GCalibration(new Point2D.Double(0.0,-55.0), new Point((int)(45148*Math.pow(2,-7)),(int)(65792*Math.pow(2,-7))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,-7)),(int)(49621*Math.pow(2,-7))))
   };
*/

   public static final GCalibration[] calibrationPoints = {
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,0)),(int)(8421376*Math.pow(2,0))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,0)),(int)(11718625*Math.pow(2,0)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-1)),(int)(8421376*Math.pow(2,-1))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-1)),(int)(11718625*Math.pow(2,-1)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-2)),(int)(8421376*Math.pow(2,-2))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-2)),(int)(11718625*Math.pow(2,-2)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-3)),(int)(8421376*Math.pow(2,-3))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-3)),(int)(11718625*Math.pow(2,-3)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-4)),(int)(8421376*Math.pow(2,-4))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-4)),(int)(11718625*Math.pow(2,-4)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-5)),(int)(8421376*Math.pow(2,-5))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-5)),(int)(11718625*Math.pow(2,-5)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-6)),(int)(8421376*Math.pow(2,-6))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-6)),(int)(11718625*Math.pow(2,-6)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-7)),(int)(8421376*Math.pow(2,-7))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-7)),(int)(11718625*Math.pow(2,-7)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-8)),(int)(8421376*Math.pow(2,-8))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-8)),(int)(11718625*Math.pow(2,-8)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-9)),(int)(8421376*Math.pow(2,-9))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-9)),(int)(11718625*Math.pow(2,-9)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-10)),(int)(8421376*Math.pow(2,-10))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-10)),(int)(11718625*Math.pow(2,-10)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-11)),(int)(8421376*Math.pow(2,-11))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-11)),(int)(11718625*Math.pow(2,-11)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-12)),(int)(8421376*Math.pow(2,-12))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-12)),(int)(11718625*Math.pow(2,-12)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-13)),(int)(8421376*Math.pow(2,-13))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-13)),(int)(11718625*Math.pow(2,-13)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-14)),(int)(8421376*Math.pow(2,-14))), new Point2D.Double(28.235106, -82.742353), new Point((int)(5375157*Math.pow(2,-14)),(int)(11718625*Math.pow(2,-14))))
   };


   //constructors
   public GPhysicalPoint(Point2D.Double point){
      this.point = point;
   }

   public GPhysicalPoint(double x, double y){
      this(new Point2D.Double(x,y));
   }

   public GPhysicalPoint(Point point, int calibration){
      setPixelPoint(point,calibration);
   }

   public GPhysicalPoint(int x, int y, int calibration){
      setPixelPoint(new Point(x,y),calibration);
   }


   //getters

   //calibration corresponds to zoom levels, different pixel/meter ratios
   public Point getPixelPoint(int calibration){
      //convert to zero indexing
      calibration--;
      //validate
      if(calibration < 0 || calibration > calibrationPoints.length) return null;
      if(calibrationPoints[calibration] == null) return null;

      //now valid, use appropriate calibrator
      return calibrationPoints[calibration].getPixelPoint(point);
   }

   public int getPixelX(int calibration){
      return getPixelPoint(calibration).x;
   }

   public int getPixelY(int calibration){
      return getPixelPoint(calibration).y;
   }

   public Point2D.Double getPoint(){
      return point;
   }

   public double getX(){
      return point.x;
   }

   public double getY(){
      return point.y;
   }


   //setters
   public void setPoint(Point2D.Double point){
      this.point = point;
   }

   public void setX(double x){
      point.x = x;
   }

   public void setY(double y){
      point.y = y;
   }


   public void setPixelPoint(Point setPoint, int calibration){
      //convert to zero indexing
      calibration--;
      //validate
      if(calibration < 0 || calibration > calibrationPoints.length) return ;
      if(calibrationPoints[calibration] == null) return ;

      //now valid, use appropriate calibrator
      point = calibrationPoints[calibration].getPhysicalPoint(setPoint);
   }


   public void setPixelX(int pixel, int calibration){
      setPixelPoint(new Point(pixel,getPixelY(calibration)), calibration);
   }

   public void setPixelY(int pixel, int calibration){
      setPixelPoint(new Point(getPixelX(calibration),pixel), calibration);
   }

   //clone
   public Object clone(){
      return new GPhysicalPoint(point);
   }

   public String toString(){
      return point.x + ", " + point.y;
   }

}


class GCalibration{

   /*
   * GCalibration accepts two calibration points in pixels and lat/long
   * it then provides methods that can convert between them
   *
   * GCalibration is IMMUTABLE - that is, it can not be modified after
   * construction
   *
   */

   //calibration points supplied by user
   private Point2D.Double physicalOnEquator;
   private Point pixelOnEquator;
   private Point2D.Double physical2;
   private Point pixel2;

   //mercator projection data
   private double M;
   private double B;

   public GCalibration(Point2D.Double physicalOnEquator, Point pixelOnEquator, Point2D.Double physical2, Point pixel2){
      //store input
      this.physicalOnEquator = physicalOnEquator;
      this.pixelOnEquator = pixelOnEquator;
      this.physical2 = physical2;
      this.pixel2 = pixel2;

      //compute M and B
      B = pixelOnEquator.y;
      M = (pixel2.y - B)/((0.5)*Math.log( (-1.0)* (Math.sin(degToRad(physical2.x)) + 1.0)/(Math.sin(degToRad(physical2.x)) - 1.0)));


   }


   public Point getPixelPoint(Point2D.Double physical){
      //compute x
      double xm = (pixelOnEquator.x - pixel2.x)/(degToRad(physicalOnEquator.y) - degToRad(physical2.y));
      double xb = pixelOnEquator.x - xm * degToRad(physicalOnEquator.y);
      double xy = xm * degToRad(physical.y) + xb;

      //compute y
      double yy = (M)*(0.5)*Math.log( (-1.0)* (Math.sin(degToRad(physical.x)) + 1.0)/(Math.sin(degToRad(physical.x)) - 1.0)) + B;


      //System.out.println("CAL ("+physicalOnEquator.x+","+physicalOnEquator.y+") --> ("+pixelOnEquator.x+","+pixelOnEquator.y+")");
      //System.out.println("    ("+physical.x+","+physical.y+") --> ("+xy+","+yy+")");

      return new Point((int)xy, (int)yy);
   }

   public Point2D.Double getPhysicalPoint(Point pixel){
      //compute x
      double xm = (degToRad(physicalOnEquator.y) - degToRad(physical2.y))/(pixelOnEquator.x - pixel2.x);
      double xb = degToRad(physicalOnEquator.y) - xm * pixelOnEquator.x;
      double xy = xm * pixel.x + xb;

      //compute y
      double yy = 2.0*Math.atan(Math.pow(Math.E,((pixel.y-B)/M)))-(Math.PI/2.0);


      xy = radToDeg(xy);
      yy = radToDeg(yy);
      return new Point2D.Double(yy, xy);
   }


   //helpers for doing repetitive mathematics
   public static double degToRad(double degrees){
      return degrees * Math.PI/180.0;
   }

   public static double radToDeg(double radians){
      return radians * 180.0/Math.PI;
   }


   public static void main(String[] args){
      //make points
      Point2D.Double physical1 = new Point2D.Double(0.0,55.0);
      Point pixel1 = new Point(45148,65792);
      Point2D.Double physical2 = new Point2D.Double(40.0,0.0);
      Point pixel2 = new Point(65536,49621);

      //make calibration object
//      GCalibration test = new GCalibration(new Point2D.Double(40.0,0.0), new Point((int)(45148*Math.pow(2,0)),(int)(65792*Math.pow(2,0))), new Point2D.Double(40.0,0.0), new Point((int)(65536*Math.pow(2,0)),(int)(49621*Math.pow(2,0))));
      GCalibration test = new GCalibration(physical1, pixel1, physical2, pixel2);

      //test using calibration points
      System.out.println("------------------");
      System.out.println("M="+test.M);
      System.out.println("B="+test.B);
      System.out.println("------------------");
      System.out.println("getPixelPoint Test");
      System.out.println("------------------");
      System.out.println("("+physical1.x+", "+physical1.y+") --> ("+test.getPixelPoint(physical1).x+", "+test.getPixelPoint(physical1).y+")");
      System.out.println("("+physical2.x+", "+physical2.y+") --> ("+test.getPixelPoint(physical2).x+", "+test.getPixelPoint(physical2).y+")");
      System.out.println("");
      System.out.println("");
      System.out.println("");
      System.out.println("---------------------");
      System.out.println("getPhysicalPoint Test");
      System.out.println("---------------------");
      System.out.println("("+pixel1.x+", "+pixel1.y+") --> ("+test.getPhysicalPoint(pixel1).x+", "+test.getPhysicalPoint(pixel1).y+")");
      System.out.println("("+pixel2.x+", "+pixel2.y+") --> ("+test.getPhysicalPoint(pixel2).x+", "+test.getPhysicalPoint(pixel2).y+")");
   }


}