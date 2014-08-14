
public class City{

	private double[] edges; 
	private int node;
	private boolean visited;
	
	public City(int city_node, int num_edges){
		node = city_node;
		edges = new double[num_edges];
		visited = false;
	}

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