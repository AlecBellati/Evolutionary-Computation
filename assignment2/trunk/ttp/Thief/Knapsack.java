package TTP.Thief;

import TTP.Thief.Travel.Item;

import java.util.ArrayList;


public class Knapsack {
    
    private long capacity;
    private long currentWeight;
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
    * @return: long: currentWeight of the knapsack
    */
    public long getCurrentWeight(){
        return currentWeight;
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
}