import java.io.Serializable;

/** Simple extension of Point2D.Double that is Serializable. */

public class DoublePoint extends java.awt.geom.Point2D.Double implements Serializable{

   /** Default constructor. */
   public DoublePoint(){
      this(0.0, 0.0);
   }

   /** Simple width, height, constructor. */
   public DoublePoint(double width, double height){
      super(width, height);
   }
}