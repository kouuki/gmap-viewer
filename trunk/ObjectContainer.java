import java.io.Serializable;

/** A container that holds objects. Searches are done in O(n) time. All other actions are O(1). */
public class ObjectContainer implements Serializable{

   private Object[] objects;
   private int size;

   public ObjectContainer(){
      objects = new Object[10];
      size = 0;
   }

   /**
    * Method for adding a new Object
    * @param object  The new Object
    */
   public void add(Object object){
      //enlarge if needed
      if(size == objects.length) enlargeObjectArray();
      objects[size++] = object;
   }

   /**
    * Method for removing a Object by index reference
    * @param index   The index of the object to remove
    */
   public void remove(int index){
      if(index == -1) return;
      if(index == -1 || index >= size) return;
      objects[index] = objects[(size--)-1];
   }

   /**
    * Method for removing a Object by object reference
    * @param object   The object to remove
    */
   public void remove(Object object){
      remove(getIndex(object));
   }

   /** Method for removing all current Objects */
   public void removeAll(){
      objects = new Object[10];
      size = 0;
   }

   /**
    * Method for getting a Object by index reference
    * @param index   The index of the object
    * @return        The object with the given index
    */
   public Object get(int index){
      return objects[index];
   }

   /**
    * Method gets the size of this container.
    * @return        The size.
    */
   public int getSize(){
      return size;
   }

   /**
    * Method for getting the index of a given Object
    * @param object  The object to get the index of
    * @return        The index of the given object
    */
   public int getIndex(Object object){
      for(int i=0;i<size;i++) if(object == objects[i]) return i;
      return -1;
   }

   /** Method to enlarge the array for Objects */
   private void enlargeObjectArray(){
      Object[] temp = new Object[(int)(objects.length * 1.5)];
      for(int i=0;i<objects.length;i++)
         temp[i] = objects[i];
      objects = temp;
   }

   /**
    * Method for converting object references into a string representation
    * @return  The string representation for the Object array
    */
   public String toString(){
      String s = "ObjectContainer{";
      for(int i=0;i<size;i++){
         s += objects[i];
      }
      s += "}";
      return s;
   }

}