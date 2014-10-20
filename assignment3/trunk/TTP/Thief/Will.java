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
import TTP.TTPInstance;
import TTP.Thief.Travel.Item;
import TTP.Thief.Travel.Control;
import TTP.Thief.Travel.Individual;
import TTP.Thief.Travel.Dynamic;
import TTP.Optimisation.Optimisation;
import TTP.Thief.Knapsack;

import java.util.ArrayList;
import java.util.List;
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
    private ArrayList<Item> itemsListOptimalRemoved;
    private ArrayList<Item> itemsListTemp;
    private double minSpeed, maxSpeed;
    private long capacityOfKnapsack;
    private double rentingRatio;
    
    /**
     * CONSTRUCTOR
     * Assign local variables
     * Initialise and create a new knapsack
     * @param - _cities: contains a list of all the cities
     * @param - _itemsArray: contains a list of all the items
     * @param - _minSpeed, _max_speed: max and min speed of the thief
     * @param - _capacityOfKnapsack: capacity of the knapsack
     * @param - _rentingRatio: cost to rent the knapsack for a unit time period
     */
    public Will(City[] _cities, Item[] _itemsArray, double _minSpeed, double _maxSpeed, long _capacityOfKnapsack, double _rentingRatio) {
        //Setup variables
        cities = _cities;
        itemsArray = _itemsArray;
        minSpeed = _minSpeed;
        maxSpeed = _maxSpeed;
        capacityOfKnapsack = _capacityOfKnapsack;
        rentingRatio = _rentingRatio;
        
        //create new empty knapsack
        knapsack = new Knapsack(capacityOfKnapsack);
    }

    /**
    * Used to return parameters for the algorithm "Will"
    * @param - instance: instance parameters to return
    * @return - String[]: parameters relating to a particular instance
    */
    private String[] setupRun(int instance){
        ArrayList<String[]> instances = new ArrayList<String[]>();

        //knapsack seed, TSP choice, generations, iterations, {good items, random items}
        instances.add(0, new String[]{"2", "1", "10", "-1", "-1", "-1"}); //default
        instances.add(1, new String[]{"2", "1", "10", "-1", "-1", "-1"}); //280, 279
        instances.add(2, new String[]{"2", "1", "10", "-1", "-1", "-1"}); //280, 1395
        instances.add(3, new String[]{"2", "1", "6", "-1", "-1", "-1"}); //280, 2790
        instances.add(4, new String[]{"2", "1", "1", "-1", "-1", "-1"}); //4461, 4461
        instances.add(5, new String[]{"2", "1", "10", "8", "-1", "-1"}); //4461, 22300
        instances.add(6, new String[]{"2", "1", "1", "25", "-1", "-1"}); //4461, 44610
        instances.add(7, new String[]{"4", "1", "6", "5", "7500", "2500"}); //33810, 33809
        instances.add(8, new String[]{"4", "1", "1", "12", "2000", "500"}); //33810, 169045
        instances.add(9, new String[]{"4", "1", "1", "50", "2000", "500"}); //33810, 338090

        if(instance < 0 || instance > 9){
            return instances.get(0);
        }
        return instances.get(instance);
    }

    /**
    * Formats the parameters for the particular instance for the algorithm "Will"
    * @param - instanceChoice: instance to be run, used to get the appropriate parameters
    */
    public void runWill(int instanceChoice, TTPInstance ttp){
        String[] instance = setupRun(instanceChoice);
        int goodItems = Integer.parseInt(instance[4]);
        int randomItems = Integer.parseInt(instance[5]);
        int[] itemChoice = null;
        if(goodItems != -1){
            if(randomItems != -1){
                itemChoice = new int[]{goodItems, randomItems};
            }else{
                itemChoice = new int[]{goodItems};
            }
        }

        getSolution(ttp, Integer.parseInt(instance[0]), Integer.parseInt(instance[1]), Integer.parseInt(instance[2]), Integer.parseInt(instance[3]), itemChoice);
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
            case 6:
                weightedCostFirst();
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
     * @param: knapsackAlgorithm - how to seed the knapsack
     * @param: TSPAlgorithm - how to generate the TSP solution
     * @param: boolean randomChoice - if true, randomly pick items to add and remove
     */
    public void getSolution(TTPInstance ttp, int knapsackAlgorithm, int TSPAlgorithm, int generations, int _iterations, int[] itemChoice) {
        this.ttp = ttp;
        System.out.println("Will: Running Program");

        //get a good TSP tour
        generateTSP(TSPAlgorithm);
        generateKnapsack(knapsackAlgorithm);
        double bestCost = calculateCost(Integer.MIN_VALUE);
        boolean randomChoice = false;
        
        for(int i = 0; i < generations; i++){
            itemsListTemp = new ArrayList<Item>();
            
            //if there are more than 160000 items, we will spend most of the time just removing items!
            int removal = knapsack.getNumItems();
            if(itemsArray.length > 160000){
                removal = 40000;
            }

            System.out.println("Iteration " + (i+1) + " Starting Cost: " + bestCost);

            //pick the items to try and inject into the knapsack          
            Item[] itemsMinusOptimal;
            if(itemChoice == null){
                itemsMinusOptimal = convertOptimal(itemsListOptimalRemoved);
            }else if(itemChoice.length == 1){
                itemsMinusOptimal = randomSelect(itemChoice[0]);
            }else if(itemChoice.length == 2){
                itemsMinusOptimal = pickBestItems(itemChoice[0], itemChoice[1]);
            }else{
                itemsMinusOptimal = convertOptimal(itemsListOptimalRemoved);
            }

            //check if there is a more optimal solution using the items not already in the solution
            bestCost = removeItemsKnapsack(bestCost, removal, randomChoice);

            //iterations determines the number of times to replace an item in the knapsack with the current one
            int iterations = knapsack.getNumItems();
            if(_iterations != -1){
                iterations = _iterations;
            }

            bestCost = checkBetterSolutionHeuristic(itemsMinusOptimal, bestCost, randomChoice, iterations);

            //get the new list of items not in the knapsack and run it again
            itemsMinusOptimal = convertOptimal(itemsListTemp);
            bestCost = checkBetterSolutionHeuristic(itemsMinusOptimal, bestCost, randomChoice, iterations);
            
            //with the best knapsack, pass to the control algorithm to solve the tour based on the knapsack
            //bestCost = useBestTTPAlgorithm(bestCost);
        }

        System.out.println("End Cost: " + bestCost);
    }

    /**
    * Replaces each item in the knapsack with a new item and checks if the solution is better
    * If it is then the change remains in place
    * @param: currentItems: current set of items to place into the optimal solution to check if they make it better
    * @param: _bestCost: what is the current best cost
    * @param: randomChoice: if true, choose items from the currentItems array at random
    * @param: iterations: number of times that the algorithm will try and place the current Item into a better position
    * @return: double: current best cost
    */
    private double checkBetterSolutionHeuristic(Item[] currentItems, double _bestCost, boolean randomChoice, int iterations){
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

            compare = knapsack.containsItem(currentItem);
            int newIndex = -1;

            boolean added = false;
            //check if it can just be added right away
            if(currentItem.getWeight() <= knapsack.getCurrentCapacity() && !compare){
                knapsack.addItem(currentItem);
                double newCost = calculateCost(bestCost);

                //if its better, set added to true so it won't be added again the function below
                if(newCost > bestCost){
                    bestCost = newCost;
                    itemsListOptimalRemoved.remove(currentItem);
                    added = true;
                }else{
                    knapsack.removeItem(currentItem);
                }
            }

            //if the item was not added above, do the following
            if(!added){

                //place the item in every position (or only a subset depending on the "iterations" variable) in the array to get its optimal position
                for(int j = 0; j < iterations; j++){
                    int tempIndex = rnd.nextInt(knapsack.getNumItems());

                    //for one set of arrays, there is the possibility for duplicates
                    Item temp = knapsack.getItem(tempIndex);

                    //check its going to fit and that it isnt already in the solution
                    if(currentItem.getWeight() <= (temp.getWeight() + knapsack.getCurrentCapacity()) && !compare){
                        //replace the item and position j and calculate its cost
                        knapsack.setItem(tempIndex, currentItem);
                        double newCost = calculateCost(bestCost);

                        //if its better, remember the index number and keep going
                        if(newCost > bestCost){
                            bestCost = newCost;
                            newIndex = tempIndex;
                        }
                        knapsack.setItem(tempIndex, temp);
                    }
                }

                //if we eventually did find a good place to put it, make it permanent by updating the current weight
                //and placing it into the knapsack solution one last time
                if(newIndex != -1){
                    itemsListTemp.add(knapsack.getItem(newIndex));

                    //maintain the itemsList not in the knapsack
                    itemsListOptimalRemoved.remove(currentItem);
                    itemsListOptimalRemoved.add(knapsack.getItem(newIndex));

                    knapsack.setItem(newIndex, currentItem);
                }
            }
        }

        return bestCost;
    }

    /**
    * Calculates the cost (profit) of the knapsack using the TTPInstance evaluate function
    * @param: bestCost: the current best cost of the knapsack
    * @return: double: the cost of this solution
    */
    private double calculateCost(double bestCost){
        int[] optimalItemsOrdered = knapsack.getPackingPlan(TSPSolution, itemsArray.length);

        TTPSolution tempSolution = new TTPSolution(TSPSolution.getCitiesByID(), optimalItemsOrdered);
        ttp.evaluate(tempSolution);
        double cost = tempSolution.getObjective();

        if(cost > bestCost){
            solution = tempSolution;
        }

        return cost;
    }

    /**
    * Remove some items from the knapsack and check if it gets a better cost
    * @param: _bestCost - current best cost
    * @param: iterations - number of items to remove
    * @oaram: random - if true, pick items at random
    */
    private double removeItemsKnapsack(double _bestCost, int iterations, boolean random){
        double bestCost = _bestCost;
        Random rnd = new Random();

        //check boundary size just in case
        if(iterations > knapsack.getNumItems()){
            iterations = knapsack.getNumItems();
        }

        //check every item in the knapsack
        for(int j = iterations-1; j >= 0; j--){
            int pos = j;
            if(random){
                pos = rnd.nextInt(knapsack.getNumItems());
            }

            Item temp = knapsack.getItem(pos);
            knapsack.removeItem(temp);

            //calculate the cost of removing this item
            double newCost = calculateCost(bestCost);
            if(newCost > bestCost){
                bestCost = newCost;
                itemsListOptimalRemoved.add(temp);
            }else{
                knapsack.addItem(pos, temp);
            }
        }

        return bestCost;
    }

    /**
    * From the itemsArray, pick the best items, both by weighted cost and at random
    * @param - good: pick this many of the top Items (sorted by weighted cost)
    * @param - random: pick this many items at random for funsies!
    * @return - Item[]: contains (good+random) number of items
    */
    private Item[] pickBestItems(int good, int random){
        Item[] pickedItems = new Item[good+random];
        ArrayList<Item> itemsList = new ArrayList<Item>();

        //get the sorted list of items by weighted cost
        if(good > 0){
            itemsList = sortByWeightedCost(convertOptimal(itemsListOptimalRemoved));
        }

        //check boundary size
        if(good > itemsList.size()){
            good = itemsList.size();
        }
        //get the top items specified by the "good" variable
        for(int i = 0; i < good; i++){
            pickedItems[i] = itemsList.get(i);
        }

        //check boundary size
        if(random > itemsList.size()){
            random = itemsList.size();
        }
        //then just pick at random for the remainder
        Random rnd = new Random();
        for(int i = good; i < (good+random); i++){
            pickedItems[i] = itemsList.get(rnd.nextInt(itemsList.size()));
        }

        return pickedItems;
    }

    /**
    * Shuffle an array containing integers [0, length)
    * @param - length: size of the array to shuffle
    * @return - List<Integer>: A shuffled integer array containing values [0, length)
    */
    private List<Integer> shuffleArray(int length){
        List<Integer> dataList = new ArrayList<Integer>();
        for (int i = 0; i < length; i++) {
            dataList.add(i);
        }
        Collections.shuffle(dataList);
        return dataList;
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

        //pick items at random until the knapsack is at capacity
        while(currentItem.getWeight() <= knapsack.getCurrentCapacity()){
            knapsack.addItem(currentItem);

            //or if we run out of items
            if(itemsList.size() == 0){
                break;
            }

            currentItem = itemsList.get(rand.nextInt(itemsList.size()));            
            itemsList.remove(currentItem); //keeps track of items NOT in the knapsack
        }
        itemsListOptimalRemoved = itemsList;
    }

    /**
    * Seeds the knapsack with the items having the highest cost
    * Calls the sortByCost() method to achieve this
    * Modifies the global knapsack variable
    */
    private void costFirst(){
        ArrayList<Item> itemsList = sortByCost();
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsListOptimalRemoved = itemsList;
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
    * Using the given instance, loads the cities into a TSPInstance for use
    * Uses to Optimisation.linkernTour method to extract the cities from file
    * Also converts from using an integer array to our CITY objects
    * Modifies the global TSPSolution variable
    */
    private void useInstance(){
        int[] tour = Optimisation.linkernTour(ttp);
        TSPSolution = new Individual(tour.length-1);

        //matches the city number with the CITY object supplied in the cities array
        for(int i = 0; i < tour.length-1; i++){
            int currentCity = tour[i];
            City current;
            for(int j = 0; j < cities.length; j++){
                current = cities[j];

                //sets that city in the TSPSolution with our CITY objects
                if(current.getNodeNum() == currentCity){
                    TSPSolution.setCity(i, current);
                    break;
                }
            }
        }
    }

    /**
    * Converts and ArrayList<Item> to an Item[]
    * @param: optimal: the current knapsack solution
    * @return: Item[]: returns a new Item[] without items in the knapsack
    */
    private Item[] convertOptimal(ArrayList<Item> itemsToConvert){
        return itemsToConvert.toArray(new Item[itemsToConvert.size()]);
    }
    
    /**
     * Function that gets called when time is up
     * @return: TTPSolution: solution after 10 minutes
     */
    public TTPSolution getBestSolution() {        
        //check to see if null no solution has been found
        if(TSPSolution == null) {
            System.out.println("Will: TSPSolution has not been found");
            return null;
        }
        
        //create TTP variables
        //int[] tspTour = TSPSolution.getCitiesByID();
        //int[] packingPlan = knapsack.getPackingPlan(TSPSolution, itemsArray.length);
        
        //create a new solution
        //solution = new TTPSolution(tspTour, packingPlan);
        
        //exit
        return solution;
    }




    /***************************
     ********* NOT USED ********
     ***************************/
    //but were useful for the testing process and hence have been preserved here

    /**
    * Randomly pick items from the itemsArray
    * Uses sortByWeightedCost to ensure a uniform order to choose from
    * @param - top: max number to pick
    * @return - Item[]: "top" number of Items randomly chosen
    */
    private Item[] randomSelect(int top){
        Random rand = new Random();
        List<Integer> itemsList = shuffleArray(itemsListOptimalRemoved.size());
        Item[] pickedItems = convertOptimal(itemsListOptimalRemoved);
        ArrayList<Item> sortedItems = sortByWeightedCost(pickedItems);

        pickedItems = new Item[top];
        for(int i = 0; i < sortedItems.size(); i++){
            if(i < top){
                pickedItems[i] = sortedItems.get(i);
            }else{
                //maintains the items not in the knapsack
                itemsListOptimalRemoved.add(sortedItems.get(i));
            }
        }
        return pickedItems;
    }

        /**
    * Seeds the knapsack with the items having the highest weighted cost
    * Calls the sortByWeightedCost() method to achieve this
    * Modifies the global knapsack variable
    */
    private void weightedCostFirst(){
        ArrayList<Item> itemsList = sortByWeightedCost(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsListOptimalRemoved = itemsList;
    }

    /**
    * Seeds the knapsack with the items closest to the end of the tour
    * Calls the sortByCity() method to achieve this
    * Modifies the global knapsack variable
    */
    private void tourLast(){
        ArrayList<Item> itemsList = sortByCity();
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsListOptimalRemoved = itemsList;
    }

    /**
    * Seeds the knapsack with the items having the lowest weight
    * Calls the sortByWeight() method to achieve this
    * Modifies the global knapsack variable
    */
    private void weightFirst(){
        ArrayList<Item> itemsList = sortByWeight();
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        itemsListOptimalRemoved = itemsList;
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
    * Generates the best TSPSolution for this instance using our best algorithm from assignment 1
    * Modifies the global TSPSolution variable
    */
    private void useBestTSPAlgorithm(){
        Control control = new Control();
        int generations = 5000, populationSize = 50;
        int solutionSize = populationSize/2;
        double mutationPercentage = 0.10, operationPercentage = 0.90;
        int removalRate = (int)Math.ceil(populationSize/10);
        TSPSolution = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 3);
    }

    /**
    * Generates the best TSPSolution for this instance using our best algorithm from assignment 1
    * That has been modified to find the best tour based on the current knapsack
    * Modifies the global TSPSolution variable
    */
    private double useBestTTPAlgorithm(double bestCost){
        Control control = new Control(maxSpeed, minSpeed, knapsack);
        int generations = 5000, populationSize = 50;
        int solutionSize = populationSize/2;
        double mutationPercentage = 0.10, operationPercentage = 0.90;
        int removalRate = (int)Math.ceil(populationSize/10);

        Individual oldTSPSolution = TSPSolution.clone();
        TSPSolution = control.runSequence(cities, solutionSize, populationSize, generations, mutationPercentage, operationPercentage, removalRate, 3);
        
        double newCost = calculateCost(bestCost);
        System.out.println("Result from use of TSPAlgorithm: " + newCost);

        if(newCost < bestCost){
            TSPSolution = oldTSPSolution;
            newCost = bestCost;
        }
        return newCost;
    }
}