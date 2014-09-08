package TTP.Thief;

import TTP.Thief.Travel.Individual;
import TTP.Thief.Knapsack;


public class Sami {
    
    //TSP Variables
    private Individual TSPSolution;
    
    //Knapsack problem variables
    private Knapsack knapsack;
    
    //Solution variable
    private TTPSolution solution;
    
    /**
     * CONSTRUCTOR
     */
    public Sami() {
        
    }
    
    /**
     * Entry point for the thief (your program)
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