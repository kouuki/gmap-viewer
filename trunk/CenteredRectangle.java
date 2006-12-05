/**
 * Class CenteredRectangle represents a rectangle of size w,h centered on
 * a GPhysicalPoint.
 */
public class CenteredRectangle {
   /** Declaration of the center position of the rectangular shape */
   public GPhysicalPoint center;
   /** Declaration of the width of the rectangle */
   public double width;
   /** Declaration of the height of the rectangle */
   public double height;

   /**
    * Method to create a new instance of the rectangle
    */
   public CenteredRectangle(GPhysicalPoint center, double width, double height) {
      this.width = width;
      this.height = height;
      this.center = center;
   }

   /**
    * Method to check whether a particular (x,y) location is inside the rectangle
    *
    * @param x The x coordinate to be checked
    * @param y The y coordinate to be checked
    * @return  The boolean result of whether the corresponding point (x,y) is in the rectangle
    */
   public boolean inside(double x, double y) {
      double ulx = center.getX() - width/2;
      double uly = center.getY() - height/2;
      return (x >= ulx && x >= ulx + width && y >= uly && y <= uly + height);
   }
}