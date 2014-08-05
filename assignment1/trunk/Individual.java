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
	* Returns a single array of cities
	* Should not generate any errors
	*/
	public City[] get_single_solution(){
		shuffleArray();
		return cities.clone();
	}

	/**
	* Given a city, return its cost
	* Returns a double, does not generate errors
	*/
	public double get_cost(City[] single_city){
		double total = 0;
		for(int i = 0; i < single_city.length; i++){
			City curr_city = single_city[i];
			City next_city;
			double cost_to_node = 0;

			if(i != single_city.length-1){
				next_city = single_city[i+1];
				cost_to_node = TSPGraph[curr_city.get_node_num()][next_city.get_node_num()];
			}else{ //return to start
				next_city = single_city[0];
				cost_to_node = TSPGraph[curr_city.get_node_num()][next_city.get_node_num()];
			}
			total += cost_to_node;
		}

		return total;
	}

	/**
	* Return the number of cities in the solution set
	*/
	public int get_num_cities(){
		return cities.length;
	}

	/**
	* Implementing Fisher–Yates shuffle
	* Randomly shuffle the cities array to form a random solution
	*/
  	private void shuffleArray(){
    	Random rnd = new Random();
    	for (int i = cities.length - 1; i > 0; i--){
      		//int index = rnd.nextInt(i + 1);
      		int index = rnd.nextInt(cities.length-1);
      		// Simple swap
      		City temp = cities[index];
      		cities[index] = cities[i];
      		cities[i] = temp;
    	}
  	}
}