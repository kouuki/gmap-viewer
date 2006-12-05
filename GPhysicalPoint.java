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
	GPhysicalPoint is a representation of a point on the 2D google maps plane.  It represents both pixel and physical locations, basing its mapping calculations on the mercator projections.
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
	Constructs a GPhysicalPoint object based on a Point2D.Double object representing the physical (latitude, longitude) location of the point.
	@param point - Point2D representing latitude and longitude data.
       */
   public GPhysicalPoint(Point2D.Double point){
      this.point = point;
   }
	/**
	Constructs a GPhysicalPoint object based on a physical values.
	@param x - latitude
	@param y - longitude
	*/
   public GPhysicalPoint(double x, double y){
      this(new Point2D.Double(x,y));
   }
	/**
	Constructs a GPhysicalPoint object based on pixel point data and a zoom level.
	@param point - represnts a pixel point in the (x,y) format
	@param calibration - zoom level the pixel point represents (1 - 15)
	*/
   public GPhysicalPoint(Point point, int calibration){
      setPixelPoint(point,calibration);
   }
	/**
	Constructs a GPhysicalPoint object based on pixel point data and a zoom level.
	@param x - represents the x pixel point value
	@param y - represnts the y pixel point value
	@param calibration - zoom level the pixel point represents(1-15)
	*/
   public GPhysicalPoint(int x, int y, int calibration){
      setPixelPoint(new Point(x,y),calibration);
   }


   //getters

   //calibration corresponds to zoom levels, different pixel/meter ratios
   /**
	Returns the pixel point location of the GPhysicalPoint based on a zoom level.
	@param calibration - zoom level you want the returned pixel point to correspond to.
	@return A Point object representing the pixel of the current point at zoom level calibration in a (x,y) format.
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
	Returns the x pixel value based on a zoom level
	@param calibration - zoom level you want the returned x value to correspond to.
	@return An int value respresnting the x pixel value.
	*/
   public int getPixelX(int calibration){
      return getPixelPoint(calibration).x;
   }
	/**
	Returns the y pixel value based on a zoom level
	@param calibration - zoom level you want the returned y value to correspond to.
	@return An int value respresnting the y pixel value.
	*/
   public int getPixelY(int calibration){
      return getPixelPoint(calibration).y;
   }
	/**
	Returns the physical point location of the GPhysicalPoint.
	@return A Point2D object representing the physical location of the current point in a (latitude, longitude) format.
	*/
   public Point2D.Double getPoint(){
      return point;
   }
	/**
	Returns a latitude of the current GPhysicalPoint
	@return Latitude
	*/
   public double getX(){
      return point.x;
   }
	/**
	Returns the longitude of the current GPhysicalPoint
	@return longitude
	*/
   public double getY(){
      return point.y;
   }


   //setters
   /**
	Resets the value of the GPhysicalPoint to the latitude and longitude represented by the Point2D paramter.
	@param point - Represents the latitude and longitude of a new physical point.
	*/
   public void setPoint(Point2D.Double point){
      this.point = point;
   }
	/**
	Resets the latitude value of the current GPhysicalPoint
	@param x - latitude value
	*/
   public void setX(double x){
      point.x = x;
   }
	/**
	Resets the longitude value of the current GPhysicalPoint
	@param y - longitude value
	*/
   public void setY(double y){
      point.y = y;
   }

	/**
	Resets the value of the GPhysicalPoint to the x and y pixel values represented by the Point paramter.
	@param point - Represents the x and y of a new pixel point.
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
	Resets the x pixel value of the current GPhysicalPoint
	@param x - pixel x value
	*/
   public void setPixelX(int pixel, int calibration){
      setPixelPoint(new Point(pixel,getPixelY(calibration)), calibration);
   }
	
	/**
	Resets the x pixel value of the current GPhysicalPoint
	@param y - pixel y value
	*/
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


