interface GMapListener{
   abstract void updateGMapCompleted(int completed);
   abstract void updateGMapTaskSize(int size);
   abstract void updateGMapMessage(int messageNumber);
   abstract void updateGMapPainting();
   abstract boolean asynchronousGMapStopFlag();

}