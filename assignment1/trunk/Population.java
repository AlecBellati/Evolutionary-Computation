public class Population{
	
	/** TSP graph generated from XML parser */
	private double[][] TSPGraph;
	
	/** Set of solutions */
	private Individual[] solution_set;
	
	/** Size of population.
	Not necessarily the actual size,
	but what the size of the selected set should be */
	private int population_size;
	
	/**
	* Constructor takes in a TSP graph
	* Used to initialise multiple single solutions to form a population
	*/
	public Population(double[][] TSPGraph, City[] cities, int population_size){
		this.TSPGraph = TSPGraph;
		this.population_size = population_size;
		
		create_solution_set(cities);
	}

	/*
	* Gets the TSP graph
	* @return - A 2D double array
	*/
	public double[][] get_TSPGraph(){
		return TSPGraph;
	}
	
	/*
	* Gets a basic set of solutions
	* @return - A Individual array
	*/
	public Individual[] get_solution_set(){
		return solution_set;
	}
	
	/*
	* Gets the size of set of solutions
	* @return - The size of solution_set
	*/
	public int get_solution_set_size(){
		return solution_set.length;
	}
	
	/*
	* Gets the size of the population
	* @return - population_size
	*/
	public int get_population_size(){
		return population_size;
	}
	
	/*
	* Add a solution
	* @param - The new solution to add
	*/
	public void add_solution(City[] new_solution){
		// Create the new Individual
		Individual new_sol = new Individual(TSPGraph, new_solution, true);
		
		// Add the new Individual to the solution set
		Individual[] new_sol_set = new Individual[solution_set.length + 1];
		for (int i = 0; i < solution_set.length; i++){
			new_sol_set[i] = solution_set[i];
		}
		new_sol_set[solution_set.length] = new_sol;
		
		solution_set = new_sol_set;
		
	}
	
	/*
	* Initialise the solution set
	* Should not generate any errors
	* @param - An array of the cities in the problem
	*/
	private void create_solution_set(City[] cities){
		solution_set = new Individual[population_size];
		
		for(int i = 0; i < population_size; i++){
			// Create an individual
			solution_set[i] = new Individual(TSPGraph, cities, false);
		}
		
	}
}