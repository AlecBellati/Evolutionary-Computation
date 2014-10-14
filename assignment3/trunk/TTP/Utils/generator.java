/*
 * Evolutionary Computation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
 
package TTP.Utils;

import TTP.Thief.Travel.Item;

import java.util.Random;

public class generator{

    public generator(){}
	
	/*
	 * Randomly generates n number of profits and weights
	 */
	public Item[] generate(int n){
		Item[] items = new Item[n];
		
		int profit;
		int weight;

		Random profitGenerator = new Random();
		Random weightGenerator = new Random();
		for (int i = 0; i < n; i++){
			profit = profitGenerator.nextInt(99999) +1;
			weight = weightGenerator.nextInt(99999) +1;
			
			items[i] = new Item(i, profit, weight);
		}

		return items;
    }

}
