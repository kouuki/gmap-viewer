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

   //Test method
   public GCalibration(Point2D.Double physicalOnEquator, Point pixelOnEquator, Point2D.Double physical2, Point pixel2, Point2D.Double physical3, Point pixel3){
      //store input
      this.physicalOnEquator = physicalOnEquator;
      this.pixelOnEquator = pixelOnEquator;
      this.physical2 = physical2;
      this.pixel2 = pixel2;


      //compute M and B
      B = pixelOnEquator.y;
     double M1;
     double M2;

      //M1 = (pixel2.y - B)/((0.5)*Math.log( (-1.0)* (Math.sin(degToRad(physical2.x)) + 1.0)/(Math.sin(degToRad(physical2.x)) - 1.0)));

     //M2 = (pixel3.y - B)/((0.5)*Math.log( (-1.0)* (Math.sin(degToRad(physical3.x)) + 1.0)/(Math.sin(degToRad(physical3.x)) - 1.0)));

      M1 = (pixel2.y - B)/((0.5)*Math.log( (1.0)* (Math.sin(degToRad(physical2.x)) + 1.0)/(1.0 - Math.sin(degToRad(physical2.x)))));

     M2 = (pixel3.y - B)/((0.5)*Math.log( (1.0)* (Math.sin(degToRad(physical3.x)) + 1.0)/(1.0 - Math.sin(degToRad(physical3.x)))));


     M = M1;

     System.out.println("M1: " + M1);
     System.out.println("M2: " + M2);
     System.out.println("M: " + M);
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