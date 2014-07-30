
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
}