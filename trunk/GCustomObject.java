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
  * A class providing functionality for modifying
  * the visual representation of GCustomObject.
  *
  * @author Bill
  *
  */

public abstract class GCustomObject implements GDrawableObject{

   //private
   transient private AlphaComposite alpha;
   private float opacity;
   private int stroke;
   private Color color;
   private Color background;


   /**
   * Constructor, creates a object with attributes as specified.
   */
   public GCustomObject(float opacity, int stroke, Color color, Color background){
      this.opacity = opacity;
      this.stroke = stroke;
      this.alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
      this.color = color;
      this.background = background;
   }

   /**
   * Constructor, sets attributes for GCustomObject.
   */
   public GCustomObject(){
      this(1.0f, 3, new Color(0,0,155), new Color(175,175,175));
   }


   /**
   * Returns the Color associated with this object.
   */
   public Color getColor(){
      return color;
   }

   /**
   * Returns the background Color associated with this object.
   */
   public Color getBackground(){
      return background;
   }


   /**
   * Returns the opacity value associated with this object.
   */
   public float getOpacity(){
      return opacity;
   }


   /**
   * Returns the size of this object's stroke.
   */
   public int getStroke(){
      return stroke;
   }

   /**
   * Sets the Color value for this object.
   */
   public void setColor(Color color){
      this.color = color;
   }

   /**
   * Sets the background Color value for this object.
   */
   public void setBackground(Color background){
      this.background = background;
   }

   /**
   * Sets the opacity value for this object.
   */
   public void setFloat(Float opacity){
      this.opacity = opacity;
   }

   /**
   * Sets the size of this object's stroke.
   */
   public void setStroke(int stroke){
      this.stroke = stroke;
   }


   /**
   * This method only calls update unless it is overidden. Subclassers should be sure to call
   * this method when overriding draw(), otherwise the alpha may not match the opacity value.
   */
   public void draw(BufferedImage image, GPhysicalPoint p, int zoom){
      update();
   }


   /**
   * Ensures that the AlphaComposite matches the opacity value.
   */
   private void update(){
      //determine if we need an update
      boolean updateAlpha = false;
      if(alpha == null) updateAlpha = true;
      else if(alpha.getAlpha() != opacity) updateAlpha = true;
      //do it
      if(updateAlpha) alpha = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, opacity);
   }

}