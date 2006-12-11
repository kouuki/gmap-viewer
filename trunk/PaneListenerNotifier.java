import java.io.Serializable;

class PaneListenerNotifier implements Serializable{

   /**PaneListener methods
    * This fires an action to every registered menu or group
    */

   private PaneListener[] listeners;
   private int listenersSize;

   /**
    * Constructs a basic PaneListenerNotifier
    */
   public PaneListenerNotifier(){
      listeners = new PaneListener[10];
      listenersSize = 0;
   }

   /**
    * Adds Listener to be sent a PaneNotifier action
    * @param listener - the PaneListner to add
    */
   public void addPaneListener(PaneListener listener){
      //enlarge if needed
      if(listenersSize == listeners.length) enlargeListenerArray();
      listeners[listenersSize++] = listener;
   }

   /**
    * Removes Listener from PaneNotifier list
    *  @param index - numbered index of listener
    */
   public void removePaneListener(int index){
      if(index == -1 || index >= listenersSize) return;
      listeners[index] = listeners[listenersSize--];
   }
   /**
    * Removes Listener from PaneNotifier list
    *  @param listener - listener to be removed
    */
   public void removePaneListener(PaneListener listener){
      removePaneListener(getPaneListenerIndex(listener));
   }
   /**
    * Removes all PaneListener objects
    */
   public void removeAllPaneListeners(){
      listeners = new PaneListener[10];
      listenersSize = 0;
   }
   /**
    * Gets Listener from PaneNotifier list
    * @param index - numbered index of listener to get
    * @return A PaneListener at index
    */
   public PaneListener getPaneListener(int index){
      return listeners[index];
   }
   /**
    * Returns index of a PaneListener in PaneListenerNotifier
    * @param listener - PaneListener you want index of
    * @return An int representing PaneListener index
    */
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

   /**
    * Fires a PaneEevent
    * @param object - used to fire PaneEvent
    */
   public void firePaneEvent(Object object){
      for(int i=0;i<listenersSize;i++) if(listeners[i] != null) listeners[i].paneEvent(object);
   }

}

