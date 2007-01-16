import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Class GPopupMenu is a group of radio buttons/menu actions displayed as a popup menu when a
 * right click is performed on the GUI. It extends JPopupMenu.
 * @author Dan
 */
public class GPopupMenu extends JPopupMenu{
   /**
    * Declaration of the instance of the popup
    */
   private JPopupMenu popup;
   /**
    * Declaration of the instance of the GUI
    */
   private final GUI gui;
   /**
    * Declaration of the objectIndex for referencing objects
    */
   private int objectIndex;
   /**
    * Declaration of the menu action for removing an object
    */
   private JMenuAction removeObject;

   /**
    * Method to initiate the popup and add the necessary menu items and radio
    * buttons to it including:
    * <ul>
    * <li> Remove Pane menu action
    * <li> Add Object menu action
    * <li> Remove Object menu action
    * <li> Map Dragging radio button
    * <li> Selection Rectangle radio button </ul>
    *
    * @param gui The GUI instance which will listen for the popup creation.
    */
   public GPopupMenu(GUI gui) {
      // Save the instance of the gui
      this.gui = gui;
      // Declaration of the general menuItem
      JMenuItem menuItem;
      // Declaration of the general JMenuRadioButton
      JRadioButtonMenuItem rb, rb2;
      // Instantiation of the PopupMenu
      popup = new JPopupMenu();

      // Create some menu items for the popupmenu
      menuItem = new JMenuActionSetPaneTitle(gui);
      this.add(menuItem);

      menuItem = new JMenuActionRemovePane(gui);
      this.add(menuItem);

      removeObject = new JMenuActionRemoveGDrawableObject(gui);
      this.add(removeObject);

      // New separator between actions and the radio buttons
      this.add( new JSeparator() );

      //copy
      menuItem = new JMenuActionCopy(gui);
      this.add(menuItem);

      //paste
      menuItem = new JMenuActionPaste(gui);
      this.add(menuItem);

      // New separator between actions and the radio buttons
      this.add( new JSeparator() );

      // New button group for the radio buttons
      ButtonGroup group = new ButtonGroup();

      // Create the radio buttons
      rb = new JMenuRadioButtonDragOn(gui);
      group.add(rb);
      this.add(rb);

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

      rb = new JMenuRadioButtonAddImages(gui);
      group.add(rb);
      this.add(rb);

      // New separator between actions and the radio buttons
      this.add( new JSeparator() );

      //paste
      menuItem = new JMenuActionShowProperties(gui);
      this.add(menuItem);

      // Define the radio button to be selected by default
      this.setSelected(rb2);
   }

   /**
    * Method to check and update the removeObject menu actions state when necessary
    *
    * @param invoker The component in whose space the popup menu is to appear.
    * @param x The x coordinate in invoker's coordinate space at which the popup menu
    * is to be displayed.
    * @param y The y coordinate in invoker's coordinate space at which the popup menu
    * is to be displayed.
    */
   public void show(Component invoker, int x, int y){
      update();
      super.show(invoker,x,y);
   }

   /**
    * Method to set the boolean value of whether the menu action removeObject
    * is enabled or not depending if the right click occured on an object.
    */
   public void update(){
      if(gui.getGMap().getGDraw().getSelected().getSize() == 0){
         removeObject.setEnabled(false);
      }else{
         removeObject.setEnabled(true);
      }
   }

}




