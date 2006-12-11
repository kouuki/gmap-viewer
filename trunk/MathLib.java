import java.awt.geom.*;
/**this is the class that does the calc for determing the image size and placement variables
 */
class MathLib{
   /**this is the main class that creates the 2Dlines intersection point
    */
   public static void main(String[] args){
      Line2D l1 = new Line2D.Double(5.0,6.0,7.0,9.0);
      Line2D l2 = new Line2D.Double(6.0,10.0,6.0,5.0);

         Point2D ret = MathLib.intersectPoint(l1,l2);
         System.out.println(ret.getX() + " " + ret.getY());
         ret = MathLib.intersectPoint(l2,l1);
         System.out.println(ret.getX() + " " + ret.getY());
   }
/**This is the construcotr for the MathLib class
 */
   public MathLib(){
      super();
   }


   /**this is the declaration that determines the slop of Point @D
    * @param p1 this is the Point2D
    * @param p2 this is the Point2d
    * @return the slope value
    */
   public static double slope(Point2D p1, Point2D p2) throws ArithmeticException{
      double x1 = p1.getX();
      double y1 = p1.getY();
      double x2 = p2.getX();
      double y2 = p2.getY();

      return (y2 - y1)/(x2 - x1);
   }
   /**this is the y intercept of the 2D points
    *@param p1 this is the Point2D
    * @param p2 this is the Point2d
    * @return the Y intercept
    */
   public static double yIntercept(Point2D p1, Point2D p2) throws ArithmeticException
   {

      double a = slope(p1, p2);

      return p1.getY() - (a*p1.getX());

   }
   /**this is the Point2D interscept
    * @param 11 this is the Point2D
    * @param 12 this is the Point2d
    * @return the new value of the interceptpoint
    */
   public static Point2D intersectPoint(Line2D l1, Line2D l2)
   {
      double a1 = 0.0;
      double a2 = 0.0;
      double b1 = 0.0;
      double b2 = 0.0;

      double x = 0.0;
      double y = 0.0;

      boolean slopeExists1 = true;
      boolean slopeExists2 = true;;

      try{
         a1 = slope(l1.getP1(), l1.getP2());
         if(a1 == Double.POSITIVE_INFINITY || a1 == Double.NEGATIVE_INFINITY){
            x = l1.getP1().getX();
            slopeExists1 = false;
         }else{
            b1 = yIntercept(l1.getP1(), l1.getP2());
         }
      }catch(ArithmeticException ex){
         x = l1.getP1().getX();
         slopeExists1 = false;
      }

      try{
         a2 = slope(l2.getP1(), l2.getP2());
         if(a2 == Double.POSITIVE_INFINITY || a2 == Double.NEGATIVE_INFINITY){
            x = l2.getP1().getX();
            slopeExists2 = false;
         }else{
            b2 = yIntercept(l2.getP1(), l2.getP2());
         }
      }catch(ArithmeticException ex){
         x = l2.getP1().getX();
         slopeExists2 = false;
      }

      if(slopeExists1){
         if(slopeExists2){
            try{
               x = (b1 - b2)/(a2 - a1);
            }catch(ArithmeticException ex){
               System.out.println("Both lines have zero slopes, they can't intersect");
               return null;
            }
         }
         y = a1*x + b1;
      }else{
         if(!slopeExists2){
            System.out.println("Both lines have not yIntercepts, they can't intersect");
            return null;
         }
         y = a2*x + b2;
      }


      return new DoublePoint(x, y);
   }
   /**this is the X intersectpoint
    * @param 11 this is the Point2D
    * @param 12 this is the Point2d
    * @return the intersection point for X
    */
   public static double intersectPointX(Line2D l1, Line2D l2)
   {
      return intersectPoint(l1, l2).getX();
   }
   /**this is the Y intersectpoint
    * @param 11 this is the Point2D
    * @param 12 this is the Point2d
    * @return the intersection point for Y
    */
   public static double intersectPointY(Line2D l1, Line2D l2)
   {
      return intersectPoint(l1, l2).getY();
   }
}