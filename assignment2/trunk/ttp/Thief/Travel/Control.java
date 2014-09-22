/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
package TTP.Thief.Travel;


import TTP.Thief.Knapsack;

import java.util.Random;
import java.util.ArrayList;

public class Control {

	/** Class variables */
	private Random rnd;					// Random number generator
	private Mutators mutator;			// Contains 4 mutation operators
	private Operators operator;			// Contains 4 operator functions
	private Selection selection;		// Contains 3 selection functions
	private Individual bestSolution;	// Holds the best solution for current algorithm
    
    //TTP Variables
    private double[][] TTPGraph;
    private double minSpeed, maxSpeed;
    private Knapsack knapsack;
	
	/**
	 * Constructor
	 * Initialises the mutator, operator and selection objects
	 */
	public Control() {
		rnd = new Random();
		mutator = new Mutators();
		operator = new Operators();
		selection = new Selection();
	}
    
    /**
     * TTP Control Constructor
     * @param: double[][]: graph of edges between nodes
     * @param: double: maxSpeed of thief
     * @param: double: minSpeed of thief
     * @param: Knapsack: knapsack of the thief
     */
    public Control(double[][] _TTPGraph, double _maxSpeed, double _minSpeed, Knapsack _knapsack) {
        TTPGraph = _TTPGraph;
        maxSpeed = _maxSpeed;
        minSpeed = _minSpeed;
        knapsack = _knapsack;
        
        rnd = new Random();
		mutator = new Mutators();
		operator = new Operators();
		selection = new Selection();
    }
	
	/**
	 * Determines what algorithm to be used based on the supplied "algorithm" number
	 * Passes over relevant data to the specified algorithm
	 * @param City[] - the cites to perform the algorithm on
	 * @param int solutionSize - population to begin each generation (cut down at selection)
	 * @param int poplationSize - population size at the end of a generation (before selection)
	 * @param int generations - number of times to perform the mutation/operation cycles
	 * @param double mutationPercentage - chances of mutation occuring
	 * @param double operationPercentage - chances of an operation occuring
	 * @param int removalRate - determines how many duplicate or similar solutions are removed from a population
	 * @param int algorithm - which algorithm to use (1-5)
	 * @return Individual - the best individual from the populations generated
	 */
	public Individual runSequence(City[] cities, int solutionSize, int populationSize, int generations, double mutationPercentage, double operationPercentage, int removalRate, int algorithm) {
		Population population = new Population(solutionSize);
		population.generateRandomSolutionSet(cities);
		
		switch (algorithm) {
			case 1:
				return algorithm1(population, solutionSize, populationSize, mutationPercentage, operationPercentage, generations, removalRate);
			case 2:
				return algorithm2(population, solutionSize, populationSize, generations, removalRate);
			case 3:
				return algorithm3(population, solutionSize, populationSize, mutationPercentage, operationPercentage, generations);
			case 4:
				return inverOver(cities, population, populationSize, generations);
            case 5:
                return algorithm3TTP(population, solutionSize, populationSize, mutationPercentage, operationPercentage, generations);
		}
		return null;
	}
	
		/**
	 * Simulates an algorithm something similar to that of the GA in the book/lecture
	 * @param Population - the TSPGraph to perform the algorithm on
	 * @param int solutionSize - population to begin each generation (cut down at selection)
	 * @param int poplation_size - population size at the end of a generation (before selection)
	 * @param int generations - number of times to perform the mutation/operation cycles
	 * @param double mutationPercentage - chances of mutation occuring
	 * @param double operationPercentage - chances of an operation occuring
	 * @param int removalRate - determines how many duplicate or similar solutions are removed from a population
	 * @return Individual - the best individual from the populations generated
	 */
	public Individual algorithm1(Population population, int solutionSize, int populationSize, double mutationPercentage, double operationPercentage, int generations, int removalRate) {
		Individual individualA;
		Individual individualB;
		bestSolution = population.getBestSolution().clone();

		int runs = populationSize/5;
		int rand = 0;
		for (int i = 0; i < generations; i++) {
			Individual best = population.getBestSolution().clone();

			individualA = population.getBestSolution();
			individualB = population.getSolution(rnd.nextInt(population.getSize()));
            for(int j = 0; j < runs; j++){				
	            //check the best solution
				checkBest(i, population.getBestSolution());
	            //if the population size is one off the maximum do edge recombination (produces 1 child)
				if (population.getSize() == (populationSize-1)) {
					rand = 1;
				} else {
	                //else do crossovers at random
					rand = rnd.nextInt(4);
				}
					
	            //do cross-overs based on the operation percentage and add the children to the population
				double operate = rnd.nextDouble();
				if (operate < operationPercentage) {
					switch (rand) {
						case 0:
							population.addSet(operator.orderCrossover(individualA, individualB));
							break;
						case 1:
								population.add(operator.edgeRecombination(individualA, individualB));
							break;
						case 2:
							population.addSet(operator.cycleCrossover(individualA, individualB));
								break;
						case 3:
							population.addSet(operator.pmxCrossover(individualA, individualB));
							break;
					}
				}else{
					
	            //choose mutators at random
				rand = rnd.nextInt(4);
	            //do cross-overs based on the mutation percentage on the selected individuals (may have been operated on already)
					switch (rand) {
						case 0:
							mutator.insert(individualA);
							mutator.insert(individualB);
							break;
						case 1:
							mutator.swap(individualA);
							mutator.swap(individualB);
							break;
						case 2:
							mutator.inversion(individualA);
							mutator.inversion(individualB);
							break;
						case 3:
							mutator.scramble(individualA);
							mutator.scramble(individualB);
							break;
					}
				}
				individualA = population.getSolution(rnd.nextInt(population.getSize()));
				individualB = population.getSolution(rnd.nextInt(population.getSize()));
			}
			
			population.add(best);
			rand = 0;
	        //choose the selection method based on percentage
			double select = rnd.nextDouble();
			if (select < 0.55) {
				rand = 2;
			} else if(select < 0.99) {
				rand = 1;
			}
				
	        //remove only a few duplicates from each set
			population = checkDuplicates(population, removalRate);
			switch (rand) {
				case 0:
					population = selection.fitnessProportional(population, solutionSize);
					break;
				case 1:
					population = selection.tournamentSelection(population, populationSize, solutionSize);
					break;
				case 2:
					population = selection.elitism(population, solutionSize);
					break;
			}
		}
		return bestSolution;
	}
	
	/**
	 * Simulates an algorithm something similar to that of the GA in the book/lecture
	 * @param Population - the TSPGraph to perform the algorithm on
	 * @param int solutionSize - population to begin each generation (cut down at selection)
	 * @param int poplation_size - population size at the end of a generation (before selection)
	 * @param int generations - number of times to perform the mutation/operation cycles
	 * @param int removalRate - determines how many duplicate or similar solutions are removed from a population
	 * @return Individual - the best individual from the populations generated
	 */
	public Individual algorithm2(Population population, int solutionSize, int populationSize, int generations, int removalRate) {
		Individual individualA;
		Individual individualB;
		bestSolution = population.getBestSolution().clone();
		
		int runs = populationSize/5;
		int rand = 0;
		for (int i = 0; i < generations; i++) {
			//get the best solution and set it aside
            Individual best = population.getBestSolution().clone();
            //choose two individuals at random
            individualA = population.getBestSolution();
            individualB = population.getSolution(rnd.nextInt(population.getSize())).clone();
            
            //check the best solution
			checkBest(i, population.getBestSolution());

            //based on the supplied percentage, either do mutation OR cross-over
			double operate_mutate = rnd.nextDouble();
			for(int j = 0; j < runs; j++){

                //select a cross-over based on these percentages
				if (operate_mutate < 0.6) {
					rand = 2;
					double operationPercentage = rnd.nextDouble();
					if (operationPercentage < 0.4) {
						rand = 0;
					} else if(operationPercentage < 0.70) {
						rand = 3;
					} else if(operationPercentage < 0.95) {
						rand = 1;
					}
					
                    //if the population size is one off the maximum do edge recombination (produces 1 child)
					if (population.getSize() == (populationSize-1)) {
						rand = 1;
					}
					
					switch (rand) {
						case 0:
							population.addSet(operator.orderCrossover(individualA, individualB));
							break;
						case 1:
							population.add(operator.edgeRecombination(individualA, individualB));
							break;
						case 2:
							population.addSet(operator.cycleCrossover(individualA, individualB));
							break;
						case 3:
							population.addSet(operator.pmxCrossover(individualA, individualB));
							break;
					}
				} else {
					rand = 3;
					double mutationPercentage = rnd.nextDouble();
                    //select a mutator based on these percentages
					if (mutationPercentage < 0.5) {
						rand = 2;
					} else if(mutationPercentage < 0.75) {
						rand = 0;
					} else if(mutationPercentage < 0.95) {
						rand = 1;
					}
					
                    //this time only mutate individual A, not individual B
					switch (rand) {
						case 0:
							mutator.insert(individualA);
							break;
						case 1:
							mutator.swap(individualA);
							break;
						case 2:
							mutator.inversion(individualA);
							break;
						case 3:
							mutator.scramble(individualA);
							break;
					}
					
					population.add(individualA);
				}
                //choose new individuals to operate on
				individualA = population.getSolution(rnd.nextInt(population.getSize())).clone();
				individualB = population.getSolution(rnd.nextInt(population.getSize())).clone();
			}
            //add back in the best selection method
			population.add(best);
			
			rand = 0;
			double select = rnd.nextDouble();
            //choose the selection method based on percentage
			if (select < 0.55) {
				rand = 2;
			} else if (select < 0.99) {
				rand = 1;
			}
			         
            //remove only a few duplicates from each set
			population = checkDuplicates(population, removalRate);
			switch (rand) {
				case 0:
					population = selection.fitnessProportional(population, solutionSize);
					break;
				case 1:
					population = selection.tournamentSelection(population, populationSize, solutionSize);
					break;
				case 2:
					population = selection.elitism(population, solutionSize);
					break;
			}
		}
		return bestSolution;
	}
	
	/**
	 * Removes duplicates based on the "removalRate"
	 * @param Population - the solution set from which to remove some or all duplicates
	 * @param int removalRate - the number of duplicates detected before one is removed
	 * @return Population - the modified population with some duplicates removed
	 */
	private Population checkDuplicates(Population solution, int removalRate) {
		solution.sort();
		int count = 0;
		ArrayList<Individual> cities = solution.getSolutionSet();
		for (int i = cities.size()-1; i > 0; i--) {
			if (cities.get(i).getCost() == cities.get(i-1).getCost()) {
				count++;
				if (count == removalRate) {
					cities.remove(i);
					count = 0;
				}
			} else {
				count = 0;
			}
		}
		
		Population modified = new Population(cities.size());
		modified.setSolutionSet(cities);
		return modified;
	}
	
	
	/**
	 * Algorithm based on Operator/Mutator and Selection efficiacy.
	 * @param Population - the TSPGraph to perform the algorithm on
	 * @param int solutionSize - population to begin each generation (cut down at selection)
	 * @param int poplation_size - population size at the end of a generation (before selection)
	 * @param int generations - number of times to perform the mutation/operation cycles
	 * @param double mutationPercentage - chances of mutation occuring
	 * @param double operationPercentage - chances of an operation occuring
	 * @return Individual - the best individual from the populations generated
	 */
	public Individual algorithm3(Population population, int solutionSize, int populationSize, double mutationPercentage, double operationPercentage, int generations) {
		Individual individualA;
		Individual individualB;
		Individual bestSolution = null;
		
		int rand = 0;
		for (int i = 0; i < generations; i++) {
			Population mutants = population.clone();
			Population operators = new Population(population.size());
			
			// Mutate to get mutants
			for (int j = 0; j < mutants.size(); j++) {
				individualA = mutants.getSolution(j);
				
				//Order of preference for mutators
				//1) Inversion          50%
				//2/3) Insert / swap    45%
				//4) Scramble            5%
				
				double doMutation = rnd.nextDouble();
				if (doMutation < 0.05) {
					
					// Do scramble
					mutator.scramble(individualA);
					
				} else if (doMutation < 0.50) {
					// Do Insert or Scramble (50% change of either)
					rand = rnd.nextInt(2);
					switch (rand) {
						case 0:
							mutator.insert(individualA);
							break;
						case 1:
							mutator.swap(individualA);
							break;
					}
					
				} else {
					// Do inversion
					mutator.inversion(individualA);
				}
			}
			
			// Get the ArrayList of individuals
			ArrayList<Individual> individuals = new ArrayList<Individual>();
			
			// Create a deep copy of the population such that the original is not destroyed
			for(Individual in : population.getSolutionSet()) {
				individuals.add(in.clone());
			}
			
			// Operate to get operators
			while(individuals.size() > 1) {
				
				// Choose the first individual and remove it from the list
				int randomIdx = rnd.nextInt(individuals.size());
				individualA = population.getSolution(randomIdx);
				individuals.remove(randomIdx);
				
				// Choose the second individual and remove it from the list
				randomIdx = rnd.nextInt(individuals.size());
				individualB = population.getSolution(rnd.nextInt(population.getSize()));
				individuals.remove(randomIdx);
				
				// Order of preference for Operators
				// 1) Edge Recombination             40%
				// 2) Order Crossover                30%
				// 3) PMX Crossover                  20%
				// 4) Cycle Crossover                10%
				
				double doOperation = 1;//rnd.nextDouble();
				
				if (doOperation < 0.10) {
					// Do Cycle Crossover
					operators.addSet(operator.cycleCrossover(individualA, individualB));
				} else if (doOperation < 0.30) {
					// Do PMX Crossover
					operators.addSet(operator.pmxCrossover(individualA, individualB));
				} else if (doOperation < 0.60) {
					//do Order Crossover
					operators.addSet(operator.orderCrossover(individualA, individualB));
				} else {
					// do Edge Recombination
					operators.add(operator.edgeRecombination(individualA, individualB));
				}
			}
			
			// Combine all populations into one
			population.addPopulation(mutants);
			population.addPopulation(operators);
			
			// Add the best individual (if they exist)
			if(bestSolution != null) {
				population.add(bestSolution);
			}
			
			// Order of preference for Selectors
			// 1) Elitism / Tournament (split evenly)  95%
			// 2) Fitness                               5%
			
			double doSelection = rnd.nextDouble();
			if (doSelection < 0.475) {
				// Do Elitism
				population = selection.elitism(population, solutionSize);
			} else if (doSelection < 0.95) {
				// Do Tournament
				int popnSize = population.getSize()/2;
				
				// Check to see if there are enough in the population to pick a solution
				if(popnSize < solutionSize) {
					popnSize = solutionSize;
				}
				population = selection.tournamentSelection(population, popnSize, solutionSize);
			} else {
				// Do Fitness
				population = selection.fitnessProportional(population, solutionSize);
			}
			
			// Keep the very best individual and always have them in the population
			Individual challenger = population.getBestSolution();
			if (bestSolution == null) {
				bestSolution = challenger;
			} else if(challenger.getCost() < bestSolution.getCost()) {
				bestSolution = challenger;
				System.out.println((i+1) + ": ***** Best Solution ***** = " + bestSolution.getCost());
			}
			
			// Every n generations, add some new individuals
			int nGens = 10, nGuys = 10;
			if(i % nGens == 0) {
				// Add in some random solutions
				Population randomGuys = new Population(nGuys);
				randomGuys.generateRandomSolutionSet(population.getSolution(0).getCities());
				
				population.addPopulation(randomGuys);
			}
		}
		
		return bestSolution;
	}
    
    /**
	 * Algorithm 3 for TTP based on Operator/Mutator and Selection efficiacy.
	 * @param Population - the TSPGraph to perform the algorithm on
	 * @param int solutionSize - population to begin each generation (cut down at selection)
	 * @param int poplation_size - population size at the end of a generation (before selection)
	 * @param int generations - number of times to perform the mutation/operation cycles
	 * @param double mutationPercentage - chances of mutation occuring
	 * @param double operationPercentage - chances of an operation occuring
	 * @return Individual - the best individual from the populations generated
	 */
	public Individual algorithm3TTP(Population population, int solutionSize, int populationSize, double mutationPercentage, double operationPercentage, int generations) {
		Individual individualA;
		Individual individualB;
		Individual bestSolution = null;
		
		int rand = 0;
		for (int i = 0; i < generations; i++) {
			Population mutants = population.clone();
			Population operators = new Population(population.size());
			
			// Mutate to get mutants
			for (int j = 0; j < mutants.size(); j++) {
				individualA = mutants.getSolution(j);
				
				//Order of preference for mutators
				//1) Inversion          50%
				//2/3) Insert / swap    45%
				//4) Scramble            5%
				
				double doMutation = rnd.nextDouble();
				if (doMutation < 0.05) {
					
					// Do scramble
					mutator.scramble(individualA);
					
				} else if (doMutation < 0.50) {
					// Do Insert or Scramble (50% change of either)
					rand = rnd.nextInt(2);
					switch (rand) {
						case 0:
							mutator.insert(individualA);
							break;
						case 1:
							mutator.swap(individualA);
							break;
					}
					
				} else {
					// Do inversion
					mutator.inversion(individualA);
				}
			}
			
			// Get the ArrayList of individuals
			ArrayList<Individual> individuals = new ArrayList<Individual>();
			
			// Create a deep copy of the population such that the original is not destroyed
			for(Individual in : population.getSolutionSet()) {
				individuals.add(in.clone());
			}
			
			// Operate to get operators
			while(individuals.size() > 1) {
				
				// Choose the first individual and remove it from the list
				int randomIdx = rnd.nextInt(individuals.size());
				individualA = population.getSolution(randomIdx);
				individuals.remove(randomIdx);
				
				// Choose the second individual and remove it from the list
				randomIdx = rnd.nextInt(individuals.size());
				individualB = population.getSolution(rnd.nextInt(population.getSize()));
				individuals.remove(randomIdx);
				
				// Order of preference for Operators
				// 1) Edge Recombination             40%
				// 2) Order Crossover                30%
				// 3) PMX Crossover                  20%
				// 4) Cycle Crossover                10%
				
				double doOperation = 1;//rnd.nextDouble();
				
				if (doOperation < 0.10) {
					// Do Cycle Crossover
					operators.addSet(operator.cycleCrossover(individualA, individualB));
				} else if (doOperation < 0.30) {
					// Do PMX Crossover
					operators.addSet(operator.pmxCrossover(individualA, individualB));
				} else if (doOperation < 0.60) {
					//do Order Crossover
					operators.addSet(operator.orderCrossover(individualA, individualB));
				} else {
					// do Edge Recombination
					operators.add(operator.edgeRecombination(individualA, individualB));
				}
			}
			
			// Combine all populations into one
			population.addPopulation(mutants);
			population.addPopulation(operators);
			
			// Add the best individual (if they exist)
			if(bestSolution != null) {
				population.add(bestSolution);
			}
			
			// Order of preference for Selectors
			// 1) Elitism / Tournament (split evenly)  95%
			// 2) Fitness                               5%
			
			double doSelection = rnd.nextDouble();
			if (doSelection < 0.475) {
				// Do Elitism
				population = selection.elitism(population, solutionSize);
			} else if (doSelection < 1) {
				// Do Tournament
				int popnSize = population.getSize()/2;
				
				// Check to see if there are enough in the population to pick a solution
				if(popnSize < solutionSize) {
					popnSize = solutionSize;
				}
				population = selection.tournamentSelection(population, popnSize, solutionSize);
			} else {
				// Do Fitness (never becaues it's minimising at the moment)
				population = selection.fitnessProportional(population, solutionSize);
			}
			
			// Keep the very best individual and always have them in the population
			Individual challenger = population.getBestTTPSolution(TTPGraph, maxSpeed, minSpeed, knapsack);
			if (bestSolution == null) {
				bestSolution = challenger;
			} else if(challenger.getProfit() > bestSolution.getProfit()) {
				bestSolution = challenger;
				System.out.println((i+1) + ": ***** Best Solution ***** = " + bestSolution.getProfit());
			}
			
			// Every n generations, add some new individuals
			int nGens = 10, nGuys = 10;
			if(i % nGens == 0) {
				// Add in some random solutions
				Population randomGuys = new Population(nGuys);
				randomGuys.generateRandomSolutionSet(population.getSolution(0).getCities());
				
				population.addPopulation(randomGuys);
			}
		}
		
		return bestSolution;
	}
	
	/**
	 * The inver-over algorithm that was specified in the paper given
	 * @param City[] - the cities in the TSP tour
	 * @param Population - the population of individuals
	 * @param int - the size of the population
	 * @param int - the number of generations
	 * @return Individual - the best individual from the populations generated
	 */
	public Individual inverOver(City[] cities, Population population, int populationSize, int generations) {
		// Set up the population
		Population new_pop = new Population(populationSize);
		new_pop.generateRandomSolutionSet(cities);
		
		// Operate on the population
		Individual curr, bestSolution = new_pop.getBestSolution();
		double curr_cost;
		double record = -1.0;
		for (int i = 0; i < generations; i++) {
			for (int j = 0; j < populationSize; j++) {
				curr = new_pop.getSolution(j);
				curr = operator.inverOver(curr, new_pop);
				new_pop.setSolution(j, curr);
			}
			
			// Only print the solution if it is an improvement
			bestSolution = new_pop.getBestSolution();
			curr_cost = bestSolution.getCost();
			if (record > bestSolution.getCost() || record == -1.0) {
				record = curr_cost;
				System.out.println((i+1) + ": ***** Best Solution ***** = " + record);
            }
		}
		
		return bestSolution;
	}
	
	/**
	 * Checks if one solution is better than another
	 * If it is it assigns the new solution to the global "bestSolution" variable
	 * And then prints it out to the user
	 * @param int generation - the current generation where the solution was found
	 * @param Individual compare - the solution to be compare against the current best solution
	 */
	public void checkBest(int generation, Individual compare){
		if (compare.getCost() < bestSolution.getCost()) {
			bestSolution = compare.clone();
			System.out.println((generation+1) + ": ***** Best Solution ***** = " + compare.getCost());
		}
	}
}