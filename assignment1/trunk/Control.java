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
	public Population runSequence(City[] cities, int solution_size, int population_size, int generations, double mutation_percentage, int algorithm){
		Population population = new Population(solution_size);
		population.generateRandomSolutionSet(cities);

		switch(algorithm){
			case 1:
				return algorithm1(population, solution_size, population_size, mutation_percentage, generations);
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
	public Population algorithm1(Population population, int solution_size, int population_size, double mutation_percentage, int generations){
		int modular_size = 2;
		Individual individualA;
		Individual individualB;
		Individual[] operated = null;
		
		int rand = 0;
		for(int i = 0; i < generations; i++){
			Population best = selection.elitism(population.clone(), 5);
			for(int j = 0; j < population.getSize()/2; j++){
				individualA = population.getSolution(rnd.nextInt(population.getSize()));
				individualB = population.getSolution(rnd.nextInt(population.getSize()));

				if(individualA != individualB){
					//if only one more individual required, choose form the first two cases
					if(population.getSize()%2 != 0) {
						rand = 1;
					//otherwise, select any of them at random
					} else {
						rand = rnd.nextInt(4);
					}
					switch(rand){
						case 0:
							//population.addSet(operator.orderCrossover(individualA, individualB));
							break;
						case 1:
							//System.out.println("edgeRecombination");
							population.add(operator.edgeRecombination(individualA, individualB));
							break;
						case 2:
							//System.out.println("cycleCrossover");
							population.addSet(operator.cycleCrossover(individualA, individualB));
							break;
						case 3:
							//System.out.println("pmxCrossover");
							population.addSet(operator.pmxCrossover(individualA, individualB));
							break;
					}
					
					double doMutation = rnd.nextDouble();
					if(doMutation < mutation_percentage) {
						rand = rnd.nextInt(4);
						if(rnd.nextInt(100) < 60){
							rand = 2;
						}
						switch(rand){
							case 0:
								//System.out.println("Insert");
								mutator.insert(individualA);
								mutator.insert(individualB);
								break;
							case 1:
								//System.out.println("swap");
								mutator.swap(individualA);
								mutator.swap(individualB);
								break;
							case 2:
								//System.out.println("inversion");
								mutator.inversion(individualA);
								mutator.inversion(individualB);
								break;
							case 3:
								//System.out.println("scramble");
								mutator.scramble(individualA);
								mutator.scramble(individualB);
								break;
						}
					}
				}
			}
			
			rand = rnd.nextInt(2);
			if(rnd.nextInt(100) < 60){
				rand = 2;
			}
			switch(rand){
				case 0:
					//System.out.println("fitness");
					population = selection.fitnessProportional(population, solution_size);
					break;
				case 1:
					//System.out.println("tournament");
					population = selection.tournamentSelection(population, population_size, solution_size);
					break;
				case 2:
					//System.out.println("elitism");
					population = selection.elitism(population, solution_size);
					break;
			}
			population.addPopulation(best);

			System.out.println("***** Best Solution ***** = " + population.getBestSolution().getCost());
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