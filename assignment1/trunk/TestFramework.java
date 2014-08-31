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
import java.util.Hashtable;
import java.util.Random;
import java.util.Set;

public class TestFramework {
	
	/* Class variables */
	private double[][] TSPGraph;		// 2D array of floats: X source, Y dest, value is cost.
	private City[] cities;					// Stores city information
	private int numVertex;					// Number of nodes in the graph
	private Random rnd;							// Random number generator
	private Individual individual;	// Generates random individual solution
	private Population population;	// Returns a number of random solutions as a City[][]
	private Mutators mutators;			// Contains 4 primary mutation operators
	private Operators operators;		// Contains 4 primary operator functons
	private Selection selection;		// Contains 3 primary selection methods
	private Control control;				// Control class to handle the GA/GP
	
	/* Printing constants */
	private final String[] MUTATORNAMES = {"Insert", "Swap", "Inversion", "Scramble", "Random"};
	private final String[] OPERATORNAMES = {"OrderCrossover", "PMXCrossover", "CycleCrossover", "EdgeRecombination", "Random"};
	private final String[] SELECTORNAMES = {"Fitness", "Tournament", "Elitism", "Random"};
	
	public TestFramework(String fileToLoad) {
		
		rnd = new Random();
		
		// Entry code from the TSPProblem
		try {
			// Open file for reading
			BufferedReader br = new BufferedReader(new FileReader(fileToLoad));
			
			// Construct variables
			String line;
			int row = -1;
			int col = 0;
			
			// Read the XML file and parse into TSPGraph[][]
			while ((line = br.readLine()) != null) {
				
				// Get number of nodes in graph
				if (line.contains("-city problem")) {
					String[] firstCut = line.split("-");
					String[] getNum = firstCut[0].split(">");
					numVertex = Integer.parseInt(getNum[1]);
					
					TSPGraph = new double[numVertex][numVertex];
					cities = new City[numVertex];
				}
				
				// Update for nodes
				if (line.contains("<vertex>")) {
					row++;
					col = 0;
					
					cities[row] = new City(row, numVertex);
				}
				
				// Update for edges
				if (line.contains("<edge")) {
					// Get cost
					String[] split = line.split("\"");
					double cost = Double.parseDouble(split[1]);
					
					// Get destNode
					String[] destSplit = line.split(">");
					String[] getDest = destSplit[1].split("<");
					int node = Integer.parseInt(getDest[0]);
					
					// Update graph
					TSPGraph[row][node] = cost;
					
					// Update cities
					cities[row].addEdge(node, cost);
					
					// Go to next city location
					col++;
				}
			}
			br.close();
			
			// Initialise objects for solution generation, mutation and operators
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
	 * Exports data to CSV.
	 */
	public void createCSV(String outputFile) {
		writeMutatorsOnly(outputFile);
		writeOperatorsOnly(outputFile);
	}
	
	/**
	 * Writes the mutator results only out to file.
	 */
	private void writeMutatorsOnly(String outputFile) {
		
		int solutionSize = 100, generations = 10000;
		double mutationPercentage = 0;
		
		int mutator = 0, selector = 0, numRuns = 10;
		
		
		while (mutationPercentage <= 1) {
			Hashtable<String, ArrayList<Double>> allResults = new Hashtable<String, ArrayList<Double>>();
			
			// For each mutator
			for(int mut = 0; mut < MUTATORNAMES.length; mut++) {
				
				// Create a new arrayList for the new mutator
				ArrayList<Double> mutatorResults = new ArrayList<Double>();
				// Add the mutator percentage at the front of the list
				mutatorResults.add(0, new Double(mutationPercentage));
				
				// For each selector for each run, run the test.
				for (int sel = 0; sel < SELECTORNAMES.length; sel++) {
					double best = 0;
					for (int run = 0; run < numRuns; run++) {
						best += mutateOnly(mut, sel, solutionSize, mutationPercentage, generations);
					}
					
					best /= numRuns;
					
					// Update the ArrayList
					mutatorResults.add((sel+1), best);
				}
				
				// Add new results to the Hashtable
				allResults.put(MUTATORNAMES[mut], mutatorResults);
			}
			
			// Append new information to current file
			try {
				File file = new File(outputFile);
				BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
				
				// Write happens here
				Set<String> keySet = allResults.keySet();
				
				for (String s : keySet) {
					// Write header
					output.write(s+",,,,\n");
					
					// Write values
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
					
					// Write footer
					output.write(",,,,\n");
				}
				
				output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			// Increase the mutationPercentage
			mutationPercentage += 0.05;
		}
	}
	
	
	/**
	 * Write the operators only out to a file.
	 */
	private void writeOperatorsOnly(String outputFile) {
		
		int solutionSize = 100, generations = 10000;
		double operation_percentage = 0;
		
		int mutator = 0, selector = 0, numRuns = 10;
		
		while (operation_percentage <= 1) {
			Hashtable<String, ArrayList<Double>> allResults = new Hashtable<String, ArrayList<Double>>();
			
			// For each mutator
			for(int ope = 0; ope < OPERATORNAMES.length; ope++) {
				
				// Create a new arrayList for the new mutator
				ArrayList<Double> operatorResults = new ArrayList<Double>();
				// Add the mutator percentage at the front of the list
				operatorResults.add(0, new Double(operation_percentage));
				
				// For each selector for each run, run the test
				for (int sel = 0; sel < SELECTORNAMES.length; sel++) {
					double best = 0;
					for (int run = 0; run < numRuns; run++) {
						best += operateOnly(ope, sel, solutionSize, operation_percentage, generations);
					}
					
					best /= numRuns;
					
					// Update the ArrayList
					operatorResults.add((sel+1), best);
				}
				
				// Add new results to the Hashtable
				allResults.put(OPERATORNAMES[ope], operatorResults);
			}
			
			// Append new information to current file
			try {
				File file = new File(outputFile);
				BufferedWriter output = new BufferedWriter(new FileWriter(file, true));
				
				// Write happens here
				Set<String> keySet = allResults.keySet();
				
				for (String s : keySet) {
					// Write header
					output.write(s+",,,,\n");
					
					// Write values
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
					
					// Write footer
					output.write(",,,,\n");
				}
				
				output.close();
			} catch ( IOException e ) {
				e.printStackTrace();
			}
			
			// Increase the mutationPercentage
			operation_percentage += 0.05;
		}
	}
	
	
	
	/**
	 * Generator a solution for mutation.
	 */
	private double mutateOnly(int mutator, int selector, int solutionSize, double mutationPercentage, int generations) {
		Population population = new Population(solutionSize);
		population.generateRandomSolutionSet(cities);
		
		for (int gen = 0; gen < generations; gen++) {
			Population clone = population.clone();
			
			for (int i = 0; i < solutionSize; i++) {
				Individual individualA = clone.getSolution(i);
				
				// Last option is random mutator
				if (mutator == 4) {
					mutator = rnd.nextInt(4);
				}
				// Do a mutation mutationPercentage amount of the time
				double doMutate = rnd.nextDouble();
				if (doMutate < mutationPercentage) {
					switch (mutator){
						case 0:
							mutators.insert(individualA);
							break;
						case 1:
							mutators.swap(individualA);
							break;
						case 2:
							mutators.inversion(individualA);
							break;
						case 3:
							mutators.scramble(individualA);
							break;
					}
				}
			}
			
			// Combine clone and population and then do selection
			population.addPopulation(clone);
			
			// Last option is random selection
			if(selector == 3) {
				selector = rnd.nextInt(3);
			}
			switch (selector){
				case 0:
					population = selection.fitnessProportional(population, solutionSize);
					break;
				case 1:
					population = selection.tournamentSelection(population, population.size(), solutionSize);
					break;
				case 2:
					population = selection.elitism(population, solutionSize);
					break;
			}
		}
		
		// Return the best solution from the result of the generation
		return population.getBestSolution().getCost();
	}
	
	
	/**
	 * Run operators only.
	 */
	private double operateOnly(int operator, int selector, int solutionSize, double operation_percentage, int generations) {
		Population population = new Population(solutionSize);
		population.generateRandomSolutionSet(cities);
		
		
		for (int gen = 0; gen < generations; gen++) {
			Population children = new Population(solutionSize);
			
			for (int i = 0; i < solutionSize/2; i++) {
				int r1 = rnd.nextInt(solutionSize);
				int r2 = rnd.nextInt(solutionSize);
				// Make sure they're not the same individual
				while(r1 == r2) {
					r2 = rnd.nextInt(solutionSize);
				}
				
				Individual individualA = population.getSolution(r1);
				Individual individualB = population.getSolution(r2);
				
				// Last option is random mutator
				if (operator == 4) {
					operator = rnd.nextInt(4);
				}
				// Do a mutation mutationPercentage amount of the time
				double doOperate = rnd.nextDouble();
				if (doOperate < operation_percentage) {
					switch (operator){
						case 0:
							children.addSet(operators.orderCrossover(individualA, individualB));
							break;
						case 1:
							children.addSet(operators.pmxCrossover(individualA, individualB));
							break;
						case 2:
							children.addSet(operators.cycleCrossover(individualA, individualB));
							break;
						case 3:
							children.add(operators.edgeRecombination(individualA, individualB));
							
							// Repeat to get two children
							int r3 = rnd.nextInt(solutionSize);
							int r4 = rnd.nextInt(solutionSize);
							
							// Make sure they're not the same individual
							while (r3 == r4) {
								r4 = rnd.nextInt(solutionSize);
							}
							Individual individualC = population.getSolution(r3);
							Individual individualD = population.getSolution(r4);
							children.add(operators.edgeRecombination(individualC, individualD));
							
							break;
					}
				}
			}
			
			// Combine population and children
			population.addPopulation(children);
			
			// Last option is random selection
			if(selector == 3) {
				selector = rnd.nextInt(3);
			}
			
			switch (selector){
				case 0:
					population = selection.fitnessProportional(population, solutionSize);
					break;
				case 1:
					population = selection.tournamentSelection(population, population.size(), solutionSize);
					break;
				case 2:
					population = selection.elitism(population, solutionSize);
					break;
			}
		}
		
		// Return the best solution from the result of the generation
		return population.getBestSolution().getCost();
	}
	
	/**
	 * Main method.
	 */
	public static void main(String[] args) {
		// Read in input file
		int inFileIdx = Arrays.asList(args).lastIndexOf("-f");
		String inputFile = "";
		
		if (inFileIdx == -1) {
			usage();
			System.exit(1);
		} else {
			inputFile = args[inFileIdx+1];
		}
		
		// Read in output file
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