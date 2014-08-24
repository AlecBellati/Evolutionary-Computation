import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;

public class TSPProblem {
	
	/* 2D array of floats where X is source, Y is destination and the value is cost
	 * eg TSPGraph[0][1] will return the cost of the edge connecting node 0 to node 1*/
	private double[][] TSPGraph;
	/** Alternate method for storing city information */
	private City[] cities;
	/** Number of nodes in the graph **/
	private int numVertex;
	
	/** Given a City[], generates a random individual solution */
	private Individual individual;
	/** Given an Individual object, returns a number of random solutions as a City[][] */
	private Population population;
	/** Contains the four primary mutation operators (returns modified parent object) */
	private Mutators mutators;
	/** Contains the four primary operator functions (returns children) */
	private Operators operators;
	/** Three primary selection methods for filtering solutions */
	private Selection selection;
	/** Control class to handle the GA/GP */
	private Control control;
	
	/**
	 * CONSTRUCTOR
	 * Takes in a file name and reads the supplied file
	 * Places it into City objects and also into a 2D double array
	 * @param String fileToLoad
	 */
	public TSPProblem(String fileToLoad) {
		try {
			//open file for reading
			BufferedReader br = new BufferedReader(new FileReader(fileToLoad));
			
			//construct variables
			String line;
			int row = -1;
			int col = 0;
			
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
			
    /**
    * CONSTRUCTOR
    * Takes in a file name and reads the supplied file
    * Places it into City objects and also into a 2D double array
    * @param String fileToLoad
    */
    public TSPProblem(String fileToLoad) {
        try {
            //open file for reading
            BufferedReader br = new BufferedReader(new FileReader(fileToLoad));
            
            //construct variables
            String line;
            int row = -1;
            int col = 0;
            
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
            //individual = new Individual(TSPGraph, cities);
            population = new Population(2);
            population.generateRandomSolutionSet(cities);
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
    * Basis testing function
    * Use for regression testing (DO NOT MODIFY)
    */
    private void testing(){
        printSolution(population);
    }

    /*****************************************
     *****************************************
     ****** Personal testing functions. ******
     *****************************************
     *****************************************/

    private void testingAlec(){
        printSolution(population);
    }

    private void testingMatt(){
        int solution_size = 5, population_size = 100, generations = 10000;
        double mutation_percentage = 0.75, operation_percentage = 1;
        population = control.runSequence(cities, solution_size, population_size, generations, mutation_percentage, operation_percentage, 1);
        //printSolution(population);

        //System.out.println();
        System.out.println("******************** BEST SOLUTION ********************\n");
        printSolution(population.getBestSolution());
    }

    private void testingWill(){
        /*population = new Population(2);
        Individual testI = new Individual(9);
        testI.setCity(0, new City(0, 9));
        testI.setCity(1, new City(1, 9));
        testI.setCity(2, new City(2, 9));
        testI.setCity(3, new City(3, 9));
        testI.setCity(4, new City(4, 9));
        testI.setCity(5, new City(5, 9));
        testI.setCity(6, new City(6, 9));
        testI.setCity(7, new City(7, 9));
        testI.setCity(8, new City(8, 9));

        Individual testJ = new Individual(9);
        testJ.setCity(0, new City(8, 9));
        testJ.setCity(1, new City(2, 9));
        testJ.setCity(2, new City(6, 9));
        testJ.setCity(3, new City(7, 9));
        testJ.setCity(4, new City(1, 9));
        testJ.setCity(5, new City(5, 9));
        testJ.setCity(6, new City(4, 9));
        testJ.setCity(7, new City(0, 9));
        testJ.setCity(8, new City(3, 9));

        population.setSolution(0, testI);
        population.setSolution(1, testJ);*/

        //printSolution(population);
        printInline(population.getSolution(0));
        //printInline(population.getSolution(1));
        System.out.println("");

        mutators.inversion(population.getSolution(0));
        //printSolution(control.runSequence(cities, cities.length, 2, 1));
        //Individual[] test = operators.cycleCrossover(population.getSolution(0), population.getSolution(1));
        //printInline(test[0]);
        //printInline(test[1]);

        //System.out.println(test[0].getNumCities());
        //System.out.println(test[1].getNumCities());

        printInline(population.getSolution(0));
    }

    private void testingSami(){
			printSolution(population);
    }


    /**
    * For Testing only
    * Given a City solution array, print its info.
    * @param Population results - A solution set to be printed
    */
    private void printSolution(Population result){
        for(int i = 0; i < result.getSize(); i++) {
            System.out.println("***** Solution " + (i+1) + " *****");
            Individual solution = result.getSolution(i);
            for(int j = 0; j < solution.getNumCities(); j++){
                if(j != solution.getNumCities()-1){
                    System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(j+1)));
                }else{ //return to start
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
    private void printSolution(Individual solution){
        for(int j = 0; j < solution.getNumCities(); j++){
            if(j != solution.getNumCities()-1){
                System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(j+1)));
            }else{ //return to start
                System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(0)));
            }
        }
        System.out.println("Total Cost = " + solution.getCost());
        System.out.println();
    }
    
    /**
    * TESTING PURPOSES: Print the graph (best to pipe to file)
    */
    private void printGraph() {
        for(int i = 0; i < TSPGraph.length; i++) {
            System.out.print("[");
            for(int j = 0; j < TSPGraph[i].length; j++) {
                if(j+1 == TSPGraph[i].length) {
                    System.out.print(String.format("%-8f" , TSPGraph[i][j]));
                } else {
                    System.out.print(String.format("%-8f, " , TSPGraph[i][j]));
                }
            }
            System.out.println("]\n");
        }
    }

    /**
    *
    */
    private void printInline(Individual individual){
        System.out.print("[ ");
        for(int i = 0; i < individual.getNumCities(); i++){
            System.out.print(individual.getCityByIndex(i).getNodeNum()+1 + " ");
        }
        System.out.println("]");
    }
    
    /*****************************************
     *****************************************
     ** Main method for driver setup below. **
     *****************************************
     *****************************************/
    
    /**
    * Main method for TSPProblem handles CLI and creates a new TSPProblem instance
    */
    public static void main(String[] args) {
        /* Do options here (if there ever are any) */
        
        //Read in and load file
        int fileIdx = Arrays.asList(args).lastIndexOf("-f");
        String fileToLoad = "";
        
        if(fileIdx == -1) {
            usage();
            System.exit(1);
        } else {
            fileToLoad = args[fileIdx+1];
        }
        
        TSPProblem TSPInstance = new TSPProblem(fileToLoad); 

        
        //Uncomment your testing function when needed
        //TSPInstance.testing();   
        //TSPInstance.testingAlec();
        TSPInstance.testingMatt();
        //TSPInstance.testingWill();
        //TSPInstance.testingSami();
			
			//initialise objects for solution generation, mutation and operators
			//individual = new Individual(TSPGraph, cities);
			population = new Population(2);
			population.generateRandomSolutionSet(cities);
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
	 * Basis testing function
	 * Use for regression testing (DO NOT MODIFY)
	 */
	private void testing(){
		printSolution(population);
	}
	
	/*****************************************
	 *****************************************
	 ****** Personal testing functions. ******
	 *****************************************
	 *****************************************/
	
	private void testingAlec(){
		printSolution(population);
	}
	
	private void testingMatt(){
		int solution_size = 50, population_size = 100, generations = 10000;
		double mutation_percentage = 0.75;
		population = control.runSequence(cities, solution_size, population_size, generations, mutation_percentage, 1);
		//printSolution(population);
		
		//System.out.println();
		//System.out.println("******************** BEST SOLUTION ********************\n");
		//printSolution(population.getBestSolution());
	}
	
	private void testingWill(){
		/*population = new Population(2);
		 Individual testI = new Individual(9);
		 testI.setCity(0, new City(0, 9));
		 testI.setCity(1, new City(1, 9));
		 testI.setCity(2, new City(2, 9));
		 testI.setCity(3, new City(3, 9));
		 testI.setCity(4, new City(4, 9));
		 testI.setCity(5, new City(5, 9));
		 testI.setCity(6, new City(6, 9));
		 testI.setCity(7, new City(7, 9));
		 testI.setCity(8, new City(8, 9));
		 
		 Individual testJ = new Individual(9);
		 testJ.setCity(0, new City(8, 9));
		 testJ.setCity(1, new City(2, 9));
		 testJ.setCity(2, new City(6, 9));
		 testJ.setCity(3, new City(7, 9));
		 testJ.setCity(4, new City(1, 9));
		 testJ.setCity(5, new City(5, 9));
		 testJ.setCity(6, new City(4, 9));
		 testJ.setCity(7, new City(0, 9));
		 testJ.setCity(8, new City(3, 9));
		 
		 population.setSolution(0, testI);
		 population.setSolution(1, testJ);*/
		
		//printSolution(population);
		printInline(population.getSolution(0));
		//printInline(population.getSolution(1));
		System.out.println("");
		
		mutators.inversion(population.getSolution(0));
		//printSolution(control.runSequence(cities, cities.length, 2, 1));
		//Individual[] test = operators.cycleCrossover(population.getSolution(0), population.getSolution(1));
		//printInline(test[0]);
		//printInline(test[1]);
		
		//System.out.println(test[0].getNumCities());
		//System.out.println(test[1].getNumCities());
		
		printInline(population.getSolution(0));
	}
	
	private void testingSami(){
		
		population = new Population(2);
		Individual testI = new Individual(9);
		testI.setCity(0, new City(0, 9));
		testI.setCity(1, new City(1, 9));
		testI.setCity(2, new City(2, 9));
		testI.setCity(3, new City(3, 9));
		testI.setCity(4, new City(4, 9));
		testI.setCity(5, new City(5, 9));
		testI.setCity(6, new City(6, 9));
		testI.setCity(7, new City(7, 9));
		testI.setCity(8, new City(8, 9));
		
		Individual testJ = new Individual(9);
		testJ.setCity(0, new City(8, 9));
		testJ.setCity(1, new City(2, 9));
		testJ.setCity(2, new City(6, 9));
		testJ.setCity(3, new City(7, 9));
		testJ.setCity(4, new City(1, 9));
		testJ.setCity(5, new City(5, 9));
		testJ.setCity(6, new City(4, 9));
		testJ.setCity(7, new City(0, 9));
		testJ.setCity(8, new City(3, 9));
		
		population.setSolution(0, testI);
		population.setSolution(1, testJ);
		
		printInline(population.getSolution(0));
		printInline(population.getSolution(1));
		System.out.println("");
		
		Individual[] test = operators.orderCrossover(testI, testJ);
		
		printInline(test[0]);
		printInline(test[1]);
		
	}
	
	
	/**
	 * For Testing only
	 * Given a City solution array, print its info.
	 * @param Population results - A solution set to be printed
	 */
	private void printSolution(Population result){
		for(int i = 0; i < result.getSize(); i++) {
			System.out.println("***** Solution " + (i+1) + " *****");
			Individual solution = result.getSolution(i);
			for(int j = 0; j < solution.getNumCities(); j++){
				if(j != solution.getNumCities()-1){
					System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(j+1)));
				}else{ //return to start
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
	private void printSolution(Individual solution){
		for(int j = 0; j < solution.getNumCities(); j++){
			if(j != solution.getNumCities()-1){
				System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(j+1)));
			}else{ //return to start
				System.out.println(solution.getCityByIndex(j).toString(solution.getCityByIndex(0)));
			}
		}
		System.out.println("Total Cost = " + solution.getCost());
		System.out.println();
	}
	
	/**
	 * TESTING PURPOSES: Print the graph (best to pipe to file)
	 */
	private void printGraph() {
		for(int i = 0; i < TSPGraph.length; i++) {
			System.out.print("[");
			for(int j = 0; j < TSPGraph[i].length; j++) {
				if(j+1 == TSPGraph[i].length) {
					System.out.print(String.format("%-8f" , TSPGraph[i][j]));
				} else {
					System.out.print(String.format("%-8f, " , TSPGraph[i][j]));
				}
			}
			System.out.println("]\n");
		}
	}
	
	/**
	 *
	 */
	private void printInline(Individual individual){
		System.out.print("[ ");
		for(int i = 0; i < individual.getNumCities(); i++){
			System.out.print(individual.getCityByIndex(i).getNodeNum() + " ");
		}
		System.out.println("]");
	}
	
	/*****************************************
	 *****************************************
	 ** Main method for driver setup below. **
	 *****************************************
	 *****************************************/
	
	/**
	 * Main method for TSPProblem handles CLI and creates a new TSPProblem instance
	 */
	public static void main(String[] args) {
		/* Do options here (if there ever are any) */
		
		//Read in and load file
		int fileIdx = Arrays.asList(args).lastIndexOf("-f");
		String fileToLoad = "";
		
		if(fileIdx == -1) {
			usage();
			System.exit(1);
		} else {
			fileToLoad = args[fileIdx+1];
		}
		
		TSPProblem TSPInstance = new TSPProblem(fileToLoad);
		
		
		//Uncomment your testing function when needed
		//TSPInstance.testing();
		//TSPInstance.testingAlec();
		//TSPInstance.testingMatt();
		//TSPInstance.testingWill();
		//TSPInstance.testingSami();
		
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