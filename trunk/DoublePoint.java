import java.io.Serializable;

/** Simple extension of Point2D.Double that is Serializable. */

public class DoublePoint extends java.awt.geom.Point2D implements Serializable, Cloneable{

   /** X coordinate. */
   public double x;

   /** Y coordinate. */
   public double y;

   /** Default constructor. */
   public DoublePoint(){
      this(0.0, 0.0);
   }

   /** Simple width, height, constructor. */
   public DoublePoint(double x, double y){
      this.x = x;
      this.y = y;
   }

   /** Get X coordinate. */
   public double getX(){
      return x;
   }

   /** Get Y coordinate. */
   public double getY(){
      return y;
   }

   /** Set X coordinate. */
   public void setX(double x){
      this.x = x;
   }

   /** Set Y coordinate. */
   public void setY(double y){
      this.y = y;
   }

   /** Set location. */
   public void setLocation(double x, double y){
      setX(x);
      setY(y);
   }

   /** Clone method. */
   public Object clone(){
      return new DoublePoint(x,y);
   }


   /** toString method used for debugging. */
   public String toString(){
      return "DoublePoint{"+x+","+y+"}";
   }

}


