public class Population{
	
	/** Individual contains logic for generating a single solution */
	public Individual single_sol;

	/**
	* Constructor takes in initialised individual object
	* Used to extract multiple single solutions to form a population
	*/
	public Population(Individual single_sol){
		this.single_sol = single_sol;
	}

	/*
	* Gets a basic set of solutions - specified by the user
	* Returns a City array of solutions
	* Should not generate any errors
	*/
	public City[][] get_solution_set(int num_solutions){
		City[][] solution_set = new City[num_solutions][single_sol.get_num_cities()];
		for(int i = 0; i < num_solutions; i++){
			solution_set[i] = single_sol.get_single_solution();
		}
		return solution_set;
	}
}