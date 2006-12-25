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

/** Class defining the instance of the drawing mechanism for the map view */
public class GDraw implements GDrawableObject{
   /** Declaration for the instance of an array of GDrawableObjects */
   private GDrawableObject[] objects;
   /** Declaration for the size of the object array */
   private int objectsSize;
   /** An ObjectContainer that holds the selected objects. */
   private ObjectContainer selected;
   /** Constructor for the GDraw instance */
   public GDraw(){
      objects = new GDrawableObject[10];
      objectsSize = 0;
      selected = new ObjectContainer();
   }
   /**
    * Method for adding a new GDrawableObject
    * @param object  The new GDrawableObject
    */
   public void add(GDrawableObject object){
      //enlarge if needed
      if(objectsSize == objects.length) enlargeGDrawableObjectArray();
      objects[objectsSize++] = object;
      //System.out.println(this.toString());
   }
   /**
    * Method for removing a GDrawableObject by index reference
    * @param index   The index of the object to remove
    */
   public void remove(int index){
      if(index == -1 || index >= objectsSize) return;
      selected.remove(objects[index]);
      int selectedIndex = selected.getIndex(objects[index]);
      objects[index] = objects[(objectsSize--)-1];
      //System.out.println(this.toString());
   }
   /**
    * Method for removing a GDrawableObject by object reference
    * @param object   The object to remove
    */
   public void remove(GDrawableObject object){
      remove(getIndex(object));
   }
   /** Method for removing all current GDrawableObjects */
   public void removeAll(){
      selected.removeAll();
      objects = new GDrawableObject[10];
      objectsSize = 0;
   }
   /**
    * Method for getting a GDrawableObject by index reference
    * @param index   The index of the object
    * @return        The object with the given index
    */
   public GDrawableObject get(int index){
      return objects[index];
   }
   /**
    * Method for getting the index of a given GDrawableObject
    * @param object  The object to get the index of
    * @return        The index of the given object
    */
   public int getIndex(GDrawableObject object){
      for(int i=0;i<objectsSize;i++) if(object == objects[i]) return i;
      return -1;
   }

   /**
    * Method for getting size of this GDrawindex of
    * @return        The size.
    */

    public int getSize(){
      return objectsSize;
    }


   /**
    * Method for drawing the GDrawableObjects to the current map view
    * @param image   The buffered image to render to the screen
    * @param p       The upper left corner of the viewable window
    * @param zoom    The current zoom level of the map view
    */
   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      //draw the objects first
      for(int i=0;i<objectsSize;i++)
         //if(objects[i] != null) objects[i].draw(image, p, zoom);
         //else System.out.println("......null pointer @ "+i+"......");
         objects[i].draw(image, p, zoom);

      //now draw the selection rectangle tickmarks unless nothing is selected
      if(selected.getSize() == 0) return;

      for(int i=0;i<selected.getSize();i++){
         //selection
         GDrawableObject thisObj = (GDrawableObject)selected.get(i);
         Rectangle selectionRectangle = thisObj.getRectangle(p,zoom);

         if(selectionRectangle == null) return ;

         Graphics2D g = image.createGraphics();

         g.setColor(Color.RED);
         //upper left
         g.drawLine(selectionRectangle.x, selectionRectangle.y, selectionRectangle.x + 5, selectionRectangle.y);
         g.drawLine(selectionRectangle.x, selectionRectangle.y, selectionRectangle.x, selectionRectangle.y + 5);
         //upper right
         g.drawLine(selectionRectangle.x+selectionRectangle.width, selectionRectangle.y, selectionRectangle.x+selectionRectangle.width - 5, selectionRectangle.y);
         g.drawLine(selectionRectangle.x+selectionRectangle.width, selectionRectangle.y, selectionRectangle.x+selectionRectangle.width, selectionRectangle.y + 5);
         //lower right
         g.drawLine(selectionRectangle.x+selectionRectangle.width, selectionRectangle.y+selectionRectangle.height, selectionRectangle.x+selectionRectangle.width - 5, selectionRectangle.y+selectionRectangle.height);
         g.drawLine(selectionRectangle.x+selectionRectangle.width, selectionRectangle.y+selectionRectangle.height, selectionRectangle.x+selectionRectangle.width, selectionRectangle.y+selectionRectangle.height - 5);
         //lower left
         g.drawLine(selectionRectangle.x, selectionRectangle.y+selectionRectangle.height, selectionRectangle.x + 5, selectionRectangle.y+selectionRectangle.height);
         g.drawLine(selectionRectangle.x, selectionRectangle.y+selectionRectangle.height, selectionRectangle.x, selectionRectangle.y+selectionRectangle.height - 5);
      }
   }

   /**
    * Method returns a rectangle that encompasses the entire product.
    * @param p       The upper left corner of the viewable window
    * @param zoom    The current zoom level of the map view
    * @return        The rectangle. Origin is the upper left corner of the screen.
    */
   public Rectangle getRectangle(GPhysicalPoint p, int zoom){
      //return a null rectangle if empty
      if(objectsSize == 0) return null;

      //this is a counter variable that will be shared by two for loops
      int testObject = 0;

      //initialize return
      Rectangle toReturn = null;
      Rectangle thisRectangle;

      //check each object to see if its getRectangle is not null
      for(testObject = 0; testObject < objectsSize; testObject++){
         thisRectangle = objects[testObject].getRectangle(p,zoom);
         if(thisRectangle != null){
            toReturn = thisRectangle;
            break;
         }
      }

      //if we didnt find a non-null child, return null
      if(toReturn == null) return null;

      //union the first rectangle with each subrectangle
      for(testObject = testObject; testObject < objectsSize; testObject++){
         thisRectangle = objects[testObject].getRectangle(p,zoom);
         if(thisRectangle != null){
            toReturn = toReturn.union(thisRectangle);
         }
      }

      return toReturn;
   }

   /**
    * Method moves every item by latitude, longitude. This is a recursive call to each object's move method.
    * @param lat The amount to add to this items latitude
    * @param lat The amount to add to this items longitude
    */

   public void move(double latitude, double longitude){
      for(int i=0;i<objectsSize;i++){
         objects[i].move(latitude, longitude);
      }
   }

   /**
    * Method to test whether an object resides on a given point
    * @param testPoint  The point to test
    * @param p          The point of where the object is located
    * @param zoom       The current zoom level of the current view
    * @return           The index of the object if one exists on the point
    */
   public int inside(Point testPoint, GPhysicalPoint p, int zoom){
      for(int i=0;i<objectsSize;i++){
         Rectangle rect = objects[i].getRectangle(p,zoom);
         if(rect == null) continue;
         if(rect.contains(testPoint.x, testPoint.y)) return i;
      }
      return -1;
   }
   /**
    * Method to set the container of selected objects.
    * @param selected   The index
    */
   public void setSelected(ObjectContainer selected){
      this.selected = selected;
   }

   /**
    * Method to get the container of selected objects.
    * @return  The index for the selected object
    */
   public ObjectContainer getSelected(){
      return selected;
   }
   /** Method to enlarge the array for GDrawableObjects */
   private void enlargeGDrawableObjectArray(){
      GDrawableObject[] temp = new GDrawableObject[(int)(objects.length * 1.5)];
      for(int i=0;i<objects.length;i++)
         temp[i] = objects[i];
      objects = temp;
   }
   /**
    * Method for converting object references into a string representation
    * @return  The string representation for the GDrawableObject array
    */
   public String toString(){
      String s = "";
      for(int i=0;i<objectsSize;i++){
         if(objects[i] != null) s += "["+i+" is not null]\n";
         else s += "["+i+" is null]\n";
      }
      return s;
   }

}