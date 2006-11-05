/*
 * PopUpMenu.java
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PopUpMenu {
   JPopupMenu popup;

   // Test main method for popup test
   public static void main(String[] args) {
      PopUpMenu p = new PopUpMenu();
   }
    
   public PopUpMenu() {
      
      JRadioButton rb;
      JMenuItem menuItem;
      popup = new JPopupMenu();
      
      // Test frame creation and frame MouseListener
      JFrame frame = new JFrame("Popup Test");
      frame.setSize(400,400);
      frame.setVisible(true);
      frame.addMouseListener(new MouseAdapter(){
      public void mouseReleased(MouseEvent event){
         if(event.isPopupTrigger()){ 
            popup.show(event.getComponent(), event.getX(), event.getY());
         }
      }
      });
      //
      
      // Create some menu items for the popup
      menuItem = new JMenuItem( "Remove current pane" );
      popup.add(menuItem);
      // Action listener for the menuItem
      menuItem.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e){
            //System.out.println("Pane Removed");
            //code for removing the current pane
         }
      } );
      
      popup.add( new JSeparator() );
      ButtonGroup group = new ButtonGroup();
      
      rb = new JRadioButton( "Map Dragging" );
      group.add(rb);
      popup.add(rb);
       // Action listeners for the radioButton
      rb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //System.out.println("Map Dragging Enabled");
            //code for enabling boolean map dragging
            //mapDragging = true;
         }
      } ); 
    
      rb = new JRadioButton( "Selection Rectangle" );
      group.add(rb);
      popup.add(rb);
      //Action listener for the radiobutton
      rb.addActionListener(new ActionListener() {
         public void actionPerformed(ActionEvent e) {
            //System.out.println("Selection Rectangle enabled");
            //code for enabling boolean selectionEnabled
         }
      } ); 
      
   }
}     
