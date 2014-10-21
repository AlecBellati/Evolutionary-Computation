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
import TTP.Thief.Travel.Item;
import TTP.Thief.Knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;

public class ObsessivePackingv2 {
    
    //TSP Variables
    private City[] cities;
    
    //TTP Variables
    private Item[] itemsArray;
    
    /**
     * CONSTRUCTOR
     * Assign local variables
     * Initialise and create a new knapsack
     * @param - _cities: contains a list of all the cities
     * @param - _itemsArray: contains a list of all the items
     */
    public ObsessivePackingv2(City[] _cities, Item[] _itemsArray) {
        //Setup variables
        cities = _cities;
        itemsArray = _itemsArray;
    } 

    /**
    * Depending on the supplied integer, seed the knapsack with values
    * @param: algorithm - determines the algorithm to use
    * @return: Knapsack - 
    */
    public Knapsack seedKnapsack(Knapsack knapsack, int algorithm){
        switch(algorithm){
            case 1:
                return costFirst(knapsack);
            case 2:
                return weightFirst(knapsack);
            case 3:
                return random(knapsack);
            case 4:
                return tourLast(knapsack);
            case 5:
                return weightedCostFirst(knapsack);
        }
        return null;
    }   

    /**
    * 
    *
    * @param: knapsack -
    * @param: randomProbability -
    * @param: items -
    * @param: sortChoice -
    * @param: removal -
    * @return: Knapsack - 
    */
    public Knapsack changePacking(Knapsack knapsack, double randomProbability, int items, int sortChoice, int removal){
        Item[] itemsMinusKnapsack = itemsMinusKnapsack(knapsack);
        ArrayList<Item> sortedItems = null;
        switch(sortChoice){
            case 1:
                sortedItems = sortByCost(itemsMinusKnapsack);
                break;
            case 2:
                sortedItems = sortByWeight(itemsMinusKnapsack);
                break;
            case 3:
                sortedItems = sortByWeightedCost(itemsMinusKnapsack);
                break;
            case 4:
                sortedItems = sortByCity(itemsMinusKnapsack);
                break;
            default:
                sortedItems = new ArrayList<Item>(Arrays.asList(itemsMinusKnapsack));
            }

        Random rnd = new Random();
        int goodReplacement = (int)Math.round(items * randomProbability);

        //check every item we are sent
        for(int x = 0; x < items; x++){

            Item currentItem = null;
            if(x <= goodReplacement){
                currentItem = sortedItems.get(x);
            }else{
                currentItem = sortedItems.get(rnd.nextInt(sortedItems.size()));
            }
            

            boolean added = false;
            //check if it can just be added right away
            if(currentItem.getWeight() <= knapsack.getCurrentCapacity()){
                knapsack.addItem(currentItem);
                added = true;
            }

            //if the item was not added above, do the following
            if(!added){
                int tempIndex = rnd.nextInt(knapsack.getNumItems());
                Item temp = knapsack.getItem(tempIndex);

                //check its going to fit and that it isnt already in the solution
                if(currentItem.getWeight() <= (temp.getWeight() + knapsack.getCurrentCapacity())){
                    //replace the item and position j and calculate its cost
                    knapsack.setItem(tempIndex, currentItem);
                }
            }
        }
        knapsack = removeItemsKnapsack(knapsack, removal);

        return knapsack;
    }

    /**
    *
    * @param: knapsack -
    * @return: ArrayList<Item> -
    */
    private Item[] itemsMinusKnapsack(Knapsack knapsack){
        Item[] removedKnapsack = new Item[itemsArray.length - knapsack.getNumItems()];

        int pos = 0;
        for(int i = 0; i < itemsArray.length; i++){
            if(!knapsack.containsItem(itemsArray[i])){
                removedKnapsack[pos] = itemsArray[i];
                pos++;
            }
        }

        return removedKnapsack;
    }

    /**
    * 
    * @param: knapsack -
    * @param: iterations -
    * @return: Knapsack -
    */
    private Knapsack removeItemsKnapsack(Knapsack knapsack, int iterations){
        Random rnd = new Random();

        //check boundary size just in case
        if(iterations > knapsack.getNumItems()){
            iterations = knapsack.getNumItems();
        }

        //check every item in the knapsack
        for(int j = iterations-1; j >= 0; j--){
            int pos = rnd.nextInt(knapsack.getNumItems());
            knapsack.removeItem(pos);
        }

        return knapsack;
    }

    /**
    * Seeds the knapsack with a set of randomly chosen items
    * Modifies the local knapsack variable and returns it
    * @param: knapsack - knapsack to seed
    * @return: Knapsack - seeded Knapsack
    */
    private Knapsack random(Knapsack knapsack){
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

        return knapsack;
    }

    /**
    * Seeds the knapsack with the items having the highest cost
    * Calls the sortByCost() method to achieve this
    * Modifies the local knapsack variable and returns it
    * @param: knapsack - knapsack to seed
    * @return: Knapsack - seeded Knapsack
    */
    private Knapsack costFirst(Knapsack knapsack){
        ArrayList<Item> itemsList = sortByCost(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }
        
        return knapsack;
    }

    /**
    * Seeds the knapsack with the items having the highest weighted cost
    * Calls the sortByWeightedCost() method to achieve this
    * Modifies the local knapsack variable and returns it
    * @param: knapsack - knapsack to seed
    * @return: Knapsack - seeded Knapsack
    */
    private Knapsack weightedCostFirst(Knapsack knapsack){
        ArrayList<Item> itemsList = sortByWeightedCost(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }

        return knapsack;
    }

    /**
    * Seeds the knapsack with the items closest to the end of the tour
    * Calls the sortByCity() method to achieve this
    * Modifies the local knapsack variable and returns it
    * @param: knapsack - knapsack to seed
    * @return: Knapsack - seeded Knapsack
    */
    private Knapsack tourLast(Knapsack knapsack){
        ArrayList<Item> itemsList = sortByCity(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }

        return knapsack;
    }

    /**
    * Seeds the knapsack with the items having the lowest weight
    * Calls the sortByWeight() method to achieve this
    * Modifies the local knapsack variable and returns it
    * @param: knapsack - knapsack to seed
    * @return: Knapsack - seeded Knapsack
    */
    private Knapsack weightFirst(Knapsack knapsack){
        ArrayList<Item> itemsList = sortByWeight(itemsArray);
        for(int i = 0; i < itemsList.size(); i++){
            Item currentItem = itemsList.get(0);
            itemsList.remove(0);
            if(currentItem.getWeight() > knapsack.getCurrentCapacity()){
                break;
            }
            knapsack.addItem(currentItem);
        }

        return knapsack;
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
    * Sorts the items in the itemsArray by the TSP tour
    * @return: ArrayList<Item>: List containing sorted items matching the TSP tour
    */
    private ArrayList<Item> sortByCity(Item[] itemsToSort) {
        ArrayList<Item> itemsList = new ArrayList<Item>(Arrays.asList(itemsToSort));
        ArrayList<Item> sortedItemsList = new ArrayList<Item>();

        for(int i = cities.length-1; i > 0; i--){
            for(int j = itemsList.size()-1; j >= 0; j--){
                if(cities[i].getNodeNum() == itemsList.get(j).getCityNum()){
                    sortedItemsList.add(itemsList.get(j));
                    itemsList.remove(j);
                }
            }
        }

        return sortedItemsList;
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

    /**
    * HELPER METHOD
    * Converts and ArrayList<Item> to an Item[]
    * @param: optimal: the current knapsack solution
    * @return: Item[]: returns a new Item[] without items in the knapsack
    */
    private Item[] convertArrayList(ArrayList<Item> itemsToConvert){
        return itemsToConvert.toArray(new Item[itemsToConvert.size()]);
    }

    public static void main(String[] args){

    }
}