import java.util.Random;

public class Control{

	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;
	/**  */
	private Mutators mutator;
	/**  */
	private Operators operator;
	/**  */
	private Selection selection;
	
	/**
	* CONSTRUCTOR
	*
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
	public Population runSequence(City[] cities, int solution_size, int population_size, int generations, int algorithm){
		Population population = new Population(solution_size);
		population.generateRandomSolutionSet(cities);

		switch(algorithm){
			case 1:
                return algorithm1(population, solution_size, population_size, generations);
			case 2:
                algorithm2(population, solution_size, population_size, generations);
                break;
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
	public Population algorithm1(Population population, int solution_size, int population_size, int generations){
		int modular_size = 2;
		Individual individualA;
		Individual individualB;
		Individual[] offspring = null;
		
		int rand = 0;
		for(int i = 0; i < generations; i++){
			while(population.getSize() < population_size){
				individualA = population.getSolution(rnd.nextInt(population.getSize()));
				individualB = population.getSolution(rnd.nextInt(population.getSize()));

				if(individualA != individualB){
                    //if only one more individual required, choose form the first two cases
                    if(population.getSize()%2 != 0) {
                        rand = rnd.nextInt(1);
                    //otherwise, select any of them at random
                    } else {
                        rand = rnd.nextInt(3);
                    }
					switch(rand){
						/*case 0:
                            offspring = operator.orderCrossover(individualA, individualB);
                            break;*/
						case 0:
                            offspring = new Individual[]{operator.edgeRecombination(individualA, individualB)};
                            break;
						case 1:
                            offspring = operator.cycleCrossover(individualA, individualB);
                            break;
						case 2:
                            offspring = operator.pmxCrossover(individualA, individualB);
                            break;
                    }
                    population = new Population(population, offspring);

					rand = rnd.nextInt(4);
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
            
            rand = rnd.nextInt(3);
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
        return population;
	}

	/**
	*
	*
	*/
	public void algorithm2(Population population, int solution_size, int population_size, int generations){

	}

	/**
	*
	*
	*/
	public void algorithm3(Population population, int solution_size, int population_size, int generations){

	}
}