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
     *
     *
     */
	public Population runSequence(City[] cities, int solution_size, int population_size, int generations, double mutation_percentage, double operation_percentage, int algorithm){
		Population population = new Population(solution_size);
		population.generateRandomSolutionSet(cities);
        
		switch(algorithm){
			case 1:
				return algorithm1(population, solution_size, population_size, mutation_percentage, operation_percentage, generations);
			case 2:
				return algorithm2(population, solution_size, population_size, generations);
			case 3:
				return algorithm3(population, solution_size, population_size, mutation_percentage, operation_percentage, generations);
			case 4:
				return inver_over(cities, population, population_size, generations);
		}
		return null;
	}
    
	/**
     *
     *
     */
	public Population algorithm1(Population population, int solution_size, int population_size, double mutation_percentage, double operation_percentage, int generations){
		Individual individualA;
		Individual individualB;
		
		int rand = 0;
		for(int i = 0; i < generations; i++){
			Individual best = population.getBestSolution().clone();
            individualA = population.getBestSolution();
            individualB = population.getSolution(rnd.nextInt(population.getSize()));
            
			while(population.getSize() < (population_size-1)){
                
				if(individualA != individualB){
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
				}
                individualA = population.getSolution(rnd.nextInt(population.getSize()));
				individualB = population.getSolution(rnd.nextInt(population.getSize()));
			}
            
			population.add(best);
			rand = 2;
			double select = rnd.nextDouble();
			if(select < 0.6){
				rand = 2;
			}else if(select < 0.9){
				rand = 1;
			}
            
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
            
			System.out.println(i + ": ***** Best Solution ***** = " + population.getBestSolution().getCost());
		}
		return population;
	}
    
    /**
     *
     *
     */
    public Population algorithm2(Population population, int solution_size, int population_size, int generations){
        Individual individualA;
        Individual individualB;
        
        int rand = 0;
        for(int i = 0; i < generations; i++){
            Individual best = population.getBestSolution().clone();
            individualA = population.getBestSolution();
            individualB = population.getSolution(rnd.nextInt(population.getSize())).clone();
            
            double operate_mutate = rnd.nextDouble();
            while(population.getSize() < (population_size-1)){
                if(individualA != individualB){
                    
                    if(operate_mutate < 0.5){
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
                        
                        population.add(individualA);
                        population.add(individualB);
                    }
                }
                individualA = population.getSolution(rnd.nextInt(population.getSize())).clone();
                individualB = population.getSolution(rnd.nextInt(population.getSize())).clone();
            }
            
            population.add(best);
            
            rand = 2;
            double select = rnd.nextDouble();
            if(select < 0.6){
                rand = 2;
            }else if(select < 0.9){
                rand = 1;
            }
            
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
            
            System.out.println(i + ": ***** Best Solution ***** = " + population.getBestSolution().getCost());
        }
        return population;
    }
    
    
    /**
     *
     *
     */
	public Population algorithm3(Population population, int solution_size, int population_size, double mutation_percentage, double operation_percentage, int generations) {
        Individual individualA;
		Individual individualB;
        Individual theBest = null;
		
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
            if(theBest != null) {
                population.add(theBest);
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
            
			System.out.println("***** Best Solution ***** = " + population.getBestSolution().getCost());
            
            //keep the very best individual and always have them in the population
            Individual challenger = population.getBestSolution();
            if(theBest == null) {
                theBest = challenger;
            } else if(challenger.getCost() < theBest.getCost()) {
                theBest = challenger;
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
        
		return population;
	}
	
	/**
     * The inver-over algorithm that was specified in the paper given
     * @param City[] - the cities in the TSP tour
	 * @param Population - the population of individuals
	 * @param int - the size of the population
     * @param int - the number of generations
     */
	public Population inver_over(City[] cities, Population population, int population_size, int generations) {
        // Set up the population
		Population new_pop = new Population(population_size);
		new_pop.generateRandomSolutionSet(cities);        
		
		// Operate on the population
		Individual curr, curr_best;
		double curr_cost;
		double record = -1.0;
		for (int i = 0; i < generations; i++){
			for (int j = 0; j < population_size; j++){
				curr = new_pop.getSolution(j);
				curr = operator.inverOver(curr, new_pop);
				new_pop.setSolution(j, curr);
			}
			
			// Only print the solution if it is an improvement
			curr_best = new_pop.getBestSolution();
			curr_cost = curr_best.getCost();
			if (record > curr_best.getCost() || record == -1.0){
				record = curr_cost;
				
				System.out.println(i + ": ***** Best Solution ***** = " + curr_cost);
			}
		}
		
		return population;
	}
}