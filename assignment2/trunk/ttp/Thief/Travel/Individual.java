/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
package TTP.Thief.Travel;


import java.util.ArrayList;
import java.util.Random;

public class Individual {
	
	/* Class variables */
	private City[] cities;				// Contains cities and their weights
	private double cost;					// Cost of current individual's current solution
	private boolean costFound;		// Indicates whether the cost needs to be calculated
    private double profit;
	
	
	/**
	 * Constructor of an Individual.
	 * Takes given TSP Graph and assigns to local variable
	 * Initialises solution array list and generate a set of random solutions
	 * @param City[] cities - array of cities, current solution
	 * @param boolean random - if true, will shuffle the supplied City array
	 */
	public Individual(City[] cities, boolean random) {
		this.cities = cities.clone();
		if (random) {
			generateRandomSolution();
		}
		costFound = false;
	}
	
	/**
	 * Constructor of an Individual. 
	 * Initialises an empty array
	 */
	public Individual(int size) {
		cities = new City[size];
		costFound = false;
	}
	
	/**
	 * Create a deep copy of the cities array
	 * @return Individual - a deep copy of this object;
	 */
	public Individual clone() {
		return new Individual(cities.clone(), false);
	}
	
	/*
	 * Gets the basic solution set and puts it into the City[]
	 */
	public void generateRandomSolution() {
		//will not modify city[0]
		Random rnd = new Random();
		for (int i = cities.length - 1; i > 0; i--) {
			int index = rnd.nextInt(cities.length-1)+1;
			// Simple swap
			if(index != i) {
				City temp = cities[index];
				cities[index] = cities[i];
				cities[i] = temp;
			}
		}
		costFound = false;
	}
	
	/**
	 * Get the cost of the current solution
	 * @return double - cost of current solution
	 */
	public double getCost() {
		// If the cost has not been found, calculate it
		if(!costFound){
			calculateCost();
			costFound = true;
		}
		return cost;
	}
	
    /**
     * Set the profit of this individual
     * @param: double: profit of this individual
     */
    public void setProfit(double _profit) {
        profit = _profit;
    }
    
    /**
     * get the profit of this individual
     * @return: double: profit of this individual
     */
    public double getProfit() {
        return profit;
    }
    
	/**
	 * Return the number of cities in the solution set
	 * @return int - number of cities in this solution
	 */
	public int getNumCities() {
		return cities.length;
	}
	
	/**
	 * Return the City array
	 * @return City[] - solution set in the form of a City[]
	 */
	public City[] getCities() {
		return cities;
	}
    
    /**
     * Return a representation of this individual by node number
     * Assignment requires start node to be at the end too
     * @return: int[]: array of cities by node number
     */
    public int[] getCitiesByID() {
        //create an int[] of city IDs
        int[] nodes = new int[cities.length+1];
        for(int i = 0; i < cities.length; i++) {
			nodes[i] = cities[i].getNodeNum();
        }
        nodes[nodes.length-1] = cities[0].getNodeNum();
        
        return nodes;
    }
	
	/**
	 * Set a new City array
	 * @param City[] new_cities - the new array of cities
	 */
	public void setCities(City[] new_cities) {
		cities = new_cities;
		costFound = false;
	}
	
	/**
	 * Set a particular city in the City array
	 * @param int index - array index position
	 * @param City city - City to be set at the index position
	 */
	public void setCity(int index, City city) {
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
	public double getEdgeCost(int city_from, int city_to) {
		City from = getCityByNumber(city_from);
		if (from != null) {
			return from.getEdge(city_to);
		}
		return 0;
	}
	
	/**
	 * Get the city in the set with the City node number that is given
	 * @param int node_num - City node number to be found
	 * @return City - City with supplied node number, null if not found
	 */
	public City getCityByNumber(int node_num) {
		int i = getCityIndex(node_num);
		if(i != -1) {
			return cities[i];
		}
		return null;
	}
	
	/**
	 * Return the City based on its index position in the City array
	 * @param int index - index position of array
	 * @return City - City at position index in the array
	 */
	public City getCityByIndex(int index) {
		return cities[index];
	}
	
	/**
	 * Get the index of city in the set with the City node number that is given
	 * @param int node_num - City node number to be found
	 * @return City - Index of the City with supplied node number, -1 if not found
	 */
	public int getCityIndex(int node_num) {
		for (int i = 0; i < cities.length; i++) {
			if (node_num == cities[i].getNodeNum()) {
				return i;
			}
		}
		return -1;
	}
	/**
	 * Get the next city in the set to the City node number that is given
	 * @param int node_num - City node number of the previous City to be found
	 * @return City - City following the City with supplied node number, null if not found
	 */
	public City getNextCityByNumber(int node_num) {
		int i = getCityIndex(node_num);
		
		// Get the next city
		if (i != -1) {
			if ((i + 1) >= cities.length) {
				return cities[0];
			} else {
				return cities[i + 1];
			}
		}
		
		return null;
	}
	
	/**
	 * Calculate the cost of the current solution
	 */
	private void calculateCost() {
		cost = 0;
		for (int i = 0; i < cities.length; i++) {
			City curr_city = cities[i];
			City next_city;
			double cost_to_node = 0;
			
			if (i != cities.length - 1) {
				next_city = cities[i+1];
			} else {
				//return to start
				next_city = cities[0];
			}
			cost_to_node = curr_city.getEdge(next_city.getNodeNum());
			cost += cost_to_node;
		}
	}
	
	/**
	* Print an Individual solution in-line
	* This format is easier to read when comparing solutions (for debugging)
	*/
	public void print() {
		System.out.print("[ ");
		for (int j = 0; j < cities.length; j++) {
			System.out.print(cities[j].getNodeNum() + " ");
		}
		System.out.println("]");
	}
}