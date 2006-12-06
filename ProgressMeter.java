import java.lang.String;
import java.lang.Object;
import java.lang.Math;

/**
 * Class ProgressMeter represents the characteristics of the progress meter
 * which are modified by EmbeddedProgressMeter
 */
class ProgressMeter{

   private Object object;
   private double percent;
   private String message;
   private boolean stopFlag;

   /** Creates a new instance of ProgressMeter */
   public ProgressMeter(String object, double percent, String message){
      this.object = object;
      this.percent = percent;
      this.message = message;
      this.stopFlag = false;
   }

   /** Creates a new empty instance of ProgressMeter */
   public ProgressMeter(){
      this(null,0.0,"");
   }

   /**
    * Method to set the message for the current status of the progress bar
    *
    * @param message The message to display next to the progress bar
    * @param o       The object of the progress bar
    * @return        The boolean result for whether the message was set or not
    */
   public boolean setMessage(String message, Object o){
      if(object == o){
         this.message=message;
         return true;
      }
      else{
         return false;
      }
   }

   /**
    * Method to set the percentage for the progress bar
    *
    * @param percent The percentage to be set for the progress bar
    * @param o       The object of the progress bar
    * @return        The boolean result for whether the percent was set or not
    */
   public boolean setPercent(double percent, Object o){
      if(object == o){
         this.percent=percent;
         return true;
      }
      else{
         return false;
      }
   }

   /**
    * Method to get the current percent value of the progress bar
    *
    * @return The current percent value of the progress bar
    */
   public double getPercent(){
      return percent;
   }

   /**
    * Method to get the status message of the progress bar
    *
    * @return The current status message of the progress bar
    */
   public String getMessage(){
      return message;
   }

   /**
    * Method to check whether an object instance was grabbed by the progress bar
    *
    * @return The boolean result for whether an object instance was grabbed by the progress bar
    */
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

   /**
    * Method to released the progress bar for use by another object.
    *
    * @return The boolean result for whether an object instance was released by the progress bar
    */
   public boolean release(Object o){
      if(object == o || object == null){
         object=null;
         stopFlag = false;
         return true;
      }
      else{
         return false;
      }
   }

   /**
    * Method to immediately stop the current thread action used by the progress bar
    */
   public void stop(){
      stopFlag = true;
   }

   /**
    * Method to get the flag value of stopFlag
    *
    * @return The boolean result of stopFlag
    */
   public boolean getStopFlag(){
      return stopFlag;
   }

   /**
    * Method to check whether the passed paramater o is an object
    *
    * @param o The object passed for checking
    * @return  The boolean result of whether the paramater is indeed an object
    */
   public boolean isObject(Object o){
      return o == object;
   }

   /**
    * Method for making the progress bar visible or not
    *
    * @param b The boolean value for setting the progress bar visible or not
    */
   public void setVisible(boolean b){
      // Simple declared method for making the progress bar visible or not
   }

   /**
    * Method for converting the job's integer total into a usable percentage
    *
    * @param p  The integer value to be converted into a usable percentage value
    * @return   The double usable percentage value
    */
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

   /**
    * Method for converting the job's complete status into a percentage
    *
    * @param done    The double value of how much is already complete for the current job
    * @param total   The double value of the job's total value needed for completion
    * @return        The percentage value of how complete the job is
    */
   public static double computePercent(double done, double total){
      return (done/total)*100.0;
   }
};