package TTP.Thief;

import TTP.Thief.Travel.Item;
import TTP.Thief.Travel.Individual;

import java.util.ArrayList;
import java.util.Arrays;


public class Knapsack {
    
    private long capacity;
    private long currentWeight;
    private double rentingRatio;
    private ArrayList<Item> items;
    
    /**
     * Construct an empty knapsack with a known weight limit
     */
    public Knapsack(long _capacity) {
        capacity = _capacity;
        currentWeight = 0;
        items = new ArrayList<Item>();
    }

    /**
     * Construct a knapsack with items and a known weight limit
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
    *
    *
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
}