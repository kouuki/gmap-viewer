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


/** Geocoder **/
public class Geocoder{
   /**
    * Method to geocode a string into a GPhysicalPoint
    * @return              The GPhysicalPoint of this location.
    */



   public static GPhysicalPoint geocode(String str){
      //connect to server
      String xmlReturn = "";
      try{
         xmlReturn = LibGUI.getURL("http://geocoder.us/service/rest/geocode","address="+URLEncoder.encode(str,"UTF-8"),"");
      }catch(Exception e){
         System.out.println("Platform dependency error: UTF-8 encoder not supported");
         return null;
      }

      //parse XML
      int startLong = xmlReturn.indexOf("<geo:long>");
      if(startLong == -1) return null;

      int endLong = xmlReturn.indexOf("</geo:long>");
      if(endLong == -1) return null;

      int startLat = xmlReturn.indexOf("<geo:lat>");
      if(startLat == -1) return null;

      int endLat = xmlReturn.indexOf("</geo:lat>");
      if(endLat == -1) return null;


      String longString = xmlReturn.substring(startLong+10, endLong);
      String latString = xmlReturn.substring(startLat+9, endLat);

      double longDouble = Double.parseDouble(longString);
      double latDouble = Double.parseDouble(latString);


      //decide what to return
      GPhysicalPoint gp = new GPhysicalPoint(latDouble, longDouble);
      return gp;
   }
}