import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import java.io.Serializable;

/**
 * GZoomSlider is a graphical component that allows the user to zoom in or out
 * by clicking on the up or down arrows.
 * @author taeber
 */
public class GZoomSlider extends JPanel implements PaneListener, MouseListener {

   private GUI gui;

   private Arrow upArrow;
   private Arrow downArrow;

   public GZoomSlider(GUI gui) {
      super();
      this.gui = gui;
      this.upArrow = new Arrow(0, 0, 15, Arrow.Orientation.UP);
      this.downArrow = new Arrow(0, 20, 15, Arrow.Orientation.DOWN);

      this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
      this.setOpaque(false);

      this.addMouseListener(this);
      gui.getNotifier().addPaneListener(this);
   }

    /**
     * This method paints the JPanel with the graphical representation of the
     * Zoom Slider's current state. You have a 40x250 pixel area near the upper
     * left corner of the window.
     *
     * @param g
     */
   protected void paintComponent(Graphics g) {
      super.paintComponent(g);
      this.upArrow.draw(g);
      this.downArrow.draw(g);
   }

   /*
    * (non-Javadoc)
    * @see PaneListener#paneEvent(java.lang.Object)
    */
   public void paneEvent(Object source) {
      // TODO Auto-generated method stub

   }

   /*
    * (non-Javadoc)
    * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
    */
   public void mouseClicked(MouseEvent arg0) {
   }

   /*
    * (non-Javadoc)
    * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
    */
   public void mouseEntered(MouseEvent arg0) {
      // TODO Auto-generated method stub

   }

   /*
    * (non-Javadoc)
    * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
    */
   public void mouseExited(MouseEvent arg0) {
      // TODO Auto-generated method stub

   }

   /*
    * (non-Javadoc)
    * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
    */
   public void mousePressed(MouseEvent arg0) {
      // TODO Auto-generated method stub
      int mouseX = arg0.getX();
      int mouseY = arg0.getY();
      if (this.upArrow.contains(mouseX, mouseY)) {
         upArrow.setDepressed(true);
      } else if (this.downArrow.contains(mouseX, mouseY)) {
         downArrow.setDepressed(true);
      }
      repaint();
   }

   /*
    * (non-Javadoc)
    * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
    */
   public void mouseReleased(MouseEvent arg0) {
      // TODO Auto-generated method stub
      int mouseX = arg0.getX();
      int mouseY = arg0.getY();

      if (this.upArrow.contains(mouseX, mouseY)) {
         this.gui.getTopPane().setZoom(this.gui.getTopPane().getZoom() - 1);
      } else if (this.downArrow.contains(mouseX, mouseY)) {
         this.gui.getTopPane().setZoom(this.gui.getTopPane().getZoom() + 1);
      }
      upArrow.setDepressed(false);
      downArrow.setDepressed(false);
      repaint();
  }


   /**
    * The graphical representation of the arrows.
    */
   static class Arrow implements Serializable{

      /*
       * The direction the arrow is facing.
       */
      enum Orientation { UP, DOWN, LEFT, RIGHT }

      /**
       * Horizontal position of the arrow in the userspace coordinate system.
       */
      private int x;

      /**
       * Vertical position of the arrow in the userspace coordinate system.
       */
      private int y;

      /**
       * The length of the base edge.
       */
      private int size;

      /**
       * The direction the arrow is facing.
       */
      private Orientation orientation;

      /**
       * Whether or not its depressed.
       */
      private boolean depressed;

      /*
       * The Java2D polygon representation of this arrow.
       */
      private Polygon polygon;

      /**
       * Accessor for the <tt>orientation</tt> property.
       * @return the <tt>orientation</tt> property
       */
      public Orientation getOrientation() {
         return orientation;
      }

      /**
       * Modifier for the <tt>orientation</tt> property.
       */
      public void setOrientation(Orientation orientation) {
         this.orientation = orientation;
      }

      /**
       * Accessor for the <tt>size</tt> property.
       * @return the <tt>size</tt> property
       */
      public int getSize() {
         return size;
      }

      /**
       * Modifier for the <tt>size</tt> property.
       */
      public void setSize(int size) {
         this.size = size;
      }

      /**
       * Accessor for the <tt>x</tt> property.
       * @return the <tt>x</tt> property
       */
      public int getX() {
         return x;
      }

      /**
       * Modifier for the <tt>x</tt> property.
       */
      public void setX(int x) {
         this.x = x;
      }

      /**
       * Accessor for the <tt>y</tt> property.
       * @return the <tt>y</tt> property
       */
      public int getY() {
         return y;
      }

      /**
       * Modifier for the <tt>x</tt> property.
       */
      public void setY(int y) {
         this.y = y;
      }

      /**
       * Modifier for the <tt>depressed</tt> property.
       */
      public void setDepressed(boolean depressed) {
         this.depressed = depressed;
      }

      /**
       * Accessor for the <tt>depressed</tt> property.
       */
      public boolean getDepressed() {
         return depressed;
      }


      /**
       * A triangular arrow.
       * @param x top left horizontal coordinate
       * @param y top left vertical coordinate
       * @param size length of the base
       */
      public Arrow(int x, int y, int size) {
         this(x, y, size, Orientation.UP);
      }

      /**
       * A triangular arrow.
       * @param x top left horizontal coordinate
       * @param y top left vertical coordinate
       * @param size length of the base
       * @param orientation the direction the arrow should face
       */
      public Arrow(int x, int y, int size, Orientation orientation) {
         this.x = x;
         this.y = y;
         this.size = size;
         this.orientation = orientation;
         this.polygon = null;
      }

      /**
       * Determines if a given point is on or within the borders of the
       * arrow.
       * @param x the horizontal coordinate in usersapce
       * @param y the vertical cooridinate in userspace
       * @return <tt>true</tt> if the point is on or within the borders,
       *          <tt>false</tt> otherwise.
       */
      public boolean contains(int x, int y) {
         return this.polygon.contains(x, y);
      }

      /**
       * Paints the arrow's triangular polygon onto the screen.
       * @param graphics
       */

      public void draw(Graphics graphics) {
         Graphics2D g = (Graphics2D) graphics;

         int[] xs;
         int[] ys;

         switch (this.orientation) {
            case DOWN:
               xs = new int[] {x, x+size/2, x+size};
               ys = new int[] {y, y+size, y};
               break;
            case LEFT:
               xs = new int[] {x, x+size, x+size};
               ys = new int[] {y+size/2, y, y+size};
               break;
            case RIGHT:
               xs = new int[] {x, x, x+size};
               ys = new int[] {y, y+size, y+size/2};
               break;
            case UP:
            default:
               xs = new int[] {x, x+size/2, x+size};
               ys = new int[] {y+size, y, y+size};
               break;
         }

         //depressed
         int translation = 0;
         if(depressed) translation = 2;

         //apply the translation
         for(int i=0;i<xs.length;i++){
            xs[i] += translation;
            ys[i] += translation;
         }

         this.polygon = new Polygon(xs, ys, xs.length);
         g.fillPolygon(this.polygon);
         Color tempColor = graphics.getColor();
         graphics.setColor(Color.WHITE);
         g.drawPolygon(this.polygon);
         graphics.setColor(tempColor);
      }
   }
}