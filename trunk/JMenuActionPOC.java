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


class JMenuActionPOC extends JMenuAction{
   public JMenuActionPOC(GUI registeredObject){super("Progress Meter Test",registeredObject);}

   public void run(){
      GUI gui = (GUI)registeredObject;
      GPane pane = gui.getTopPane();
      if(pane == null) return;

      //try{
         String newTitle = "";
         String message = "";
         newTitle = (String)JOptionPane.showInputDialog(gui.frame, "Enter a task size. "+message,"Set Task Size",JOptionPane.PLAIN_MESSAGE,null,null,newTitle);
         //newTitle = "1000";
         if(newTitle != null){
            int taskSize = Integer.parseInt(newTitle);
            ProgressMeter meter = gui.getProgressMeter();
            meter.grab(this);
            for(int i=0;i<taskSize;i++){
               meter.setPercent(ProgressMeter.computePercent(i,taskSize), this);
               meter.setMessage(""+meter.getRoundedPercent(3), this);
               System.out.println("Completed "+meter.getRoundedPercent(3));
               Object o = null;
               o.equals("");
            }
            meter.release(this);
         }
      //}
      //catch(Exception e){System.out.println(e);}
   }
}
