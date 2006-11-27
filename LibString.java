import java.awt.*;


class LibString{
  static char[] hex = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

   public static Color makeColor(String hexColor){
      //ignore some characters that might end up in there by accident
      char[] ignoreCharacters = {' ','(',')','{','}','[',']','|','%','#',','};
      hexColor = filter(hexColor, ignoreCharacters);
      //do the work
      if(hexColor.length() != 6) return new Color(0,0,0);
      hexColor = hexColor.toUpperCase();
      int r = hexToInt(hexColor.charAt(0) + "" + hexColor.charAt(1));
      int g = hexToInt(hexColor.charAt(2) + "" + hexColor.charAt(3));
      int b = hexToInt(hexColor.charAt(4) + "" + hexColor.charAt(5));
      return new Color(r,g,b);
   }

   public static String makeColorString(Color color){
      if(color == null) return "";
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      return minimumSize(intToHex(r)+"",2,'0')+minimumSize(intToHex(g)+"",2,'0')+minimumSize(intToHex(b)+"",2,'0');
   }

   //returns a string of arbitrary length expressing this decimal in hex
   public static String intToHex(int decimal){
      String out = "";
      int maxPlace = 0;
      for(int i=0;(int)Math.pow(16,i)<decimal;i++) maxPlace = i;
      int[] hexValue = new int[maxPlace+1];
      for(int i=maxPlace;i>=0;i--){
         int thisPower = (int)Math.pow(16,i);
         hexValue[maxPlace-i] = decimal/thisPower;
         decimal -= hexValue[maxPlace-i]*thisPower;
         out += hex[hexValue[maxPlace-i]];
      }
      return out;
   }

   public static int hexToInt(String hexString){
      //ignore some characters that might end up in there by accident
      char[] ignoreCharacters = {' ','(',')','{','}','[',']','|','%','#',','};
      hexString = filter(hexString, ignoreCharacters);
      //do the computations
      int total = 0;
      int base = hex.length;
      int strlen = hexString.length();
      int placeValue = 0;
      for(int i=strlen-1;i>=0;i--){
         total += Math.pow(base,placeValue)*hexToInt(hexString.charAt(i));
         placeValue++;
      }
      return total;
   }
   public static int hexToInt(char hexChar){
      for(int i=0;i<hex.length;i++){
         if(hex[i] == hexChar) return i;
      }
      return 0;
   }

   public static GPhysicalPoint makeGPhysicalPoint(String input){
      //ignore some characters that might end up in there by accident
      char[] ignoreCharacters = {' ','(',')','{','}','[',']','|'};
      input = filter(input, ignoreCharacters);
      //if we now have an empty string return error
      if(input.length() == 0) return null;
      //split in to parts on a comma
      String[] pointValues = split(input,',');
      double[] parsedValues = new double[pointValues.length];
      //if there arent 2 parts return error
      if(pointValues.length != 2) return null;
      for(int i=0;i<pointValues.length;i++){
         try{
            parsedValues[i] = Double.parseDouble(pointValues[i]);
         }catch(Exception e){return null;}
      }
      return new GPhysicalPoint(parsedValues[0], parsedValues[1]);
   }

   public static Point makePoint(String input){
      //ignore some characters that might end up in there by accident
      char[] ignoreCharacters = {' ','(',')','{','}','[',']','|'};
      input = filter(input, ignoreCharacters);
      //if we now have an empty string return error
      if(input.length() == 0) return null;
      //split in to parts on a comma
      String[] pointValues = split(input,',');
      double[] parsedValues = new double[pointValues.length];
      //if there arent 2 parts return error
      if(pointValues.length != 2) return null;
      for(int i=0;i<pointValues.length;i++){
         try{
            parsedValues[i] = Double.parseDouble(pointValues[i]);
         }catch(Exception e){return null;}
      }
      return new Point((int)parsedValues[0], (int)parsedValues[1]);
   }

   public static double makeDouble(String input){
      //ignore some characters that might end up in there by accident
      char[] ignoreCharacters = {' ','(',')','{','}','[',']','|','%'};
      input = filter(input, ignoreCharacters);
      //if we now have an empty string return error
      if(input.length() == 0) return 0.0;
      double parsedValue = 0.0;
      try{
         parsedValue = Double.parseDouble(input);
      }catch(Exception e){return 0.0;}
      return parsedValue;
   }

   public static String filter(String in, char[] chars){
      for(int i=0;i<chars.length;i++){
         in = filter(in,chars[i]);
      }
      return in;
   }

   public static String filter(String in, char filterChar){
      String out = "";
      for(int i=0;i<in.length();i++){
         if(in.charAt(i) != filterChar) out += in.charAt(i);
      }
      return out;
   }

   public static String appendExtensionIfNeeded(String input, String extension){
      String output = input;
      input = input.toLowerCase();
      String lastChars = input.substring(input.length() - extension.length());
      if(!lastChars.equals(extension)) {
    	  output += extension;
      }
      return output;
   }

   public static String[] split(String in, char delimiter){
      //determine the size of the output
      int size = 1;
      for(int i=0;i<in.length();i++){
         if(in.charAt(i) == delimiter) size++;
      }
      //make output string array
      String[] out = new String[size];
      //initialize the string array
      for(int i=0;i<out.length;i++) out[i] = "";

      //put stuff in the string array
      int onIndex = 0;
      for(int i=0;i<in.length();i++){
         if(in.charAt(i) == delimiter) onIndex++;
         else out[onIndex] += in.charAt(i);
      }
      return out;
   }

   public static String minimumSize(int integer, int size){
      return minimumSize(integer+"",size,'0');
   }

   public static String minimumSize(String value, int size){
      return minimumSize(value,size,' ');
   }

   public static String minimumSize(String value, int size, char fillChar){
      String out = value;
      while(out.length() < size) out = fillChar + out;
      return out;
   }

   public static int findFirstChar(String string, char thisChar){
      for(int i=0;i<string.length();i++)
         if(string.charAt(i) == thisChar) return i;
      return -1;
   }

   public static int findLastChar(String string, char thisChar){
      for(int i=string.length()-1;i>=0;i--)
         if(string.charAt(i) == thisChar) return i;
      return -1;
   }
   public static String replace(String string, String what, String with){
      String output = "";
      for(int i=0;i<string.length();i++){
         boolean match = false;
         int j=0;
         while(true){
            if(j == what.length()){ match = true; break; }
            if(string.charAt(i+j) != what.charAt(j)){ break; }
            j++;
         }
         if(match){
            output += with;
            i += what.length()-1;
         }else{
            output += string.charAt(i);
         }
      }
      return output;
   }


}