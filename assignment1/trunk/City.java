public class City{

	/** Contains the edge weights, edges[1] indicates cost to travel to City 1 */
	private double[] edges; 
	/** This nodes city number, considered its name */
	private int node;
	/** Used to determine if this node has been visited in a solution */
	private boolean visited;
	
	/**
	* CONSTRUCTOR
	* Initialise variables
	* @param int city_node - number to characterize this city (considered its name)
	* @param int num_edges - number of cities it connects to
	*/
	public City(int city_node, int num_edges){
		node = city_node;
		edges = new double[num_edges];
		visited = false;
	}


	/***********************************
	******* GETTERS AND SETTERS ********
	***********************************/

	public void addEdge(int to_city, double edge_cost){
		edges[to_city] = edge_cost;
	}

	public int getNodeNum(){
		return node;
	}

	public double getEdge(int city){
		return edges[city];
	}

	public void hasBeenVisited(boolean b){
		visited = b;
	}

	public boolean visited(){
		return visited;
	}

	public String toString(City to_city){
		int next_city = to_city.getNodeNum();
		return "CITY: " + node + ",\t COST TO CITY " + next_city + " = " + edges[next_city];
	}
}