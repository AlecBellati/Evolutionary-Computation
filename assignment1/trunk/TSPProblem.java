/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;

public class TSPProblem {
	
	/* Class variables */
	private double[][] TSPGraph;		// 2D array of floats: X source, Y dest, value cost
	private City[] cities;				// Stores city information
	private int numVertex;				// Number of nodes in the graph
	private Individual individual;		// Generates random individual solution
	private Population population;		// Returns random solutions as a City[][]
	private Mutators mutators;			// Contains 4 mutation operators
	private Operators operators;		// Contains 4 operator functions
	private Selection selection;		// Contains 3 selection methods
	private Control control;			// Control class to handle the GA/GP
	
	/**
	 * Constructor
	 * Takes in a file name and reads the supplied file
	 * Places it into City objects and also into a 2D double array
	 * @param String fileToLoad
	 */
    public TSPProblem(String fileToLoad) {
        try {
            //write name of file to output file
            File file = new File("results.csv");
            BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
            String strOut = fileToLoad + ":,,\n";
            output.write(strOut);
            output.close();
            
            System.out.println(fileToLoad);
            
            
            //open file for reading
            BufferedReader br = new BufferedReader(new FileReader(fileToLoad));
            
            //construct variables
            String line;
            int row = -1;
            int col = 0;
            
            //Yes this is hacky - Markus said it was ok so bad luck :)
            if(fileToLoad.contains("pcb442")) {
                numVertex = 442;
                
                TSPGraph = new double[numVertex][numVertex];
                cities = new City[numVertex];
            }
            
            //read the XML file and parse into TSPGraph[][]
            while((line = br.readLine()) != null) {
                
                //get number of nodes in graph
                if(line.contains("-city problem")) {
                    String[] firstCut = line.split("-");
                    String[] getNum = firstCut[0].split(">");
                    numVertex = Integer.parseInt(getNum[1]);
                    
                    
                    TSPGraph = new double[numVertex][numVertex];
                    cities = new City[numVertex];
                }
                
                //update for nodes
                if(line.contains("<vertex>")) {
                    row++;
                    col = 0;
                    
                    cities[row] = new City(row, numVertex);
                }
                
                //update for edges
                if(line.contains("<edge")) {
                    //get cost
                    String[] split = line.split("\"");
                    double cost = Double.parseDouble(split[1]);
                    
                    //get destNode
                    String[] destSplit = line.split(">");
                    String[] getDest = destSplit[1].split("<");
                    int node = Integer.parseInt(getDest[0]);
                    
                    //update graph
                    TSPGraph[row][node] = cost;
                    
                    //update cities
                    cities[row].addEdge(node, cost);
                    
                    //go to next city location
                    col++;
                }
            }
            br.close();
            
			//initialise objects for solution generation, mutation and operators
            mutators = new Mutators();
            operators = new Operators();
            selection = new Selection();
            control = new Control();
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
	
	/**
	 * Basic testing function
	 * Will run Algorithm 1 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private void testingAlgorithm1(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		individual = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 1);
		
		printSolution(individual);
	}

	/**
	 * Basic testing function
	 * Will run Algorithm 2 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private void testingAlgorithm2(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		individual = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 2);
		
		printSolution(individual);
	}

	/**
	 * Basic testing function
	 * Will run Algorithm 3 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private void testingAlgorithm3(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		individual = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 3);
		
		printSolution(individual);
	}

	/**
	 * Basic testing function
	 * Will run Inver-Over algorithm and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private void testingInverOver(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		individual = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 4);
		
		printSolution(individual);
	}
	
	/**
	 * For Testing only
	 * Given a Population, print its info.
	 * @param Population results - A solution set to be printed
	 */
	private void printSolution(Population result) {
		for (int i = 0; i < result.getSize(); i++) {
			System.out.println("***** Solution " + (i+1) + " *****");
			Individual solution = result.getSolution(i);
			for (int j = 0; j < solution.getNumCities(); j++) {
				if (j != solution.getNumCities()-1) {
					System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(j+1)));
				} else {
					// Return to start
					System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(0)));
				}
			}
			System.out.println("Total Cost = " + solution.getCost());
			System.out.println();
		}
	}
	
	/**
	 * For Testing only
	 * Given a City solution array (Individual), print its info.
	 * @param Individual solution - A solution set to be printed
	 */
	private void printSolution(Individual solution) {
		for (int j = 0; j < solution.getNumCities(); j++) {
			if (j != solution.getNumCities()-1) {
				System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(j+1)));
			} else {
				// Return to start
				System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(0)));
			}
		}
		System.out.println("Total Cost = " + solution.getCost());
		System.out.println();
	}
	
	/**
	 * Print an Individual solution in-line
	 * Easier to compare solutions in this format (for debugging)
	 * @param Individual solution - A solution set to be printed
	 */
	private void printInline(Individual individual) {
		System.out.print("[ ");
		for (int i = 0; i < individual.getNumCities(); i++) {
			System.out.print(individual.getCityByIndex(i).getNodeNum() + " ");
		}
		System.out.println("]");
	}
	
	/**
	 * Main method for TSPProblem handles CLI and creates a new TSPProblem instance
	 */
	public static void main(String[] args) {
		
		// Read in and load file
		int fileIdx = Arrays.asList(args).lastIndexOf("-f");
		String fileToLoad = "";
		
		if(fileIdx == -1) {
			usage();
			System.exit(1);
		} else {
			fileToLoad = args[fileIdx+1];
		}
		
		TSPProblem TSPInstance = new TSPProblem(fileToLoad);
		TSPInstance.testingAlgorithm1(50, 10000);
		TSPInstance.testingAlgorithm2(50, 10000);
		TSPInstance.testingAlgorithm3(50, 10000);
		TSPInstance.testingInverOver(50, 10000);
	}
	
	/**
	 * Incorrect parameter usage found - Print out usage notes for the user
	 */
	private static void usage() {
		System.out.println("Usage notes for TSPProblem.java");
		System.out.println("java TSPProblem <options> -f <filename>");
		System.out.println();
		System.out.println("Options:");
		System.out.println("\t-f \t Flag followed by filepath/filename.xml of input (must be XML file)");
		System.out.println();
		System.out.println("Basic Usage: java TSPProblem -f xmlFile.xml");
		System.out.println();
	}
}