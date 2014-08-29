import java.util.Random;
import java.util.ArrayList;

public class Control{
    
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;
	/** Object holds the four mutator functions */
	private Mutators mutator;
	/** Object holds the four operator functions, returns Individual(s) */
	private Operators operator;
	/** Contains the three selection functions, returns a new population */
	private Selection selection;
    /** Holds the best solution for the current algorithm */
    private Individual best_solution;
	
	/**
     * CONSTRUCTOR
     * Initialises the mutator, operator and selection objects
     */
	public Control(){
		rnd = new Random();
		mutator = new Mutators();
		operator = new Operators();
		selection = new Selection();
	}
    
	/**
     * Determines what algorithm to be used based on the supplied "algorithm" number
     * Passes over relevant data to the specified algorithm
     * @param City[] - the cites to perform the algorithm on
     * @param int solution_size - population to begin each generation (cut down at selection)
     * @param int poplation_size - population size at the end of a generation (before selection)
     * @param int generations - number of times to perform the mutation/operation cycles
     * @param double mutation_percentage - chances of mutation occuring
     * @param double operation_percentage - chances of an operation occuring
     * @param int removal_rate - determines how many duplicate or similar solutions are removed from a population
     * @param int algorithm - which algorithm to use (1-4)
     * @return Individual - the best individual from the populations generated
     */
	public Individual runSequence(City[] cities, int solution_size, int population_size, int generations, double mutation_percentage, double operation_percentage, int removal_rate, int algorithm){
		Population population = new Population(solution_size);
		population.generateRandomSolutionSet(cities);
        
		switch(algorithm){
			case 1:
				return algorithm1(population, solution_size, population_size, mutation_percentage, operation_percentage, generations, removal_rate);
			case 2:
				return algorithm2(population, solution_size, population_size, generations, removal_rate);
			case 3:
				return algorithm3(population, solution_size, population_size, mutation_percentage, operation_percentage, generations);
			case 4:
				return inver_over(cities, population, population_size, generations);
		}
		return null;
	}
    
	/**
     * Simulates an algorithm something similar to that of the GA in the book/lecture
     * @param Population - the TSPGraph to perform the algorithm on
     * @param int solution_size - population to begin each generation (cut down at selection)
     * @param int poplation_size - population size at the end of a generation (before selection)
     * @param int generations - number of times to perform the mutation/operation cycles
     * @param double mutation_percentage - chances of mutation occuring
     * @param double operation_percentage - chances of an operation occuring
     * @param int removal_rate - determines how many duplicate or similar solutions are removed from a population
     * @return Individual - the best individual from the populations generated
     */
	public Individual algorithm1(Population population, int solution_size, int population_size, double mutation_percentage, double operation_percentage, int generations, int removal_rate){
		Individual individualA;
		Individual individualB;
        best_solution = population.getBestSolution().clone();
		
		int rand = 0;
		for(int i = 0; i < generations; i++){
			Individual best = population.getBestSolution().clone();
            individualA = population.getBestSolution();
            individualB = population.getSolution(rnd.nextInt(population.getSize()));
            
            checkBest(i, population.getBestSolution());
            mutator.inversion(individualA);
			while(population.getSize() < population_size){
                
                    if(population.getSize() == (population_size-1)) {
                        rand = 1;
                    } else {
                   		rand = rnd.nextInt(4);
                    }
                    
                    double operate = rnd.nextDouble();
                    if(operate < operation_percentage){
	                    switch(rand){
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
	                }
                    
                    rand = rnd.nextInt(4);
                    double mutate = rnd.nextDouble();
                    if(mutate < mutation_percentage){
						switch(rand){
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
			double select = rnd.nextDouble();
			if(select < 0.55){
				rand = 2;
			}else if(select < 0.99){
				rand = 1;
			}
            
            population = checkDuplicates(population, removal_rate);
			switch(rand){
				case 0:
					population = selection.fitnessProportional(population, solution_size);
					break;
				case 1:
					population = selection.tournamentSelection(population, population_size, solution_size);
					break;
				case 2:
					population = selection.elitism(population, solution_size);
					break;
			}
		}
		return best_solution;
	}
    
    /**
     * Simulates an algorithm something similar to that of the GA in the book/lecture
     * @param Population - the TSPGraph to perform the algorithm on
     * @param int solution_size - population to begin each generation (cut down at selection)
     * @param int poplation_size - population size at the end of a generation (before selection)
     * @param int generations - number of times to perform the mutation/operation cycles
     * @param int removal_rate - determines how many duplicate or similar solutions are removed from a population
     * @return Individual - the best individual from the populations generated
     */
    public Individual algorithm2(Population population, int solution_size, int population_size, int generations, int removal_rate){
        Individual individualA;
        Individual individualB;
        best_solution = population.getBestSolution().clone();
        
        int rand = 0;
        for(int i = 0; i < generations; i++){
            Individual best = population.getBestSolution().clone();
            individualA = population.getBestSolution();
            individualB = population.getSolution(rnd.nextInt(population.getSize())).clone();
            
            checkBest(i, population.getBestSolution());
            mutator.inversion(individualA);
            double operate_mutate = rnd.nextDouble();
            while(population.getSize() < population_size){
                    if(operate_mutate < 0.6){
                        rand = 2;
                        double operation_percentage = rnd.nextDouble();
                        if(operation_percentage < 0.4){
                            rand = 0;
                        }else if(operation_percentage < 0.70){
                            rand = 3;
                        }else if(operation_percentage < 0.95){
                            rand = 1;
                        }
                        
                        if(population.getSize() == (population_size-1)) {
                            rand = 1;
                        }
                        
                        switch(rand){
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
                        rand = 3;
                        double mutation_percentage = rnd.nextDouble();
                        if(mutation_percentage < 0.5){
                            rand = 2;
                        }else if(mutation_percentage < 0.75){
                            rand = 0;
                        }else if(mutation_percentage < 0.95){
                            rand = 1;
                        }
                        
                        switch(rand){
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
                individualA = population.getSolution(rnd.nextInt(population.getSize())).clone();
                individualB = population.getSolution(rnd.nextInt(population.getSize())).clone();
            }
            population.add(best);
            
            rand = 0;
            double select = rnd.nextDouble();
            if(select < 0.55){
                rand = 2;
            }else if(select < 0.99){
                rand = 1;
            }
            
            population = checkDuplicates(population, removal_rate);
            switch(rand){
                case 0:
                    population = selection.fitnessProportional(population, solution_size);
                    break;
                case 1:
                    population = selection.tournamentSelection(population, population_size, solution_size);
                    break;
                case 2:
                    population = selection.elitism(population, solution_size);
                    break;
            }
        }
        return best_solution;
    }

    /**
    * Removes duplicates based on the "remove_rate"
    * @param Population - the solution set from which to remove some or all duplicates
    * @param int remove_rate - the number of duplicates detected before one is removed
    * @return Population - the modified population with some duplicates removed
    */
    private Population checkDuplicates(Population solution, int remove_rate){
        solution.sort();
        int count = 0;
        ArrayList<Individual> cities = solution.getSolutionSet();
        for(int i = cities.size()-1; i > 0; i--){
            if(cities.get(i).getCost() == cities.get(i-1).getCost()){
                count++;
                if(count == remove_rate){
                    cities.remove(i);
                    count = 0;
                }
            }else{
                count = 0;
            }
        }

        Population modified = new Population(cities.size());
        modified.setSolutionSet(cities);
        return modified;
    }
    
    
    /**
     * EXPLANATION!
     * @param Population - the TSPGraph to perform the algorithm on
     * @param int solution_size - population to begin each generation (cut down at selection)
     * @param int poplation_size - population size at the end of a generation (before selection)
     * @param int generations - number of times to perform the mutation/operation cycles
     * @param double mutation_percentage - chances of mutation occuring
     * @param double operation_percentage - chances of an operation occuring
     * @return Individual - the best individual from the populations generated
     */
	public Individual algorithm3(Population population, int solution_size, int population_size, double mutation_percentage, double operation_percentage, int generations) {
        Individual individualA;
		Individual individualB;
        Individual best_solution = null;
		
		int rand = 0;
		for(int i = 0; i < generations; i++) {
			Population mutants = population.clone();
            Population operators = new Population(population.size());
            
            //mutate to get mutants
            for(int j = 0; j < mutants.size(); j++) {
                individualA = mutants.getSolution(j);
                
                //Order of preference for mutators
                //1) Inversion          50%
                //2/3) Insert / swap    45%
                //4) Scramble            5%
                
                double doMutation = rnd.nextDouble();
                if(doMutation < 0.05) {
                    
                    //do scramble
                    mutator.scramble(individualA);
                    
                } else if (doMutation < 0.50) {
                    //do Insert or Scramble (50% change of either)
                    rand = rnd.nextInt(2);
                    switch(rand) {
                        case 0:
                            mutator.insert(individualA);
                            break;
                            
                        case 1:
                            mutator.scramble(individualA);
                            break;
                    }
                    
                } else {
                    //do inversion
                    mutator.inversion(individualA);
                }
            }
            
            //get the ArrayList of individuals
            ArrayList<Individual> individuals = new ArrayList<Individual>();
            
            //create a deep copy of the population such that the original is not destroyed
            for(Individual in : population.getSolutionSet()) {
                individuals.add(in.clone());
            }
            
            //operate to get operators
			while(individuals.size() > 1) {
                
                //choose the first individual and remove it from the list
                int randomIdx = rnd.nextInt(individuals.size());
				individualA = population.getSolution(randomIdx);
                individuals.remove(randomIdx);
                
                //choose the second individual and remove it from the list
                randomIdx = rnd.nextInt(individuals.size());
				individualB = population.getSolution(rnd.nextInt(population.getSize()));
                individuals.remove(randomIdx);
                
                //Order of preference for Operators
                //1) Edge Recombination             40%
                //2) Order Crossover                30%
                //3) PMX Crossover                  20%
                //4) Cycle Crossover                10%
                
                double doOperation = rnd.nextDouble();
                
                if(doOperation < 0.10) {
                    //do Cycle Crossover
                    operators.addSet(operator.cycleCrossover(individualA, individualB));
                } else if (doOperation < 0.30) {
                    //do PMX Crossover
                    operators.addSet(operator.pmxCrossover(individualA, individualB));
                } else if (doOperation < 0.60) {
                    //do Order Crossover
                    operators.addSet(operator.orderCrossover(individualA, individualB));
                } else {
                    // do Edge Recombination
                    operators.add(operator.edgeRecombination(individualA, individualB));
                }
            }
			
            //combine all populations into one
            population.addPopulation(mutants);
            population.addPopulation(operators);
            
            //add the best individual (if they exist)
            if(best_solution != null) {
                population.add(best_solution);
            }
            
            
            //Order of preference for Selectors
            //1) Elitism / Tournament (split evenly)  95%
            //2) Fitness                               5%
			
            double doSelection = rnd.nextDouble();
            if(doSelection < 0.475) {
                //do Elitism
                population = selection.elitism(population, solution_size);
            } else if (doSelection < 0.95) {
                //do Tournament
                int popnSize = population.getSize()/2;
                
                //check to see if there are enough in the population to pick a solution
                if(popnSize < 5) {
                    popnSize = 5;
                }
                population = selection.tournamentSelection(population, popnSize, solution_size);
            } else {
                //do Fitness
                population = selection.fitnessProportional(population, solution_size);
            }
            
            //keep the very best individual and always have them in the population
            Individual challenger = population.getBestSolution();
            if(best_solution == null) {
                best_solution = challenger;
            } else if(challenger.getCost() < best_solution.getCost()) {
                best_solution = challenger;
            }
            
            //every n generations, add some new meat
            int nGens = 10, nGuys = 10;
            if(i % nGens == 0) {
                //add in some random solutions
                Population randomGuys = new Population(nGuys);
                randomGuys.generateRandomSolutionSet(population.getSolution(0).getCities());
                
                population.addPopulation(randomGuys);
            }
		}
        
		return best_solution;
	}
	
	/**
     * The inver-over algorithm that was specified in the paper given
     * @param City[] - the cities in the TSP tour
	 * @param Population - the population of individuals
	 * @param int - the size of the population
     * @param int - the number of generations
     * @return Individual - the best individual from the populations generated
     */
	public Individual inver_over(City[] cities, Population population, int population_size, int generations) {
        // Set up the population
		Population new_pop = new Population(population_size);
		new_pop.generateRandomSolutionSet(cities);        
		
		// Operate on the population
		Individual curr, best_solution = new_pop.getBestSolution();
		double curr_cost;
		double record = -1.0;
		for (int i = 0; i < generations; i++){
			for (int j = 0; j < population_size; j++){
				curr = new_pop.getSolution(j);
				curr = operator.inverOver(curr, new_pop);
				new_pop.setSolution(j, curr);
			}
			
			// Only print the solution if it is an improvement
			best_solution = new_pop.getBestSolution();
			curr_cost = best_solution.getCost();
			if (record > best_solution.getCost() || record == -1.0){
				record = curr_cost;
				
				System.out.println(i + ": ***** Best Solution ***** = " + curr_cost);
			}
		}
		
		return best_solution;
	}

    /**
    * Checks if one solution is better than another
    * If it is it assigns the new solution to the global "best_solution" variable
    * And then prints it out to the user
    * @param int generation - the current generation where the solution was found
    * @param Individual compare - the solution to be compare against the current best solution
    */
    public void checkBest(int generation, Individual compare){
        if(compare.getCost() < best_solution.getCost()){
            best_solution = compare.clone();
            //System.out.println((generation+1) + ": ***** Best Solution ***** = " + best_solution.getCost());
        }
    }
}