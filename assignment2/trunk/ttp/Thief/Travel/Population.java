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


import TTP.Thief.Knapsack;

import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;

public class Population {
	
	/* Class variables */
	private ArrayList<Individual> solutionSet;		// Holds a set of City[] solutions
	private int numSolutions;											// Number of solutions this population holds
	private Random rnd;														// Random number generation
	private final double COST = 0.0000000005;			// For comparison of city costs
	
	/**
	 * Constructor of a population
	 * Used to extract multiple single solutions to form a population
	 * @param int size - number of solutions this population will hold
	 */
	public Population(int size) {
		numSolutions = size;
		solutionSet = new ArrayList<Individual>(size);
		rnd = new Random();
	}
	
	/**
	 * Create a deep copy of this population array
	 * @return Population - a deep copy of this object
	 */
	public Population clone() {
		Population clone = new Population(getSize());
		for (int i = 0; i < getSize(); i++) {
			clone.setSolution(i, solutionSet.get(i).clone());
		}
		return clone;
	}
	
	/**
	 * Add an individual to the solution set if not already a solution
	 * @param Individual individual - the individual to be added
	 */
	public void add(Individual individual) {
		if (checkInSolution(individual) == false) {
			solutionSet.add(individual);
		}
	}
	
	/**
	 * Add a set of individuals to the solution set if not already included
	 * @param Individual[] individuals - the set of individuals to be added
	 */
	public void addSet(Individual[] individuals) {
		for (int i = 0; i < individuals.length; i++) {
			if (individuals[i] != null) {
				if (checkInSolution(individuals[i]) == false) {
					solutionSet.add(individuals[i]);
				}
			}
		}
	}
	
	/**
	 * Add a population to the pre-existing population if not already included
	 * @param Population population - the population to be added
	 */
	public void addPopulation(Population population) {
		ArrayList<Individual> temp_list = population.getSolutionSet();
		for (int i = 0; i < temp_list.size(); i++) {
			if (temp_list.get(i) != null) {
				if (checkInSolution(temp_list.get(i)) == false) {
					solutionSet.add(temp_list.get(i));
				}
			}
		}
	}
	
	/**
	 * Remove an individual from the solution set.
	 * @param Individual individual - the individual to be removed from the set
	 */
	public void remove(Individual individual) {
		solutionSet.remove(individual);
	}
	
	/**
	 * Checks if an individual is in the solution set.
	 * @param Individual individual - the individual to be checked as a solution
	 * @return boolean bool - true if the individual is a solution already
	 */
	public boolean checkInSolution(Individual individual) {
		for (int i = 0; i < solutionSet.size(); i++) {
			if (Math.abs((solutionSet.get(i).getCost()) - individual.getCost()) < COST) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Gets a basic set of solutions - specified by the user
	 * Should not generate any errors
	 * @param double[][] TSPGraph - lookup table of city edges
	 * @param City[] cities - array of cities, current solution
	 */
	public void generateRandomSolutionSet(City[] cities) {
		solutionSet = new ArrayList<Individual>();
		for (int i = 0; i < numSolutions; i++) {
			solutionSet.add(new Individual(cities, true));
		}
	}
	
	/**
	 * Return the solution set
	 * @return Individual[] - solutions of length 'numSolutions'
	 */
	public ArrayList<Individual> getSolutionSet() {
		return solutionSet;
	}
	
	/**
	 * Set the city ArrayList from the supplied data
	 * @param ArrayList<Individual> - the solution array to assign to this population
	 */
	public void setSolutionSet(ArrayList<Individual> cities) {
		solutionSet = cities;
	}
	
	/**
	 * Get the solution at index
	 * @param int index - index position for solution retrieval
	 * @return Individual - a specific solution, provided by the index number
	 */
	public Individual getSolution(int index) {
		return solutionSet.get(index);
	}
	
	/**
	 * Get a random solution
	 * @return Individual - a random solution contained in solutionSet
	 */
	public Individual getRandomSolution() {
		int index = rnd.nextInt(numSolutions);
		return solutionSet.get(index);
	}
	
	/**
	 * Set the solution at the specific index position
	 * @param int index - index position to place solution
	 * @param Individual - solution to be inserted into the array at position index
	 */
	public void setSolution(int index, Individual individual) {
		solutionSet.add(index, individual);
	}
	
	/**
	 * Return size of the solution set
	 * @return int - size of this solution set
	 */
	public int getSize() {
		return solutionSet.size();
	}
	
	/**
	 * Sort the current solution set based on its each of their total costs
	 */
	public void sort() {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(solutionSet, new Comparator<Individual>() {
			@Override
			public int compare (Individual c1, Individual c2) {
				return (int)(c1.getCost() - c2.getCost());
			}
		});
	}
	
	/**
	 * Return the total cost of all solutions
	 * @return double - total cost of all solutions
	 */
	public double getTotalCost() {
		double cost = 0;
		for (int i = 0; i < getSize(); i++) {
			cost += solutionSet.get(i).getCost();
		}
		return cost;
	}
	
	/**
	 * Return the average cost of this solution
	 * @return double - average cost of this solution
	 */
	public double getAverageCost() {
		double cost = getTotalCost();
		
		return (cost/getSize());
	}
	
	/**
	 * Return the best tour from this population
	 * @return Individual - best solution from this population
	 */
	public Individual getBestSolution() {
		sort();
		return solutionSet.get(0);
	}
    
    /**
     * return the best tour based on TSP AND Knapsack
     *
     */
    public Individual getBestTTPSolution(double[][] TTPGraph, double maxSpeed, double minSpeed, Knapsack knapsack) {
        City thisCity = null, nextCity = null;
        double weight = 0;
        double netProfit = 0;
        
        Individual best = null;
        
        //get each individual
        for(Individual in : solutionSet) {
            int counter = 0;
            //get the list of cities
            City[] allCities = in.getCities();
            thisCity = allCities[0];
            for(int c = 1; c < allCities.length; c++){
                
                //get next city
                if((c+1) == allCities.length) {
                    nextCity = allCities[0];
                } else {
                    nextCity = allCities[c];
                }
                
                //for each item in the city
                for(Item current : thisCity.getItems()) {
                    
                    
                    //the temporary weight of the thief if taking the item
                    if(current != null) {
                        if(current.isTaken()) {
                            weight += current.getWeight();
                        }
                    }
                    //How much of the knapsack is full?
                    double weightRatio = weight / knapsack.getCapacity();
                    
                    //calculate the potential speed if taking the item
                    double tempSpeed = maxSpeed - (weight * ((maxSpeed - minSpeed)/knapsack.getCapacity()));
                    
                    //get the edge cost from the previous city to this city
                    double edgeCost = TTPGraph[thisCity.getNodeNum()][nextCity.getNodeNum()]/tempSpeed;
                    
                    //calculate the potential profit for the item
                    double grossProfit = 0;
                    if(current != null) {
                        if(current.isTaken()) {
                            grossProfit = current.getProfit();
                        }
                    }
                    double losses = edgeCost*knapsack.getRentingRatio();
                    double thisProfit = grossProfit - losses;
                    netProfit += thisProfit;
                    
                }
                
                //go to next city
                thisCity = nextCity;
            }
            in.setProfit(netProfit);

            //is this individual better?
            if(best == null) {
                best = in;
            } else {
                if(in.getProfit() > best.getProfit()) {
                    best = in;
                }
            }
            
            //reset for next solution
            thisCity = null;
            nextCity = null;
            weight = 0;
            netProfit = 0;
        }
        
        
        return best;
    }
	
	/**
	 * Return the size of the population
	 * @return int - size of current population
	 */
	public int size() {
		return solutionSet.size();
	}
	
	/**
	 * Prints out each individual in the population
	 */
	public void print() {
		for (int i = 0; i < solutionSet.size(); i++) {
			System.out.print(i + " = ");
			solutionSet.get(i).print();
		}
	}
}