import java.util.Random;
import java.util.ArrayList;

public class Operators{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	* CONSTRUCTOR
	* Initialise the random number generator (may not be needed)
	*/
	public Operators(){
		rnd = new Random();
	}

	/**
	*
	* @param City[][]
    * @return City[][]
	*/
	public City[][] order_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];

		return children;
	}

	/**
	*
	* @param City[][]
    * @return City[][]
	*/
	public City[][] pmx_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];
		
		return children;
	}

	/**
	* Performs a cycle crossover
	* Determines the crossover between two parents and then interleaves
	* The children to form the children
	* @param City[][] - 2 solutions, considered the parents
    * @return City[][] - 2 children City[] solutions
	*/
	public City[][] cycle_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];
		//holds the crossover indicies used in the second stage to generate children
		ArrayList<String> crossover = new ArrayList<String>();

		int i = 0;
		int node_num = 0;
		boolean running = true;
		//generates the cycles, adding them to the crossover array list
		while(i < parents[0].length){
			City current_node = parents[0][i];
			//find and unvisited parent node until the end of the solution set
			while(current_node.visited() && i < (parents[0].length-1)){
				i++;
				current_node = parents[0][i];
			}

			int index = i; i++;
			String crosses = "";
			//goes to a new node until returns to a visited node
			//this loop will start at parent A and then go to parent B 
			//before setting current_node to the next node in the cycle path in parent A
			while(!current_node.visited()){
				node_num = current_node.get_node_num();
				crosses += index + ",";

				current_node.has_been_visited(true);
				//parent B
				node_num = parents[1][node_num].get_node_num();

				//parent A
				current_node = parents[0][node_num];
				index = node_num;
			}
			//end of this while loop means the end of one cycle
			
			if(crosses.length() > 0){
				crossover.add(crosses.substring(0, crosses.length()-1));
			}
		}

		int cycle_index = 0;
		//generates the children objects
		for(int y = 0; y < crossover.size(); y++){
			String[] inidices = crossover.get(y).split(",");
			for(int z = 0; z < inidices.length; z++){
				cycle_index = Integer.parseInt(inidices[z]);
				//every alternate cycle, add to the opposite children object
				if(y%2 == 0){
					children[0][cycle_index] = parents[0][cycle_index];
					children[1][cycle_index] = parents[1][cycle_index];
				}else{
					children[0][cycle_index] = parents[1][cycle_index];
					children[1][cycle_index] = parents[0][cycle_index];
				}
			}
		}
		//reset the "visited" variable in each City object
		set_visited(children[0]);

		return children;
	}

    /**
     * For operator usages only
     * Given a City solution array, return its "visited" variable to false
     */
    private void set_visited(City[] city){
        for(int j = 0; j < city.length; j++){
            city[j].has_been_visited(false);
        }
    }

	/**
	*
	* @param City[][]
    * @return City[][]
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