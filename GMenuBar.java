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

public class GMenuBar extends JMenuBar{

   public GMenuBar(GUI gui){
      //file
      JMenuItem[] fileMenu = {new JMenuActionExport(gui), new JMenuActionExit(gui)};
      add(new JMenuGroup("File",gui,fileMenu));

      //pane
      JMenuItem[] paneMenu = {new JMenuActionNewPane(gui),new JMenuActionSetPaneTitle(gui),new JMenuActionRemovePane(gui)};
      add(new JMenuGroup("Pane",gui,paneMenu));

      //view
      JMenuItem[] zoomMenu = {new JMenuRadioButtonActionZoom("Level 1",gui,1),new JMenuRadioButtonActionZoom("Level 2",gui,2),new JMenuRadioButtonActionZoom("Level 3",gui,3),new JMenuRadioButtonActionZoom("Level 4",gui,4),new JMenuRadioButtonActionZoom("Level 5",gui,5),new JMenuRadioButtonActionZoom("Level 6",gui,6),new JMenuRadioButtonActionZoom("Level 7",gui,7),new JMenuRadioButtonActionZoom("Level 8",gui,8),new JMenuRadioButtonActionZoom("Level 9",gui,9),new JMenuRadioButtonActionZoom("Level 10",gui,10),new JMenuRadioButtonActionZoom("Level 11",gui,11),new JMenuRadioButtonActionZoom("Level 12",gui,12),new JMenuRadioButtonActionZoom("Level 13",gui,13),new JMenuRadioButtonActionZoom("Level 14",gui,14),new JMenuRadioButtonActionZoom("Level 15",gui,15)};
      JMenuItem[] viewMenu = {new JMenuActionSetCenter(gui),new JMenuActionSetCenterPixel(gui),new JMenuGroup("Set Zoom",gui,zoomMenu)};
      add(new JMenuGroup("View",gui,viewMenu));

      //cache
      JMenuItem[] cacheZoomMenu = {new JMenuRadioButtonActionCache("None",gui,-1),new JMenuRadioButtonActionCache("Level 1",gui,1),new JMenuRadioButtonActionCache("Level 2",gui,2),new JMenuRadioButtonActionCache("Level 3",gui,3),new JMenuRadioButtonActionCache("Level 4",gui,4),new JMenuRadioButtonActionCache("Level 5",gui,5),new JMenuRadioButtonActionCache("Level 6",gui,6),new JMenuRadioButtonActionCache("Level 7",gui,7),new JMenuRadioButtonActionCache("Level 8",gui,8),new JMenuRadioButtonActionCache("Level 9",gui,9),new JMenuRadioButtonActionCache("Level 10",gui,10),new JMenuRadioButtonActionCache("Level 11",gui,11),new JMenuRadioButtonActionCache("Level 12",gui,12),new JMenuRadioButtonActionCache("Level 13",gui,13),new JMenuRadioButtonActionCache("Level 14",gui,14),new JMenuRadioButtonActionCache("Level 15",gui,15)};
      JMenuItem[] cacheRectMenu = {new JMenuActionCacheSelected("Level 1",gui,1),new JMenuActionCacheSelected("Level 2",gui,2),new JMenuActionCacheSelected("Level 3",gui,3),new JMenuActionCacheSelected("Level 4",gui,4),new JMenuActionCacheSelected("Level 5",gui,5),new JMenuActionCacheSelected("Level 6",gui,6),new JMenuActionCacheSelected("Level 7",gui,7),new JMenuActionCacheSelected("Level 8",gui,8),new JMenuActionCacheSelected("Level 9",gui,9),new JMenuActionCacheSelected("Level 10",gui,10),new JMenuActionCacheSelected("Level 11",gui,11),new JMenuActionCacheSelected("Level 12",gui,12),new JMenuActionCacheSelected("Level 13",gui,13),new JMenuActionCacheSelected("Level 14",gui,14),new JMenuActionCacheSelected("Level 15",gui,15)};
      JMenuItem[] cacheMenu = {new JMenuGroup("Cache Selected",gui,cacheRectMenu),new JMenuGroup("Set Cache Zoom",gui,cacheZoomMenu)};
      add(new JMenuGroup("Cache",gui,cacheMenu));

      //pmeter
      JMenuItem[] menu = {new JMenuActionPOC(gui), new JMenuActionClearRAM(gui)};
      add(new JMenuGroup("Debugging",gui,menu));
   }



}
