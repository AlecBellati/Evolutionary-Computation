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
		
		solutionCost = Double.NEGATIVE_INFINITY;
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
			
			// Increase the edge pheromone values
			Boolean[][] edgeTaken = new Boolean[cities.length][];
			for (int i = 0; i < cities.length; i++){
				edgeTaken[i] = new Boolean[cities.length];
				Arrays.fill(edgeTaken[i], false);
			}
			City currCity, nextCity;
			Item[] cityItems;
			for (int t = 0; t < popTSP.length; t++){
				currCity = popTSP[t].getCityByIndex(0);
				for (int i = 1; i < popTSP[t].getNumCities(); i++){
					nextCity = popTSP[t].getCityByIndex(i);
					currCity.increasePheromone(nextCity.getNodeNum());
					edgeTaken[currCity.getNodeNum()][nextCity.getNodeNum()] = true;
					currCity = nextCity;
				}
			}
			// Decrease the edge pheromone values
			for (int i = 0; i < cities.length; i++){
				for (int j = 0; j < cities.length; j++){
					if (!edgeTaken[i][j]){
						cities[i].increasePheromone(j);
					}
				}
			}
			
			// Increase the item pheromone values
			Boolean[] itemTaken = new Boolean[items.length];
			Arrays.fill(itemTaken, false);
			Item currItem;
			for (int i = 0; i < popKnap.length; i++){
				for (int j = 0; j < popKnap[i].getNumItems(); j++){
					currItem = popKnap[i].getItem(j);
					currItem.increasePheromone();
					
					itemTaken[currItem.getItemNum()] = true;
				}
			}
			// Decrease and fix the item pheromone values
			for (int i = 0; i < popKnap.length; i++){
				for (int j = 0; j < items.length; j++){
					currItem = items[j];
					if(!itemTaken[currItem.getItemNum()]){
						currItem.decreasePheromone();
					}
				}
			}
			
			// Evaluate the solutions to get the best
			TTPSolution bestSol = null;
			
			
			double bestCost = Double.NEGATIVE_INFINITY;
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
			//bestSol.println();
			
			
			// Check if a overall better solution has been found
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
			cityItems = cities[(nodeNum - 1)].getItems();
			
			// Decide whether to take each item in the current city
			for (int j = 0; j < cityItems.length; j++){
				if (cityItems[j] != null){
					if (knapSol.getCurrentCapacity() >= cityItems[j].getWeight()){
						itemProb = items[cityItems[j].getItemNum()].getPheromone();
						
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
	 * Get the best amount of TTP solutions and set the best
	 * @param: TTPSolution[]: The TTP solutions
	 * @param: int: The number of solutions to save
	 * @param: TTPInstance: The TTP instance this is a part of
	 * @return: TTPSolution[]: The TTP solutions that are being saved
	 *//*
	private TTPSolution[] getBestSolutions(TTPSolution[] population, TTPInstance instance){
		// Evaluate the cost of the solutions
		for (int i = 0; i < population.length; i++){
			instance.evaluate(population[i])
		}
	}*/
	
}