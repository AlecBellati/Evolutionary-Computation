import java.util.Random;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

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
	* @param Individual parentA
    * @param Individual parentB
    * @return Individual - the child generate from the amalgamation of the two parents
	*/
	public Individual[] orderCrossover(Individual parentA, Individual parentB){
		Individual[] child = new Individual[2];

		return child;
	}

	/**
	* Performs Partially Mapped Crossover (PMX)
	* Determines the crossover between two parents and then interleaves
	* The children to form the children
    * @param Individual parentA
    * @param Individual parentB
    * @return Individual[] - the two children generated from the amalgamation of the two parents
	*/
	public Individual[] pmxCrossover(Individual parentA, Individual parentB){
		int length = parentA.getNumCities();
		//this assume all solutions are of the same length!
		Individual[] children = new Individual[2];
		
		for (int child = 0; child < children.length; child++){
			Individual newSol = new Individual(length);
			
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
			Individual parentAEx = new Individual(subsetLength);
			int parentAExLength = 0;
			Individual parentBEx = new Individual(subsetLength);
			int parentBExLength = 0;
			for (int i = posA; i <= posB; i++){
				// Put current value of parent 0 into child
				newSol.setCity(i, parentA.getCityByIndex(i));
				
				//check values of parents for exclusivity
				if (contains(parentB, parentA.getCityByIndex(i), posA, posB) == -1){
					parentAEx.setCity(parentAExLength, parentA.getCityByIndex(i));
					parentAExLength++;
				}
				if (contains(parentA, parentB.getCityByIndex(i), posA, posB) == -1){
					parentBEx.setCity(parentBExLength, parentB.getCityByIndex(i));
					parentBExLength++;
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
				exIdx = contains(parentAEx, parentB.getCityByIndex(childIdx), 0, parentAExLength - 1);
				if (exIdx != -1){
					newSol.setCity(childIdx, parentBEx.getCityByIndex(exIdx));
					childIdx++;
				}
				// Add city from parent 1
				else{
					newSol.setCity(childIdx, parentB.getCityByIndex(childIdx));
					childIdx++;
				}
				
				// Make sure the index value is not out of bounds
				if (childIdx >= length){
					childIdx = 0;
				}
			}
			
			children[child] = newSol;
		}
		
		return children;
	}
	
	/**
	* Checks if a subset of an array of cities contains a particular city
	* Will only return the first instance of the city
	* For operators usage only
	* @param Individual - a solution
    * @param City - the city that is being searched for
	* @param Integer - the first index to search from
    * @param Integer - the last index to search to
    * @return Integer - the index of the City found, -1 if not found
	*/
	private int contains(Individual cities, City searchKey, int first, int last){
		for (int i = first; i <= last; i++){
			if (cities.getCityByIndex(i).getNodeNum() == searchKey.getNodeNum()){
				return i;
			}
		}
		
		return -1;
	}
	

	/**
	* Performs a cycle crossover
	* Determines the crossover between two parents and then interleaves
	* The parents to form the children
    * @param Individual parentA
    * @param Individual parentB
    * @return Individual[] - the children generated from the amalgamation of the two parents
	*/
	public Individual[] cycleCrossover(Individual parentA, Individual parentB){
        
		//this assume all solutions are of the same length!
		Individual[] children = new Individual[2];
        children[0] = new Individual(parentA.getNumCities());
        children[1] = new Individual(parentA.getNumCities());
		//holds the crossover indicies used in the second stage to generate children
		ArrayList<String> crossover = new ArrayList<String>();

		int i = 0, j = 0;
		int node_num = 0;
		boolean running = true;
		//generates the cycles, adding them to the crossover array list
		while(i < parentA.getNumCities()){
			City current_node = parentA.getCityByIndex(i);
			//find and unvisited parent node until the end of the solution set
			while(current_node.visited() && i < (parentA.getNumCities()-1)){
				i++;
				current_node = parentA.getCityByIndex(i);
			}

			int index = i; i++;
			//goes to a new node until returns to a visited node
			//this loop will start at parentA and then go to parentB 
			//before setting current_node to the next node in the cycle path in parentA
			while(!current_node.visited()){
				if(j%2 == 0){
					children[0].setCity(index, parentA.getCityByIndex(index));
					children[1].setCity(index, parentB.getCityByIndex(index));
				}else{
					children[0].setCity(index, parentB.getCityByIndex(index));
					children[1].setCity(index, parentA.getCityByIndex(index));
				}

				node_num = current_node.getNodeNum();
				current_node.hasBeenVisited(true);
				//parent B
				node_num = parentB.getCityByIndex(node_num).getNodeNum();

				//parent A
				current_node = parentA.getCityByIndex(node_num);
				index = node_num;
				j++;
			}
		}
        
		//reset the "visited" variable in each City object
		setVisited(children[0]);
        setVisited(children[1]);

		return children;
	}

    /**
    * For operator usages only
    * Given a City solution array, return its "visited" variable to false
    * @param Individual city
    */
    private void setVisited(Individual city){
        for(int j = 0; j < city.getNumCities(); j++){
            city.getCityByIndex(j).hasBeenVisited(false);
        }
    }

	/**
	* Performs Edge Recombination Operator (tested and verified to be correct)
    * @param Individual parentA
    * @param Individual parentB
    * @return Individual - the child generate from the amalgamation of the two parents
	*/
	public Individual edgeRecombination(Individual parentA, Individual parentB){
        
        //create parents array of cities like it was originally - thanks Alec <_<
        City[][] parents = new City[2][parentA.getNumCities()];
        parents[0] = parentA.getCities();
        parents[1] = parentB.getCities();
        
        //Create solution array
		City[][] solution = new City[1][parents[0].length];
        
        //create edge recombination table
        Hashtable<City, ArrayList<ElementEdge>> edgeListing = new Hashtable<City, ArrayList<ElementEdge>>();
        int cityCount = 0;
        //for both individuals
        for(int i = 0; i < parents.length; i++) {
            //go through and add the elements to the edgeListing table
            for(City c : parents[i]) {
                //get the left and right indicies
                int left = cityCount-1;
                int right = (cityCount+1)%(parents[i].length);
                
                //catch edge case for left values
                if(left < 0) {
                    left = parents[i].length-1;
                }
                
                
                //add elements to the Hashtable
                //if already there, just handle adding edges
                if(edgeListing.containsKey(c)) {
                    ArrayList<ElementEdge> arr = edgeListing.get(c);
                    boolean doneLeft = false, doneRight = false;
                    
                    //check to see if Left or Right is in the list of ElementEdges
                    for(ElementEdge ee : arr) {
                        
                        //if left added, increase count
                        if(ee.get_city() == parents[i][left]) {
                            ee.count_up();
                            doneLeft = true;
                        }
                    
                        //if right added, increase count
                        if (ee.get_city() == parents[i][right]) {
                            ee.count_up();
                            doneRight = true;
                        }
                    }
                    
                    //if left not added, add it now
                    if(!doneLeft) {
                        arr.add(new ElementEdge(parents[i][left]));
                    }
                    
                    //if right not added, add it now
                    if(!doneRight) {
                        arr.add(new ElementEdge(parents[i][right]));
                    }
                    
                    //update table entry
                    edgeListing.put(c, arr);
                    
                    
                //else add new entry and add edges
                } else {
                    ArrayList<ElementEdge> e = new ArrayList<ElementEdge>();
                    e.add(new ElementEdge(parents[i][left]));
                    e.add(new ElementEdge(parents[i][right]));
                    edgeListing.put(c, e);
                }
                //increase cityCount
                cityCount++;
            }
            //reset for the second city
            cityCount = 0;
        }
        
        
        /*Uncomment to print out the table and the parents*/
        //printTable(edgeListing, parents);
        
        
        //insert random starting city
        int indiv = rnd.nextInt(2);
        int startPT = rnd.nextInt(parents[0].length);
        solution[0][0] = parents[indiv][startPT];
        
        //recombine elements into new solution
        for(int idx = 0; idx < solution[0].length-1; idx++) {
            
            /* Print out for debugging purposes
            System.out.println("CurrentCity = " + solution[0][idx].getNodeNum() + ", IDX = " + idx);
            //print solution
            for(int i = 0; i < solution.length; i++) {
                System.out.print("solutin[" + i + "] = [");
                for(int j = 0; j < solution[i].length; j++) {
                    if(solution[i][j] != null) {
                        System.out.print(" "+solution[i][j].getNodeNum()+" ");
                    }
                }
                System.out.println("]");
            }
            
            printTable(edgeListing, parents);
            */
            
            
            //remove all references to current City from edgeListing
            Set<City> keySet = edgeListing.keySet();
            for(City c : keySet) {
                ArrayList<ElementEdge> edges = edgeListing.get(c);
                
                for(int i = edges.size()-1; i >= 0; i--) {
                    if(edges.get(i).get_city() == solution[0][idx]) {
                        edges.remove(i);
                    }
                }
            }
            
            //select the next element
            City nextElem = solution[0][idx]; // has to be initialised or compiler will throw hissy fit
            boolean foundNext = false;
            ArrayList<ElementEdge> edges = edgeListing.get(solution[0][idx]);
            
            //1 common edge
            for(ElementEdge ee : edges) {
                if (ee.get_count() > 1) {
                    nextElem = ee.get_city();
                    foundNext = true;
                    break;
                }
            }
            
            //if common edge found no solutions, look for shortest list
            if(!foundNext) {
                
                //2 entry with shortest list
                int numEdges = Integer.MAX_VALUE;
                ArrayList<City> candidate = new ArrayList<City>();
                
                //loop through all edges and find the candidate(s) that have the shortest list
                for(ElementEdge ee : edges) {
                    
                    //if new shortest list found, update numEdges and candidate
                    if(edgeListing.get(ee.get_city()).size() < numEdges) {
                        numEdges = edgeListing.get(ee.get_city()).size();
                        candidate.clear();
                        candidate.add(ee.get_city());
                    
                    //if an equal shortest list found, add to the potential candidates
                    } else if(edgeListing.get(ee.get_city()).size() == numEdges) {
                        candidate.add(ee.get_city());
                        
                    //otherwise, it's the last element and needs to be added
                    } else if (edgeListing.get(ee.get_city()).size() == 0){
                        candidate.add(ee.get_city());
                    }
                }
                
                //check to see if unique next solution has been found
                if(candidate.size() == 1) {
                    nextElem = candidate.get(0);
                    foundNext = true;

                //else if more than one element with the same length use random city from list of possible candidates
                } else if(candidate.size() > 1){
                    nextElem = candidate.get(rnd.nextInt(candidate.size()));
                    foundNext = true;
                }
            }
            
            //remove old element from hash table
            edgeListing.remove(solution[0][idx]);
            
            //if there is still no solution for the next City, select one randomly from the remaining set
            if(!foundNext) {
                Set<City> randSet = edgeListing.keySet();
                int rand = rnd.nextInt(randSet.size());
                int count = 0;
                for(City c : randSet) {
                    if(count == rand) {
                        nextElem = c;
                        break;
                    }
                    count++;
                }
            }
            
            //Update the solution array with the next element to process
            solution[0][idx+1] = nextElem;
        }
        
        
        //print parents for debugging (print shows final result)
        /*
        for(int i = 0; i < parents.length; i++) {
            System.out.print("Parents[" + i + "] = [");
            for(int j = 0; j < parents[i].length; j++) {
                System.out.print(" "+parents[i][j].getNodeNum()+" ");
            }
            System.out.println("]");
        }
        
        //print child soluion
        for(int i = 0; i < solution.length; i++) {
            System.out.print("Solutin[" + i + "] = [");
            for(int j = 0; j < solution[i].length; j++) {
                System.out.print(" "+solution[i][j].getNodeNum()+" ");
            }
            System.out.println("]");
        }
        */
        
        //return new solution
		return new Individual(solution[0] , false);
	}
    
    /**
     * Test printout to show what the Hashtable looks like
     * DON'T CHANGE THE SIGNATURE - IT'S A SPECIFIC TESTING FUNCTION FOR EDGE RECOMBINATION
     */
    private void printTable(Hashtable<City, ArrayList<ElementEdge>> hash, City[][] parents) {
        for(int i = 0; i < parents.length; i++) {
            System.out.print("Parents[" + i + "] = [");
            for(int j = 0; j < parents[i].length; j++) {
                System.out.print(" "+parents[i][j].getNodeNum()+" ");
            }
            System.out.println("]");
        }
        
        Set<City> keySet = hash.keySet();
        
        for(City c : keySet) {
            ArrayList<ElementEdge> edges = hash.get(c);
            System.out.print(String.format("%2d      |", c.getNodeNum()));
            for(ElementEdge ee : edges) {
                if(ee.get_count() > 1) {
                    System.out.print(String.format("%2d+ ", ee.get_city().getNodeNum()));
                } else {
                    System.out.print(String.format("%2d  ", ee.get_city().getNodeNum()));
                }
            }
            System.out.println();
        }
    }
}