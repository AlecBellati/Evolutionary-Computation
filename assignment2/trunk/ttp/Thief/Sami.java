package TTP.Thief;

import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Individual;
import TTP.Thief.Knapsack;

import TTP.TTPInstance;


public class Sami {
    
    //TSP Variables
    private Individual TSPSolution;
    private City[] cities;
    
    //Knapsack problem variables
    private Knapsack knapsack;
    
    //Solution variable
    private TTPSolution solution;
    private TTPInstance ttp;
    
    //TTP variables
    private Item[] itemsArray;
    private ArrayList<Item> itemsListOptimalRemoved;
    private ArrayList<Item> itemsListTemp;
    private double minSpeed, maxSpeed;
    private long capacityOfKnapsack;
    private double rentingRatio;
    
    /**
     * CONSTRUCTOR
     */
    public Sami(City[] cities, Item[] itemsArray, double minSpeed, double maxSpeed, long capacityOfKnapsack, double rentingRatio) {
        this.cities = cities;
        this.itemsArray = itemsArray;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.capacityOfKnapsack = capacityOfKnapsack;
        this.rentingRatio = rentingRatio;
        
        knapsack = new Knapsack(capacityOfKnapsack);
    }
    
    /**
     * Generates a solution. 
     */
    public void getSolution() {
        System.out.println("Sami: Running Program");

        
        
        
        
        
        
    }
    
    /**
     * Function that gets called when time is up
     * @return: TSPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {
        System.out.println("Sami: Timer expired function, return the best solution");
        
        //check to see if null no solution has been found
        if(TSPSolution == null) {
            System.out.println("Sami: TSPSolution has not been found");
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
}