package lost.bot;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;

//Trivia class
public class Trivia {
	public  Timer gameTimer;
	public Boolean gameStarted;
	public Boolean gameRunning;
	public int times =0;
	public String question = "What is my name?";
	
	public Trivia(String questionFile) {
		//TODO MODIFY TO ANOTHER FUNCTION

		try {
		    int lineNumber=0;
		    FileInputStream fstream;
//			fstream = new FileInputStream("./src/intrebari.txt");
	
		    // Get the object of DataInputStream
		    DataInputStream in =  (DataInputStream) getClass().getResourceAsStream("intrebari.txt");

		    BufferedReader br = new BufferedReader(new InputStreamReader(in));
		    String line=null;
		    while ( (line = br.readLine()) != null){
		    	lineNumber++;
		    }
			System.out.println("lines!");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String [ ] args){ 
		System.out.println("Ok!");
		Trivia tr = new Trivia("abc");
		tr.StartTrivia(10);
	}
	
	public void StartTrivia(int seconds) {
		gameTimer = new Timer();
		gameTimer.scheduleAtFixedRate(new TriviaRunCheck(),seconds * 1000, seconds * 1000);
		gameStarted = true;
		gameRunning= true;
		 System.out.println("Question: " + question);
	}
	public void StopTrivia() {
		gameTimer.cancel();
		gameStarted = false;
		gameRunning= false;
	}
	
	 class TriviaRunCheck extends TimerTask {
		public void run() {
	      System.out.println("10 seconds passed..");
	      times++;
	      if (times == 2) {
	    	  System.out.println(System.currentTimeMillis()/1000 + " First hint");
	      } if (times == 4) {
	    	  System.out.println(System.currentTimeMillis()/1000 + "Second hint");
	      } if (times == 6) {
	    	  System.out.println(System.currentTimeMillis()/1000 + "Time's up.. restarting game in 10 seconds");
	    	  gameStarted = false;
	      } if (times == 7) {
	    	  times = 0;
	    	  gameStarted = true;
	    	  System.out.println(System.currentTimeMillis()/1000 + "Question: " + question);
	      }
	      //timer.cancel();
	     // System.exit(0); //Stops the AWT thread (and everything else)
	    }
	 }


}
