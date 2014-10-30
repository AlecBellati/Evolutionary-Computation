/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment Three
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
    * Either add (if space allows it) to the knapsack OR
    * Replace a number of items in the knapsack with better/random ones
    * Picks items to add/replace from a sorted list - sort dependant on the sortChoice integer supplied
    * 1 = by cost, 2 = by weight, default = random
    * Also removes a number of items at random to increase randomness!
    * @param: knapsack - knapsack to operate on - will eventually be returned
    * @param: randomProbability - probability of items randomly picked from the sorted array (remainder will be considered the "best" for that sort type)
    * @param: items - number of items to add/replace
    * @param: sortChoice - sort type to order items (and then pick from)
    * @param: removal - number of items to remove at random
    * @return: Knapsack - modified knapsack
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
            default:
                sortedItems = new ArrayList<Item>(Arrays.asList(itemsMinusKnapsack));
        }

        //remove some items from the knapsack to allow space for others
        knapsack = removeItemsKnapsack(knapsack, removal);

        Random rnd = new Random();
        int goodReplacement = (int)Math.round(items * (1-randomProbability));

        //add/replace "items" number of times
        for(int x = 0; x < items; x++){

            //pick the item based on the "randomProbability"
            //because of the sort method, the "best" items will be at the top
            Item currentItem = null;
            if(x <= goodReplacement){
                currentItem = sortedItems.get(x);
            }else{
                currentItem = sortedItems.get(rnd.nextInt(sortedItems.size()));
            }

            //hopefully its not already in the knapsack, but just in case
            if(!knapsack.containsItem(currentItem)){
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

                    //check its going to fit
                    if(currentItem.getWeight() <= (temp.getWeight() + knapsack.getCurrentCapacity())){
                        knapsack.setItem(tempIndex, currentItem);
                    }
                }
            }
        }

        return knapsack;
    }

    /**
    * Return an Item array containing those items not already in the knapsack
    * @param: knapsack - knapsack holding current set of items
    * @return: Item[] - items not already contained in the knapsack
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
    * Removes a number of items from the knapsack at random
    * @param: knapsack - knapsack to operate on, eventually returned
    * @param: iterations - number of items to remove at random
    * @return: Knapsack - modified knapsack (items now removed)
    */
    private Knapsack removeItemsKnapsack(Knapsack knapsack, int iterations){
        Random rnd = new Random();

        //check boundary size just in case
        if(iterations > knapsack.getNumItems()){
            iterations = knapsack.getNumItems();
        }

        for(int j = iterations-1; j >= 0; j--){
            int pos = rnd.nextInt(knapsack.getNumItems());
            knapsack.removeItem(pos);
        }

        return knapsack;
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