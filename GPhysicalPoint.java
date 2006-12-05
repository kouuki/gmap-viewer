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


   /**
    * calibrationPoints is an array holding multiple GCalibration objects
    */
   public static final GCalibration[] calibrationPoints = {
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,0)),(int)(8421376*Math.pow(2,0))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,0)),(int)(6309120*Math.pow(2,0)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-1)),(int)(8421376*Math.pow(2,-1))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-1)),(int)(6309120*Math.pow(2,-1)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-2)),(int)(8421376*Math.pow(2,-2))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-2)),(int)(6309120*Math.pow(2,-2)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-3)),(int)(8421376*Math.pow(2,-3))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-3)),(int)(6309120*Math.pow(2,-3)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-4)),(int)(8421376*Math.pow(2,-4))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-4)),(int)(6309120*Math.pow(2,-4)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-5)),(int)(8421376*Math.pow(2,-5))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-5)),(int)(6309120*Math.pow(2,-5)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-6)),(int)(8421376*Math.pow(2,-6))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-6)),(int)(6309120*Math.pow(2,-6)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-7)),(int)(8421376*Math.pow(2,-7))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-7)),(int)(6309120*Math.pow(2,-7)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-8)),(int)(8421376*Math.pow(2,-8))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-8)),(int)(6309120*Math.pow(2,-8)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-9)),(int)(8421376*Math.pow(2,-9))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-9)),(int)(6309120*Math.pow(2,-9)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-10)),(int)(8421376*Math.pow(2,-10))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-10)),(int)(6309120*Math.pow(2,-10)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-11)),(int)(8421376*Math.pow(2,-11))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-11)),(int)(6309120*Math.pow(2,-11)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-12)),(int)(8421376*Math.pow(2,-12))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-12)),(int)(6309120*Math.pow(2,-12)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-13)),(int)(8421376*Math.pow(2,-13))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-13)),(int)(6309120*Math.pow(2,-13)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-14)),(int)(8421376*Math.pow(2,-14))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-14)),(int)(6309120*Math.pow(2,-14))))
   };

/*
   4945779,6303939
   40.7773237,-73.8751358
*/

/*
public static final GCalibration[] calibrationPoints = {
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,0)),(int)(8421376*Math.pow(2,0))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,0)),(int)(6309120*Math.pow(2,0))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,0)),(int)(7005696*Math.pow(2,0))) ),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-1)),(int)(8421376*Math.pow(2,-1))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-1)),(int)(6309120*Math.pow(2,-1))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-1)),(int)(7005696*Math.pow(2,-1)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-2)),(int)(8421376*Math.pow(2,-2))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-2)),(int)(6309120*Math.pow(2,-2))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-2)),(int)(7005696*Math.pow(2,-2)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-3)),(int)(8421376*Math.pow(2,-3))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-3)),(int)(6309120*Math.pow(2,-3))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-3)),(int)(7005696*Math.pow(2,-3)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-4)),(int)(8421376*Math.pow(2,-4))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-4)),(int)(6309120*Math.pow(2,-4))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-4)),(int)(7005696*Math.pow(2,-4)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-5)),(int)(8421376*Math.pow(2,-5))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-5)),(int)(6309120*Math.pow(2,-5))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-5)),(int)(7005696*Math.pow(2,-5)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-6)),(int)(8421376*Math.pow(2,-6))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-6)),(int)(6309120*Math.pow(2,-6))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-6)),(int)(7005696*Math.pow(2,-6)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-7)),(int)(8421376*Math.pow(2,-7))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-7)),(int)(6309120*Math.pow(2,-7))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-7)),(int)(7005696*Math.pow(2,-7)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-8)),(int)(8421376*Math.pow(2,-8))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-8)),(int)(6309120*Math.pow(2,-8))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-8)),(int)(7005696*Math.pow(2,-8)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-9)),(int)(8421376*Math.pow(2,-9))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-9)),(int)(6309120*Math.pow(2,-9))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-9)),(int)(7005696*Math.pow(2,-9)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-10)),(int)(8421376*Math.pow(2,-10))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-10)),(int)(6309120*Math.pow(2,-10))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-10)),(int)(7005696*Math.pow(2,-10)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-11)),(int)(8421376*Math.pow(2,-11))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-11)),(int)(6309120*Math.pow(2,-11))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-11)),(int)(7005696*Math.pow(2,-11)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-12)),(int)(8421376*Math.pow(2,-12))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-12)),(int)(6309120*Math.pow(2,-12))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-12)),(int)(7005696*Math.pow(2,-12)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-13)),(int)(8421376*Math.pow(2,-13))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-13)),(int)(6309120*Math.pow(2,-13))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-13)),(int)(7005696*Math.pow(2,-13)))),
      new GCalibration(new Point2D.Double(0.0,0.0), new Point((int)(8388608*Math.pow(2,-14)),(int)(8421376*Math.pow(2,-14))), new Point2D.Double(40.6931341533081, -74.0478515625), new Point((int)(4937728*Math.pow(2,-14)),(int)(6309120*Math.pow(2,-14))), new Point2D.Double(28.4300528923357,-81.3153076171875), new Point((int)(4598784*Math.pow(2,-14)),(int)(7005696*Math.pow(2,-14))))
   };
*/

   //constructors
   /**
    * Constructor
    * @param point A point in space which this object will represent 
    */
   public GPhysicalPoint(Point2D.Double point){
      this.point = point;
   }

   /**
    * Constructor
    * @param x X axis representation of a point
    * @param y Y axis representation of a point
    */
   public GPhysicalPoint(double x, double y){
      this(new Point2D.Double(x,y));
   }

   /**
    * Constructor
    * @param point A point in space which this object will represent
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public GPhysicalPoint(Point point, int calibration){
      setPixelPoint(point,calibration);
   }

   /**
    * Constructor
    * @param x X axis representation of a point
    * @param y Y axis representation of a point
    * @param calibration Calibration is a number to corresponds to zoom levels, different pixel/meter ratios
    */
   public GPhysicalPoint(int x, int y, int calibration){
      setPixelPoint(new Point(x,y),calibration);
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
   public Point2D.Double getPoint(){
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
   public void setPoint(Point2D.Double point){
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
    * Creates a new clone of this object
    */
   public Object clone(){
      return new GPhysicalPoint(point);
   }

   /**
    * Converts this object to a string
    */
   public String toString(){
      return point.x + ", " + point.y;
   }

}


