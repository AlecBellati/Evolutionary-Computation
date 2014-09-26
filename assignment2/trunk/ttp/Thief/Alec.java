package TTP.Thief;

import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Item;
import TTP.Thief.Knapsack;
import TTP.TTPInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Alec {
    
    /** Best solution */
	/* The best solution found */
	private TTPSolution solution;
	/* The cost of the solution */
	private double bestCost;
	
	/** TTP Variables */
	/* The cities in the problem */
	private City[] cities;
	/* The items in the problem */
	private Item[] items;
	/* Capacity of the knapsack */
	private long capacityOfKnapsack;
	
	/** Algorithm parameters */
	private final int POPULATION_SIZE = 100;
	private final int SELECTION_SIZE = 50;
	private final int GENERATIONS = 10000;
	
	/** Randomiser */
	private Random rnd;
	
    /**
     * CONSTRUCTOR
     */
    public Alec(City[] cities, Item[] items, long capacityOfKnapsack) {
        this.cities = cities;
		this.items = items;
		this.capacityOfKnapsack = capacityOfKnapsack;
		
		bestCost = Double.NEGATIVE_INFINITY;
		rnd = new Random();
    }
    
    /**
     * Entry point for the thief (your program)
     * @param: TTPInstance: The instance this is a part of
     */
    public void getSolution(TTPInstance instance) {
        System.out.println("Alec: Running Program");
		
		double currBest = bestCost;
		
		//for (int g = 0; g < GENERATIONS; g++){
		for (int g = 0; true; g++){
			Individual[] popTSP = new Individual[POPULATION_SIZE];
			Knapsack[] popKnap = new Knapsack[POPULATION_SIZE];
			TTPSolution[] popTTP = new TTPSolution[POPULATION_SIZE];
			int[] packingPlan;
			
			// Get the solutions
			for (int i = 0; i < POPULATION_SIZE; i++){
				popTSP[i] = getTSPSolution();
				popKnap[i] = getKnapsackSolution(popTSP[i]);
				packingPlan = popKnap[i].getPackingPlan(popTSP[i], items.length);
				popTTP[i] = new TTPSolution(popTSP[i].getCitiesByID(), packingPlan);
			}
			
			// Update the solutions and the best solution
			getBestSolutions(popTTP, popTSP, popKnap, instance);
			
			// Update the pheromone values
			updatePheromone(popTTP, popTSP, popKnap);
			
			// Print the cost of the best solution found
			if (bestCost > currBest){
				System.out.println("****" + g + ": " + bestCost + "****");
				popTTP[0].println();
				popTSP[0].print();
				popKnap[0].print();
				
				currBest = bestCost;
			}
			
		}
    }
    
    /**
     * Function that gets called when time is up
	 * @return: TSPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {
        System.out.println("Alec: Timer expired function, return the best solution");
        
        //check to see if null no solution has been found
        if(solution == null) {
            System.out.println("Alec: TTPSolution has not been found");
            return null;
        }
        
        //exit
        return solution;
    }
	
	/**
	 * Get an tsp solution
	 * @return: Individual: The tsp solution
	 */
	private Individual getTSPSolution(){
		Individual tspSol = new Individual(cities.length);
		
		// Set the first city
		tspSol.setCity(0, cities[0]);
		
		// Get the path
		Boolean[] taken = new Boolean[cities.length];
		Arrays.fill(taken, false);
		taken[0] = true;
		
		City currentCity = cities[0];
		int i, j;
		double totalProb, next, current, total;
		
		for (i = 1; i < cities.length; i++){
			// Get the total pheromone values for each valid edge
			totalProb = 0.0;
			for (j = 1; j < cities.length; j++){
				if (!taken[j]){
					totalProb += currentCity.getEdgePheromone(j);
				}
			}
			
			// Get the next city
			next = rnd.nextDouble();
			total = 0.0;
			j = 0;
			while (j < cities.length && total <= next){
				j++;
				
				if (!taken[j]){
					current = currentCity.getEdgePheromone(j) / totalProb;
					total += current;
				}
			}
			
			currentCity = cities[j];
			tspSol.setCity(i, currentCity);
			taken[j] = true;
		}
		
		return tspSol;
	}
	
	/**
	 * Get an pack plan for a tsp solution
	 * @param: Individual: The tsp solution
	 * @return: Knapsack: The packing plan for the tsp solution
	 */
	private Knapsack getKnapsackSolution(Individual tspSol){
		Knapsack knapSol = new Knapsack(capacityOfKnapsack);
		
		// Find the items to take on the tour starting at the back of the tour
		Item[] cityItems;	
		double itemProb, takeProb;
		int nodeNum;
		for (int i = tspSol.getNumCities() - 1; i > 0; i--){
			nodeNum = tspSol.getCityByIndex(i).getNodeNum();
			cityItems = cities[nodeNum].getItems();
			
			// Decide whether to take each item in the current city
			for (int j = 0; j < cityItems.length; j++){
				if (cityItems[j] != null){
					if (knapSol.getCurrentCapacity() >= cityItems[j].getWeight()){
						itemProb = items[cityItems[j].getItemNum()].getProbability();
						
						takeProb = rnd.nextDouble();
						if (itemProb > takeProb){
							knapSol.addItem(cityItems[j]);
						}
					}
				}
			}
		}
		
		return knapSol;
	}
	
	/**
	 * Trim TTP solutions to have only the best amount and set the best
	 * @param: TTPSolution[]: The TTP solutions
	 * @param: TTPInstance: The TTP instance this is a part of
	 * @return: TTPSolution[]: The TTP solutions that are being saved
	 */
	private void getBestSolutions(TTPSolution[] popTTP, Individual[] popTSP, Knapsack[] popKnap, TTPInstance instance){
		ArrayList<TTPSolution> orderedTTP = new ArrayList<TTPSolution>();
		ArrayList<Individual> orderedTSP = new ArrayList<Individual>();
		ArrayList<Knapsack> orderedKnap = new ArrayList<Knapsack>();
		
		// Fill the array lists in order of highest cost to lowest
		int size = 0;
		int pos;
		double cost;
		for (int i = 0; i < popTTP.length; i++){
			// Add the value
			instance.evaluate(popTTP[i]);
			cost = popTTP[i].getObjective();
			pos = 0;
			while (pos < size){
				if (cost > orderedTTP.get(pos).getObjective()){
					orderedTTP.add(pos, popTTP[i]);
					orderedTSP.add(pos, popTSP[i]);
					orderedKnap.add(pos, popKnap[i]);
					
					pos = size;
				}
				pos++;
			}
			if (pos == size){
				orderedTTP.add(pos, popTTP[i]);
				orderedTSP.add(pos, popTSP[i]);
				orderedKnap.add(pos, popKnap[i]);
			}
			size++;
			
			// If the selection size is exceed, trim the last element
			if  (size > SELECTION_SIZE){
				orderedTTP.remove(size - 1);
				orderedTSP.remove(size - 1);
				orderedKnap.remove(size - 1);
				size--;
			}
			
			
		}
		
		// Convert them to arrays
		popTTP = orderedTTP.toArray(new TTPSolution[SELECTION_SIZE]);
		popTSP = orderedTSP.toArray(new Individual[SELECTION_SIZE]);
		popKnap = orderedKnap.toArray(new Knapsack[SELECTION_SIZE]);
		
		// Update the best solution
		if (popTTP[0].getObjective() > bestCost){
			solution = popTTP[0];
			bestCost = solution.getObjective();
		}
	}
	
	/**
	 * Update the pheromone values of the edges and items
	 * @param: TTPSolution[]: The population TTP solutions
	 * @param: Individual[]: The population of corresponding paths
	 * @param: Knapsack[]: The population of corresponding knapsacks
	 */
	private void updatePheromone(TTPSolution[] popTTP, Individual[] popTSP, Knapsack[] popKnap){
		for (int solution = 0; solution < popTTP.length; solution++){
			// Get the increase rate for this solution
			double cost = popTTP[solution].getObjective();
			double increaseRate = bestCost / cost;
			
			// Update the edge pheromones
			for (int i = 0; i < popTSP.length; i++){
				int currPath[] = popTSP[i].getCitiesByID();
				for (int j = 0; j < currPath.length - 1; j++){
					cities[currPath[j]].increasePheromone(currPath[j + 1], increaseRate);
				}
			}
			
			// Update the item pheromones
			for (int i = 0; i < popKnap.length; i++){
				int currKnap[] = popKnap[i].getItemsByID();
				for (int j = 0; j < currKnap.length; j++){
					items[currKnap[j]].increasePheromone(increaseRate);
				}
			}
		}
	}
	
	
}