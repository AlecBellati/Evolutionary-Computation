/*
 * Evolutionary Computation
 * COMP SCI 4095
 * Assignment two
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

package TTP.Thief.Travel;

import TTP.Thief.Travel.Item;

public class City {
	
	/* Class variables */
	private int node;				// City number, considered its name
	private boolean visited;		// Determines if node has been visited in a solution
    private int itemCount;
    private int numItems;
    private int numEdges;
    private Item[] items;           // List of items that could be stolen from this city
    private double xCoord, yCoord;
	
	/** Attributes for 'Alec' algorithm */
	private double[] edgePheromone;
	private final double PHEROMONE_MIN = 1.0;
	private final double PHEROMONE_MAX = 99999.0;
    private final double PHEROMONE_DECREASE_RATE = 50.0;
    
    /**
	 * Constructor of a City.
	 * @param: int: Number to characterize this city (considered its name)
	 * @param: int: Number of cities it connects to
     * @param: int: Number of items in the city
	 * @param: double: The x coordinate of this city
	 * @param: double: The y coordinate of this city
	 */
	public City(int cityNode, int _numEdges, int _numItems, double x, double y) {
		node = cityNode;
        visited = false;
        numEdges = _numEdges;
        xCoord = x;
        yCoord = y;
        
        //handle Items
        itemCount = 0;
        numItems = _numItems;
        items = new Item[numItems];
	}

	/**
	 * Return the x coordinate of this city
	 * @return: The x coordinate of the city
	 */
	public double getX(){
		return xCoord;
	}

	/**
	 * Return the y coordinate of this city
	 * @return: The y coordinate of the city
	 */
	public double getY(){
		return yCoord;
	}
    
    /**
     * Add an item to the city
     * @param: Item: Item to add to the city
     */
    public void addItem(Item item) {
        items[itemCount] = item;
        itemCount++;
    }
    
    /**
     * Set an item in this city as taken
     * @param: Item: Item to mark as taken
     */
    public void takeItem(Item item) {
        for(int i = 0; i < items.length; i++) {
            if(items[i].getItemNum() == item.getItemNum()) {
                items[i].takeItem(true);
                return;
            }
        }
    }

    /**
     * Get the Items from this City
     * @return: Item[]: The Items from this City
     */
    public Item[] getItems() {
        return items;
    }
	
	/**
     * Get the number of Items in this City
     * @return: int: The number of Items in this City
     */
    public int getItemCount() {
        return itemCount;
    }
    
    /**
     * Checks to see if the item is contained in this city
     * @param: boolean: True if contains item, else false
     */
	public boolean containsItem(Item item) {
        for(int i = 0; i < items.length; i++) {
            if(items[i] == item) {
                return true;
            }
        }
        return false;
    }
	/**
	 * Gets the "name" of the node
	 * @return: int: The name of the node
	 */
	public int getNodeNum() {
		return node;
	}

	/**
	 * Sets whether a node has been visited.
	 * @param: boolean: Whether the node has been visited
	 */
	public void hasBeenVisited(boolean b) {
		visited = b;
	}
	
	/**
	 * Indicates whether the node has been visited
	 * @return: boolean: Whether the node has been visited
	 */
	public boolean visited() {
		return visited;
	}
	
	/**
	 * Combine the properties of a city
	 * @param: City: The city to get the properties for
	 * @return: String: The city's properties
	 */
	public String toString(City toCity) {
		int nextCity = toCity.getNodeNum();
		return "CITY: " + node + "\n";
	}

    /**
    * Used to simulate the distance matrix
    * @param: City: The city the distance is being found to
	* @return: double: The distance between the two cities
    */
    public double distance(City city) {
        double result = 0;
        result = Math.sqrt(Math.pow(xCoord-city.getX(), 2) + Math.pow(yCoord-city.getY(), 2));
        
        return result;
    }
	
	/* Methods for the 'Alec' algorithm */
	
    /**
	 * Gets the pheromone of the edge to 'city'
	 * @param: int: The city num of the end of the edge
	 * @return: double: The pheromone value of the edge
	 */
	public double getEdgePheromone(int city) {
		return edgePheromone[city];
	}
	
	/**
	 * Set up the pheromone values for the city
	 */
	public void setupEdgePheromones() {
		edgePheromone = new double[numEdges];
		for (int i = 0; i < numEdges; i++){
			if (i != (node)){
				edgePheromone[i] = PHEROMONE_MIN;
			}
		}
	}
	
	/**
	 * Set a pheromone values to a city
	 * @param: int: The city num at the city at the end of the edge
     * @param: double: The amount to set the pheromone value
     */
	public void setEdgePheromone(int city, double pheromone) {
		edgePheromone[city] = pheromone;
	}
	
	/**
     * Increase the pheromone value of this edge to 'city'
	 * @param: int: The city num at the city at the end of the edge
     * @param: double: The amount the pheromone should be increased by
     */
    public void increasePheromone(int city, double amount){
        edgePheromone[city] = edgePheromone[city] + amount;
		
		if (edgePheromone[city] > PHEROMONE_MAX){
			edgePheromone[city] = PHEROMONE_MAX;
		}
    }
    
    /**
     * Decrease the pheromone value of this edge to 'city'
	 * @param: int: The city num of the city at the end of the edge
     */
    public void decreasePheromone(int city){
        edgePheromone[city] = edgePheromone[city] - PHEROMONE_DECREASE_RATE;
		
		if (edgePheromone[city] < PHEROMONE_MIN){
			edgePheromone[city] = PHEROMONE_MIN;
		}
    }
}