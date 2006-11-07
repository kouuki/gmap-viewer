/*
 * PopUpMenu.java
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GPopupMenu extends JPopupMenu{
   private JPopupMenu popup;
   private final GUI gui;

   public GPopupMenu(GUI gui) {
      //save the gui
      this.gui = gui;

      JMenuItem menuItem;
      popup = new JPopupMenu();

      // Create some menu items for the this
      //menuItem = new JMenuItem( "Remove current pane" );
      menuItem = new JMenuActionRemovePane(gui);
      this.add(menuItem);

      this.add( new JSeparator() );
      ButtonGroup group = new ButtonGroup();

      JRadioButtonMenuItem rb;
      rb = new JMenuRadioButtonDragOn(gui);
      group.add(rb);
      this.add(rb);


      JRadioButtonMenuItem rb2;
      rb2 = new JMenuRadioButtonSelectionOn(gui);
      group.add(rb2);
      this.add(rb2);

   }
}
