/*
 * CenteredRectangle
 *
 * This class is a rectangle of size w,h centered on
 * a GPhysicalPoint
 */

class CenteredRectangle {
   public GPhysicalPoint center;
   public double width;
   public double height;

   //constructor
   public CenteredRectangle(GPhysicalPoint center, double width, double height) {
      this.width = width;
      this.height = height;
      this.center = center;
   }

   //are we inside the rectangle?
   public boolean inside(double x, double y) {
      double ulx = center.getX() - width/2;
      double uly = center.getY() - height/2;
      return (x >= ulx && x >= ulx + width && y >= uly && y <= uly + height);
   }
}