package TTP.Thief;

import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Control;
import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.Item;
import TTP.Thief.Knapsack;

import java.lang.Math;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


public class Matt {
    
    //TSP Variables
    private Individual TSPSolution;
    private Control control;
    private City[] cities;
    private ArrayList<Item> items;
    
    //Knapsack problem variables
    private Knapsack knapsack;
    
    //Solution variable
    private TTPSolution solution;
    
    //TTP Variables
    private double[][] TTPGraph;
    private double minSpeed, maxSpeed;
    private double capacityOfKnapsack;
    private double rentingRatio;
    
    /**
     * Constructor for Thief
     *
     *
     *
     * I'mma do this later
     *
     *
     *
     */
    public Matt(City[] _cities, Item[] allItems, double[][] _TTPGraph, double _minSpeed, double _maxSpeed, double _capacityOfKnapsack, double _rentingRatio) {
        //Setup variables
        cities = _cities;
        items = new ArrayList<Item>(Arrays.asList(allItems));
        TTPGraph = _TTPGraph;
        minSpeed = _minSpeed;
        maxSpeed = _maxSpeed;
        capacityOfKnapsack = _capacityOfKnapsack;
        rentingRatio = _rentingRatio;
        
        //create Controller
        control = new Control();
        
        //create new knapsack
        knapsack = new Knapsack(capacityOfKnapsack);
    }
    
    /**
     * Entry point for the thief
     */
    public void getSolution() {
        
        //1. Sort all items based on their profit (high to low)
        sortItems();
        
        //2. Find all/most profitable items and put them in the knapsack
        stealProfitableItems();
        knapsack.print();
        
        //3. Solve TSP for Knapsack cities to maximise profit
        runTSP(3);
        
        //4. Solve TSP for all cities that are not profitable
        
        //5. Combine Knapsack TSP and non-profit TSP into TSPSolution
        
    }
    
    /**
	 * Sort the Items array based on their percieved profit to weight ratio (high to low)
	 */
	public void sortItems() {
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(items, new Comparator<Item>() {
			@Override
			public int compare(Item c1, Item c2) {
				return c1.compareRatio(c2);
			}
		});
	}
    
    /**
     * Fill the knapsack with as many profitable items as possible
     */
    public void stealProfitableItems() {
        ArrayList<Item> profit = new ArrayList<Item>();
        boolean profitable = false;
        
        //tracking variables
        double totalProfit = 0;
        City prevCity = cities[0], currCity;
        
        //go through each item and see if any are profitable
        for(int i = 0; i < items.size(); i++) {
           
            //current Item's details
            Item current = items.get(i);
            currCity = cities[current.getCityNum()];
            
            //the temporary weight of the thief if taking the item
            double tempWeight = knapsack.getCurrentWeight() + current.getWeight();
            
            //How much of the knapsack is full?
            double weightRatio = tempWeight / capacityOfKnapsack;
                
            //calculate the potential speed if taking the item
            double tempSpeed = (maxSpeed - minSpeed)*weightRatio;
                
            //get the edge cost from the previous city to this city
            double edgeCost = TTPGraph[prevCity.getNodeNum()][currCity.getNodeNum()];
                
            //calculate the potential profit for the item
            double grossProfit = current.getProfit();
            double losses = edgeCost*(1+tempSpeed)*rentingRatio;
            double netProfit = grossProfit - losses;
            
            
            /***********************************************************************************
             * Extension for later - is this item more profitable than an Item I already have? *
             ***********************************************************************************/
            
            
            //do we keep the item?
            //if we do, update prevCity and profit and add item to knapsack
            if(tempWeight <= capacityOfKnapsack && netProfit >= 0) {
                prevCity = currCity;
                totalProfit += netProfit;
                
                knapsack.addItem(current);
            }
        }
        
        System.out.println("Finished filling knapsack - ballpark profit is: " + totalProfit);
    }
    
    
    /**
     * Function that gets called when time is up
     * @return: TSPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {
        System.out.println("Matt: Timer expired function, return the best solution");
        
        //check to see if null no solution has been found
        if(TSPSolution == null) {
            System.out.println("Matt: TSPSolution has not been found");
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
     * Run the TSP algorithm with the algorithm you would like to use
     * @param: int: the algorithm number you would like to use (numbers are
     */
    private Individual runTSP(int alg) {
        if(alg == 1) {
            TSPSolution = testingAlgorithm1(50, 10000);
        } else if(alg == 2) {
            TSPSolution = testingAlgorithm2(50, 10000);
        } else if(alg == 3) {
            TSPSolution = testingAlgorithm3(50, 10000);
        } else if(alg == 4) {
            TSPSolution = testingInverOver(50, 10000);
        } else {
            System.out.println("TSP Algorithm Numbers must be [1,4]");
            return null;
        }
        
        return TSPSolution;
    }
    
    /**
	 * Basic testing function
	 * Will run Algorithm 1 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private Individual testingAlgorithm1(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		return control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 1);
	}
    
	/**
	 * Basic testing function
	 * Will run Algorithm 2 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private Individual testingAlgorithm2(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		return control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 2);
	}
    
	/**
	 * Basic testing function
	 * Will run Algorithm 3 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private Individual testingAlgorithm3(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		return control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 3);
    }
    
	/**
	 * Basic testing function
	 * Will run Inver-Over algorithm and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
	 * @return Individual - best individual from the given algorithm
	 */
	private Individual testingInverOver(int populationSize, int generations) {
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		return control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 4);
    }
    
}