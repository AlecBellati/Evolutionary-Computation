import java.util.ArrayList;
import java.util.Random;

public class Individual{

	/** Contains the cities with their associated weights */
	private City[] cities;
	
	/** Cost of the current solution represented by this individual */
	private double cost;
	
	/** Flag as to whether the cost needs to be calculated */
	private boolean costFound;
	

	/**
	* CONSTRUCTOR
	* Takes given TSP Graph and assigns to local variable
	* Initialises solution array list and generate a set of random solutions
	* @param City[] cities - array of cities, current solution
	* @param boolean random - if true, will shuffle the supplied City array
	*/
	public Individual(City[] cities, boolean random){
		this.cities = cities.clone();

		if(random){
			generateRandomSolution();
		}
		
		costFound = false;
	}

	/**
	* ALTERNATE CONSTRUCTOR
	* Initialises an empty array
	*/
	public Individual(int size){
		cities = new City[size];
		
		costFound = false;
	}

	/*
	* Gets the basic solution set and puts it into the City[]
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
		
		costFound = false;
  	}

	/**
	* Get the cost of the current solution
	* @return double - cost of current solution
	*/
	public double getCost(){
		// If the cost has not been found, calculate it
		if(!costFound){
			calculateCost();
			
			costFound = true;
		}
		
		return cost;
	}

	/**
	* Return the number of cities in the solution set
	* @return int - number of cities in this solution
	*/
	public int getNumCities(){
		return cities.length;
	}

	/**
	* Return the City array
	* @return City[] - solution set in the form of a City[]
	*/
	public City[] getCities(){
		return cities;
	}

	/**
	* Set a new City array
	* @param City[] new_cities - the new array of cities
	*/
	public void setCities(City[] new_cities){
		cities = new_cities;
		
		costFound = false;
	}

	/**
	* Set a particular city in the City array
	* @param int index - array index position
	* @param City city - City to be set at the index position
	*/
	public void setCity(int index, City city){
		cities[index] = city;
		
		costFound = false;
	}

	/**
	* Get the edge cost of a City from the array
	* Queries the City object
	* @param int city_from - start city
	* @param int city_to - end city
	* @return double - cost to travel between cities
	*/
	public double getEdgeCost(int city_from, int city_to){
		City from = getCityByNumber(city_from);
		if(from != null){
			return from.getEdge(city_to);
		}
		return 0;
	}

	/**
	* Get the city in the set with the City node number that is given
	* @param int node_num - City node number to be found
	* @return City - City with supplied node number, null if not found
	*/
	public City getCityByNumber(int node_num){
		for(int i = 0; i < cities.length; i++){
			if(node_num == cities[i].getNodeNum()){
				return cities[i];
			}
		}
		return null;
	}

	/**
	* Return the City based on its index position in the City array
	* @param int index - index position of array
	* @return City - City at position index in the array
	*/
	public City getCityByIndex(int index){
		return cities[index];
	}
	
	/**
	 * Calculate the cost of the current solution
	 */
	private void calculateCost(){
		cost = 0;
		for(int i = 0; i < cities.length; i++){
			City curr_city = cities[i];
			City next_city;
			double cost_to_node = 0;

			if(i != cities.length-1){
				next_city = cities[i+1];
			}else{ //return to start
				next_city = cities[0];
			}
			cost_to_node = curr_city.getEdge(next_city.getNodeNum());
			cost += cost_to_node;
		}

		return cost;
	}
}