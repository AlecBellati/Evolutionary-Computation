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
        population = new Population(2);
        population.generateRandomSolutionSet(cities);

            for(int i = 0; i < 2; i++){
                printInline(population.getSolution(i));
            }
            Individual[] test = operators.cycleCrossover(population.getSolution(0), population.getSolution(1));
            System.out.println();

            for(int i = 0; i < 2; i++){
                printInline(test[i]);
            }

    }

    /*****************************************
     *****************************************
     ****** Personal testing functions. ******
     *****************************************
     *****************************************/

    private void testingAlec(){
		int population_size = 50;
		int generations = 10000;
		
		population = control.runSequence(cities, 0, population_size, generations, 0, 0, 4);

        printSolution(population);
		
    }

    private void testingMatt(){
        int solution_size = 5, population_size = 10, generations = 20000;
        double mutation_percentage = 0.15, operation_percentage = 0.85;
        population = control.runSequence(cities, solution_size, population_size, generations, mutation_percentage, operation_percentage, 3);
        //printSolution(population);

        //System.out.println();
        //System.out.println("******************** BEST SOLUTION ********************\n");
        //printSolution(population.getBestSolution());
    }

    private void testingWill(){
        for(int j = 1; j < 4; j++){
        System.out.println("********** ALGORITHM " + j + " **********\n");
            int generations = 5000;

            for(int i = 1; i <= 4; i=i*2){
                int solution_size = 5, population_size = 10;
                double mutation_percentage = 0.15, operation_percentage = 0.85;
                population = control.runSequence(cities, solution_size, population_size, generations*i, mutation_percentage, operation_percentage, j);
                System.out.println("Generations: " + generations * i);
                System.out.println("Population Size: " + population_size);
                System.out.println("Mutation Percentage: " + mutation_percentage + ", Operation Percentage: " + operation_percentage);
                System.out.println("BEST RESULT = " + population.getBestSolution().getCost());
                System.out.println();
            }

            /*
            for(int i = 1; i <= 4; i=i*2){
                avg = 0;
                int solution_size = 10, population_size = 20;
                double mutation_percentage = 0.15, operation_percentage = 0.85;
                System.out.println("Generations: " + generations * i);
                System.out.println("Population Size: " + population_size);
                System.out.println("Mutation Percentage: " + mutation_percentage + ", Operation Percentage: " + operation_percentage);
                System.out.println();
                for(int x = 0; x < 10; x++){
                    population = control.runSequence(cities, solution_size, population_size, generations, mutation_percentage, operation_percentage, j);
                    System.out.println("BEST RESULT = " + population.getBestSolution().getCost());

                    avg += population.getBestSolution().getCost();
                }
                System.out.println("AVERAGE = " + avg/10);
                System.out.println();
            }

            for(int i = 1; i <= 4; i=i*2){
                avg = 0;
                int solution_size = 25, population_size = 50;
                double mutation_percentage = 0.15, operation_percentage = 0.85;
                System.out.println("Generations: " + generations * i);
                System.out.println("Population Size: " + population_size);
                System.out.println("Mutation Percentage: " + mutation_percentage + ", Operation Percentage: " + operation_percentage);
                System.out.println();
                for(int x = 0; x < 10; x++){
                    population = control.runSequence(cities, solution_size, population_size, generations, mutation_percentage, operation_percentage, j);
                    System.out.println("BEST RESULT = " + population.getBestSolution().getCost());
                
                    avg += population.getBestSolution().getCost();
                }
                System.out.println("AVERAGE = " + avg/10);
                System.out.println();
            }

            for(int i = 1; i <= 4; i=i*2){
                avg = 0;   
                int solution_size = 50, population_size = 100;
                double mutation_percentage = 0.15, operation_percentage = 0.85; 
                System.out.println("Generations: " + generations * i);
                System.out.println("Population Size: " + population_size);
                System.out.println("Mutation Percentage: " + mutation_percentage + ", Operation Percentage: " + operation_percentage);
                System.out.println();
                for(int x = 0; x < 10; x++){
                    population = control.runSequence(cities, solution_size, population_size, generations, mutation_percentage, operation_percentage, j);
                    System.out.println("BEST RESULT = " + population.getBestSolution().getCost());
                
                    avg += population.getBestSolution().getCost();
                }
                System.out.println("AVERAGE = " + avg/10);
                System.out.println();
            }*/
        }
        
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
        TSPInstance.testingAlec();
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