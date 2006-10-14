

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


class ExportDialog extends JDialog implements MouseListener, ActionListener{
   //properties
   private JButton save;
   private JButton cancel;
   private JRadioButton useScreen;
   private JRadioButton useSelected;
   private JRadioButton useCustom;
   private ButtonGroup group;
   private JTextField centerLat;
   private JTextField centerLong;
   private JTextField centerWidth;
   private JTextField centerHeight;
   private JList zoomLevel;
   private JList cacheLevel;
   private JLabel fileSize;

   //store stuff
   private CenteredRectangle screenRect;
   private CenteredRectangle selectedRect;
   private GUI gui;

   public ExportDialog(GUI gui){

      //superclass constructor
      super(gui, "Export to PNG", true);

      //store screen data
      this.gui = gui;
      GPane pane = gui.getTopPane();
      selectedRect = pane.getMouseRectanglePositionCentered();
      screenRect = pane.getScreenDimensionsCentered();

      //set up content pane
      Container content = getContentPane();
      content.setLayout(null);

      //setup size
      this.setSize(260,350);

      //create select group
      group = new ButtonGroup();

      //create stuff

      //use screen coords
      useScreen = new JRadioButton("Use screen coordinates");
      useScreen.setBounds(20,20,200,30);
      useScreen.addActionListener(this);
      group.add(useScreen);
      content.add(useScreen);

      //use selection coords
      useSelected = new JRadioButton("Use selection coordinates");
      useSelected.setBounds(20,50,200,30);
      useSelected.addActionListener(this);
      group.add(useSelected);
      content.add(useSelected);

      //use custom coords
      useCustom = new JRadioButton("Use specified coordinates");
      useCustom.setBounds(20,80,200,30);
      useCustom.addActionListener(this);
      group.add(useCustom);
      content.add(useCustom);

      //latitude Label
      JLabel l2 = new JLabel("Lat");
      l2.setBounds(40,110,70,30);
      content.add(l2);

      //latitude box
      centerLat = new JTextField();
      centerLat.setBounds(78,115,45,20);
      centerLat.addActionListener(this);
      content.add(centerLat);

      //longitude label
      JLabel l3 = new JLabel("Long");
      l3.setBounds(130,110,70,30);
      content.add(l3);

      //longitude box
      centerLong = new JTextField();
      centerLong.setBounds(170,115,45,20);
      centerLong.addActionListener(this);
      content.add(centerLong);

      //width label
      JLabel l5 = new JLabel("Width");
      l5.setBounds(40,140,70,30);
      content.add(l5);

      //width box
      centerWidth = new JTextField();
      centerWidth.setBounds(78,145,45,20);
      centerWidth.addActionListener(this);
      content.add(centerWidth);

      //height label
      JLabel l6 = new JLabel("Height");
      l6.setBounds(130,140,70,30);
      content.add(l6);

      //height box
      centerHeight = new JTextField();
      centerHeight.setBounds(170,145,45,20);
      centerHeight.addActionListener(this);
      content.add(centerHeight);

      //zoom scroll pane
      JPanel scrollPanePanel = new JPanel();
      scrollPanePanel.setLayout(new BorderLayout());
      scrollPanePanel.setBounds(40,190,170,25);
      scrollPanePanel.setPreferredSize(new Dimension(170,25));
      content.add(scrollPanePanel, BorderLayout.CENTER);

      Object[] listObj = {new ZoomListObj("Zoom Level ",1),new ZoomListObj("Zoom Level ",2),new ZoomListObj("Zoom Level ",3),new ZoomListObj("Zoom Level ",4),new ZoomListObj("Zoom Level ",5),new ZoomListObj("Zoom Level ",6),new ZoomListObj("Zoom Level ",7),new ZoomListObj("Zoom Level ",8),new ZoomListObj("Zoom Level ",9),new ZoomListObj("Zoom Level ",10),new ZoomListObj("Zoom Level ",11),new ZoomListObj("Zoom Level ",12),new ZoomListObj("Zoom Level ",13),new ZoomListObj("Zoom Level ",14),new ZoomListObj("Zoom Level ",15)};
      zoomLevel = new JList(listObj);
      zoomLevel.setFixedCellWidth(50);

      JScrollPane scrollPane = new JScrollPane(zoomLevel);
      scrollPanePanel.add(scrollPane);

      zoomLevel.setSelectedValue(listObj[pane.getZoom()-1],true);

      //cache zoom scroll pane
      JPanel scrollPanePanel2 = new JPanel();
      scrollPanePanel2.setLayout(new BorderLayout());
      scrollPanePanel2.setBounds(40,220,170,25);
      scrollPanePanel2.setPreferredSize(new Dimension(170,25));
      content.add(scrollPanePanel2, BorderLayout.CENTER);

      Object[] listObj2 = {new ZoomListObj("No Cache Zoom ",-1,false), new ZoomListObj("Cache Zoom Level ",1),new ZoomListObj("Cache Zoom Level ",2),new ZoomListObj("Cache Zoom Level ",3),new ZoomListObj("Cache Zoom Level ",4),new ZoomListObj("Cache Zoom Level ",5),new ZoomListObj("Cache Zoom Level ",6),new ZoomListObj("Cache Zoom Level ",7),new ZoomListObj("Cache Zoom Level ",8),new ZoomListObj("Cache Zoom Level ",9),new ZoomListObj("Cache Zoom Level ",10),new ZoomListObj("Cache Zoom Level ",11),new ZoomListObj("Cache Zoom Level ",12),new ZoomListObj("Cache Zoom Level ",13),new ZoomListObj("Cache Zoom Level ",14),new ZoomListObj("Cache Zoom Level ",15)};
      cacheLevel = new JList(listObj2);
      cacheLevel.setFixedCellWidth(50);

      JScrollPane scrollPane2 = new JScrollPane(cacheLevel);
      scrollPanePanel2.add(scrollPane2);


      //initialize scrollpane stuff
      if(!pane.getShowCachedZoom() || pane.getShowCachedZoomLevel() == -1) cacheLevel.setSelectedValue(listObj2[0],true);
      else cacheLevel.setSelectedValue(listObj2[pane.getShowCachedZoomLevel()],true);


      //save button
      save = new JButton("Save");
      save.setBounds(140,270,80,30);
      content.add(save);
      save.addActionListener(this);

      //cancel button
      cancel = new JButton("Cancel");
      cancel.setBounds(20,270,100,30);
      content.add(cancel);
      cancel.addActionListener(this);

      //disable selection if selection is null
      if(selectedRect != null){
         setUseSelected();
      }else{
         useSelected.setEnabled(false);
         setUseScreen();
      }

      //disable custom to start
      setEnabledCustom(false);

   }

   public void setUseScreen(){
      useScreen.setSelected(true);
      setEnabledCustom(false);
      centerLat.setText(screenRect.center.getX()+"");
      centerLong.setText(screenRect.center.getY()+"");
      centerWidth.setText(screenRect.width+"");
      centerHeight.setText(screenRect.height+"");
   }

   public void setUseSelected(){
      useSelected.setSelected(true);
      setEnabledCustom(false);
      centerLat.setText(selectedRect.center.getX()+"");
      centerLong.setText(selectedRect.center.getY()+"");
      centerWidth.setText(selectedRect.width+"");
      centerHeight.setText(selectedRect.height+"");
   }

   public void setUseCustom(){
      useCustom.setSelected(true);
      setEnabledCustom(true);
   }

   private void setEnabledCustom(boolean value){
      centerLat.setEnabled(value);
      centerLong.setEnabled(value);
      centerWidth.setEnabled(value);
      centerHeight.setEnabled(value);
   }

   public void save(){
      //build the image
      GMap map = gui.getGMap();

      try{
         GPhysicalPoint center = new GPhysicalPoint(Double.parseDouble(centerLat.getText()),Double.parseDouble(centerLong.getText()));
         ZoomListObj selectedZoom = (ZoomListObj)zoomLevel.getSelectedValue();
         ZoomListObj selectedCacheZoom = (ZoomListObj)cacheLevel.getSelectedValue();

         BufferedImage imageToWrite = map.getImage(
            center,
            (int)Double.parseDouble(centerWidth.getText()),
            (int)Double.parseDouble(centerHeight.getText()),
            selectedZoom.getZoom(),
            selectedCacheZoom.getZoom()
         );

         //set extension
         String extension = "png";

         //file chooser
         String filename = File.separator+extension;
         JFrame frame= new JFrame();
         JFileChooser fileChooser = new JFileChooser(new File(filename));

         // Show save dialog; this method does not return until the dialog is closed
         fileChooser.showSaveDialog(frame);
         File outputFile = fileChooser.getSelectedFile();

         if(outputFile != null){
            outputFile = new File(LibString.appendExtensionIfNeeded(outputFile.getPath(),"."+extension));
            try {
               if(extension == "png"){
                  ImageIO.write(imageToWrite, "png", outputFile);
               }
               else if(extension == "jpg"){
                  ImageIO.write(imageToWrite, "jpg", outputFile);
               }
            }catch (IOException e) {
               System.out.println("File I/O Error Caught, continuing.");
            }
         }

         //close
         close();
      }catch(Exception e){System.out.println(e);}
   }

   //close method
   public void close(){
      setVisible(false);
   }


   //action listener
   public void actionPerformed(ActionEvent e){
      Object sourceObject = e.getSource();
      if(sourceObject == save) save();
      else if(sourceObject == cancel) close();
      else if(sourceObject == useScreen) setUseScreen();
      else if(sourceObject == useSelected || sourceObject == centerLat || sourceObject == centerLong || sourceObject == centerWidth || sourceObject == centerHeight) setUseSelected();
      else if(sourceObject == useCustom) setUseCustom();
   }


   //mouselistener methods

   public void mouseReleased(MouseEvent e){}
   public void mousePressed(MouseEvent e){}
   public void mouseExited(MouseEvent e){}
   public void mouseEntered(MouseEvent e){}
   public void mouseClicked(MouseEvent e){}



}
