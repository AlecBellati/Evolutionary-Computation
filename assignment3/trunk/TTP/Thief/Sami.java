/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment Two
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

package TTP.Thief;

import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Control;
import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.Item;
import TTP.Thief.Knapsack;

import TTP.TTPInstance;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import java.util.Random;


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
    private ArrayList<Item> itemsList;
    private ArrayList<Integer> requiredCities;
    private double minSpeed, maxSpeed;
    private long capacityOfKnapsack;
    private double rentingRatio;
    private int[] packingPlan;
    
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
     * Generates an optimal tour for a given knapsack.
     */
    public void getSolution(TTPInstance ttp) {
        System.out.println("Sami: Running Program");
        this.ttp = ttp;
        
        // Parameters!
        int generations = 10000; //Best number of generations as per assignment 1
        int knapsackChoice = 2;
        
        
        // Create a starting solution
        generateTSPSolution();
        
        // Get a packing plan
        generateKnapsack(knapsackChoice);
        packingPlan = knapsack.getPackingPlan(TSPSolution, itemsArray.length);
        
        // Finish setting up starting solution
        getRequiredCities();
        setRandomCitiesSolution();
        
        // Get initial cost of random tour
        double bestCost = getCost(Integer.MIN_VALUE, TSPSolution);
        
        // Collect only items in packing plan, in their fixed order
        // But visit all cities in the order in the solution
        for (int i = 0; i < generations; i++) {
            
            //System.out.println("Generation " + (i+1) + " Starting Cost: " + bestCost);
            
            // Mutate tour
            Individual mutant = mutateTour();
            
            // Get cost
            double currentCost = getCost(bestCost, mutant);
            
            // Update cost and individual
            if (currentCost > bestCost) {
                bestCost = currentCost;
                TSPSolution = mutant;
            }
        }
        
        System.out.println("End Cost: "+bestCost);
        
    }
    
    /**
     * Calculates the cost (profit) of the knapsack using the TTPInstance evaluate function
     * @param: bestCost: the current best cost of the knapsack
     * @return: double: the cost of this solution
     */
    private double getCost(double bestCost, Individual newSolution){
        int[] optimalItemsOrdered = knapsack.getPackingPlan(newSolution, itemsArray.length);
        
        TTPSolution tempSolution = new TTPSolution(newSolution.getCitiesByID(), optimalItemsOrdered);
        ttp.evaluate(tempSolution);
        double cost = tempSolution.getObjective();
        
        if(cost > bestCost){
            solution = tempSolution;
        }
        
        return cost;
    }
    
    
    /**
     * Generates a TSP solution.
     */
    public void generateTSPSolution(){
        TSPSolution = new Individual(cities.length);
        
        for (int i = 0; i < cities.length; i++) {
            TSPSolution.setCity(i, cities[i]);
        }
    }
    
    /**
     * Returns a list of all cities that are required to be visited.
     */
    public void getRequiredCities() {
        requiredCities = new ArrayList<Integer>();
        
        for (int i = 0; i < packingPlan.length; i++) {
            if (packingPlan[i] == 1) {
                requiredCities.add(i);
            }
        }
    }
    
    /**
     * Randomise cities in solution, allowing for pre-set cities.
     */
    public void setRandomCitiesSolution() {
        Random rand = new Random();
        ArrayList<Integer> alreadyAdded = new ArrayList<Integer>();
        
        int i = 1;
        while (i < cities.length) {
            if (requiredCities.contains(i)) {
                // Ensure not replacing already set index
                i++;
            } else {
                
                int index = rand.nextInt(cities.length);
                // Ensure not replacing already set index
                while (alreadyAdded.contains(index) || requiredCities.contains(index) || index == 0) {
                    index = rand.nextInt(cities.length);
                }
                
                TSPSolution.setCity(i, cities[index]);
                alreadyAdded.add(index);
                i++;
            }
        }
    }
    
    /**
     * Mutates a provided tour, keeping the packing list intact.
     */
    public Individual mutateTour() {

        Random rand = new Random();
        ArrayList<Integer> alreadyAdded = new ArrayList<Integer>();

        // Copy the original solution
        Individual mutated = TSPSolution.clone();
        
        // Find subset to mutate
        int posA = 0;
        int posB = 0;
        
        // Ensure not starting on the first city
        while (posA == 0) {
            posA = rand.nextInt(mutated.getNumCities());
        }
        
        while (posB == 0) {
            posB = rand.nextInt(mutated.getNumCities());
        }
        
        // Ensure that posA is less than posB
        if (posA > posB){
            int posTemp = posA;
            posA = posB;
            posB = posTemp;
        }
        
        int subsetSize = posB - posA;

        int i = posA;
        while (i < posB) {
            
            if (requiredCities.contains(posA)) {
                // Ensure not replacing already set index
                i++;
            } else {
                
                int index = rand.nextInt((posB - posA)) + posA;

                while (alreadyAdded.contains(index)) {
                    index = rand.nextInt((posB - posA)) + posA;
                }
                
                mutated.setCity(i, cities[index]);
                alreadyAdded.add(index);
                i++;
            }
        }
        
        return mutated;
        
    }
    
    /**
     * Depending on the supplied integer, seed the knapsack with values
     * @param: algorithm: determines the algorithm to use
     */
    private void generateKnapsack(int algorithm){
        switch(algorithm){
            case 1:
                generateCostFirst();
                break;
            case 2:
                generateWeightFirst();
                break;
            case 3:
                generateRandomKnapsack();
                break;
            case 4:
                generateWeightedCostFirst();
                break;
        }
    }
    

    /**
     * Generates a random knapsack.
     */
    public void generateRandomKnapsack() {
        Random rand = new Random();
        
        ArrayList<Item> items = new ArrayList<Item>(Arrays.asList(itemsArray));
        Item currentItem = items.get(rand.nextInt(items.size()));
        items.remove(currentItem);
        
        // pick items at random until the knapsack is at capacity
        while(currentItem.getWeight() <= knapsack.getCurrentCapacity()){
            knapsack.addItem(currentItem);
            
            //or if we run out of items
            if(items.size() == 0){
                break;
            }
            
            currentItem = items.get(rand.nextInt(items.size()));
            items.remove(currentItem); //keeps track of items NOT in the knapsack
        }
        itemsList = items;
        
    }
    
    /**
     * Seeds the knapsack with the items having the highest cost
     * Calls the sortByCost() method to achieve this
     * Modifies the global knapsack variable
     */
    private void generateCostFirst(){
        ArrayList<Item> items = sortByCost();
        for(int i = 0; i < items.size(); i++){
            Item currentItem = items.get(0);
            items.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsList = items;
    }

    /**
     * Seeds the knapsack with the items having the lowest weight
     * Calls the sortByWeight() method to achieve this
     * Modifies the global knapsack variable
     */
    private void generateWeightFirst(){
        ArrayList<Item> items = sortByWeight();
        for(int i = 0; i < items.size(); i++){
            Item currentItem = items.get(0);
            items.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsList = items;
    }
    
    /**
     * Seeds the knapsack with the items having the highest weighted cost
     * Calls the sortByWeightedCost() method to achieve this
     * Modifies the global knapsack variable
     */
    private void generateWeightedCostFirst(){
        ArrayList<Item> items = sortByWeightedCost(itemsArray);
        for(int i = 0; i < items.size(); i++){
            Item currentItem = items.get(0);
            items.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsList = items;
    }
    
    /**
     * Sorts the items in the itemsArray by weight
     * @return: ArrayList<Item>: List containing sorted items from lowest to highest weight
     */
    private ArrayList<Item> sortByWeight() {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsArray));
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(itemsList, new Comparator<Item>() {
            @Override
            public int compare (Item i1, Item i2) {
                return (int)(i1.getWeight() - i2.getWeight());
            }
        });
        return itemsList;
    }
    
    /**
     * Sorts the items in the itemsArray by cost
     * @return: ArrayList<Item>: List containing sorted items from highest to lowest cost
     */
    private ArrayList<Item> sortByCost() {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsArray));
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(itemsList, new Comparator<Item>() {
            @Override
            public int compare (Item i1, Item i2) {
                return (int)((i2.getProfit()/i2.getWeight()) - (i1.getProfit()/i1.getWeight()));
            }
        });
        return itemsList;
    }
    
    /**
     * Sorts the items in the itemsArray by a weighted cost
     * @param: itemsToSort - Items that need sorting!
     * @return: ArrayList<Item>: List containing sorted items from highest to lowest weighted cost
     * Weighted costs is dependant on where it is in the tour as well as its profit/weight ratio
     */
    private ArrayList<Item> sortByWeightedCost(Item[] itemsToSort) {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsToSort));
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(itemsList, new Comparator<Item>() {
            @Override
            public int compare (Item i1, Item i2) {
                return (int)(((i2.getProfit()/i2.getWeight())*i2.getCityNum()) - ((i1.getProfit()/i1.getWeight())*i1.getCityNum()));
            }
        });
        return itemsList;
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
        
        /*//create TTP variables
        int[] tspTour = TSPSolution.getCitiesByID();
        int[] packingPlan = knapsack.getPackingPlan(TSPSolution, itemsArray.length);
        
        //create a new solution
        solution = new TTPSolution(tspTour, packingPlan);*/
        
        //exit
        return solution;
    }
}