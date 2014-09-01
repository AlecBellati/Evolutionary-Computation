import java.io.File;

import java.util.Arrays;

import ttp.TTPInstance;

public class Driver {
    
    //TTPInstance instance that creates a parsed *.ttp file
    private TTPInstance ttp;
    
    /**
     * CONSTRUCTOR
     * @params: String: path/to/file.ttp to load and solve
     */
    public Driver(String fileToLoad) {
        //create new TTPInstance
        File f = new File(fileToLoad);
        ttp = new TTPInstance(f);
        
        //print the instance to ensure it's parsed correctly
        ttp.printInstance();
    }
    
    
    
    
    /**
     *Main function
     */
    public static void main(String[] args) {
        
        //Read in and load file
        int fileIdx = Arrays.asList(args).lastIndexOf("-f");
        String fileToLoad = "";
        
        if(fileIdx == -1) {
            usage();
            System.exit(1);
        } else {
            fileToLoad = args[fileIdx+1];
            
        }
        
        //create a new instance of the problem
        Driver driver = new Driver(fileToLoad);
    }
    
    /**
	 * Incorrect parameter usage found - Print out usage notes for the user
	 */
	private static void usage() {
		System.out.println("Usage notes for Driver.java");
		System.out.println("java Driver <options> -f <filename>");
		System.out.println();
		System.out.println("Options:");
		System.out.println("\t-f \t Flag followed by filepath/filename.xml of input (must be XML file)");
		System.out.println();
		System.out.println("Basic Usage: java Driver -f ttpFile.ttp");
		System.out.println();
	}
}