/*
 * Evolutionary Computation
 * COMP SCI 4095
 * Assignment three
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

package TTP.Thief;

import TTP.Thief.Travel.Item;
import TTP.Thief.Travel.Individual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;


public class Knapsack {
    
    private long capacity;
    private long currentWeight;
    private double rentingRatio;
    private ArrayList<Item> items;
    private Item[] itemsArray;
    
    /**
     * Construct an empty knapsack with a known weight limit
     * @param - _capacity: capacity of this knapsack
     */
    public Knapsack(long _capacity) {
        capacity = _capacity;
        currentWeight = 0;
        items = new ArrayList<Item>();
    }

    /**
     * Construct a knapsack with items and a known weight limit
     * @param - _capacity: capacity of this knapsack
     * @param - item: items for this knapsack to hold
     */
    public Knapsack(long _capacity, Item[] item) {
        capacity = _capacity;
        currentWeight = 0;
        items = new ArrayList<Item>();
        for(int i = 0; i < item.length; i++){
            items.add(item[i]);
            currentWeight += item[i].getWeight();
        }
    }

    /**
     * Construct a knapsack with a known weight limit and seed using the supplied seedChoice
     * @param - _capacity: capacity of this knapsack
     * @param - _itemsArray: list of all items - used for seeding the knapsack
     * @param - seedChoice: method to see the knapsack
     */
    public Knapsack(long _capacity, Item[] _itemsArray, int seedChoice) {
        capacity = _capacity;
        currentWeight = 0;
        itemsArray = _itemsArray;
        items = new ArrayList<Item>();
        seedKnapsack(seedChoice);
    }

    /**
    * Depending on the supplied integer, seed the knapsack with values
    * @param: algorithm - determines the algorithm to use
    */
    public void seedKnapsack(int algorithm){
        switch(algorithm){
            case 1:
                costFirst();
                break;
            case 2:
                weightFirst();
                break;
            case 3:
                random();
                break;
        }
    }   
    
    /**
     * set rentingratio
     * @param: double: renting ratio of knapsack
     */
    public void setRentingRatio(double RR) {
        rentingRatio = RR;
    }
    
    /**
     * get rentingratio
     * @return: double: renting ratio of knapsack
     */
    public double getRentingRatio() {
        return rentingRatio;
    }
    
    /**
     * Add an item to the knapsack
     * @param: Item: the item to add to the knapsack
     */
    public void addItem(Item item) {
        items.add(item);
        currentWeight += item.getWeight();
    }

    /**
     * Add an item to the knapsack at a specific index
     * @param: index: index to place the item
     * @param: Item: the item to add to the knapsack
     */
    public void addItem(int index, Item item) {
        items.add(index, item);
        currentWeight += item.getWeight();
    }

    /**
     * Add an item to the knapsack at a specific point
     * @param: index: index to place the item
     * @param: Item: the item to add to the knapsack
     */
    public void setItem(int index, Item item) {
        removeItem(items.get(index));
        addItem(index, item);
    }
    
    /**
     * Remove an item from the knapsack
     * @param: Item: Item to remove from knapsack
     * @return: Boolean: True if item was successfully removed, else false
     */
    public boolean removeItem(Item item) {
        if(items.remove(item)){
            currentWeight -= item.getWeight();
            return true;
        }
        return false;
    }

    /**
     * Remove an item from the knapsack
     * @param: pos: position of item to remove
     * @return: Boolean: True if item was successfully removed, else false
     */
    public boolean removeItem(int pos) {
        Item removedItem;
        if((removedItem = items.remove(pos)) != null){
            currentWeight -= removedItem.getWeight();
            return true;
        }
        return false;
    }

    /**
     * Returns the current weight of the knapsack
     * @return: long: currentWeight of the knapsack
     */
    public long getCurrentWeight(){
        return currentWeight;
    }
    
    /**
     * Returns the current capacity of the knapsack
     * @return: long: The amount of more weight the knapsack can hold
     */
    public long getCurrentCapacity(){
        return (capacity - currentWeight);
    }
    
    /**
     * get the capacity of the knapsack
     * @return long: capacity of the knapsack
     */
    public long getCapacity() {
        return capacity;
    }

    /**
    *
    */
    public Item[] getItems(){
        Item[] itemsArray = new Item[items.size()];
        
        for(int i = 0; i < items.size(); i++) {
            itemsArray[i] = items.get(i);
        }
        
        return itemsArray;
    }

    /**
    * Return the array list of items
    * @return - ArrayList<Item>: list of items in this knapsack
    */
    public ArrayList<Item> getItemsArray(){
        return items;
    }
    
    /**
     * Returns an int array of items in the knapsack
     * @return: int[]: list of all items in the knapsack (by ID number)
     */
    public int[] getItemsByID() {
        int[] packingPlan = new int[items.size()];
        
        for(int i = 0; i < items.size(); i++) {
            packingPlan[i] = items.get(i).getItemNum();
        }
        
        return packingPlan;
    }
    
	/**
     * Returns the packing plan
     * @param: Individual: The tsp solution this knapsack is for
     * @param: int: The total amount of items available
     * @return: int[]: A binary int array of the packing plan
     */
    public int[] getPackingPlan(Individual TSPSolution, int totalItems) {
        // Setup a binary array
		int[] packingPlan = new int[totalItems];
        Arrays.fill(packingPlan, 0);
        
		for (int i = 0; i < items.size(); i++){
			packingPlan[items.get(i).getItemNum()] = 1;
		}
        int itemCount = totalItems/(TSPSolution.getNumCities()-1);
		
		// Order the plan
		int[] tours = TSPSolution.getCitiesByID();
		int[] packingPlanOrdered = new int[packingPlan.length];
		int pos = 0;
		for (int tourNum = 1; tourNum < tours.length - 1; tourNum++){
			int tourId = tours[tourNum];
			for (int i = 0; i < itemCount; i++){
				packingPlanOrdered[pos] = packingPlan[(tourId*itemCount) + i - itemCount];
				pos++;
			}
		}
		
		return packingPlanOrdered;
    }
	
    /**
     * Returns an the item specified by the given index
     * @param: int: The index of the item
     * @return: Item: The item at 'index'
     */
    public Item getItem(int index) {
       return items.get(index);
    }
    
    /**
     * Returns the number of items in the knapsack
     * @return: int: The length of items
     */
    public int getNumItems() {
       return items.size();
    }
    
    /**
     * Does the knapsack contain an item from this city?
     * @param: int: the cityNumber you're looking for
     */
    public boolean containsCity(int cityNum) {
        for(int i = 0; i < items.size(); i++) {
            Item it = items.get(i);
            if(it.getCityNum() == cityNum) {
                return true;
            }
        }
        return false;
    }

    /**
    * Checks to see if the currentItem is in the provided array
    * @param: currentItem: item to look for in the array
    * @return: boolean: true if the current item is in the solution, false otherwise
    */
    public boolean containsItem(Item currentItem){
        for(int i = 0; i < items.size(); i++){
            //item number will be the same (among other things)
            if(items.get(i).getItemNum() == currentItem.getItemNum()){
                return true;
            }
        }
        return false;
    }
    
    /**
     * Print function
     */
    public void print() {
        System.out.println("*** Knapsack Details ***");
        System.out.println("-> Capacity of Knapsack: " + capacity);
        System.out.println("-> Current Weight of Knapsack: " + currentWeight);
        //NB: percent sign is escaped with a percent sign.
        double percent = ((double)currentWeight/capacity) * 100;
        System.out.println(String.format("-> the Knapsack is %2.2f %% full!", percent));
        System.out.println("-> Current Items in the Knapsack:\n");
        if(items.isEmpty()) {
            System.out.println("--> Knapsack is empty");
        } else {
            System.out.printf("%-11s  %-11s  %-11s  %-11s  %-11s\n", "Item #", "Profit", "Weight", "Ratio", "From City #");
            System.out.println("-----------------------------------------------------------------");
            for(int i = 0; i < items.size(); i++) {
                Item it = items.get(i);
                System.out.printf("%-11d  %-11d  %-11d  %-9.2f    %-11d\n", it.getItemNum(), it.getProfit(), it.getWeight(), it.profitToWeightRatio(), it.getCityNum());
            }
        }
    }

    /**
    * Seeds the knapsack with a set of randomly chosen items
    * Modifies the local knapsack variable and returns it
    */
    private void random(){
        Random rand = new Random();
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsArray));
        Item currentItem = itemsList.get(rand.nextInt(itemsList.size()));
        itemsList.remove(currentItem);

        //pick items at random until the knapsack is at capacity
        while(currentItem.getWeight() <= getCurrentCapacity()){
            addItem(currentItem);

            //or if we run out of items
            if(itemsList.size() == 0){
                break;
            }

            currentItem = itemsList.get(rand.nextInt(itemsList.size()));            
            itemsList.remove(currentItem); //keeps track of items NOT in the knapsack
        }
    }

    /**
    * Seeds the knapsack with the items having the highest cost
    * Calls the sortByCost() method to achieve this
    * Modifies the local knapsack variable and returns it
    */
    private void costFirst(){
        ArrayList<Item> itemsList = sortByCost(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > getCurrentCapacity()){
                break;
            }
            addItem(currentItem);
        }
    }

    /**
    * Seeds the knapsack with the items having the lowest weight
    * Calls the sortByWeight() method to achieve this
    * Modifies the local knapsack variable and returns it
    */
    private void weightFirst(){
        ArrayList<Item> itemsList = sortByWeight(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > getCurrentCapacity()){
                break;
            }
            addItem(currentItem);
        }
    }

    /**
    * Sorts the items in the itemsArray by weight
    * @return: ArrayList<Item>: List containing sorted items from lowest to highest weight
    */
    private ArrayList<Item> sortByWeight(Item[] itemsToSort) {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsToSort));
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
    private ArrayList<Item> sortByCost(Item[] itemsToSort) {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsToSort));
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(itemsList, new Comparator<Item>() {
            @Override
            public int compare (Item i1, Item i2) {
                return (int)((i2.getProfit()/i2.getWeight()) - (i1.getProfit()/i1.getWeight()));
            }
        });
        return itemsList;
    }
}