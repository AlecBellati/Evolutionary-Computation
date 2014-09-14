package TTP.Thief;

import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Item;
import TTP.Thief.Knapsack;
import TTP.TTPInstance;

import java.util.Arrays;
import java.util.Random;

public class Alec {
    
    /** Best solution */
	/* The best solution found */
	private TTPSolution solution;
	/* The cost of the solution */
	private double solutionCost;
	
	
	/** TTP Variables */
	/* The cities in the problem */
	private City[] cities;
	/* The items in the problem */
	private Item[] items;
	/* Capacity of the knapsack */
	private double capacityOfKnapsack;
	
	/** Algorithm parameters */
	private final int POPULATION_SIZE = 50;
	private final int GENERATIONS = 1000;
	
	/** Randomiser */
	private Random rnd;
	
    /**
     * CONSTRUCTOR
     */
    public Alec(City[] cities, Item[] items, double capacityOfKnapsack) {
        this.cities = cities;
		this.items = items;
		this.capacityOfKnapsack = capacityOfKnapsack;
		
		solutionCost = 0.0;
		rnd = new Random();
		
		for (int i = 0; i < cities.length; i++){
			cities[i].setupEdgePheromones();
		}
		for (int i = 0; i < items.length; i++){
			items[i].setupPheromone();
		}
    }
    
    /**
     * Entry point for the thief (your program)
     * @param: TTPInstance: The instance this is a part of
     */
    public void getSolution(TTPInstance instance) {
        System.out.println("Alec: Running Program");
		
		for (int g = 0; g < GENERATIONS; g++){
			TTPSolution[] popTTP = new TTPSolution[POPULATION_SIZE];
			Individual[] popTSP = new Individual[POPULATION_SIZE];
			Knapsack[] popKnap = new Knapsack[POPULATION_SIZE];
			
			// Get the solutions
			for (int i = 0; i < POPULATION_SIZE; i++){
				popTSP[i] = getTSPSolution();
				popKnap[i] = getKnapsackSolution(popTSP[i]);
				popTTP[i] = new TTPSolution(popTSP[i].getCitiesByID(), popKnap[i].getItemsByID());
			}
			
			// Increase the edge pheromone values
			City currCity, nextCity;
			for (int i = 0; i < popTSP.length; i++){
				nextCity = popTSP[i].getCityByIndex(0);
				for (int j = 1; j < popTSP[i].getNumCities(); j++){
					currCity = nextCity;
					nextCity = popTSP[i].getCityByIndex(j);
					
					currCity.increasePheromone(nextCity.getNodeNum());
					nextCity.increasePheromone(currCity.getNodeNum());
				}
			}
			// Decrease and fix the edge pheromone values
			for (int i = 0; i < cities.length; i++){
				for (int j = 0; j < cities.length; j++){
					cities[i].decreasePheromone(j);
					cities[i].fixPheromone(j);
				}
			}
			
			// Increase the item pheromone values
			for (int i = 0; i < popKnap.length; i++){
				for (int j = 0; j < popKnap[i].getNumItems(); j++){
					popKnap[i].getItem(j).increasePheromone();
				}
			}
			// Decrease and fix the item pheromone values
			for (int i = 0; i < popKnap.length; i++){
				for (int j = 0; j < items.length; j++){
					items[j].decreasePheromone();
					items[j].fixPheromone();
				}
			}
			
			// Evaluate the solutions to get the best
			TTPSolution bestSol = null;
			double bestCost = 0.0;
			double currCost;
			for (int i = 0; i < popTTP.length; i++){
				instance.evaluate(popTTP[i]);
				currCost = popTTP[i].getObjective();
				
				if (currCost > bestCost){
					bestSol = popTTP[i];
					bestCost = currCost;
				}
			}
			
			// Print the cost of the best solution found
			System.out.println("****" + g + ": " + bestCost + "****");
			
			// Check if a overall better solution has been found;
			if (bestCost > solutionCost){
				solution = bestSol;
				solutionCost = bestCost;
			}
			
		}
    }
    
    /**
     * Function that gets called when time is up
	 * @return: TSPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {
        System.out.println("Alec: Timer expired function, return the best solution");
        /*
        //check to see if null no solution has been found
        if(TSPSolution == null) {
            System.out.println("Alec: TSPSolution has not been found");
            return null;
        }
        
        //create TTP variables
        int[] tspTour = TSPSolution.getCitiesByID();
        int[] packingPlan = knapsack.getItemsByID();
        
        //create a new solution
        solution = new TTPSolution(tspTour, packingPlan);
        */
        //exit
        return solution;
    }
	
	/**
	 * Get an tsp solution
	 * @return: Individual: The tsp solution
	 */
	private Individual getTSPSolution(){
		Individual tspSol = new Individual(cities.length + 1);
		
		// Set the first city
		tspSol.setCity(0, cities[0]);
		
		// Get the path
		Boolean[] taken = new Boolean[cities.length];
		Arrays.fill(taken, false);
		taken[0] = true;
		
		City currentCity = cities[0];
		int totalProb, i, j;
		double next, current, total;
		
		for (i = 1; i < cities.length; i++){
			// Get the total pheromone values for each valid edge
			totalProb = 0;
			for (j = 1; j < cities.length; j++){
				if (!taken[j]){
					totalProb += currentCity.getEdgePheromone(j);
				}
			}
			
			// Get the next city
			next = rnd.nextDouble();
			total = 0;
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
		}
		
		// Set the last city
		tspSol.setCity(cities.length, cities[0]);
		
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
			// Decide whether to take each item in the current city
			if (i == cities.length){
				cityItems = cities[0].getItems();
			}
			else{
				nodeNum = tspSol.getCityByIndex(i).getNodeNum();
				cityItems = cities[(nodeNum - 1)].getItems();
			}
			
			for (int j = 0; j < cityItems.length; j++){
				if (cityItems[j] != null){
					if (knapSol.getCurrentCapacity() >= cityItems[j].getWeight()){
						itemProb = cityItems[j].getPheromone();
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
	
}