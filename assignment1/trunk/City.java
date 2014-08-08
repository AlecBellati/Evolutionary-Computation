
public class City{

	private double[] edges; 
	private int node;
	private boolean visited;
	
	public City(int city_node, int num_edges){
		node = city_node;
		edges = new double[num_edges];
		visited = false;
	}

	public void add_edge(int to_city, double edge_cost){
		edges[to_city] = edge_cost;
	}

	public int get_node_num(){
		return node;
	}

	public double get_edge(int city){
		return edges[city];
	}

	public void has_been_visited(boolean b){
		visited = b;
	}

	public boolean visited(){
		return visited;
	}

	public String toString(City to_city){
		int next_city = to_city.get_node_num();
		return "CITY: " + node + ",\t COST TO CITY " + next_city + " = " + edges[next_city];
	}
}