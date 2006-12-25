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

/**
 * GPhysicalPoint is an object that represents a point in space
 * that can be represented in pixels or as a physical location
 */
public class GPhysicalPoint implements Cloneable, Serializable{

   //parameters
   private DoublePoint point;
   public static GCalibration[] calibrationPoints;

   //constructors
   /**
    * Constructor
    * @param point A point in space which this object will represent
    */
   public GPhysicalPoint(DoublePoint point){
      this.point = point;
      calibrate();
   }

   /**
    * Constructor
    * @param x X axis representation of a point
    * @param y Y axis representation of a point
    */
   public GPhysicalPoint(double x, double y){
      this(new DoublePoint(x,y));
   }

   /**
    * Constructor
    * @param point A point in space which this object will represent
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public GPhysicalPoint(Point point, int calibration){
      setPixelPoint(point,calibration);
      calibrate();
   }

   /**
    * Constructor
    * @param x X axis representation of a point
    * @param y Y axis representation of a point
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public GPhysicalPoint(int x, int y, int calibration){
      setPixelPoint(new Point(x,y),calibration);
      calibrate();
   }


   //getters

   //calibration corresponds to zoom levels, different pixel/meter ratios
   /**
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public Point getPixelPoint(int calibration){
      //convert to zero indexing
      calibration--;
      //validate
      if(calibration < 0 || calibration > calibrationPoints.length - 1) return null;
      if(calibrationPoints[calibration] == null) return null;

      //now valid, use appropriate calibrator
      return calibrationPoints[calibration].getPixelPoint(point);
   }

   /**
    *
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    * @return X pixel number
    */
   public int getPixelX(int calibration){
      return getPixelPoint(calibration).x;
   }

   /**
    *
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    * @return Y pixel number
    */
   public int getPixelY(int calibration){
      return getPixelPoint(calibration).y;
   }

   /**
    *
    * @return a Point
    */
   public DoublePoint getPoint(){
      return point;
   }

   /**
    *
    * @return X pixel number for a point
    */
   public double getX(){
      return point.x;
   }

   /**
    *
    * @return Y pixel number for a point
    */
   public double getY(){
      return point.y;
   }


   //setters
   /**
    * Setter for a point
    * @param point A Point
    */
   public void setPoint(DoublePoint point){
      this.point = point;
   }

   /**
    * Setter for X-axis pixel number
    * @param x X-axis pixel number
    */
   public void setX(double x){
      point.x = x;
   }

   /**
    * Setter for Y-axis pixel number
    * @param y Y-axis pixel number
    */
   public void setY(double y){
      point.y = y;
   }

   /**
    *
    * @param setPoint A Point
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public void setPixelPoint(Point setPoint, int calibration){
      //convert to zero indexing
      calibration--;
      //validate
      if(calibration < 0 || calibration > calibrationPoints.length - 1) return ;
      if(calibrationPoints[calibration] == null) return ;

      //now valid, use appropriate calibrator
      point = calibrationPoints[calibration].getPhysicalPoint(setPoint);
   }


   /**
    *
    * @param pixel Pixel number
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public void setPixelX(int pixel, int calibration){
      setPixelPoint(new Point(pixel,getPixelY(calibration)), calibration);
   }

   /**
    *
    * @param pixel Pixel number
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public void setPixelY(int pixel, int calibration){
      setPixelPoint(new Point(getPixelX(calibration),pixel), calibration);
   }

   //clone
   /**
    * Creates a clone of this object. Clone is a deep copy.
    */
   public Object clone(){
      return new GPhysicalPoint((DoublePoint)point.clone());
   }

   /**
    * Converts this object to a string
    */
   public String toString(){
      return "GPhysicalPoint{"+point.x + ", " + point.y + "}";
   }


   //calibrate
   private void calibrate(){
      calibrationPoints = new GCalibration[15];
      for(int i=0;i<calibrationPoints.length;i++)
         calibrationPoints[i] = new GCalibration(
            new DoublePoint(0.0,0.0),
//            new Point((int)(8388608*Math.pow(2,-1*i)), (int)(8421376*Math.pow(2,-1*i))),
            new Point((int)(8388608*Math.pow(2,-1*i)), (int)(8388608*Math.pow(2,-1*i))),
            new DoublePoint(40.6931341533081, -74.0478515625),
            new Point((int)(4937728*Math.pow(2,-1*i)),(int)(6309120*Math.pow(2,-1*i))));

//8376347
   }

}


