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

public abstract class GPropertiesPanel extends JPanel{
   /**
   * Constructor simply passes work on to the JPanel
   */
   public GPropertiesPanel(){
      super();
   }

   /**
    * The apply method is called by GPropertiesDialog when Apply is clicked.
    * It does the work of actually making the necessary changes to the object.
    */

   public abstract void apply();

}

