package TTP.Thief;

import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Control;
import TTP.Thief.Travel.Individual;
import TTP.Thief.Knapsack;


public class Matt {
    
    //TSP Variables
    private Individual TSPSolution;
    private Control control;
    private City[] cities;
    
    //Knapsack problem variables
    private Knapsack knapsack;
    
    //Solution variable
    private TTPSolution solution;
    
    //TTP Variables
    private double[][] TTPGraph;
    private double minSpeed, maxSpeed;
    private long capacityOfKnapsack;
    private double rentingRatio;
    
    /**
     * Constructor for Thief 
     *
     *
     *
     * I'mma do this later
     *
     *
     */
    public Matt(City[] _cities, double[][] _TTPGraph, double _minSpeed, double _maxSpeed, long _capacityOfKnapsack, double _rentingRatio) {
        //Setup variables
        cities = _cities;
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
        /***************************************
         *RUN THE TSP ALGORITHM YOU WANT TO USE*
         ***************************************/
        //TSPSolution = testingAlgorithm1(50, 10000);
        //TSPSolution = testingAlgorithm2(50, 10000);
        TSPSolution = testingAlgorithm3(50, 10000);
        //TSPSolution = testingInverOver(50, 10000);
        
        System.out.println("TSP Solution Costs is: " + TSPSolution.getCost());
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