/*
 * Evolutionary Computation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
 
public class FPTAS{

    public FPTAS(){}

    /*
     * Adjust the given profits based on the epsilon and maximum profit
	 * @param: Item[]: An array of the items
	 * @param: double: The epsilon value to modify the profits
	 * @return: Item[]: The array of items with adjusted profits
     */
    public Item[] adjustProfits(Item[] items, double epsilon){

		int size = items.length;
		Item[] newItems = new Item[size];

		//find maximum profit
		int maxProfit = items[0].getProfit();
		int nextProfit;
		for(int i = 1; i < size; i++){
			nextProfit = items[i].getProfit();
			
			if(maxProfit < nextProfit){
				maxProfit = nextProfit;
			}
		}

		//K calculated here and applied to each profit using the floor function
		double K = ((epsilon*maxProfit)/((1 + epsilon)*size));
		int itemNum, itemProfit, itemWeight;
		for(int i = 0; i < size; i++){
			itemNum = items[i].getItemNum();
			itemProfit = (int)Math.floor((items[i].getProfit()/K));
			itemWeight = items[i].getWeight();
			
			newItems[i] = new Item(itemNum, itemProfit, itemWeight);
		}

		return newItems;
    }
}
