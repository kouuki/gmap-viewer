/*
 * EmbeddedProgressMeter.java
 *
 */

import javax.swing.*;
import java.awt.*;

public class EmbeddedProgressMeter extends ProgressMeter {

   private JPanel panel;
   private JProgressBar progressBar;
   private JLabel messageLabel;

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

      messageLabel = new JLabel();

      panel = new JPanel();
      panel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 2));
      panel.add(progressBar);
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
         return true;
      }
      else {
         return false;
      }
   }

}
