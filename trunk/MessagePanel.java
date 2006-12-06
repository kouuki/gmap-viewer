import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.image.*;

/**
 * Exception panel is a panel for representing messages to the user, including Exception handling.
 *@author bill c
 */

public class MessagePanel extends JPanel implements MouseListener, MouseMotionListener{

   //GMap object
   private GUI gui;

   //exception
   private JLabel label;
   private String message;

   /**
    * Constructor.
    * Creates an empty panel.
    */
   public MessagePanel(){
      label = new JLabel("");
      message = null;
      add(label);
      addMouseListener(this);
   }

   /**
    * Set a new exception.
    */
   public void setException(Exception e){
      //build message
      String dialog = e+"\n\n";
      StackTraceElement[] trace = e.getStackTrace();
      for(int i=0;i<trace.length;i++)
         dialog += "   " + trace[i] + "\n";
      //dialog
      setMessage("Internal error caught.", dialog);
   }

   /**
    * Set a new message.
    */
   public void setMessage(String title, String message){
      label.setText(title);
      this.message = message;
      label.repaint();
   }

   /**
    * Clear the exception.
    */
   public void clearMessage(){
      message = null;
      label.setText("");
      label.repaint();
   }

   /**
    * Show the exception in a JDialog.
    */
   public void showMessage(){
      if(message == null) return;
      JOptionPane.showMessageDialog(new JFrame(), message);
   }

   //mouse methods - use e.getX()
   public void mouseMoved(MouseEvent e){}
   public void mouseDragged(MouseEvent e){}

   /**
    * @see java.awt.event.MouseListener#mouseClicked(MouseEvent)
    */
   public void mouseClicked(MouseEvent e){
      int m = e.getModifiers();
      if(m == 4){
         showMessage();
      }else{
         clearMessage();
      }
   }

   public void mouseEntered(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mousePressed(MouseEvent e){}
   public void mouseReleased(MouseEvent e){}

}
