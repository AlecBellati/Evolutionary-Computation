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
        
        //create new knapsack
        knapsack = new Knapsack(capacityOfKnapsack);
        knapsack.setRentingRatio(rentingRatio);
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
        
        //3. create lists of profitable and not profitable cities
        ArrayList<City> inSack = new ArrayList<City>();
        ArrayList<City> notInSack = new ArrayList<City>();
        notInSack.add(cities[0]);
        
        for(int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            
            //is it in the knapsack?
            if(knapsack.containsCity(it.getCityNum())) {
                //is it already in the list of cities?
                if(!inSack.contains(cities[it.getCityNum()])) {
                    inSack.add(cities[it.getCityNum()]);
                }
            
            //otherwise, add to notInSack (if not already added)
            } else {
                if(!notInSack.contains(cities[it.getCityNum()])) {
                    notInSack.add(cities[it.getCityNum()]);
                }
            }
        }
        
        //4a. get TSP for the rest cities
        City[] notInSackArray = new City[notInSack.size()];
        notInSackArray = notInSack.toArray(notInSackArray);
        
        System.out.println("Run TSP for rest");
        Individual rest = runTSP(50, 15000, 3, notInSackArray);
        
        
        //4b. Solve TSP for all cities that are profitable
        inSack.add(0, rest.getCities()[rest.getCities().length-1]);
        
        City[] inSackArray = new City[inSack.size()];
        inSackArray = inSack.toArray(inSackArray);
        
        System.out.println("Run TSP for best");
        System.out.println("best.lenght = " + inSackArray.length);
        Individual best = null;
        if(inSackArray.length == 1) {
            best = new Individual(inSackArray, true);
        } else {
            best = runTSP(50, 15000, 3, inSackArray);
        }
        System.out.println("done");
        
        //5. Combine Knapsack TSP and non-profit TSP into TSPSolution
        System.out.println("Best Individual: ");
        best.print();
        
        System.out.println("\nRest Individual:");
        rest.print();
        System.out.println();
        
        System.out.println("Total Profit = " + (best.getProfit() + rest.getProfit()) );
        
        //create the new individual to combine two city arrays
        TSPSolution = new Individual(inSackArray.length-1 + notInSackArray.length);
        int counter = 0;

        //add notInSackArray
        for(int i = 0; i < rest.getCities().length; i++) {
            TSPSolution.setCity(i, rest.getCityByIndex(i));
            counter++;
        }

        //add inSackArray
        for(int i = 1; i < best.getCities().length; i++) {
            TSPSolution.setCity(counter+i-1, best.getCityByIndex(i));
        }
        
        TSPSolution.print();
        
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
     * needs rewrite
     */
    public void stealProfitableItems() {
        ArrayList<Item> profit = new ArrayList<Item>();
        
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
                
                int currIdx = current.getCityNum();
                cities[currIdx].takeItem(current);
            }
        }
        
        System.out.println("Finished filling knapsack - ballpark profit is: " + totalProfit);
    }
    
    
    /**
     * Function that gets called when time is up
     * @return: TTPSolution: solution after 10 minutes
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
        int[] packingPlan = knapsack.getPackingPlan(TSPSolution, items.size());
        
        //create a new solution
        solution = new TTPSolution(tspTour, packingPlan);
        
        //exit
        return solution;
    }
    
    /**
	 * Basic testing function
	 * Will run Algorithm 1 and output best solution
	 * @param populationSize - size of population to run with
	 * @param generations - number of cycles to perform algorithm
     * @param int - Algorithm number to use [1,4]
     * @param City[] - cities to pass to the TSP solver
	 * @return Individual - best individual from the given algorithm
	 */
	private Individual runTSP(int populationSize, int generations, int alg, City[] subCity) {
        if(alg < 0 || alg > 6) {
            System.out.println("TSP Algorithm Numbers must be [1,5]");
            return null;
        }

        //create Controller
        control = new Control(TTPGraph, maxSpeed, minSpeed, knapsack);
        
		int solutionSize = populationSize/2;
		double mutationPercentage = 0.10, operationPercentage = 0.90;
		int removalRate = (int)Math.ceil(populationSize/10);
		return control.runSequence(subCity, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, alg);
	}
}