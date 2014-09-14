package TTP.Thief;

import TTP.Thief.Travel.City;
import TTP.TTPInstance;
import TTP.Thief.Travel.Item;
import TTP.Thief.Travel.Control;
import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.Dynamic;
import TTP.Optimisation.Optimisation;
import TTP.Thief.Knapsack;

import java.util.ArrayList;
import java.util.Arrays;

public class Will {
    
    //TSP Variables
    private Individual TSPSolution;
    private Control control;
    private City[] cities;
    
    //Knapsack problem variables
    private Knapsack knapsack;
    private Dynamic knapsackSolver;
    private Optimisation optimiser;
    
    //Solution variable
    private TTPSolution solution;
    private TTPInstance ttp;
    
    //TTP Variables
    private Item[] itemsArray;
    private Item[] optimal;
    private double minSpeed, maxSpeed;
    private double capacityOfKnapsack;
    private double currentWeight;
    private double rentingRatio;
    
    /**
     * CONSTRUCTOR
     * Assign local variables
     * Initialise control algorithm and create a new knapsack and knapsack solver
     */
    public Will(City[] _cities, Item[] _itemsArray, double _minSpeed, double _maxSpeed, double _capacityOfKnapsack, double _rentingRatio) {
         //Setup variables
        cities = _cities;
        itemsArray = _itemsArray;
        minSpeed = _minSpeed;
        maxSpeed = _maxSpeed;
        capacityOfKnapsack = _capacityOfKnapsack;
        rentingRatio = _rentingRatio;
        
        //create Controller
        control = new Control();
        
        //create new knapsack and a solver
        knapsack = new Knapsack(capacityOfKnapsack);
        knapsackSolver = new Dynamic();
        optimiser = new Optimisation();
    }

    /**
    *
    *
    */
    public void generateTSP(int[] tour){
        TSPSolution = new Individual(tour.length-1);
        for(int i = 0; i < tour.length-1; i++){
            int currentCity = tour[i];
            City current;
            for(int j = 0; j < cities.length; j++){
                current = cities[j];

                if(current.getNodeNum() == currentCity){
                    TSPSolution.setCity(i, current);
                    break;
                }
            }
        }
    }   
    
    /**
     * Entry point for the thief (your program) - thanks bro!
     * Generates an optimal knapsack for an optimal TSP tour
     * Does so by first generating an optimal knapsack based purely on profit/weight of the items
     * Then find the best TSP solution in the supplied time frame
     * Then adjusts the current optimal knapsack by injecting more items to see if it increases the profit
     */
    public void getSolution(TTPInstance ttp) {
        System.out.println("Will: Running Program");

        currentWeight = 0;
        //get the optimal value from the knapsack solver, independent of the TSP tour
        optimal = knapsackSolver.getSolution(itemsArray, capacityOfKnapsack, 1);
        for(int j = 0; j < optimal.length; j++){
            currentWeight += optimal[j].getWeight();
            //System.out.println(optimal[j].getItemNum() + ": " + optimal[j].getProfit() + " - " + optimal[j].getWeight());
        }

        Item[] originalOptimal = optimal.clone();
        //gets the items that are not part of the optimal solution
        Item[] itemsMinusOptimal = removeOptimal(optimal);

        //get a good TSP tour
        //TSPSolution = control.runSequence(cities, 2
        this.ttp = ttp;
        int[] tour = Optimisation.linkernTour(ttp);
        generateTSP(tour);
        double bestCost = calculateCost();

        System.out.println("Starting Cost: " + bestCost);

        //check if there is a more optimal solution using the items not already in the solution
        bestCost = checkBetterSolution(itemsMinusOptimal, true, bestCost);
        //some of the items original in the optimal solution may have been removed
        //check they can't still find a good home
        bestCost = checkBetterSolution(originalOptimal, false, bestCost);

        //this is just to get it in the format your are looking for
        //this will be removed once I start using the knapsack class
        //int[] optimalItems = new int[itemsArray.length+1];
        //Arrays.fill(optimalItems, 0);
        //optimalItems[0] = 0;
        //for(int i = 1; i < optimal.length; i++){
            //optimalItems[optimal[i].getItemNum()] = 1;
        //}

        System.out.println("End Cost: " + bestCost);
        //solution = new TTPSolution(TSPSolution.getCitiesByID(), optimalItems);
    }

    /**
    * Replaces each item in the optimal array with a new item and checks if the solution is better
    * If it is then the change remains in place
    * @param: currentItems: current set of items to place into the optimal solution to check if they make it better
    * @param: optimalRemoved: do we need to check if the current item is already in the solution (for one of the arrays above we do)
    * @param:_bestCost: what is the current best cost
    * @return: long: current best cost
    */
    public double checkBetterSolution(Item[] currentItems, boolean optimalRemoved, double _bestCost){
        double remainder = capacityOfKnapsack - currentWeight;
        double bestCost = _bestCost;
        boolean compare = false;

        //check every item we are sent
        for(int i = 0; i < currentItems.length; i++){
            Item currentItem = currentItems[i]; 
            int newIndex = -1;

            //then place it in every position in the array to get its optimal position
            for(int j = 0; j < optimal.length; j++){
                //for one set of arrays, there is the possibility for duplicates
                //only run this when needed as it takes a little while
                if(!optimalRemoved){
                    compare = checkInSolution(optimal, currentItem);
                }

                Item temp = optimal[j];
                //check its going to fit and that it isnt already in the solution
                if(currentItem.getWeight() <= (temp.getWeight() + remainder) && !compare){
                    //replace the item and position j and calculate its cost
                    optimal[j] = currentItem;
                    double newCost = calculateCost();

                    //if its better, remember the index number and keep going
                    if(newCost > bestCost){
                        bestCost = newCost;
                        newIndex = j;
                    }
                    optimal[j] = temp;
                }
            }

            //if we eventually did find a good place to put it, make it permanent by updating the current weight
            //and placing it into the optimal solution one last time
            if(newIndex != -1){
                currentWeight -= optimal[newIndex].getWeight();
                currentWeight += currentItem.getWeight();
                optimal[newIndex] = currentItem;
                remainder = capacityOfKnapsack - currentWeight;
            }
        }

        return bestCost;
    }

    /**
    * Takes the current itemArray and removes the items in the optimal solution
    * @param: optimal: the current optimal solution
    * @return: Item[]: returns a new Item[] without items in the optimal array
    */
    public Item[] removeOptimal(Item[] optimal){
        Item[] removedOptimal = new Item[itemsArray.length - optimal.length];
        int counter = 0;
        for(int i = 0; i < itemsArray.length; i++){
            Item currentItem = itemsArray[i];

            //if its NOT in the current optimal solution, add it to the new array
            if(!checkInSolution(optimal, currentItem)){
                removedOptimal[counter] = currentItem;
                counter++;
            }
        }
        return removedOptimal;
    }

    /**
    * Checks to see if the currentItem is in the provided array
    * @param: items: array to be searched
    * @param: currentItem: item to look for in the array
    * @return: boolean: true if the current item is in the solution, false otherwise
    */
    public boolean checkInSolution(Item[] items, Item currentItem){
        for(int i = 0; i < items.length; i++){
            //item number will be the same (among other things)
            if(items[i].getItemNum() == currentItem.getItemNum()){
                return true;
            }
        }
        return false;
    }

    /**
    * Calculates the cost (profit) of the knapsack based on various qualities (see below)
    * @param: sack: the items contained in the current knapsack
    * @return: long: the cost of this solution
    */
    public double calculateCost(){
        //what if instead you just call TTPSolution.evaluate.
        int[] optimalItems = new int[itemsArray.length];
        Arrays.fill(optimalItems, 0);

        for(int i = 0; i < optimal.length; i++){
            //System.out.println(optimal[i].getItemNum());
            optimalItems[optimal[i].getItemNum()] = 1;
        }

        //optimalItems now goes from 0 to 280
        //but we want it to be in the same order as the tour

        int[] tours = TSPSolution.getCitiesByID();
        int[] optimalItemsOrdered = new int[optimalItems.length];
        for(int i = 1; i < tours.length-1; i++){
            optimalItemsOrdered[i-1] = optimalItems[tours[i]-1];
        }


        TTPSolution tempSolution = new TTPSolution(TSPSolution.getCitiesByID(), optimalItemsOrdered);
        ttp.evaluate(tempSolution);
        //tempSolution.println();


        double cost = tempSolution.getObjective();
        //System.out.println(cost);

        //profit will determine where the item is on the path
        /*City[] bestCities = TSPSolution.getCities();
        //using a list such that items can be removed (reduces search space)
        ArrayList<Item> sackItems = new ArrayList<Item>(Arrays.asList(sack));

        long cost = 0;
        long currentWeight = 0;
        double currentSpeed = maxSpeed;
        //go through each city
        for(int i = 0; i < bestCities.length; i++){

            //go through all items in the sack
            for(int j = sackItems.size()-1; j >= 0; j--){
                //if the items belong to this City, do the following calculations
                if(sackItems.get(j).getCityNum() == bestCities[i].getNodeNum()){
                    //add the weight to the currentWeight variable
                    currentWeight += sackItems.get(j).getWeight();
                    //provided in TTP paper
                    currentSpeed = maxSpeed - (currentWeight * ((maxSpeed-minSpeed)/capacityOfKnapsack));

                    //NEW_COST = profit_of_item((edgecost/currentSpeed)*rent)
                    if(i != bestCities.length-1){
                        cost += sackItems.get(j).getProfit()/TSPSolution.getEdgeCost(bestCities[i].getNodeNum(), bestCities[i+1].getNodeNum());
                    }else{ //if it is the last city, loop back to the start
                        cost += sackItems.get(j).getProfit()/TSPSolution.getEdgeCost(bestCities[i].getNodeNum(), bestCities[0].getNodeNum());
                    }
                    //remove from the sack as it is no longer needed
                    sackItems.remove(j);
                }
            }
        }*/
        return cost;
    }
    
    /**
     * Function that gets called when time is up
     * @return: TSPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {
        System.out.println("Will: Timer expired function, return the best solution");
        
        //check to see if null no solution has been found
        if(TSPSolution == null) {
            System.out.println("Will: TSPSolution has not been found");
            return null;
        }
        
        //create TTP variables
        int[] tspTour = TSPSolution.getCitiesByID();
        int[] packingPlan = knapsack.getItemsByID();
        
        //create a new solution
        //dont need this yet cause im not using the knapsack class!
        //solution = new TTPSolution(tspTour, packingPlan);
        
        //exit
        return solution;
    }
}