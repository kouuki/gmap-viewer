import java.lang.String;
import java.lang.Object;
import java.lang.Math;



class ProgressMeter{

   private Object object;
   private double percent;
   private String message;
   private boolean stopFlag;

   public ProgressMeter(String object, double percent, String message){
      this.object = object;
      this.percent = percent;
      this.message = message;
      this.stopFlag = false;
   }

   public ProgressMeter(){
      this(null,0.0,"");
   }


   public boolean setMessage(String message, Object o){
      if(object == o){
         this.message=message;
         return true;
      }
      else{
         return false;
      }
   }

   public boolean setPercent(double percent, Object o){
      if(object == o){
         this.percent=percent;
         return true;
      }
      else{
         return false;
      }
   }

   public double getPercent(){
      return percent;
   }

   public String getMessage(){
      return message;
   }

   public boolean grab(Object o){
      if(object == null){
         object=o;
         stopFlag = false;
         return true;
      }
      else{
         return false;
      }
   }

   public boolean release(Object o){
      if(object== o || object == null){
         object=null;
         stopFlag = false;
         return true;
      }
      else{
         return false;
      }
   }

   public void stop(){
      stopFlag = true;
   }

   public boolean getStopFlag(){
      return stopFlag;
   }


   public boolean isObject(Object o){
      return o == object;
   }

   public void setVisible(boolean b){
      /*This function was said to do nothing.  Guess the child will put something here later*/
   }

   public double getRoundedPercent(int p){
      long factor = (long)Math.pow(10,p);
      double val = percent;
      // Shift the decimal the correct number of places
      // to the right.
      val = val * factor;

      // Round to the nearest integer.
      long tmp = Math.round(val);

      // Shift the decimal the correct number of places
      // back to the left.
      return (double)tmp / factor;
   }
   public static double computePercent(double done, double total){
      return (done/total)*100.0;
   }
};