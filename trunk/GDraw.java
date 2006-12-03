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
class GDraw{
   /** Declaration for the instance of an array of GDrawableObjects */
   private GDrawableObject[] objects;
   /** Declaration for the size of the object array */
   private int objectsSize;
   /** Declaration for the index of the current object selected */
   private int selected;
   /** Constructor for the GDraw instance */
   public GDraw(){
      objects = new GDrawableObject[10];
      objectsSize = 0;
      selected = -1;
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
      if(index == -1) return;
      if(index == selected) selected = -1;
      if(index == -1 || index >= objectsSize) return;
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
    * Method for drawing the GDrawableObjects to the current map view
    * @param image   The buffered image to render to the screen
    * @param p       The defined point for where the object is located
    * @param zoom    The current zoom level of the map view  
    */
   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      //draw the objects first
      for(int i=0;i<objectsSize;i++)
         //if(objects[i] != null) objects[i].draw(image, p, zoom);
         //else System.out.println("......null pointer @ "+i+"......");
         objects[i].draw(image, p, zoom);

      //now draw the selection rectangle tickmarks unless nothing is selected
      if(selected < 0 || selected >= objectsSize) return;

      Rectangle selectionRectangle = objects[selected].getRectangle(p,zoom);

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
    * Method to set the current index
    * @param selected   The index
    */
   public void setSelected(int selected){
      this.selected = selected;
   }
   /** 
    * Method to set the index for a selected object
    * @param object     The object to set the index of
    */
   public void setSelected(GDrawableObject object){
      setSelected(getIndex(object));
   }
   /** 
    * Method to get the index
    * @return  The index for the selected object
    */
   public int getSelected(){
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