import javax.swing.JToolBar;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener ;

/**
 * This class contains buttons for the GUI's toolbar.
 *@author bill
 */
public class GToolBar extends JToolBar implements ActionListener{

   private GUI gui;

   /**
    * Creates a GToolbar with the necessary buttons and registers it to gui.
    *@param gui The GUI to register the GToolBar to.
    */
    public GToolBar(GUI gui)
    {
      this.gui = gui;

        String[] imageFiles = { "hand_grab.png", "selection_cursor.gif", "point_marker.gif", "line_marker1.gif", "text_marker.gif" };
        String[] toolbarLabels = { "Map Dragging", "Selection Rectangle", "Add Points", "Add Lines", "Add Text"};
        Insets margins = new Insets(0, 0, 0, 0);
        for(int i=0; i<toolbarLabels.length; i++) {
            GToolBarButton button = new GToolBarButton("images/" + imageFiles[i]);
            button.setActionCommand(imageFiles[i]);
            button.addActionListener(this);
            button.setToolTipText(toolbarLabels[i]);
            button.setMargin(margins);
            add(button);
        }
    }

   /**
    * Fires an action in the GToolBar. This method uses the actionCommand supplied by
    * the action event to determine what action should be performed. It also handles
    * the work the action needs to do.
    *@param arg0 The ActionEvent that called this method.
    */
    public void actionPerformed(ActionEvent arg0) {
      if (arg0.getActionCommand() == "hand_grab.png") {
            GPane pane = gui.getTopPane();
            pane.setMode(GPane.DRAGGING_MODE);
            gui.getNotifier().firePaneEvent(this);
            gui.getProgressMeter().getPanel().repaint();
      } else if (arg0.getActionCommand() == "selection_cursor.gif") {
            GPane pane = gui.getTopPane();
            pane.setMode(GPane.SELECTION_MODE);
            gui.getNotifier().firePaneEvent(this);
            gui.getProgressMeter().getPanel().repaint();
      } else if (arg0.getActionCommand() == "point_marker.gif") {
            GPane pane = gui.getTopPane();
            pane.setMode(GPane.DRAW_MARKER_MODE);
            gui.getNotifier().firePaneEvent(this);
            gui.getProgressMeter().getPanel().repaint();
      } else if (arg0.getActionCommand() == "line_marker1.gif") {
            GPane pane = gui.getTopPane();
            pane.setMode(GPane.DRAW_LINE_MODE);
            gui.getNotifier().firePaneEvent(this);
            gui.getProgressMeter().getPanel().repaint();
      } else if (arg0.getActionCommand() == "text_marker.gif") {
            GPane pane = gui.getTopPane();
            pane.setMode(GPane.DRAW_STRING_MODE);
            gui.getNotifier().firePaneEvent(this);
            gui.getProgressMeter().getPanel().repaint();
      }
    }
}