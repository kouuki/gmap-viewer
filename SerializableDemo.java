import java.io.ObjectOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;

class Serial{
   public static void main(String [] args){
      String filename = "time.ser";
      if(args.length > 0)
      {
        filename = args[0];
      }
      SampleClass time = new SampleClass();
      FileOutputStream fos = null;
      ObjectOutputStream out = null;
      try
      {
         fos = new FileOutputStream(filename);
         out = new ObjectOutputStream(fos);
         out.writeObject(time);
         out.close();
      }
      catch(IOException ex)
      {
         ex.printStackTrace();
      }
   }
}


class SampleClass implements Serializable{
   String data = "simple";
   String data2 = "simple2";
   SampleClass sample2 = null;
}