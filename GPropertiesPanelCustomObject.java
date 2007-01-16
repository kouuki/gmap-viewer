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
* A nested JPanel for controlling standard object properties - those
* defined in GCustomObject.
*/

public class GPropertiesPanelCustomObject extends GPropertiesPanel{
   //The registered object
   private GCustomObject object;

   //The components used to work on it
   private JTextField stroke;
   private JTextField opacity;
   private JButton color;
   private JButton background;

   /**
   * Constructor simply passes work on to the JPanel
   */
   public GPropertiesPanelCustomObject(GCustomObject object){
      //save info and register the object
      super();
      this.object = object;

      //from here on, we're setting up the JPanel
      this.setLayout(null);

      JLabel strokeLabel = new JLabel("Stroke");
      strokeLabel.setBounds(20,20,60,30);
      this.add(strokeLabel);

      stroke = new JTextField();
      stroke.setBounds(80,20,60,25);
      this.add(stroke);

      JLabel opacityLabel = new JLabel("Opacity");
      opacityLabel.setBounds(20,60,60,30);
      this.add(opacityLabel);

      opacity = new JTextField();
      opacity.setBounds(80,60,60,25);
      this.add(opacity);

      JLabel colorLabel = new JLabel("Color");
      colorLabel.setBounds(80,120,100,30);
      this.add(colorLabel);

      color = new JButton();
      color.setBounds(20,120,40,25);
      color.setBackground(Color.BLACK);
      this.add(color);

      JLabel backgroundLabel = new JLabel("Background Color");
      backgroundLabel.setBounds(80,160,130,30);
      this.add(backgroundLabel);

      background = new JButton();
      background.setBounds(20,160,40,25);
      background.setBackground(Color.BLACK);
      this.add(background);

   }


   /**
    * The apply method is called by GPropertiesDialog when Apply is clicked.
    * It does the work of actually making the necessary changes to the object.
    */

   public void apply(){
      System.out.println("apply was clicked");

   }

}

