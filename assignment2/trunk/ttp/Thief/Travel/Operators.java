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


import java.util.Random;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.List;

public class Operators {
	
	/* Class variables */
	private Random rnd;																		// Random number generator
	private final double INVER_OVER_PROBABILITY = 0.02; 	// InverOver city selection probability
	
	/**
	 * Constructor of the Operators
	 * Initialise the random number generator (may not be needed)
	 */
	public Operators() {
		rnd = new Random();
	}
	
	/**
	 * Performs the orderCrossover operation.
	 * Determines the crossover between two parents and then interleaves
	 * The parents to form the children
	 * @param Individual parentA
	 * @param Individual parentB
	 * @return Individual[] - the children generated from the amalgamation of the two parents
	 */
	public Individual[] orderCrossover(Individual parentA, Individual parentB) {
		Individual[] children = new Individual[2];
		
		// Choose random part of parents to copy
		int length = parentA.getNumCities();
        int posA = 0;
        int posB = 0;
        
        // Ensure first city isn't selected
        while (posA == 0 || posB == 0) {
            posA = rnd.nextInt(length);
            posB = rnd.nextInt(length);
        }
		
        // Ensure that posA is less than  or equal to posB
		if (posA > posB){
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		}
		int subsetLength = posB - posA + 1;
		

        // Create children
        // Store copied values so they don't get copied again
		Individual childA = new Individual(length);
		Individual childB = new Individual(length);
		ArrayList<Integer> valuesCopiedA = new ArrayList<Integer>();
		ArrayList<Integer> valuesCopiedB = new ArrayList<Integer>();
        
        // Copy first cities over so they don't change
        childA.setCity(0, parentA.getCityByIndex(0));
        childB.setCity(0, parentB.getCityByIndex(0));
        valuesCopiedA.add(parentA.getCityByIndex(0).getNodeNum());
        valuesCopiedB.add(parentB.getCityByIndex(0).getNodeNum());
        
        // Copy subset from parentA to childA and parentB to childB
		for (int i = posA; i <= posB; i++) {
			childA.setCity(i, parentA.getCityByIndex(i));
			childB.setCity(i, parentB.getCityByIndex(i));
			valuesCopiedA.add(parentA.getCityByIndex(i).getNodeNum());
			valuesCopiedB.add(parentB.getCityByIndex(i).getNodeNum());
		}
		
		// Fill in remaining spots in Child A
		int spotsToFill = length-subsetLength;
		int spotsFilled = 0;
		
		int childIndex;
		int parentIndex;
		
		// Ensure counters stay in range and ignore city[0]
		if (posB + 1 == length) {
			childIndex = 1;
			parentIndex = 1;
		} else {
			childIndex = posB + 1;
			parentIndex = posB + 1;
		}
		
		while (spotsFilled < spotsToFill) {
			// If current value wasn't copied before, include it in the child
			Integer value = parentB.getCityByIndex(parentIndex).getNodeNum();
			
			if (!valuesCopiedA.contains(value)) {
				childA.setCity(childIndex, parentB.getCityByIndex(parentIndex));
				
				// Increment counters
				spotsFilled++;
				if (childIndex == length - 1) {
					childIndex = 1;
				} else {
					childIndex++;
				}
			}
			
			if (parentIndex == length - 1) {
				parentIndex = 1;
			} else {
				parentIndex++;
			}
		}
		
		// Fill in remaining spots in Child B
		spotsFilled = 0;
		
		// Ensure counters stay in range and ignore city[0]
		if (posB + 1 == length) {
			childIndex = 1;
			parentIndex = 1;
		} else {
			childIndex = posB + 1;
			parentIndex = posB + 1;
		}
		
		while (spotsFilled < spotsToFill) {
			// If current value wasn't copied before, include it in the child
			Integer value = parentA.getCityByIndex(parentIndex).getNodeNum();
			
			if (!valuesCopiedB.contains(value)) {
				childB.setCity(childIndex, parentA.getCityByIndex(parentIndex));
				// Increment counters
				spotsFilled++;
				if (childIndex == length - 1) {
					childIndex = 1;
				} else {
					childIndex++;
				}
			}
			
			if (parentIndex == length - 1) {
				parentIndex = 1;
			} else {
				parentIndex++;
			}
		}
		
		children[0] = childA;
		children[1] = childB;
		
		return children;
	}
	
	/**
	 * Performs Partially Mapped Crossover (PMX)
	 * Determines the crossover between two parents and then interleaves
	 * The children to form the children
	 * @param Individual parentA
	 * @param Individual parentB
	 * @return Individual[] - the two children generated from the amalgamation of the two parents
	 */
	public Individual[] pmxCrossover(Individual parentA, Individual parentB) {
		int length = parentA.getNumCities();
		//this assume all solutions are of the same length!
		Individual[] children = new Individual[2];
		
		for (int child = 0; child < children.length; child++) {
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
			Individual parentBEx = new Individual(subsetLength);
			int parentAExLength = 0;
			int parentBExLength = 0;
			
			for (int i = posA; i <= posB; i++) {
				// Put current value of parent 0 into child
				newSol.setCity(i, parentA.getCityByIndex(i));
				
				//check values of parents for exclusivity
				if (contains(parentB, parentA.getCityByIndex(i), posA, posB) == -1) {
					parentAEx.setCity(parentAExLength, parentA.getCityByIndex(i));
					parentAExLength++;
				}
				if (contains(parentA, parentB.getCityByIndex(i), posA, posB) == -1) {
					parentBEx.setCity(parentBExLength, parentB.getCityByIndex(i));
					parentBExLength++;
				}
				
			}
			
			// Fill in the remaining spots in the child
			int childIdx, exIdx;
			if ((posB + 1) >= length) {
				childIdx = 0;
			}
			else {
				childIdx = posB + 1;
			}
			
			while (childIdx != posA) {
				// Check if this spot was in the subset of parent 0
				exIdx = contains(parentAEx, parentB.getCityByIndex(childIdx), 0, parentAExLength - 1);
				if (exIdx != -1) {
					newSol.setCity(childIdx, parentBEx.getCityByIndex(exIdx));
					childIdx++;
				}
				// Add city from parent 1
				else {
					newSol.setCity(childIdx, parentB.getCityByIndex(childIdx));
					childIdx++;
				}
				
				// Make sure the index value is not out of bounds
				if (childIdx >= length) {
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
	private int contains(Individual cities, City searchKey, int first, int last) {
		for (int i = first; i <= last; i++) {
			if (cities.getCityByIndex(i).getNodeNum() == searchKey.getNodeNum()) {
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
	public Individual[] cycleCrossover(Individual parentA, Individual parentB) {
		int size = parentA.getNumCities();
		// Choose random starting place
		int index = rnd.nextInt(size);
		
		Individual childA = parentA.clone();
		Individual childB = parentB.clone();
		
		// Holds the stored indices
		ArrayList<Integer> visited = new ArrayList<Integer>(size);
		ArrayList<Integer> indices = new ArrayList<Integer>(size);
		
		int j = 1;
		while (visited.size() < size) {
			indices.add(index);
			
			City currentNode = parentB.getCityByIndex(index);
			index = parentA.getCityIndex(currentNode.getNodeNum());
			
			//get the indicies of the cities contained within a cycle
			while (index != indices.get(0)) {
				indices.add(index);
				//parent B city
				currentNode = parentB.getCityByIndex(index);
				//parent A city index
				index = parentA.getCityIndex(currentNode.getNodeNum());
			}
			
			j++;
			//every alternate iteration, place the cycle cities into the opposite child
			if (j%2 != 0) {
				for (int i = 0; i < indices.size(); i++) {
					int pos = indices.get(i);
					City temp = childA.getCityByIndex(pos);
					childA.setCity(pos, childB.getCityByIndex(pos));
					childB.setCity(pos, temp);
				}
			}
			
			//add all the indicies copied over to the visited array
			visited.addAll(indices);
			index = (indices.get(0) + 1) % size;
			while (visited.contains(index) && visited.size() < size) {
				index++;
				//we are at the end, go back to the start
				if (index >= size) {
					index = 0;
				}
			}
			indices.clear();
		}
		
		return new Individual[]{childA, childB};
	}
	
	/**
	 * For operator usages only
	 * Given a City solution array, return its "visited" variable to false
	 * @param Individual city
	 */
	private void setVisited(Individual city) {
		for (int j = 0; j < city.getNumCities(); j++) {
			city.getCityByIndex(j).hasBeenVisited(false);
		}
	}
	
	/**
	 * Performs Edge Recombination Operator (tested and verified to be correct)
	 * @param Individual parentA
	 * @param Individual parentB
	 * @return Individual - the child generate from the amalgamation of the two parents
	 */
	public Individual edgeRecombination(Individual parentA, Individual parentB) {
		
		// Create parents array of cities
		City[][] parents = new City[2][parentA.getNumCities()];
		parents[0] = parentA.getCities();
		parents[1] = parentB.getCities();
		
		// Create solution array
		City[][] solution = new City[1][parents[0].length];
		
		// Create edge recombination table
		Hashtable<City, ArrayList<ElementEdge>> edgeListing = new Hashtable<City, ArrayList<ElementEdge>>();
		int cityCount = 0;
		// For both individuals go through and add the elements to the edgeListing table
		for (int i = 0; i < parents.length; i++) {
			for (City c : parents[i]) {
				// Get the left and right indicies
				int left = cityCount-1;
				int right = (cityCount+1)%(parents[i].length);
				
				// Catch edge case for left values
				if(left < 0) {
					left = parents[i].length-1;
				}
				
				// Add elements to the Hashtable
				// If already there, just handle adding edges
				if (edgeListing.containsKey(c)) {
					ArrayList<ElementEdge> arr = edgeListing.get(c);
					boolean doneLeft = false, doneRight = false;
					
					// Check to see if Left or Right is in the list of ElementEdges
					for (ElementEdge ee : arr) {
						
						// If left added, increase count
						if (ee.getCity() == parents[i][left]) {
							ee.countUp();
							doneLeft = true;
						}
						
						// If right added, increase count
						if (ee.getCity() == parents[i][right]) {
							ee.countUp();
							doneRight = true;
						}
					}
					
					// If left not added, add it now
					if (!doneLeft) {
						arr.add(new ElementEdge(parents[i][left]));
					}
					
					// If right not added, add it now
					if (!doneRight) {
						arr.add(new ElementEdge(parents[i][right]));
					}
					
					// Update table entry
					edgeListing.put(c, arr);
					
					// Else add new entry and add edges
				} else {
					ArrayList<ElementEdge> e = new ArrayList<ElementEdge>();
					e.add(new ElementEdge(parents[i][left]));
					e.add(new ElementEdge(parents[i][right]));
					edgeListing.put(c, e);
				}
				// Increase cityCount
				cityCount++;
			}
			// Reset for the second city
			cityCount = 0;
		}
        
		// Insert random starting city
		int indiv = rnd.nextInt(2);
		int startPT = 0;
		solution[0][0] = parents[indiv][startPT];
		
		// Recombine elements into new solution
		for (int idx = 0; idx < solution[0].length-1; idx++) {
			
			// Remove all references to current City from edgeListing
			Set<City> keySet = edgeListing.keySet();
			for (City c : keySet) {
				ArrayList<ElementEdge> edges = edgeListing.get(c);
				
				for (int i = edges.size()-1; i >= 0; i--) {
					if (edges.get(i).getCity() == solution[0][idx]) {
						edges.remove(i);
					}
				}
			}
			
			// Select the next element
			City nextElem = solution[0][idx];
			boolean foundNext = false;
			ArrayList<ElementEdge> edges = edgeListing.get(solution[0][idx]);
			
			// One common edge
			for(ElementEdge ee : edges) {
				if (ee.getCount() > 1) {
					nextElem = ee.getCity();
					foundNext = true;
					break;
				}
			}
			
			// If common edge found no solutions, look for shortest list
			if (!foundNext) {
				
				//Two entry with shortest list
				int numEdges = Integer.MAX_VALUE;
				ArrayList<City> candidate = new ArrayList<City>();
				
				// Loop through all edges and find the candidate(s) that have the shortest list
				for (ElementEdge ee : edges) {
					
					// If new shortest list found, update numEdges and candidate
					if (edgeListing.get(ee.getCity()).size() < numEdges) {
						numEdges = edgeListing.get(ee.getCity()).size();
						candidate.clear();
						candidate.add(ee.getCity());
						
						// If an equal shortest list found, add to the potential candidates
					} else if(edgeListing.get(ee.getCity()).size() == numEdges) {
						candidate.add(ee.getCity());
						
						// Otherwise, it's the last element and needs to be added
					} else if (edgeListing.get(ee.getCity()).size() == 0) {
						candidate.add(ee.getCity());
					}
				}
				
				// Check to see if unique next solution has been found
				if (candidate.size() == 1) {
					nextElem = candidate.get(0);
					foundNext = true;
					
					// Else if more than one element with the same length use random city from list of possible candidates
				} else if (candidate.size() > 1) {
					nextElem = candidate.get(rnd.nextInt(candidate.size()));
					foundNext = true;
				}
			}
			
			// Remove old element from hash table
			edgeListing.remove(solution[0][idx]);
			
			// If there is still no solution for the next City, select one randomly from the remaining set
			if (!foundNext) {
				Set<City> randSet = edgeListing.keySet();
				int rand = rnd.nextInt(randSet.size());
				int count = 0;
				for (City c : randSet) {
					if (count == rand) {
						nextElem = c;
						break;
					}
					count++;
				}
			}
			
			// Update the solution array with the next element to process
			solution[0][idx+1] = nextElem;
		}

		// Return new solution
		return new Individual(solution[0] , false);
	}
	
	/**
	 * Test printout to show what the Hashtable looks like
	 * @param Hashtable<City, ArrayList<ElementEdge>> hash - the hashtable to print
	 * @param City[][] parents - the cities to print
	 */
	private void printTable(Hashtable<City, ArrayList<ElementEdge>> hash, City[][] parents) {
		for (int i = 0; i < parents.length; i++) {
			System.out.print("Parents[" + i + "] = [");
			for (int j = 0; j < parents[i].length; j++) {
				System.out.print(" "+parents[i][j].getNodeNum()+" ");
			}
			System.out.println("]");
		}
		
		Set<City> keySet = hash.keySet();
		
		for (City c : keySet) {
			ArrayList<ElementEdge> edges = hash.get(c);
			System.out.print(String.format("%2d      |", c.getNodeNum()));
			for (ElementEdge ee : edges) {
				if (ee.getCount() > 1) {
					System.out.print(String.format("%2d+ ", ee.getCity().getNodeNum()));
				} else {
					System.out.print(String.format("%2d  ", ee.getCity().getNodeNum()));
				}
			}
			System.out.println();
		}
	}
  
	/**
	 * Performs inver-over on a given solution
	 * @param Individual - The solution of cities to be mutated
	 * @param Population - The Population that Individual is from
	 * @return Individual - The new solution to replace the old one
	 */
	public Individual inverOver(Individual individual, Population population) {
		// Create a copy of the individual
		Individual newInd = individual.clone();
		int size = individual.getNumCities();
		
		// Get the index of the starting city
		int index = rnd.nextInt(size);
		
		boolean running = true;
		int nextIndex;
		double prob;
		Individual otherInd;
		City currCity, nextCity;
		Mutators mutator = new Mutators();
		
		while (running) {
			// Get the next city
			nextIndex = index;
			otherInd = newInd;
			prob = rnd.nextDouble();
			while (nextIndex == index) {
				// Get the next city from within the same individual
				if (prob <= INVER_OVER_PROBABILITY) {
					nextIndex = rnd.nextInt(size);
				}
				// Get the next city from within a different individual
				else {
					while (otherInd == individual) {
						otherInd = population.getRandomSolution();
					}
					currCity = newInd.getCityByIndex(index);
					nextCity = otherInd.getNextCityByNumber(currCity.getNodeNum());
					nextIndex = newInd.getCityIndex(nextCity.getNodeNum());
				}
			}
			
			// Check whether the next city is connected to the current city
			if ((index + 1) == nextIndex) {
				running = false;
			} else if (index == (newInd.getNumCities() - 1) && nextIndex == 0) {
				running = false;
			}
			// Otherwise inverse the subset
			else {
				if (index == (size - 1)){
					index = 0;
				} else {
					index++;
				}
				mutator.inverseSubset(newInd, index, nextIndex);
			}
			
		}
		
		// Check whether to keep the new Individual
		if (newInd.getCost() < individual.getCost()) {
			individual = newInd;
		}
		
		return individual;
	}
	
	/**
	 * Prints the cities within an individual.
	 * @param Individual individual - the individual to be printed
	 */
	private void printInline(Individual individual) {
		System.out.print("[ ");
		for (int i = 0; i < individual.getNumCities(); i++) {
			if (individual.getCityByIndex(i) != null) {
				if (individual.getCityByIndex(i).getNodeNum() < 10) {
					System.out.print(" " + individual.getCityByIndex(i).getNodeNum() + " ");
				} else {
					System.out.print(individual.getCityByIndex(i).getNodeNum() + " ");
				}
			} else {
				System.out.print("-1 ");
			}
		}
		System.out.println("]");
	}
}