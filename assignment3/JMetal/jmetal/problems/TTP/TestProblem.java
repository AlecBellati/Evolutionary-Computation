package jmetal.problems.TTP;

import java.io.File;

import java.util.Arrays;


public class TestProblem {
    
    /**
     * Constructor for the TestProblem (run everything from here to avoid issues with static
     *@param String: path/to/file.ttp to load into the problem
     */
    public TestProblem(String fileToLoad) {
        try {
            File f = new File(fileToLoad);
            
            TTP test = new TTP(f);
            
            System.out.println("numberOfNodes = " + test.numberOfNodes);
            
            //taking results from a previously run Will algorithm
            int[] tspTour = {0,1,241,242,243,240,239,238,237,236,235,234,233,232,231,230,245,244,246,249,250,229,228,227,226,225,224,223,222,221,220,219,218,217,216,215,214,213,212,211,210,209,208,207,206,205,204,203,202,201,200,197,196,195,194,193,192,191,190,189,188,187,186,185,184,183,182,181,180,175,179,178,149,177,176,150,151,155,152,154,153,128,127,126,125,29,124,123,122,121,120,119,118,156,157,158,159,174,160,161,162,163,164,165,166,167,168,169,171,170,172,173,106,105,104,103,102,101,100,99,98,97,96,95,94,93,92,91,90,89,88,108,107,109,110,113,114,116,115,85,84,83,86,112,111,87,82,81,80,79,78,77,76,74,75,73,72,71,70,69,68,67,66,65,64,63,57,56,55,54,53,52,51,50,49,48,47,46,45,44,43,58,62,61,117,60,59,42,41,40,39,38,37,36,35,34,33,32,31,30,28,27,26,25,21,24,22,23,13,14,12,11,10,9,8,7,6,5,4,3,276,275,274,273,272,271,270,15,16,17,18,19,20,129,130,131,132,133,269,268,134,135,267,266,136,137,138,148,147,146,145,144,198,199,143,142,141,140,139,265,264,263,262,261,260,259,258,257,256,253,252,251,254,255,248,247,277,278,2,279,0};
            
            int[] packingPlan = {0,1,2,3,17,18,19,20,42,48,49,50,55,58,59,60,61,62,63,64,65,66,67,83,84,85,86,105,106,107,124,125,127,129,130,138,139,140,141,142,143,165,166,180,181,209,212,213,214,215,219,220,246,247,250,252,253,254,255,256,257,260,263,276,277,278};

            
            TTPSolution soln = new TTPSolution(tspTour, packingPlan);
            
            
            //evaluate the plan
            test.evaluate(soln);
            
            soln.println();
            
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
    
    
    
    /**
     * Main function
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
        }
        
        //create a new instance of the problem
        TestProblem tp = new TestProblem(fileToLoad);
    }
    
    /**
	 * Incorrect parameter usage found - Print out usage notes for the user
	 */
	private static void usage() {
		System.out.println("Usage notes for TestProblem.java");
		System.out.println("java TestProblem <options> -f <filename>");
		System.out.println();
		System.out.println("Options:");
		System.out.println("\t-f \t Flag followed by filepath/filename.ttp");
		System.out.println();
	}
}