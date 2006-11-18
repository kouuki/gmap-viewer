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

   private JMenuAction removeObject;

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
      removeObject = new JMenuActionRemoveGDrawableObject(gui);
      this.add(removeObject);


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

      rb = new JMenuRadioButtonCalculateDistance(gui);
      group.add(rb);
      this.add(rb);
      
      rb = new JMenuRadioButtonAddPoints(gui);
      group.add(rb);
      this.add(rb);
      
      rb = new JMenuRadioButtonAddLines(gui);
      group.add(rb);
      this.add(rb);
      
      rb = new JMenuRadioButtonAddText(gui);
      group.add(rb);
      this.add(rb);
      
      this.setSelected(rb2);
   }

   public void show(Component invoker, int x, int y){
      update();
      super.show(invoker,x,y);
   }

   public void update(){
      if(gui.getGMap().getGDraw().getSelected() == -1){
         removeObject.setEnabled(false);
      }else{
         removeObject.setEnabled(true);
      }
   }

}




