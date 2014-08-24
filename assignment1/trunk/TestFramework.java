import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class TestFramework {
    
    /* 2D array of floats where X is source, Y is destination and the value is cost
	 * eg TSPGraph[0][1] will return the cost of the edge connecting node 0 to node 1*/
	private double[][] TSPGraph;
	/** Alternate method for storing city information */
	private City[] cities;
    /** Number of nodes in the graph **/
    private int numVertex;
    /** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;
    
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
    
    
    /** printing constants*/
    private final String[] MUTATORNAMES = {"Insert", "Swap", "Inversion", "Scramble", "Random"};
    private final String[] OPERATORNAMES = {"OrderCrossover", "PMXCrossover", "CycleCrossover", "EdgeRecombination", "Random"};
    private final String[] SELECTORNAMES = {"Fitness", "Tournament", "Elitism", "Random"};
    
    public TestFramework(String fileToLoad) {

        rnd = new Random();

        //entry code from the TSPProblem
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
     *
     *
     */
    public void createCSV(String outputFile) {
        //comment out the functions to get data for
        //writeMutatorsOnly(outputFile);
        writeOperatorsOnly(outputFile);
    }
    
    /**
     *
     *
     */
    private void writeMutatorsOnly(String outputFile) {
        
        int solution_size = 100, generations = 10000;
        double mutation_percentage = 0;
        
        int mutator = 0, selector = 0, num_runs = 10;
        
        
        while(mutation_percentage <= 1) {
            Hashtable<String, ArrayList<Double>> allResults = new Hashtable<String, ArrayList<Double>>();
            
            //for each mutator
            for(int mut = 0; mut < MUTATORNAMES.length; mut++) {
                
                //create a new arrayList for the new mutator
                ArrayList<Double> mutatorResults = new ArrayList<Double>();
                //add the mutator percentage at the front of the list
                mutatorResults.add(0, new Double(mutation_percentage));
                
                //for each selector (index of the array)
                for(int sel = 0; sel < SELECTORNAMES.length; sel++) {
                    double best = 0;
                    //for the number of runs
                    for(int run = 0; run < num_runs; run++) {
                        
                        //run the test
                        best += mutateOnly(mut, sel, solution_size, mutation_percentage, generations);
                    }
                    
                    best /= num_runs;
                    
                    //update the ArrayList
                    mutatorResults.add((sel+1), best);
                }
                
                //add new results to the Hashtable
                allResults.put(MUTATORNAMES[mut], mutatorResults);
            }
            
            //Append new information to current file
            try {
                File file = new File(outputFile);
                BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                
                //write happens here
                Set<String> keySet = allResults.keySet();
                
                for(String s : keySet) {
                    //write header
                    output.write(s+",,,,\n");
                    
                    //write values
                    ArrayList<Double> values = allResults.get(s);
                    String line  = "";
                    for(int i = 0; i < values.size(); i++) {
                        line += Double.toString(values.get(i));
                        if((i+1) != values.size()) {
                            line += ",";
                        }
                    }
                    line += "\n";
                    output.write(line);
                    
                    //write footer
                    output.write(",,,,\n");
                }
                
                output.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            
            //increase the mutation_percentage
            mutation_percentage += 0.05;
        }
    }

    
    /**
     *
     *
     */
    private void writeOperatorsOnly(String outputFile) {
        
        int solution_size = 100, generations = 10000;
        double operation_percentage = 0;
        
        int mutator = 0, selector = 0, num_runs = 10;
        
        
        while(operation_percentage <= 1) {
            Hashtable<String, ArrayList<Double>> allResults = new Hashtable<String, ArrayList<Double>>();
            
            //for each mutator
            for(int ope = 0; ope < OPERATORNAMES.length; ope++) {
                
                //create a new arrayList for the new mutator
                ArrayList<Double> operatorResults = new ArrayList<Double>();
                //add the mutator percentage at the front of the list
                operatorResults.add(0, new Double(operation_percentage));
                
                //for each selector (index of the array)
                for(int sel = 0; sel < SELECTORNAMES.length; sel++) {
                    double best = 0;
                    //for the number of runs
                    for(int run = 0; run < num_runs; run++) {
                        
                        //run the test
                        best += operateOnly(ope, sel, solution_size, operation_percentage, generations);
                    }
                    
                    best /= num_runs;
                    
                    //update the ArrayList
                    operatorResults.add((sel+1), best);
                }
                
                //add new results to the Hashtable
                allResults.put(OPERATORNAMES[ope], operatorResults);
            }
            
            //Append new information to current file
            try {
                File file = new File(outputFile);
                BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
                
                //write happens here
                Set<String> keySet = allResults.keySet();
                
                for(String s : keySet) {
                    //write header
                    output.write(s+",,,,\n");
                    
                    //write values
                    ArrayList<Double> values = allResults.get(s);
                    String line  = "";
                    for(int i = 0; i < values.size(); i++) {
                        line += Double.toString(values.get(i));
                        if((i+1) != values.size()) {
                            line += ",";
                        }
                    }
                    line += "\n";
                    output.write(line);
                    
                    //write footer
                    output.write(",,,,\n");
                }
                
                output.close();
            } catch ( IOException e ) {
                e.printStackTrace();
            }
            
            //increase the mutation_percentage
            operation_percentage += 0.05;
        }
    }

    
    
    /**
     *
     *
     */
    private double mutateOnly(int mutator, int selector, int solution_size, double mutation_percentage, int generations) {
        Population population = new Population(solution_size);
        population.generateRandomSolutionSet(cities);
        
        
        for(int gen = 0; gen < generations; gen++) {
            Population clone = population.clone();

            for(int i = 0; i < solution_size; i++) {
                Individual individualA = clone.getSolution(i);
                
                //last option is random mutator
                if(mutator == 4) {
                    mutator = rnd.nextInt(4);
                }
                //Do a mutation mutation_percentage amount of the time
                double doMutate = rnd.nextDouble();
                if(doMutate < mutation_percentage) {
                    switch(mutator){
                        case 0:
                            //System.out.println("Insert");
                            mutators.insert(individualA);
                            break;
                        case 1:
                            //System.out.println("swap");
                            mutators.swap(individualA);
                            break;
                        case 2:
                            //System.out.println("inversion");
                            mutators.inversion(individualA);
                            break;
                        case 3:
                            //System.out.println("scramble");
                            mutators.scramble(individualA);
                            break;
                    }
                }
            }
            
            //combine clone and population and then do selection
            population.addPopulation(clone);
            
            //last option is random selection
            if(selector == 3) {
                selector = rnd.nextInt(3);
            }
            switch(selector){
                case 0:
                    //System.out.println("fitness");
                    population = selection.fitnessProportional(population, solution_size);
                    break;
                case 1:
                    //System.out.println("tournament");
                    population = selection.tournamentSelection(population, population.size(), solution_size);
                    break;
                case 2:
                    //System.out.println("elitism");
                    population = selection.elitism(population, solution_size);
                    break;
            }
        }
        
        //return the best solution from the result of the generation
        return population.getBestSolution().getCost();
    }
    
    
    /**
     *
     *
     */
    private double operateOnly(int operator, int selector, int solution_size, double operation_percentage, int generations) {
        Population population = new Population(solution_size);
        population.generateRandomSolutionSet(cities);
        
        
        for(int gen = 0; gen < generations; gen++) {
            Population children = new Population(solution_size);
            
            for(int i = 0; i < solution_size/2; i++) {
                int r1 = rnd.nextInt(solution_size);
                int r2 = rnd.nextInt(solution_size);
                //make sure they're not the same individual
                while(r1 == r2) {
                    r2 = rnd.nextInt(solution_size);
                }
                
                Individual individualA = population.getSolution(r1);
                Individual individualB = population.getSolution(r2);
                
                //last option is random mutator
                if(operator == 4) {
                    operator = rnd.nextInt(4);
                }
                //Do a mutation mutation_percentage amount of the time
                double doOperate = rnd.nextDouble();
                if(doOperate < operation_percentage) {
                    switch(operator){
                        case 0:
                            //System.out.println("orderCrossover");
                            //currently has issues
                            children.addSet(operators.orderCrossover(individualA, individualB));
                            break;
                        case 1:
                            //System.out.println("pmxCrossover");
                            children.addSet(operators.pmxCrossover(individualA, individualB));
                            break;
                        case 2:
                            //System.out.println("cycleCrossover");
                            children.addSet(operators.cycleCrossover(individualA, individualB));
                            break;
                        case 3:
                            //System.out.println("edgeRecombination");
                            children.add(operators.edgeRecombination(individualA, individualB));
                            
                            //repeat to get two children
                            int r3 = rnd.nextInt(solution_size);
                            int r4 = rnd.nextInt(solution_size);
                            while(r3 == r4) {
                                r4 = rnd.nextInt(solution_size);
                            }
                            Individual individualC = population.getSolution(r3);
                            Individual individualD = population.getSolution(r4);
                            children.add(operators.edgeRecombination(individualC, individualD));
                            
                            break;
                    }
                }
            }
            //testing
            /*System.out.println("**********CHILDREN**********");
            System.out.println("children.size = " + children.size());
            //children.print();
            
            System.out.println("**********POPULATION BEFORE**********");
            System.out.println("population.size = " + population.size());
            //population.print();
            */
            
            //combine population and children
            population.addPopulation(children);
            
            //testing
            /*System.out.println("**********POPULATION AFTER**********");
            System.out.println("population.size = " + population.size());
            population.print();
             */
            
            //last option is random selection
            if(selector == 3) {
                selector = rnd.nextInt(3);
            }
            switch(selector){
                case 0:
                    //System.out.println("fitness");
                    population = selection.fitnessProportional(population, solution_size);
                    break;
                case 1:
                    //System.out.println("tournament");
                    population = selection.tournamentSelection(population, population.size(), solution_size);
                    break;
                case 2:
                    //System.out.println("elitism");
                    population = selection.elitism(population, solution_size);
                    break;
            }
        }
        
        //return the best solution from the result of the generation
        return population.getBestSolution().getCost();
    }
    
    
    
    
    
    
    /******************************
     ******************************
     ** Main Method Starts Below **
     ******************************
     ******************************/
    
    public static void main(String[] args) {
        //Read in input file
        int inFileIdx = Arrays.asList(args).lastIndexOf("-f");
        String inputFile = "";
        
        if(inFileIdx == -1) {
            usage();
            System.exit(1);
        } else {
            inputFile = args[inFileIdx+1];
        }
        
        //read in output file
        int outFileIdx = Arrays.asList(args).lastIndexOf("-o");
        String outputFile = "";
        
        if(outFileIdx == -1) {
            usage();
            System.exit(1);
        } else {
            outputFile = args[outFileIdx+1];
        }
        
        TestFramework test = new TestFramework(inputFile);
        
        test.createCSV(outputFile);
    }
    
    /**
     * Incorrect parameter usage found - Print out usage notes for the user
     */
    private static void usage() {
        System.out.println("Usage notes for TestFramework.java");
        System.out.println("java TestFramework <options> -f <filename> -o <filename>");
        System.out.println();
        System.out.println("Options:");
        System.out.println("\t-f \t Flag followed by filepath/filename.xml of input (must be XML file)");
        System.out.println("\t-o \t Flag followed by filepath/filename.csv of output (must be CSV file)");
        System.out.println();
        System.out.println("Basic Usage: java TSPProblem -f xmlFile.xml -o output.csv");
        System.out.println();
    }
    
    
    
    
    
    
    
}