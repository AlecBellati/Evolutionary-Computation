
public class City{

	private double[] edges; 
	private int node;
	
	public City(int city_node, int num_edges){
		node = city_node;
		edges = new double[num_edges];
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

	public String toString(City to_city){
		int next_city = to_city.get_node_num();
		return "CITY: " + node + ",\t COST TO CITY " + next_city + " = " + edges[next_city];
	}
}