/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
package TTP.Thief.Travel;


public class Item {

    //Item Attributes
    private int itemNum;
    private int profit;
    private int weight;
    private int cityNum;
	
	/** Attributes for 'Alec' algorithm */
	private double pheromone;
	private double increaseRate;
	private double decreaseRate;
	private final double PHEROMONE_MIN = 1.0;
	private final double PHEROMONE_MAX = 100.0;
    
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
        this.cityNum = -1;
		
		setupPheromone();
    }

    /**
     * Secondary Item Constructor
     * @param: int: Item node number
     * @param: int: Profit value of item
     * @param: int: Weight of the item
     * @param: int: City number this item belongs too
     */
    public Item(int itemNumber, int itemProfit, int itemWeight, int cityNum) {
        itemNum = itemNumber;
        profit = itemProfit;
        weight = itemWeight;
        this.cityNum = cityNum;
		
		setupPheromone();
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
     * Return the City that this item belongs too
     * @return: int: city number that this item belongs to
     */
    public int getCityNum(){
        return cityNum;
    }
    
    /**
     * Calculate and return the Item's profit:weight ratio
     * @return: double: profit to weight ratio
     */
    public double profitToWeightRatio() {
        return ( (double)profit/weight );
    }
    
    /**
     * Compares the ratio of two Items (high to low)
     * @return int: -1 if this is less than the passed item, 0 if they're the same, else 1
     */
    public int compareRatio(Item item) {
        if(item.profitToWeightRatio() < this.profitToWeightRatio()) {
            return -1;
        } else if(item.profitToWeightRatio() == this.profitToWeightRatio()) {
            return 0;
        } else {
            return 1;
        }
    }
    
	/* Methods for the 'Alec' algorithm */
	
    /**
     * Get the pheromone value of this item
     * @return: double: the pheromone value of the item
     */
    public double getPheromone(){
		return pheromone;
    }
    
    /**
     * Get the probability of taking the item
     * @return: double: the probability of taking the item
     */
    public double getProbability(){
		return (pheromone / PHEROMONE_MAX);
    }
    
    /**
	 * Set up the pheromone value for the item
	 */
	public void setupPheromone(){
		pheromone = PHEROMONE_MIN;
	}
	
	/**
	 * Set the increase rate
	 * @param: double: the increase rate
	 */
	public void setIncreaseRate(double rate) {
		increaseRate = rate;
		
		increasePheromone();
	}
	
	/**
	 * Set the decrease rate
	 * @param: double: the decrease rate
	 */
	public void setDecreaseRate(double rate) {
		decreaseRate = rate;
	}
	
	/**
     * Increase the pheromone value of this item
     */
    public void increasePheromone(){
        pheromone += increaseRate;
		
		if (pheromone > PHEROMONE_MAX){
			pheromone = PHEROMONE_MAX;
		}
    }
    
    /**
     * Decrease the pheromone value of this item
     */
    public void decreasePheromone(){
        pheromone -= decreaseRate;
		
		if (pheromone < PHEROMONE_MIN){
			pheromone = PHEROMONE_MIN;
		}
    }
    
}