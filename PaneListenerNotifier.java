
class PaneListenerNotifier{

   /* PaneListener methods
    * This fires an action to every registered menu or group
    */

   private PaneListener[] listeners;
   private int listenersSize;

   public PaneListenerNotifier(){
      listeners = new PaneListener[10];
      listenersSize = 0;
   }

   public void addPaneListener(PaneListener listener){
      //enlarge if needed
      if(listenersSize == listeners.length) enlargeListenerArray();
      listeners[listenersSize++] = listener;
   }

   public void removePaneListener(int index){
      if(index == -1 || index >= listenersSize) return;
      listeners[index] = listeners[listenersSize--];
   }

   public void removePaneListener(PaneListener listener){
      removePaneListener(getPaneListenerIndex(listener));
   }

   public void removeAllPaneListeners(){
      listeners = new PaneListener[10];
      listenersSize = 0;
   }

   public PaneListener getPaneListener(int index){
      return listeners[index];
   }

   public int getPaneListenerIndex(PaneListener listener){
      for(int i=0;i<listenersSize;i++) if(listener == listeners[i]) return i;
      return -1;
   }

   private void enlargeListenerArray(){
      PaneListener[] temp = new PaneListener[(int)(listeners.length * 1.5)];
      for(int i=0;i<listeners.length;i++)
         temp[i] = listeners[i];
      listeners = temp;
   }

   public void firePaneEvent(Object object){
      for(int i=0;i<listenersSize;i++) if(listeners[i] != null) listeners[i].paneEvent(object);
   }

}

