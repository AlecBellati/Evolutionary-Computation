import java.util.Random;

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
			Population offspring = population.clone();
			Population small_offspring = selection.elitism(population, 5);
			while(offspring.getSize() < population_size){
				individualA = offspring.getSolution(rnd.nextInt(offspring.getSize()));
				individualB = offspring.getSolution(rnd.nextInt(offspring.getSize()));

				if(individualA != individualB){
					//if only one more individual required, choose form the first two cases
					if(offspring.getSize()%2 != 0) {
						rand = rnd.nextInt(1);
					//otherwise, select any of them at random
					} else {
						rand = rnd.nextInt(3);
					}
					switch(rand){
						/*case 0:
							operated = operator.orderCrossover(individualA, individualB);
							break;*/
						case 0:
							//System.out.println("edgeRecombination");
							operated = new Individual[]{operator.edgeRecombination(individualA, individualB)};
							break;
						case 1:
							//System.out.println("cycleCrossover");
							operated = operator.cycleCrossover(individualA, individualB);
							break;
						case 2:
							//System.out.println("pmxCrossover");
							operated = operator.pmxCrossover(individualA, individualB);
							break;
					}
					offspring = new Population(offspring, operated);
					
					double doMutation = rnd.nextDouble();
					if(doMutation < mutation_percentage) {
						rand = rnd.nextInt(4);
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
			population = new Population(population, offspring.getSolutionSet());
			
			rand = rnd.nextInt(2);
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
			population = new Population(population, small_offspring.getSolutionSet());

			System.out.println("***** Best Solution ***** = " + population.getBestSolution().getCost());
		}
		return population;
	}

	/**
	*
	*
	*/
	public Population algorithm2(Population population, int solution_size, int population_size, int generations){
		Individual[] offspring = null;
		Individual individual;
		
		for(int i = 0; i < generations; i++){
			offspring = population.clone().getSolutionSet();
			for(int j = 0; j < population.getSize(); j++){
				individual = offspring[j];
				mutator.inversion(individual);
			}
			
			population = new Population(population, offspring);
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