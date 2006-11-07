/*
 * EmbeddedProgressMeter.java
 *
 */

import javax.swing.*;
import java.awt.*;
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

public class EmbeddedProgressMeter extends ProgressMeter implements MouseListener{

   private JPanel panel;
   private JProgressBar progressBar;
   private JButton cancel;
   private JLabel messageLabel;
   private Thread thread;

   private static final String notWorkingMessage = "Done.";

    /** Creates a new instance of EmbeddedProgressMeter */
   public EmbeddedProgressMeter() {
      super(null,0.0,notWorkingMessage);

      //initialize components
      progressBar = new JProgressBar();
      progressBar.setMinimum(0);
      progressBar.setMaximum(100);
      progressBar.setStringPainted(false);
      progressBar.setValue(0);

      cancel = new JButton("Cancel");
      cancel.addMouseListener(this);
      cancel.setPreferredSize(new Dimension(90,18));

      messageLabel = new JLabel();
      messageLabel.setPreferredSize(new Dimension(200,22));

      panel = new JPanel();
      panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 1));
      panel.add(progressBar);
      panel.add(cancel);
      panel.add(messageLabel);

      update();
   }

   public boolean setMessage( String message, Object o ) {
      if( super.setMessage( message, o ) == true ) {
         update();
         return true;
      }
      else {
      return false;
      }
   }

   public boolean setPercent( double percent, Object o ) {
      if ( super.setPercent( percent, o ) == true ) {
         update();
         return true;
      }
      else {
         return false;
      }
   }

   private void update() {
      //further methods called here - not sure how the controller was to handle this
      progressBar.setValue((int)getRoundedPercent(1));
      messageLabel.setText(getMessage());
      messageLabel.paintImmediately( 0, 0, messageLabel.getWidth(), messageLabel.getHeight() );
      progressBar.paintImmediately( 0, 0, progressBar.getWidth(), progressBar.getHeight() );
   }

   public JPanel getPanel() {
      return panel;
   }

   public boolean grab( Object o ) {
      if( super.grab( o ) == true ) {
         progressBar.setStringPainted(true);
         progressBar.setVisible(true);
         cancel.setVisible(true);
         return true;
      }
      else {
         return false;
      }
   }

   public boolean release( Object o ) {
      if( super.release( o ) == true ) {
         progressBar.setStringPainted(false);
         setPercent(0.0, null);
         setMessage(notWorkingMessage,null);
         progressBar.setVisible(false);
         cancel.setVisible(false);
         this.thread = null;
         return true;
      }
      else {
         return false;
      }
   }

   public boolean registerThread(Thread thread, Object o){
      if(super.isObject(o)) {
         this.thread = thread;
         return true;
      }
      else {
         return false;
      }
   }

   public void stop(){
      super.stop();
      if(thread != null) thread.interrupt();
   }

   //mouse methods - use e.getX()
   public void mouseMoved(MouseEvent e) {}
   public void mouseDragged(MouseEvent e){}
   public void mouseClicked(MouseEvent e){
      if(e.getSource() == cancel){
         System.out.println("Stop!");
         stop();
      }
   }
   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){}
   public void mouseReleased(MouseEvent e){}


}
