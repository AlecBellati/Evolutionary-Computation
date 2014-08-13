import java.util.ArrayList;
import java.util.Random;

public class Individual{

	/** Contains the solution associated with this Individual */
	private City[] solution;

	/** The fitness value of this solution*/
	private double fitness;

	/*
	 * Takes given TSP Graph and assigns to local variable
	 * Initialises solution array list
	 * @param TSPGraph - The TSP graph that is being used
	 * @param solution - The solution for this Individual
	 * @param preserve - A flag to indicate whether the order solution should be preserved
	 */
	public Individual(double[][] TSPGraph, City[] solution, boolean preserve){
		this.solution = solution;
		
		// Shuffle the solution if it does not need to be preserved
		if (!preserve){
			shuffleArray();
		}
		
		// Calculate the cost of this solution
		calculate_fitness(TSPGraph);
	}

	/*
	* Gets the basic solution set
	* @return An array of cities
	*/
	public City[] get_solution(){
		return solution;
	}

	/**
	* Get the number of cities in the solution set
	* @return The number of cities in the solution
	*/
	public int get_num_cities(){
		return solution.length;
	}

	/**
	* Get the fitness of the solution
	* @return - The fitness of this solution
	*/
	public double get_fitness(){
		return fitness;
	}

	/**
	* Given a city, calculate its fitness
	* @param TSPGraph - The TSP graph being used
	*/
	private void calculate_fitness(double[][] TSPGraph){
		double fitness = 0;
		for(int i = 0; i < solution.length; i++){
			City curr_city = solution[i];
			City next_city;
			double cost_to_node = 0;

			if(i != solution.length-1){
				next_city = solution[i+1];
				cost_to_node = TSPGraph[curr_city.get_node_num()][next_city.get_node_num()];
			}else{ //return to start
				next_city = solution[0];
				cost_to_node = TSPGraph[curr_city.get_node_num()][next_city.get_node_num()];
			}
			fitness += cost_to_node;
		}
	}
	
	/**
	* Implementing Fisher–Yates shuffle
	* Randomly shuffle the cities array to form a random solution
	*/
  	private void shuffleArray(){
    	Random rnd = new Random();
    	for (int i = solution.length - 1; i > 0; i--){
      		//int index = rnd.nextInt(i + 1);
      		int index = rnd.nextInt(solution.length-1);
      		// Simple swap
      		City temp = solution[index];
      		solution[index] = solution[i];
      		solution[i] = temp;
    	}
  	}
}