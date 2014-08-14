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
	public Individual(double[][] TSPGraph, City[] cities, boolean random){
		this.TSPGraph = TSPGraph.clone();
		this.cities = cities.clone();

		if(random){
			generateRandomSolution();
		}
	}

	/*
	* Gets the basic solution set
	* Returns a single array of cities
	* Should not generate any errors
	*/
	public void generateRandomSolution(){
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

	/**
	* Given a city, return its cost
	* Returns a double, does not generate errors
	*/
	public double getCost(){
		double total = 0;
		for(int i = 0; i < cities.length; i++){
			City curr_city = cities[i];
			City next_city;
			double cost_to_node = 0;

			if(i != cities.length-1){
				next_city = cities[i+1];
				cost_to_node = TSPGraph[curr_city.getNodeNum()][next_city.getNodeNum()];
			}else{ //return to start
				next_city = cities[0];
				cost_to_node = TSPGraph[curr_city.getNodeNum()][next_city.getNodeNum()];
			}
			total += cost_to_node;
		}

		return total;
	}

	/**
	* Return the number of cities in the solution set
	*/
	public int getNumCities(){
		return cities.length;
	}

	/**
	*
	*/
	public City[] getCities(){
		return cities;
	}

	/**
	*
	*/
	public void setCities(City[] new_cities){
		cities = new_cities;
	}

	/**
	*
	*/
	public void setCity(int index, City city){
		cities[index] = city;
	}

	/**
	*
	*/
	public double getEdgeCost(int city_from, int city_to){
		City from = getCityByNumber(city_from);
		if(from != null){
			return from.getEdge(city_to);
		}
		return 0;
	}

	/**
	* Return the number of cities in the solution set
	*/
	public City getCityByNumber(int node_num){
		for(int i = 0; i < cities.length; i++){
			if(node_num == cities[i].getNodeNum()){
				return cities[i];
			}
		}
		return null;
	}

	public City getCityByIndex(int index){
		return cities[index];
	}
    	
}