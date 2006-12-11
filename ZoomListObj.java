import java.io.Serializable;

class ZoomListObj implements Serializable{
   private int zoom;
   private String message;
   private boolean showZoomInt;

   public ZoomListObj(String message, int zoom, boolean showZoomInt){
      this.zoom = zoom;
      this.message = message;
      this.showZoomInt = showZoomInt;
   }

   public ZoomListObj(String message, int zoom){
      this(message,zoom,true);
   }

   public int getZoom(){
      return zoom;
   }

   public String toString(){
      if(showZoomInt) return message+zoom;
      else return message;
   }
}