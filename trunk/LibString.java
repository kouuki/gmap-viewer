import java.awt.*;


class LibString{
  static char[] hex = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};

   /**
   * Creates a color from the hex string
   *
   * @param hexColor a string a hex values used to specify a new color
   * @return Color returns a Color object
   */
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

   /**
   * Creates a hex string from the color object
   *
   * @param color the color from which to construct the string
   * @return String the string representation of the color object
   */
   public static String makeColorString(Color color){
      if(color == null) return "";
      int r = color.getRed();
      int g = color.getGreen();
      int b = color.getBlue();
      return minimumSize(intToHex(r)+"",2,'0')+minimumSize(intToHex(g)+"",2,'0')+minimumSize(intToHex(b)+"",2,'0');
   }

   /**
   * returns a string of arbitrary length expressing this decimal in hex
   *
   * @param decimal the integer value to turn into a hex value
   * @return String the hex string representing the integer value
   */
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

   /**
   * returns an integer representation of the hex value
   *
   * @param hexString the hex string to turn into an integer value
   * @return Int the integer representation of the hex string
   */
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

   /**
   * converts a hex value into an integer value
   * @param hexChar the hex character to be converted
   * @return Int the integer value representing the hex character
   */
   public static int hexToInt(char hexChar){
      for(int i=0;i<hex.length;i++){
         if(hex[i] == hexChar) return i;
      }
      return 0;
   }

   /**
   * Creates a GPhysicalPoint from a String input
   * @param input the string input containing the data for the GPhysicalPoint
   * @return GPhysicalPoint the GPhysicalPoint represented by the data in the input string
   */
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

   /**
   * Creates a Point object from the string input
   * @param input the string containing the data for a Point object
   * @return Point the point defined by the data in the input string
   */
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

   /**
   * Converts a string input into a Double value
   * @param input the string input that is to be converted into a Double
   * @return double the Double value representation of the string input
   */
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

   /**
   * Passes each piece of a character array into the filter method
   * @param in the string to be filtered
   * @param chars a character array containing the characters to be filtered out
   * @return String the filtered string
   */
   public static String filter(String in, char[] chars){
      for(int i=0;i<chars.length;i++){
         in = filter(in,chars[i]);
      }
      return in;
   }

   /**
   * Filters a string one character at a time
   * @param in the string to be filtered
   * @param filterChar the character to be filtered out
   * @return String the filtered string
   */
   public static String filter(String in, char filterChar){
      String out = "";
      for(int i=0;i<in.length();i++){
         if(in.charAt(i) != filterChar) out += in.charAt(i);
      }
      return out;
   }

   /**
   * Adds the appropriate extension, if necessary
   * @param input the initial string
   * @param extension the extension as a string
   * @return String the string with the extension attached
   */
   public static String appendExtensionIfNeeded(String input, String extension){
      String output = input;
      input = input.toLowerCase();
      String lastChars = input.substring(input.length() - extension.length());
      if(!lastChars.equals(extension)) {
        output += extension;
      }
      return output;
   }

   /**
   * Splits a string each time a delimiter is found
   * @param in the string to be split
   * @param delimiter the character used as the delimiter
   * @return String[] the array of strings resulting from the split
   */
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

   /**
   * Extends the length of the string representing a number to a specified size
   * @param integer the integer representation of the number to be converted to a string
   * @param size the minimum length of the desired string
   * @return String the string of the specified length representating the integer
   */
   public static String minimumSize(int integer, int size){
      return minimumSize(integer+"",size,'0');
   }

   /**
   * Extends the length of the string representing a number to a specified size
   * @param value the string representation of the number to be converted to a longer string
   * @param size the minimum length of the desired string
   * @return String the string of the specified length representating the shorter string
   */
   public static String minimumSize(String value, int size){
      return minimumSize(value,size,' ');
   }

   /**
   * Extends the length of the string representing a number to a specified size
   * @param value the string representation of the number to be converted to a longer string
   * @param size the minimum length of the desired string
   * @param fillChar the character to be appended to the end of the provided string to extend
   * the final string length
   * @return String the string of the specified length representating the integer
   */
   public static String minimumSize(String value, int size, char fillChar){
      String out = value;
      while(out.length() < size) out = fillChar + out;
      return out;
   }

   /**
   * Finds the first instance of a specified character
   * @param string the string to search for the specified character
   * @param thisChar the character for which to search in string
   * @return int the index of the first instance of thisChar (or -1 if not found)
   */
   public static int findFirstChar(String string, char thisChar){
      for(int i=0;i<string.length();i++)
         if(string.charAt(i) == thisChar) return i;
      return -1;
   }

   /**
   * Finds the last instance of a specified character
   * @param string the string to search for the specified character
   * @param thisChar the character for which to search in string
   * @return int the index of the last instance of thisChar (or -1 if not found)
   */
   public static int findLastChar(String string, char thisChar){
      for(int i=string.length()-1;i>=0;i--)
         if(string.charAt(i) == thisChar) return i;
      return -1;
   }

   /**
   * Replaces all instance of a string with another string.
   * @param string the string to be searched
   * @param string the string to be replaced
   * @param string the replacement string
   */
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