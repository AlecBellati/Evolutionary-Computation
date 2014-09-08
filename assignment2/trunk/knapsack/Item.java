/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */


public class Item {

    //Item Attributes
    private int itemNum;
    private int profit;
    private int weight;
    
    /**
     * Item Constructor
     * @param: int: Item node number
     * @param: int: Profit value of item
     * @param: int: Weight of the item
     */
    public Item(int itemNumber, int itemProfit, int itemWeight) {
        itemNum = itemNumber;
        profit = itemProfit;
        weight = itemWeight;
    }
    
    /**
     * Return Item number
     * @return: int: item number
     */
    public int getItemNum() {
        return itemNum;
    }
    
    /**
     * Get item profit
     * @return: int: item profit
     */
    public int getProfit() {
        return profit;
    }
    
    /**
     * Get item weight
     * @return: int: item weight
     */
    public int getWeight() {
        return weight;
    }
    
    /**
     * Calculate and return the Item's profit:weight ratio
     * @return: double: profit to weight ratio
     */
    public double profitToWeightRatio() {
        return ( (double)profit/weight );
    }
}