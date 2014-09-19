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
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

public class Will {
    
    //TSP Variables
    private Individual TSPSolution;
    private City[] cities;
    
    //Knapsack problem variables
    private Knapsack knapsack;
    
    //Solution variable
    private TTPSolution solution;
    private TTPInstance ttp;
    
    //TTP Variables
    private Item[] itemsArray;
    private double minSpeed, maxSpeed;
    private double capacityOfKnapsack;
    private double rentingRatio;
    
    /**
     * CONSTRUCTOR
     * Assign local variables
     * Initialise and create a new knapsack
     */
    public Will(City[] _cities, Item[] _itemsArray, double _minSpeed, double _maxSpeed, double _capacityOfKnapsack, double _rentingRatio) {
        //Setup variables
        cities = _cities;
        itemsArray = _itemsArray;
        minSpeed = _minSpeed;
        maxSpeed = _maxSpeed;
        capacityOfKnapsack = _capacityOfKnapsack;
        rentingRatio = _rentingRatio;
        
        //create new knapsack and a solver
        knapsack = new Knapsack(capacityOfKnapsack);
    }

    /**
    * Depending on the supplied integer, seed the knapsack with values
    * @param: algorithm: determines the algorithm to use
    */
    private void generateKnapsack(int algorithm){
        switch(algorithm){
            case 1:
                knapsackSolution();
                break;
            case 2:
                costFirst();
                break;
            case 3:
                weightFirst();
                break;
            case 4:
                random();
                break;
            case 5:
                tourLast();
                break;
        }
    }   

    /**
    * Depending on the supplied integer, generate a TSP tour for use (remains constant for entire knapsack)
    * @param: choice: determines the algorithm to use
    */
    private void generateTSP(int choice){
        switch(choice){
            case 1:
                useInstance();
                break;
            case 2:
                useBestTSPAlgorithm();
                break;
        }
    }   


    /**
     * Generates an optimal knapsack for an optimal TSP tour
     * Does so by first generating a knapsack solution (choice of 4 algorithms)
     * Then find the best TSP solution (either use TSP algorithm or instance supplied)
     * Then adjusts the current optimal knapsack by injecting more items to see if it increases the profit
     * @param: ttp: the TTPInstance of this solution, used for the evaluate function
     */
    public void getSolution(TTPInstance ttp, int knapsackAlgorithm, int TSPAlgorithm, boolean randomChoice) {
        this.ttp = ttp;
        System.out.println("Will: Running Program");

        //get a good TSP tour
        generateTSP(TSPAlgorithm);

        generateKnapsack(knapsackAlgorithm);
        Item[] optimal = knapsack.getItems();
        //gets the items that are not part of the knapsack solution
        Item[] itemsMinusOptimal = removeOptimal(optimal);

        double bestCost = calculateCost(0);
        System.out.println("Starting Cost: " + bestCost);

        //check if there is a more optimal solution using the items not already in the solution
        bestCost = checkBetterSolution(itemsMinusOptimal, true, bestCost, randomChoice);
        removeItemsKnapsack(bestCost);
        addItemsKnapsack(bestCost);
        //some of the items originaly in the knapsack solution may have been removed
        //check they can't still find a good home
        bestCost = checkBetterSolution(optimal, false, bestCost, randomChoice);
        addItemsKnapsack(bestCost);

        System.out.println("End Cost: " + bestCost);
    }

    /**
    * Replaces each item in the knapsack with a new item and checks if the solution is better
    * If it is then the change remains in place
    * @param: currentItems: current set of items to place into the optimal solution to check if they make it better
    * @param: optimalRemoved: do we need to check if the current item is already in the solution (for one of the arrays above we do)
    * @param: _bestCost: what is the current best cost
    * @return: double: current best cost
    */
    private double checkBetterSolution(Item[] currentItems, boolean optimalRemoved, double _bestCost, boolean randomChoice){
        double bestCost = _bestCost;
        boolean compare = false;

        ArrayList<Item> listOfItems = new ArrayList<Item>(Arrays.asList(currentItems));
        Random rnd = new Random();
        int pos = 0;
        //check every item we are sent
        while(listOfItems.size() > 0 && pos < currentItems.length){

            Item currentItem = null;
            //depending on the supplied boolean, either choose an item randomly or just get the next item
            if(randomChoice){
                currentItem = listOfItems.get(rnd.nextInt(listOfItems.size()));
                listOfItems.remove(pos); 
            }else{
                currentItem = listOfItems.get(pos);
                pos++;
            }

            int newIndex = -1;

            //then place it in every position in the array to get its optimal position
            for(int j = 0; j < knapsack.getNumItems(); j++){
                //for one set of arrays, there is the possibility for duplicates
                //only run this when needed as it takes a little while
                if(!optimalRemoved){
                    compare = knapsack.containsItem(currentItem);
                }

                Item temp = knapsack.getItem(j);
                //check its going to fit and that it isnt already in the solution
                if(currentItem.getWeight() <= (temp.getWeight() + knapsack.getCurrentCapacity()) && !compare){
                    //replace the item and position j and calculate its cost
                    knapsack.setItem(j, currentItem);
                    double newCost = calculateCost(bestCost);

                    //if its better, remember the index number and keep going
                    if(newCost > bestCost){
                        bestCost = newCost;
                        newIndex = j;
                    }
                    knapsack.setItem(j, temp);
                }
            }

            //if we eventually did find a good place to put it, make it permanent by updating the current weight
            //and placing it into the knapsack solution one last time
            if(newIndex != -1){
                knapsack.setItem(newIndex, currentItem);
            }
        }

        return bestCost;
    }

    /**
    * Takes the current itemArray and removes the items in the knapsack solution
    * @param: optimal: the current knapsack solution
    * @return: Item[]: returns a new Item[] without items in the knapsack
    */
    private Item[] removeOptimal(Item[] optimal){
        System.out.println(itemsArray.length + " " + knapsack.getNumItems());
        Item[] removedOptimal = new Item[itemsArray.length - knapsack.getNumItems()];
        int counter = 0;
        for(int i = 0; i < itemsArray.length; i++){
            Item currentItem = itemsArray[i];

            //if its NOT in the current optimal solution, add it to the new array
            if(!knapsack.containsItem(currentItem)){
                removedOptimal[counter] = currentItem;
                counter++;
            }
        }
        return removedOptimal;
    }

    /**
    * Calculates the cost (profit) of the knapsack usin the TTPInstance evaluate function
    * @param: bestCost: the current best cost of the knapsack
    * @return: double: the cost of this solution
    */
    private double calculateCost(double bestCost){
        int[] optimalItemsOrdered = knapsack.getPackingPlan(TSPSolution, itemsArray.length);

        TTPSolution tempSolution = new TTPSolution(TSPSolution.getCitiesByID(), optimalItemsOrdered);
        ttp.evaluate(tempSolution);
        double cost = tempSolution.getObjective();

        if(bestCost == 0){
            solution = tempSolution;
            solution.println();
        }

        if(cost > bestCost){
            solution = tempSolution;
        }

        return cost;
    }

    /**
    * Remove some items from the knapsack and check if it gets a better cost
    * Checks to ensure it removes an item from the optimal position
    * If no further item is being removed, the function ends
    * params - _bestCost: current best cost
    */
    private void removeItemsKnapsack(double _bestCost){
        double bestCost = _bestCost;
        int pos = -1;

        do{
            //if pos != -1 then an item to remove has been found
            if(pos != -1){
                knapsack.removeItem(knapsack.getItem(pos));
                pos = -1;
            }

            //check every item in the knapsack
            for(int j = knapsack.getNumItems()-1; j >= 0; j--){
                Item temp = knapsack.getItem(j);
                knapsack.removeItem(temp);

                //calculate the cost of removing this item
                double newCost = calculateCost(bestCost);

                //if its better, remember the index number and keep going
                if(newCost > bestCost){
                    bestCost = newCost;
                    pos = j;
                }
                //add the item back in, only remove at the end of the loop
                knapsack.addItem(j, temp);
            }
        //until removing more items does NOT gain us a profit
        }while (pos != -1);
    }

    /**
    * Add some items to the knapsack and check if it gets a better cost
    * If no further item can be added, the function ends
    * params - _bestCost: current best cost
    */
    private void addItemsKnapsack(double _bestCost){
        double bestCost = _bestCost;
        Item addition = null;

        Item[] optimal = knapsack.getItems();
        //gets the items that are not part of the knapsack solution
        Item[] itemsMinusOptimal = removeOptimal(optimal);

        do{
            //if addition != -1 then an item to add has been found
            if(addition != null){
                knapsack.addItem(addition);
                addition = null;
            }

            //check all the items that are not currently in the knapsack
            for(int j = 0; j < itemsMinusOptimal.length; j++){
                Item temp = itemsMinusOptimal[j];
                if(temp.getWeight() <= knapsack.getCurrentCapacity()){
                    knapsack.addItem(temp);
                    double newCost = calculateCost(bestCost);

                    //if its better, remember the index number and keep going
                    if(newCost > bestCost){
                        bestCost = newCost;
                        addition = temp;
                    }
                    //remove the item to obtain the original knapsack
                    knapsack.removeItem(temp);
                }
            }
        //if no more items are being added, we can end as we will not obtain a better solution
        }while (addition != null);
    }

    /**
    * Seeds the knapsack with a set of randomly chosen items
    * Modifies the global knapsack variable
    */
    private void random(){
        Random rand = new Random();
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsArray));
        Item currentItem = itemsList.get(rand.nextInt(itemsList.size()));
        itemsList.remove(currentItem);
        while(currentItem.getWeight() <= knapsack.getCurrentCapacity()){
            knapsack.addItem(currentItem);

            if(itemsList.size() == 0){
                break;
            }

            currentItem = itemsList.get(rand.nextInt(itemsList.size()));            
            itemsList.remove(currentItem);
        }
    }

    /**
    * Seeds the knapsack with the items having the highest cost
    * Calls the sortByCost() method to achieve this
    * Modifies the global knapsack variable
    */
    private void costFirst(){
        ArrayList<Item> itemsList = sortByCost();
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(i);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
    }

    /**
    * Seeds the knapsack with the items closest to the end of the tour
    * Calls the sortByCity() method to achieve this
    * Modifies the global knapsack variable
    */
    private void tourLast(){
        ArrayList<Item> itemsList = sortByCity();
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(i);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
    }

    /**
    * Seeds the knapsack with the items having the lowest weight
    * Calls the sortByWeight() method to achieve this
    * Modifies the global knapsack variable
    */
    private void weightFirst(){
        ArrayList<Item> itemsList = sortByWeight();
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(i);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
    }

    /**
    * Seeds the knapsack with the best knapsack (independent of TSP)
    * Calls the knapsack solver (Dynamic) to achieve this
    * Modifies the global knapsack variable
    */
    private void knapsackSolution(){
        Dynamic knapsackSolver = new Dynamic();
        knapsack = new Knapsack(capacityOfKnapsack, knapsackSolver.getSolution(itemsArray, capacityOfKnapsack, 1));
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
    * Sorts the items in the itemsArray by the TSP tour
    * @return: ArrayList<Item>: List containing sorted items matching the TSP tour
    */
    private ArrayList<Item> sortByCity() {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsArray));
        ArrayList<Item> sortedItemsList = new ArrayList<Item>();

        int[] tour = TSPSolution.getCitiesByID();
        for(int i = tour.length-2; i > 0; i--){
            for(int j = itemsList.size()-1; j >= 0; j--){
                if(tour[i] == itemsList.get(j).getCityNum()){
                    sortedItemsList.add(itemsList.get(j));
                    itemsList.remove(j);
                }
            }
        }

        return sortedItemsList;
    }

    /**
    * Using the given instance, loads the cities into a TSPInstance for use
    * Uses to Optimisation.linkernTour method to extract the cities from file
    * Modifies the global TSPSolution variable
    */
    private void useInstance(){
        int[] tour = Optimisation.linkernTour(ttp);
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
    * Generates the best TSPSolution for this instance using our best algorithm from assignment 1
    * Modifies the global TSPSolution variable
    */
    private void useBestTSPAlgorithm(){
        Control control = new Control();
        int generations = 10000, populationSize = 50;
        int solutionSize = populationSize/2;
        double mutationPercentage = 0.10, operationPercentage = 0.90;
        int removalRate = (int)Math.ceil(populationSize/10);
        TSPSolution = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 3);
    }
    
    /**
     * Function that gets called when time is up
     * @return: TTPSolution: solution after 10 minutes
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
        int[] packingPlan = knapsack.getPackingPlan(TSPSolution, itemsArray.length);
        
        //create a new solution
        solution = new TTPSolution(tspTour, packingPlan);
        
        //exit
        return solution;
    }
}