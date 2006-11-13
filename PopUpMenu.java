/*
 * PopUpMenu.java
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

class GPopupMenu extends JPopupMenu{
   private JPopupMenu popup;
   private final GUI gui; 
   private int objectIndex;

   public GPopupMenu(GUI gui) {
      //save the gui
      this.gui = gui;

      JMenuItem menuItem;
      popup = new JPopupMenu();

      // Create some menu items for this
      //menuItem = new JMenuItem( "Remove current pane" );
      menuItem = new JMenuActionRemovePane(gui);
      this.add(menuItem);

      // New menu item for adding a new GDrawableObject 
      menuItem = new JMenuActionAddGDrawableObject(gui);
      this.add(menuItem);
 
      // If GDrawableObject returned, add a new menu item for removing the object
      objectIndex = GDraw.isClickOnObject();
      if( objectIndex >= 0 ) { 	
         menuItem = new JMenuActionRemoveGDrawableObject(gui);
	 this.add(menuItem);
      }

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




