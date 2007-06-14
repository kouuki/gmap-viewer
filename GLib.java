import java.awt.*;
import java.awt.image.*;


/**
 * Provides a static method to compute distance.
 */
public class GLib{
   private static final int R =  3963;   //6367; //R = 6367 km = 3956 mi

   /**
    * Computes the distance from point a to point b.
    *
    * @param a Point where the line starts
    * @param b Point where the line ends
    * @return The distance
    */
   public static double computeDistance(GPhysicalPoint a, GPhysicalPoint b){
      double lat1 = degToRad(a.getX());
      double lat2 = degToRad(b.getX());
      double dlon = degToRad(b.getY()) - degToRad(a.getY()); //lon2 - lon1 in Rad
      double dlat = (lat2) - (lat1); //lat2 - lat1 in Rad
      double f = (.5 - (.5*Math.cos(dlat))) + (Math.cos(lat1) * Math.cos(lat2) * (.5 - (.5*Math.cos(dlon))));     //sin^2(x) = 1/2 - (1/2)cos(2x); x = long/2; 2x = long;
      double c = 2 * Math.atan2(Math.sqrt(f),(Math.sqrt(1-f)));
      double d = R * c;
      return d;
   }


   /**
    * This method takes a point on an image, computes the largest square containing
    * only pixels of the same color +/- tolerance, and returns the center of the rectangle.
    *
    * @param image The image on which to get pixel data.
    * @param p The point to map
    * @return The distance
    */

   public static Point smartPoint(BufferedImage image, Point p, double tolerance, int limit){
      int[] pixels = new int[image.getWidth() * image.getHeight()];
      Point toReturn = p;
      try{
         //get pixels
         PixelGrabber pixelGrabber = new PixelGrabber(image, 0, 0, image.getWidth(), image.getHeight(), pixels, 0, image.getWidth());
         pixelGrabber.grabPixels();

         //store color
         Color originalColor = new Color(pixels[p.y * image.getWidth() + p.x]);
         Color compareColor;

         //rectangle
         Rectangle borderingRectangle = new Rectangle(p.x, p.y, 1, 1);

         //stop flags
         boolean flagStopS = false;
         boolean flagStopN = false;
         boolean flagStopE = false;
         boolean flagStopW = false;

         //create a rectangle
         for(int i=0; i<limit ; i++){
            //increment as necessary
            if(!flagStopN) borderingRectangle.setBounds(borderingRectangle.x, borderingRectangle.y-1, borderingRectangle.width, borderingRectangle.height+1);
            if(!flagStopW) borderingRectangle.setBounds(borderingRectangle.x-1, borderingRectangle.y, borderingRectangle.width+1, borderingRectangle.height);
            if(!flagStopS) borderingRectangle.setBounds(borderingRectangle.x, borderingRectangle.y, borderingRectangle.width, borderingRectangle.height+1);
            if(!flagStopE) borderingRectangle.setBounds(borderingRectangle.x, borderingRectangle.y, borderingRectangle.width+1, borderingRectangle.height);

            //break if done
            if(flagStopN && flagStopS && flagStopE && flagStopW) break;

         }


               compareColor = new Color(pixels[(borderingRectangle.y-1) * image.getWidth() + borderingRectangle.x]);
               if(compareColors(originalColor, compareColor) <= tolerance)
                  borderingRectangle.setBounds(borderingRectangle.x, borderingRectangle.y-1, borderingRectangle.width, borderingRectangle.height+1);
               else
                  flagStopN = true;


         //update point
         toReturn = new Point(borderingRectangle.x + borderingRectangle.width/2, borderingRectangle.y + borderingRectangle.height/2);
      }catch(InterruptedException e){}

      //return
      System.out.println("smart point: ("+p.x+","+p.y+") ==> ("+toReturn.x+","+toReturn.y+")");

      //return it
      return toReturn;
   }

   /**
    * This method compares two colors and returns a value between 0 and 1. Zero corresponds to
    * identical colors, and one corresponds to a comparison of color opposites. <br><br> The
    * algorithm treats colors as 3-dimsional points inside a 255 by 255 cube. It calculates
    * the "distance between" two colors, and divides it by the distance between any two corners
    * of the cube.
    *
    * @param a Color a.
    * @param b Color b.
    */
   public static double compareColors(Color a, Color b){
      System.out.println("Compare( {"+a.getRed()+","+a.getGreen()+","+a.getBlue()+"} , {"+b.getRed()+","+b.getGreen()+","+b.getBlue()+"} ) = " + Math.sqrt(Math.pow(a.getRed() - b.getRed(), 2) + Math.pow(a.getGreen() - b.getGreen(), 2) + Math.pow(a.getBlue() - b.getBlue(), 2)) / Math.sqrt(3*Math.pow(255,2)));
      return Math.sqrt(Math.pow(a.getRed() - b.getRed(), 2) + Math.pow(a.getGreen() - b.getGreen(), 2) + Math.pow(a.getBlue() - b.getBlue(), 2)) / Math.sqrt(3*Math.pow(255,2));
   }

   public static void main(String[] args){
      System.out.println(compareColors(LibString.makeColor(args[0]), LibString.makeColor(args[1])));
   }



   private static double degToRad(double deg){
      return (deg*(Math.PI / 180));
   }

}
