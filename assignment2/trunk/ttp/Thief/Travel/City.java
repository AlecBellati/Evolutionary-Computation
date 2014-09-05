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
    private Item[] items;           // List of items that could be stolen from this city
	
	/**
	 * Constructor of a City.
	 * @param int city_node - number to characterize this city (considered its name)
	 * @param int num_edges - number of cities it connects to
	 */
	public City(int cityNode, int numEdges, int numItems) {
		node = cityNode;
		edges = new double[numEdges];
		visited = false;
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
     *
     *
     */
    public void addItem(Item item) {
        
    }
	
	/**
	 * Gets the "name" of the node
	 * @return int node - the name of the node
	 */
	public int getNodeNum() {
		return node;
	}
	
	/**
	 * Gets the edge of the node
	 * @return double edges[city] - the cost of the edge
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
}