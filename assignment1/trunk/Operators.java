import java.util.Random;
import java.util.ArrayList;

public class Operators{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	*
	*
	*/
	public Operators(){
		rnd = new Random();
	}

	/**
	*
	*
	*/
	public City[][] order_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];

		return children;
	}

	/**
	*
	*
	*/
	public City[][] pmx_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];
		
		return children;
	}

	/**
	*
	*
	*/
	public City[][] cycle_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];

		ArrayList<String> crossoverA = new ArrayList<String>();
		ArrayList<String> crossoverB = new ArrayList<String>();

		int i = 0;
		boolean running = true;
		while(running){
			String crossA = "";
			String crossB = "";
			int j = 0;

			City current_node = parents[0][i];
			while(current_node.visited()){
				i++;
				if(i == parents[0].length){
					running = false;
					break;
				}
				current_node = parents[0][i];
			}
			i++;

			int node_num;
			while(!current_node.visited()){
				node_num = current_node.get_node_num();
				current_node.has_been_visited(true);
				if( j%2 == 0){
					crossA += (node_num+1) + ",";
					current_node = parents[1][node_num];
				}else{
					crossB += (node_num+1) + ",";
					current_node = parents[0][node_num];
				}

				//PROBLEM IS OBJECTS HAVE BEEN LINKED, need to clone!
				System.out.println(node_num+1);
				System.out.println(parents[1][7].visited());

				j++;
			}

			crossoverA.add(crossA);
			crossoverB.add(crossB);
		}

		for(int x = 0; x < crossoverA.size(); x++){
			System.out.println("A: " + crossoverA.get(x));
			System.out.println("B: " + crossoverB.get(x));
		}

		return children;
	}

	/**
	*
	*
	*/
	public City[][] edge_recombination(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];
    
		return children;
	}

	/**
	* TESTING ONLY
	*/
	public static void main(String[] args){
		Operators test = new Operators();
	}
}