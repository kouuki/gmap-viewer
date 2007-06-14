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

/** A PropertiesDialog controls the visual appearance of GDrawablePanels.
*/
public class GPropertiesDialog extends JDialog implements MouseListener, ActionListener, ItemListener{
   //properties
   private JTabbedPane pane;
   private JPanel tabbedPanePanel;
   private JButton apply;
   private JButton close;

   //container of objects
   ObjectContainer objects;

   //store stuff
   private GUI gui;

 /**
   * The GPropertiesDialog class constructor.
   *
   *@param gui the GUI class
   */
   public GPropertiesDialog(GUI gui){
      //superclass constructor
      super(gui, "Properties", false);
      objects = new ObjectContainer();

      //gui
      this.gui = gui;

      //set the fixed size
      setSize(260,350);
      setResizable(false);
      setLayout(null);

      //set up panels for stuff
      pane = new JTabbedPane();

      //add the tabbed panel
      tabbedPanePanel = new JPanel();
      tabbedPanePanel.add(pane);
      tabbedPanePanel.setLayout(null);
      this.getContentPane().add(tabbedPanePanel);
      tabbedPanePanel.setBounds(0,0,this.getWidth(),280);
      pane.setBounds(0,0,tabbedPanePanel.getWidth(),tabbedPanePanel.getHeight());

      //set up buttons
      apply = new JButton("Apply");
      apply.setBounds(150,290,80,26);
      this.getContentPane().add(apply);

      close = new JButton("Close");
      close.setBounds(50,290,80,26);
      this.getContentPane().add(close);

      addPanel(new GPropertiesPanelCustomObject(gui.getGMap()), "Object");

      //add listeners
      addMouseListener(this);
      apply.addItemListener(this);
      apply.addActionListener(this);
      close.addItemListener(this);
      close.addActionListener(this);
   }

   /**
   *  Get the tabbed pane associated with this properties dialog.
   *@return The pane.
   */

   public JTabbedPane getPane(){
      return pane;
   }

   /**
   *  Get the panel that holds the tabbed pane associated with this properties dialog.
   *@return The pane.
   */

   public JPanel getTabbedPanePanel(){
      return tabbedPanePanel;
   }

   /**
   *  Get this dialog's close button.
   *@return The pane.
   */

   public JButton getClose(){
      return close;
   }

   /**
   *  Get this dialogs apply button.
   *@return The pane.
   */

   public JButton getApply(){
      return apply;
   }

   private void apply(){
      int size = getPanelsSize();
      for(int i=0;i<size;i++){
         getPanel(i).apply();
      }
      //System.out.println("apply from dialog");
      gui.getTopPane().draw();
   }

   private void close(){
      this.setVisible(false);
   }

   /**
    * Method for adding a new Panel
    * @param object  The new Panel
    */
   public void addPanel(GPropertiesPanel object, String title){
      pane.add(object,title);
      objects.add(object);
   }

   /**
    * Method for removing a Panel by index reference
    * @param index   The index of the object to remove
    */
   public void removePanel(int index){
      pane.remove((GPropertiesPanel)objects.get(index));
      objects.remove(index);
   }

   /**
    * Method for removing a Panel by object reference
    * @param object   The object to remove
    */
   public void removePanel(GPropertiesPanel object){
      pane.remove(object);
      objects.remove(object);
   }

   /** Method for removing all current Panels */
   public void removeAllPanels(){
      pane.removeAll();
      objects.removeAll();
   }

   /**
    * Method for getting a Panel by index reference
    * @param index   The index of the object
    * @return        The object with the given index
    */
   public GPropertiesPanel getPanel(int index){
      return (GPropertiesPanel)objects.get(index);
   }

   /**
    * Method gets the size of this container.
    * @return        The size.
    */
   public int getPanelsSize(){
      return objects.getSize();
   }

   /**
    * Method for getting the index of a given Panel
    * @param object  The object to get the index of
    * @return        The index of the given object
    */
   public int getIndex(GPropertiesPanel object){
      return objects.getIndex(object);
   }

   /**
   *
   *@param e It is the type of action event the user pursues.
   */
   public void actionPerformed(ActionEvent e){
      if(e.getSource() == apply) apply();
      else if(e.getSource() == close) close();
   }



   /**
   * A method for a mouse event when the mouse is released.
   *
   */
   public void mouseReleased(MouseEvent e){}

   /**
   * A method for a mouse event when the mouse is pressed.
   *
   */
   public void mousePressed(MouseEvent e){}
   /**
   * A method for a mouse event when the mouse is exited.
   *
   */
   public void mouseExited(MouseEvent e){}
   /**
   *
   * A method for a mouse event when the mouse is entered.
   */
   public void mouseEntered(MouseEvent e){}
   /**
   *
   *A method for a mouse event when the mouse is clicked.
   */
  public void mouseClicked(MouseEvent e){}

   /**
   *
   *A method for an event when any item state is changed.
   *@param evt It is the event that is detected to show that an item is changed.
   */
   public void itemStateChanged(ItemEvent evt) {
   }
}
