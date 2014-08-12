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
	* Performs Partially Mapped Crossover (PMX)
	* Determines the crossover between two parents and then interleaves
	* The children to form the children
	* @param City[][] - 2 solutions, considered the parents
    * @return City[][] - 2 children City[] solutions
	*/
	public City[][] pmx_crossover(City[][] parents){
		int length = parents[0].length;
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][length];
		
		for (int child = 0; child < children.length; child++){
			// Find the subset of position to crossover
			int posA = rnd.nextInt(length);
			int posB = rnd.nextInt(length);
			
			// Ensure that posA is less than  or equal to posB
			if (posA > posB){
				int posTemp = posA;
				posA = posB;
				posB = posTemp;
			}
			
			// Put all subset from parent 0 into the child
			// and find the values exclusive to each subset
			int subsetLength = posB - posA + 1;
			City[] parent0Exclusive = new City[subsetLength];
			int parent0ExclusiveLength = 0;
			City[] parent1Exclusive = new City[subsetLength];
			int parent1ExclusiveLength = 0;
			for (int i = posA; i <= posB; i++){
				// Put current value of parent 0 into child
				children[child][i] = parents[0][i];
				
				//check values of parents for exclusivity
				if (contains(parents[1], parents[0][i], posA, posB) == -1){
					parent0Exclusive[parent0ExclusiveLength] = parents[0][i];
					parent0ExclusiveLength++;
				}
				if (contains(parents[0], parents[1][i], posA, posB) == -1){
					parent1Exclusive[parent1ExclusiveLength] = parents[1][i];
					parent1ExclusiveLength++;
				}
				
			}
			
			// Fill in the remaining spots in the child
			int childIdx, exIdx;
			if ((posB + 1) >= length){
				childIdx = 0;
			}
			else{
				childIdx = posB + 1;
			}
			
			while (childIdx != posA){
				// Check if this spot was in the subset of parent 0
				exIdx = contains(parent0Exclusive, parents[1][childIdx], 0, parent0ExclusiveLength - 1);
				if (exIdx != -1){
					children[child][childIdx] = parent1Exclusive[exIdx];
					childIdx++;
				}
				// Add city from parent 1
				else{
					children[child][childIdx] = parents[1][childIdx];
					childIdx++;
				}
				
				// Make sure the index value is not out of bounds
				if (childIdx >= length){
					childIdx = 0;
				}
			}
			
		}
		
		return children;
	}
	
	/**
	* Checks if a subset of an array of cities contains a particular city
	* Will only return the first instance of the city
	* For operators usage only
	* @param City[] - an array of cities
    * @param City - the city that is being searched for
	* @param Integer - the first index to search from
    * @param Integer - the last index to search to
    * @return Integer - the index of the City found, -1 if not found
	*/
	private int contains(City[] cities, City searchKey, int first, int last){
		for (int i = first; i <= last; i++){
			if (cities[i].get_node_num() == searchKey.get_node_num()){
				return i;
			}
		}
		
		return -1;
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

		int i = 0, j = 0;
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
			//goes to a new node until returns to a visited node
			//this loop will start at parent A and then go to parent B 
			//before setting current_node to the next node in the cycle path in parent A
			while(!current_node.visited()){
				if(j%2 == 0){
					children[0][index] = parents[0][index];
					children[1][index] = parents[1][index];
				}else{
					children[0][index] = parents[1][index];
					children[1][index] = parents[0][index];
				}

				node_num = current_node.get_node_num();
				current_node.has_been_visited(true);
				//parent B
				node_num = parents[1][node_num].get_node_num();

				//parent A
				current_node = parents[0][node_num];
				index = node_num;
				j++;
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