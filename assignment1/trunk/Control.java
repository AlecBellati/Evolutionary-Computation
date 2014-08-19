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
	public Population runSequence(City[] cities, int population_size, int generations, int algorithm){
		Population population = new Population(population_size);
		population.generateRandomSolutionSet(cities);

		switch(algorithm){
			case 1:
                return algorithm1(population, population_size, generations);
			case 2:
                algorithm2(population, population_size, generations);
                break;
			case 3:
                algorithm3(population, population_size, generations);
                break;
		}
        return null;
	}

	/**
	*
	*
	*/
	public Population algorithm1(Population population, int population_size, int generations){
		int modular_size = 2;
		Individual individualA;
		Individual individualB;
		Individual[] offspring = null;
		
		int rand = 0;
        int count = 0;
		for(int i = 0; i < generations; i++){
            System.out.println("\n\n*************NEW GENERATION*************");
			while(count < population_size){
                System.out.print("[");
                System.out.println();
                System.out.println("count = " + count);
				individualA = population.getSolution(rnd.nextInt(population.getSize()));
				individualB = population.getSolution(rnd.nextInt(population.getSize()));

				if(individualA != individualB){
                    //if only one more individual required, choose form the first two cases
                    if(count+1 == population_size) {
                        rand = rnd.nextInt(2);
                    //otherwise, select any of them at random
                    } else {
                        rand = rnd.nextInt(4);
                    }
					switch(rand){
						case 0:
                            System.out.println("orderCrossover");
                            offspring = operator.orderCrossover(individualA, individualB);
                            count+=1;
                            break;
						case 1:
                            System.out.println("edgeRecombination");
                            offspring = new Individual[]{operator.edgeRecombination(individualA, individualB)};
                            count+=1;
                            break;
						case 2:
                            System.out.println("cycleCrossover");
                            offspring = operator.cycleCrossover(individualA, individualB);
                            count+=2;
                            break;
						case 3:
                            System.out.println("pmxCrossover");
                            offspring = operator.pmxCrossover(individualA, individualB);
                            count+=2;
                            break;
                    }
                    population = new Population(population, offspring);

					rand = rnd.nextInt(4);
					switch(rand){
						case 0:
                            System.out.println("insert");
                            mutator.insert(individualA);
                            mutator.insert(individualB);
                            break;
						case 1:
                            System.out.println("swap");
                            mutator.swap(individualA);
                            mutator.swap(individualB);
                            break;
						case 2:
                            System.out.println("inversion");
                            mutator.inversion(individualA);
                            mutator.inversion(individualB);
                            break;
						case 3:
                            System.out.println("scramble");
                            mutator.scramble(individualA);
                            mutator.scramble(individualB);
                            break;
                    }
				}
			}
            
                System.out.println("selection now");
                rand = rnd.nextInt(3);
                switch(rand){
                    case 0:
                        System.out.println("fitnessProportional");
                        population = selection.fitnessProportional(population, count/modular_size);
                        break;
                    case 1:
                        System.out.println("tournamentSelection");
                        population = selection.tournamentSelection(population, count, count/modular_size);
                        break;
                    case 2:
                        System.out.println("elitism");
                        population = selection.elitism(population, count/modular_size);
                        break;
                }
            
            //reset count for the next generation
            count = population.getSize();
		}
        return population;
	}

	/**
	*
	*
	*/
	public void algorithm2(Population population, int population_size, int generations){

	}

	/**
	*
	*
	*/
	public void algorithm3(Population population, int population_size, int generations){

	}
}