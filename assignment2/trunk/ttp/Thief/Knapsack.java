package TTP.Thief;

import TTP.Thief.Travel.Item;
import TTP.Thief.Travel.City;

import java.util.ArrayList;
import java.util.Arrays;


public class Knapsack {
    
    private double capacity;
    private double currentWeight;
    private ArrayList<Item> items;
    
    /**
     * Construct an empty knapsack with a known weight limit
     */
    public Knapsack(double _capacity) {
        capacity = _capacity;
        currentWeight = 0;
        items = new ArrayList<Item>();
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
     * @return: double: currentWeight of the knapsack
     */
    public double getCurrentWeight(){
        return currentWeight;
    }
    
    /**
     * Returns the current capacity of the knapsack
     * @return: double: The amount of more weight the knapsack can hold
     */
    public double getCurrentCapacity(){
        return (capacity - currentWeight);
    }
    
    /**
     * Returns an int array of items in the knapsack
     * @return: int[]: list of all items in the knapsack (by ID number)
     */
    public int[] getItemsByID() {
        int[] packingPlan = new int[items.size()];
        
        for(int i = 0; i < items.size(); i++) {
            items.get(i).getItemNum();
        }
        
        return packingPlan;
    }
    
	/**
     * Returns the packing plan
     * @param: City[]: The cities of the problem
     * @param: int: The total amount of items
     * @return: int[]: A binary int array of the packing plan
     */
    public int[] getPackingPlan(City[] cities, int totalItems) {
        // Setup a binary array
		int[] packingPlan = new int[totalItems];
        Arrays.fill(packingPlan, 0);
		
		// Fill in the taken items
		int i = 0;
		for (int c = 0; c < cities.length; c++){
			City currCity = cities[c];
			Item[] currItems = currCity.getItems();
			
			for (int j = 0; j < currCity.getItemCount(); j++){
				if (items.contains(currItems[j])){
					packingPlan[i] = 1;
				}
				i++;
			}
		}
		/*
		System.out.println("********");
		
		for (i = 0; i < packingPlan.length; i++){
			System.out.println(packingPlan[i]);
		}
		
		System.out.println("********");
		*/
        return packingPlan;
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
}