package TTP.Thief;

import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.City;
import TTP.Thief.Knapsack;

import java.util.Arrays;
import java.util.Random;

public class Alec {
    
    /** Best solution */
	/* TSP Variables */
    private Individual TSPSolution;
	/* Knapsack problem variables */
    private Knapsack knapsack;
    /* Solution variable */
    private TTPSolution solution;
	
	/** Population */
	/* Population of TSP solutions */
	private Individual[] popTSP;
	/* Population of equivalent knapsack solutions */
	private Knapsack[] popKnap;
    
	/** TTP Variables */
	/* The cities in the problem */
	private City[] cities;
	/* Capacity of the knapsack */
	private int capacityOfKnapsack;
	
	/** Algorithm parameters */
	private final int POPULATION_SIZE = 50;
	private final int GENERATIONS = 1000;
	
	/** Randomiser */
	private Random rnd;
	
    /**
     * CONSTRUCTOR
     */
    public Alec(City[] cities, int capacityOfKnapsack) {
        this.cities = cities;
		this.capacityOfKnapsack = capacityOfKnapsack;
		
		rnd = new Random();
    }
    
    /**
     * Entry point for the thief (your program)
     */
    public void getSolution() {
        System.out.println("Alec: Running Program");
		
		for (int g = 0; g < GENERATIONS; g++){
			// Initialise the populations
			popTSP = new Individual[POPULATION_SIZE];
			popKnap = new Knapsack[POPULATION_SIZE];
			
			// Get the solutions
			for (int i = 0; i < POPULATION_SIZE; i++){
				
			}
			
			// Get the best solution(s) [unsure atm whether to get just a subset]
			
			
			// Update pheromone values
			
		}
    }
    
    /**
     * Function that gets called when time is up
     * @return: TSPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {
        System.out.println("Alec: Timer expired function, return the best solution");
        
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
	
}