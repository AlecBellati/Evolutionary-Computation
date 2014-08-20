import java.util.Random;

public class Selection{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;
	private double ELITISM_PERCENTAGE = 0.1;

	/**
	* CONSTRUCTOR
	* Initialise the random number generator
	*/
	public Selection(){
		rnd = new Random();
	}
	
	/**
	 * Change the elitism percentage to adjust elitism selection.
	 * @param percent - the new elitism percentage
	 */
	public void setElitismPercentage(double percent) {
		ELITISM_PERCENTAGE = percent;
	}
	
	/**
	 * Get the current elitism percentage value.
	 * @return ELITISM_PERCENTAGE - the current elitism selection percentage
	 */
	public double getElitismPercentage() {
		return ELITISM_PERCENTAGE;
	}

	/**
	* Given a solution, randomly select num_population solutions.
	* Find the total fitness of all solutions and the probability each solution contributes.
	* Using this information, "spin a wheel" where each solution has the probability of
	* being picked the same as their contribution.
	* @param Population - solution set
	* @param solution_size - number of solutions to select based on the highest profits
    * @return Population
	*/
	public Population fitnessProportional(Population solution, int solution_size){
		int length = solution.getSize();
		
		// Make sure the given population is not already less than or equal
		// to the given solution size
		if (length <= solution_size){
			return solution;
		}
		
		// Create new population
		Population modified_solution = new Population(solution_size);
		
		// Get the total fitness
		double totalFitness = solution.getTotalCost();
		
		// Find the solutions for the population
		double next, total, current;
		int i, j;
		for (i = 0; i < solution_size; i++){
			// Get a number between 0 and 1
			next = rnd.nextDouble();
			
			// Cycle through until the probability is found
			total = 0;
			j = -1;
			while (j < length & total <= next){
				j++;
				
				current = solution.getSolution(j).getCost() / totalFitness;
				total += current;
			}
			
			// Save the solution found
			modified_solution.setSolution(i, solution.getSolution(j));
		}
		
		return modified_solution;
	}

	/**
	* Given a solution, randomly select num_population solutions
	* Given this new subset, sort them by total cost and then pick the best based
	* on the supplied integer - solution_size
	* @param Population - solution set
	* @param num_population - number of random solutions to be used for selection
	* @param solution_size - number of solutions to select based on the highest profits
    * @return Population
	*/
	public Population tournamentSelection(Population solution, int num_population, int solution_size){
		//can't get a larger solution set than what was supplied!!!
		if(num_population > solution.getSize() || num_population < solution_size){
			System.out.println("ERROR: requested solution size is outside the bounds of the supplied data set - tournament_selection()");
			System.out.println("RETURNED: original supplied data");
		}else{
			int size = solution.getSolution(0).getNumCities();
			Population reduced_solution = new Population(num_population);

			//randomly select "num_population" solutions
			for(int i = 0; i < num_population; i++){
				reduced_solution.setSolution(i, solution.getSolution(rnd.nextInt(solution.getSize())));
			}
			reduced_solution.sort();

			//as the array is now sorted, select "solution_size" number of solutions, starting from 0
			Population profit_solution = new Population(solution_size);
	        for(int j = 0; j < solution_size; j++){
	        	profit_solution.setSolution(j, reduced_solution.getSolution(j));
	        }

			return profit_solution;
		}
		return solution;
	}


	/**
	* Given a soluton, generate relative fitnesses for solutions
	* And rank the population according to relative fitness
	* @param Population - solution set
	* @param num_population - number of solutions to be used for selection
    * @return Population
	*/
	public Population elitism(Population solution, int num_population) {
		
		int length = solution.getSize();
		
		// Make sure the given population is not already less than or equal
		// to the given solution size
		if (length <= num_population){
			return solution;
		}
		
		// Create new population
		Population modified_solution = new Population(num_population);
		
		// Sort current population
		solution.sort();
		
		// Select elite solutions
		int noEliteSolns = (int) (num_population * ELITISM_PERCENTAGE);
		for (int i = 0; i < noEliteSolns; i++) {
			modified_solution.setSolution(i, solution.getSolution(i));
		}
				
		int range = length - noEliteSolns;
		// Select random solutions from the remainder
		for(int i = noEliteSolns+1; i < num_population; i++){
			modified_solution.setSolution(i, solution.getSolution(rnd.nextInt(range) + noEliteSolns));
		}

		for(int i = 0; i < num_population; i++){
			modified_solution.setSolution(i, solution.getSolution(i));
		}

		return modified_solution;
	}

	/**
	* TESTING ONLY
	*/
	public static void main(String[] args){
		Selection test = new Selection();
	}
}