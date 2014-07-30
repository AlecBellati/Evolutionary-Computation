import java.util.ArrayList;
import java.util.Random;

public class Individual{

	/** TSP graph generated from XML parser */
	private double[][] TSPGraph;

	/** Contains the cities with their associated weights
	Should contain ame information as above (TSPGraph) */
	private City[] cities;

	/*
	* Takes given TSP Graph and assigns to local variable
	* Initialises solution array list
	*/
	public Individual(double[][] TSPGraph, City[] cities){
		this.TSPGraph = TSPGraph;
		this.cities = cities;
	}

	/*
	* Gets the basic solution set
	* Allows for a variable number solutions and returns in a double array
	* Should not generate any errors
	*/
	public void get_solution_set(){
		shuffleArray();
		double total = 0;

		for(int i = 0; i < cities.length; i++){
			City curr_city = cities[i];
			City next_city;
			double cost_to_node = 0;

			if(i != cities.length-1){
				next_city = cities[i+1];
				cost_to_node = TSPGraph[curr_city.get_node_num()][next_city.get_node_num()];
			}else{ //return to start
				next_city = cities[0];
				cost_to_node = TSPGraph[curr_city.get_node_num()][next_city.get_node_num()];
			}
			total += cost_to_node;

			System.out.println("CITY: " + curr_city.get_node_num() + ", COST TO CITY " + next_city.get_node_num() + " = " + cost_to_node);
		}
		System.out.println("Total cost: " + total);
	}

	/**
	* Implementing Fisher–Yates shuffle
	* Randomly shuffle the cities array to form a random solution
	*/
  	private void shuffleArray(){
    	Random rnd = new Random();
    	for (int i = cities.length - 1; i > 0; i--){
      		int index = rnd.nextInt(i + 1);
      		// Simple swap
      		City temp = cities[index];
      		cities[index] = cities[i];
      		cities[i] = temp;
    	}
  	}
}