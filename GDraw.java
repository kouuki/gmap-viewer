
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

class GDraw{


   private GDrawableObject[] objects;
   private int objectsSize;
   private int selected;

   public GDraw(){
      objects = new GDrawableObject[10];
      objectsSize = 0;
      selected = -1;
   }

   public void add(GDrawableObject object){
      //enlarge if needed
      if(objectsSize == objects.length) enlargeGDrawableObjectArray();
      objects[objectsSize++] = object;
      //System.out.println(this.toString());
   }

   public void remove(int index){
      if(index == -1) return;
      if(index == selected) selected = -1;
      if(index == -1 || index >= objectsSize) return;
      objects[index] = objects[(objectsSize--)-1];
      //System.out.println(this.toString());
   }

   public void remove(GDrawableObject object){
      remove(getIndex(object));
   }

   public void removeAll(){
      objects = new GDrawableObject[10];
      objectsSize = 0;
   }

   public GDrawableObject get(int index){
      return objects[index];
   }

   public int getIndex(GDrawableObject object){
      for(int i=0;i<objectsSize;i++) if(object == objects[i]) return i;
      return -1;
   }

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

   public int inside(Point testPoint, GPhysicalPoint p, int zoom){
      for(int i=0;i<objectsSize;i++){
         Rectangle rect = objects[i].getRectangle(p,zoom);
         if(rect == null) continue;
         if(rect.contains(testPoint.x, testPoint.y)) return i;
      }
      return -1;
   }

   public void setSelected(int selected){
      this.selected = selected;
   }

   public void setSelected(GDrawableObject object){
      setSelected(getIndex(object));
   }

   public int getSelected(){
      return selected;
   }

   private void enlargeGDrawableObjectArray(){
      GDrawableObject[] temp = new GDrawableObject[(int)(objects.length * 1.5)];
      for(int i=0;i<objects.length;i++)
         temp[i] = objects[i];
      objects = temp;
   }

   public String toString(){
      String s = "";
      for(int i=0;i<objectsSize;i++){
         if(objects[i] != null) s += "["+i+" is not null]\n";
         else s += "["+i+" is null]\n";
      }
      return s;
   }

}