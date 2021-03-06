/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment Two
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

import java.io.File;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;

import TTP.TTPInstance;

public class Driver {
    
    //TTPInstance instance that creates a parsed *.ttp file
    private TTPInstance ttp;
    
    //counter for the timer
    private int counter = 0;
    
    //constant variables
    //ten minutes is slightly shortened to account for setup and output to file - use 596000 to account for file read and write times
    private static final long TENMINUTES = 596000;
    
    /**
     * CONSTRUCTOR
     * @param: String: path/to/file.ttp to load and solve
     */
    public Driver(String fileToLoad, int choice) {
        //start new timer
        createTimer();
        
        //create new TTPInstance
        File f = new File(fileToLoad);
        ttp = new TTPInstance(f);
        
        //get the solution
        ttp.run(choice);
        
        System.out.println("Driver: Program Finished Early, Exiting Program...");
        System.exit(1);
    }
    
    /**
     * Timer thread for program.
     * Markus OK'd the use of a second thread - ONLY FOR TIMING!
     * Timer waits
     */
    void createTimer() {
        //create new timer
        TimerTask timerTask = new TimerTask() {
            
            @Override
            public void run() {
                //The thread gets called once at the start
                if(counter == 0) {
                    System.out.println("Starting Timer");
                    
                //after the initial call, output results and kill the program
                }else if(counter == 2){
                    System.out.println("\nSolution after two minutes:");
                    ttp.getBestSolution(true);
                }else if(counter == 5){
                    System.out.println("\nSolution after five minutes:");
                    ttp.getBestSolution(true);
                }else if(counter == 10){
                    System.out.println("\nDriver: Timer Has Expired");
                    ttp.getBestSolution(false);
                    System.out.println("Driver: Exiting Program");
                    System.exit(1);
                }
                counter++;
            }
        };
        
        Timer timer = new Timer("MyTimer");
        timer.scheduleAtFixedRate(timerTask, 0, 60000);
    }
    
    
    
    
    /**
     *Main function
     */
    public static void main(String[] args) {
        
        //Read in and load file
        int fileIdx = Arrays.asList(args).lastIndexOf("-f");
        String fileToLoad = "";
        
        int choice = 0;
        if(fileIdx == -1) {
            usage();
            System.exit(1);
        } else {
            fileToLoad = args[fileIdx+1];
            if(args.length == 3){
                choice = Integer.parseInt(args[2]);
            }
        }
        
        //create a new instance of the problem
        Driver driver = new Driver(fileToLoad, choice);
        
        
    }
    
    /**
	 * Incorrect parameter usage found - Print out usage notes for the user
	 */
	private static void usage() {
		System.out.println("Usage notes for Driver.java");
		System.out.println("java Driver <options> -f <filename> [1-9]");
		System.out.println();
		System.out.println("Options:");
		System.out.println("\t-f \t Flag followed by filepath/filename.xml of input (must be XML file)");
		System.out.println();
		System.out.println("Basic Usage: java Driver -f ttpFile.ttp");
		System.out.println();
	}
}