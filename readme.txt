Unzipping:
1. Extract all files from the ZIP file into a directory.

Compiling:
1. Open a Command Prompt
   a. WINDOWS: Start->Run->cmd
   b. LINUX: Open a terminal
2. cd into the directory where you unzipped all the files
   (eg. cd C:/gmap-viewer/)
3. Run 'javac *.java'.

Running:
1. Open a Command Prompt
   a. WINDOWS: Start->Run->cmd
   b. LINUX: Open a terminal
2. cd into the directory where you unzipped all the files
   (eg. cd C:/gmap-viewer/)
3. Run 'java GUI'.

Directory Structure

/images - contains all the images needed to run Google Map Viewer
/cache - default cache directory (is created when application starts running)
 /map_cache - directory where images are cached into when mode equals Map
 /sat_cache - directory where images are cached into when mode equals Satalite
 /hybid_cache - directory where images are cached into when mode equals Hybrid
 /sat_cache - 