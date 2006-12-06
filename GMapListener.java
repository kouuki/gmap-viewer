/**
 *Interface to 
 */
interface GMapListener{
	/**Method to update GMapCompleted
	 *@param completed The new amount completed
	 */
   abstract void updateGMapCompleted(int completed);
   	/**Method to update GMaptTaskSize
   	 *@param size The new task size
   	 */
   abstract void updateGMapTaskSize(int size);
   	/**Method to update the GMapMessage
   	 *@param messageNumber The new message number
   	 */
   abstract void updateGMapMessage(int messageNumber);
   	/**Method to update GMapPainting
   	 */
   abstract void updateGMapPainting();
   	/**Method returning the asynchronous GMapStopFlag
   	 */
   abstract boolean asynchronousGMapStopFlag();

}