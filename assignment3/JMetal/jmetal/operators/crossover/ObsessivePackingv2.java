/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment Three
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */

package jmetal.operators.crossover;

import jmetal.core.Variable;
import jmetal.core.Solution;

import jmetal.problems.TTP.City;
import jmetal.problems.TTP.Item;
import jmetal.problems.TTP.City;

import jmetal.util.JMException;

import jmetal.encodings.variable.Individual;
import jmetal.encodings.solutionType.IndividualSolutionType;
import jmetal.encodings.variable.Knapsack;

import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Random;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;


public class ObsessivePackingv2 extends Crossover{
    
    //TSP Variables
    private City[] cities;
    
    //TTP Variables
    private Item[] itemsArray;
    
    //random number generator
    Random rnd = new Random();
    
    /**
     * CONSTRUCTOR
     * Assign local variables
     * Initialise and create a new knapsack
     * @param - _cities: contains a list of all the cities
     * @param - _itemsArray: contains a list of all the items
     */
    public ObsessivePackingv2(HashMap<String, Object> parameters) {
        super(parameters);
        
        rnd = new Random();
    }
    
    /**
     * Run the obsessive packer
     * @param Object: casts to Solution
     */
    public Object execute(Object object) throws JMException {
        boolean debug = false;
        //Typecast the object to a solution and perform the inversion
        Solution[] solution = (Solution[]) object;
        Solution[] newSolution = new Solution[solution.length];
        
        if(debug) {
            System.out.println("########## START CROSSOVER ##########");
        }
        
        for(int i = 0; i < solution.length; i++) {
            if(debug) {
                System.out.println("********** Individual " + i + " *********");
            }

            //Get two individual from the solution and perform packing on them both
            Variable[] indivArr = (Variable[]) solution[i].getDecisionVariables();
            Individual parentA = (Individual) indivArr[i];
        
            //setup required variables
            cities = parentA.getCities();
            itemsArray = parentA.getItems();
        
            //run obsessive packing over parentA
            Knapsack knapsack = parentA.getKnapsack();
            double randomProbability = rnd.nextDouble();
            int items = rnd.nextInt(15);
            int choice = 0;//rnd.nextInt(2)+1;
            int removal = rnd.nextInt(3);
        
            if(debug) {
                System.out.println("Probability: "+randomProbability);
                System.out.println("items: "+items);
                System.out.println("choice: "+choice);
                System.out.println("removal: "+removal);
                System.out.println("before");
                knapsack.print();
            }
            //run obsessive packing over parentA
            parentA.setKnapsack(changePacking(knapsack, randomProbability, items, choice, removal));
        
            if(debug) {
                System.out.println("after");
                knapsack.print();
            }
            
            newSolution[i] = new Solution(solution[i]);
        }
        if(debug) {
            System.out.println("########## END CROSSOVER ##########");
        }
        
        return newSolution;
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