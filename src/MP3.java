/*************************************************************************
 *  Compilation:  javac -classpath .:jl1.0.jar MP3.java         (OS X)
 *                javac -classpath .;jl1.0.jar MP3.java         (Windows)
 *  Execution:    java -classpath .:jl1.0.jar MP3 filename.mp3  (OS X / Linux)
 *                java -classpath .;jl1.0.jar MP3 filename.mp3  (Windows)
 *  
 *  Plays an MP3 file using the JLayer MP3 library.
 *
 *  Reference:  http://www.javazoom.net/javalayer/sources.html
 *
 *
 *  To execute, get the file jl1.0.jar from the website above or from
 *
 *      http://www.cs.princeton.edu/introcs/24inout/jl1.0.jar
 *
 *  and put it in your working directory with this file MP3.java.
 *
 *************************************************************************/

import java.io.BufferedInputStream;
import java.io.FileInputStream;
/*
import javazoom.jl.player.Player;


public class MP3 {
    private String filename;
    private Player player;
    private boolean loop = true;

    // constructor that takes the name of an MP3 file
    public MP3(String filename) {
        this.filename = filename;
    }
    
    public void play() {
    	new Thread() {	//plays in background
            public void run() {
		        try {
		            do {
		                FileInputStream fis     = new FileInputStream(filename);
		            	BufferedInputStream bis = new BufferedInputStream(fis);
		            	player = new Player(bis);
		                player.play();
		            } while (loop);		//to loop music
		        }
		        catch (Exception e) {
		        	System.out.println("Problem playing file " + filename);
		            System.out.println(e);
		        }
            }
		}.start();
    }

    public void close(){
        loop = false;
        player.close();
    }
    
}*/
