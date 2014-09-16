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

import TTP.Thief.Travel.Item;

public class City {
	
	/* Class variables */
	private double[] edges;			// Edge weights - edges[1] indicates cost to travel to City 1
	private int node;				// City number, considered its name
	private boolean visited;		// Determines if node has been visited in a solution
    private int itemCount;
    private int numItems;
    private Item[] items;           // List of items that could be stolen from this city
	
	/** Attributes for 'Alec' algorithm */
	private double[] edgePheromone;
	private double[] increaseRate;
	private double[] decreaseRate;
	private final double PHEROMONE_MIN = 0.1;
	private final double PHEROMONE_MAX = 5.0;
    
    /**
	 * Constructor of a City.
	 * @param int cityNode - number to characterize this city (considered its name)
	 * @param int numEdges - number of cities it connects to
     * @param int numItems - number of items in the city
	 */
	public City(int cityNode, int numEdges, int _numItems) {
		node = cityNode;
		edges = new double[numEdges];
        visited = false;
        
        //handle Items
        itemCount = 0;
        numItems = _numItems;
        items = new Item[numItems];
	}
	
	/**
	 * Adds the edge and its cost to the edges array.
	 * @param int toCity - the city's name to add
	 * @param double edgeCost - the cost of the city to add
	 */
	public void addEdge(int toCity, double edgeCost) {
		edges[toCity] = edgeCost;
	}
    
    /**
     * Add an item to the city
     * @param: Item: item to add to the city
     */
    public void addItem(Item item) {
        items[itemCount] = item;
        itemCount++;
    }

    /**
     * Get the Items from this City
     * @return: Item[]: the Items from this City
     */
    public Item[] getItems() {
        return items;
    }
	
	/**
     * Get the number of Items in this City
     * @return: int: the number of Items in this City
     */
    public int getItemCount() {
        return itemCount;
    }
    
    /**
     * Checks to see if the item is contained in this city
     * @param: boolean true if contains item, else false
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
	 * @return: int node: the name of the node
	 */
	public int getNodeNum() {
		return node;
	}
	
	/**
	 * Gets the edge of the node
	 * @return: double edges[city]: the cost of the edge
	 */
	public double getEdge(int city) {
		return edges[city];
	}
	
	/**
	 * Sets whether a node has been visited.
	 * @param boolean b - whether the node has been visited
	 */
	public void hasBeenVisited(boolean b) {
		visited = b;
	}
	
	/**
	 * Indicates whether the node has been visited
	 * @return boolean visited - whether the node has been visited
	 */
	public boolean visited() {
		return visited;
	}
	
	/**
	 * Combine the properties of a city
	 * @param City toCity - the city to get the properties for
	 * @return String - the city's properties
	 */
	public String toString(City toCity) {
		int nextCity = toCity.getNodeNum();
		return "CITY: " + node + ",\t COST TO CITY " + nextCity + " = " + edges[nextCity];
	}
	
	/* Methods for the 'Alec' algorithm */
	
    /**
	 * Gets the pheromone of the edge to 'city'
	 * @param: int: the city num of the end of the edge
	 * @return: double edgePheromone[city]: the pheromone value of the edge
	 */
	public double getEdgePheromone(int city) {
		return edgePheromone[city];
	}
	
	/**
	 * Set up the pheromone values for the city
	 */
	public void setupEdgePheromones() {
		edgePheromone = new double[edges.length];
		increaseRate = new double[edges.length];
		decreaseRate = new double[edges.length];
		for (int i = 0; i < edges.length; i++){
			if (i != (node)){
				edgePheromone[i] = PHEROMONE_MIN;
				increaseRate[i] = 1.0 / edges[i];
				decreaseRate[i] = increaseRate[i] / 2.0;
			}
		}
	}
	
	/**
     * Increase the pheromone value of this edge to 'city'
	 * @param: int: The city num of the city at the end of the edge
     */
    public void increasePheromone(int city){
        edgePheromone[city] = edgePheromone[city] + increaseRate[city];
		
		if (edgePheromone[city] > PHEROMONE_MAX){
			edgePheromone[city] = PHEROMONE_MAX;
		}
    }
    
    /**
     * Decrease the pheromone value of this edge to 'city'
	 * @param: int: The city num of the city at the end of the edge
     */
    public void decreasePheromone(int city){
        edgePheromone[city] = edgePheromone[city] - decreaseRate[city];
		
		if (edgePheromone[city] < PHEROMONE_MIN){
			edgePheromone[city] = PHEROMONE_MIN;
		}
    }
    
}