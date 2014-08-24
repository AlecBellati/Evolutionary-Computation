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
				algorithm3(population, solution_size, population_size, generations);
				break;
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
			Individual best = population.getBestSolution();

			while(population.getSize() < (population_size-1)){
				individualA = population.getSolution(rnd.nextInt(population.getSize()));
				individualB = population.getSolution(rnd.nextInt(population.getSize()));
                
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
		Population offspring = null;
		Individual individual;
		
		for(int i = 0; i < generations; i++){
			offspring = population.clone();
			for(int j = 0; j < population.getSize(); j++){
				individual = offspring.getSolution(j);
				mutator.inversion(individual);
			}
			
			population.addPopulation(offspring);
			population = selection.elitism(population, solution_size);
            
			System.out.println("***** Best Solution ***** = " + population.getBestSolution().getCost());
		}
		return population;
	}
    
	/**
     *
     *
     */
	public void algorithm3(Population population, int solution_size, int population_size, int generations){
        
	}
}