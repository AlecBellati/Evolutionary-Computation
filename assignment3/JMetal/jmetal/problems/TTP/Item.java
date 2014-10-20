/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
package jmetal.problems.TTP;


public class Item {

    //Item Attributes
    private int itemNum;
    private int profit;
    private int weight;
    private int cityNum;
    private boolean taken;
	
	/** Attributes for 'Alec' algorithm */
	private double pheromone;
	private double attractiveness;
	private final double PHEROMONE_MIN = 1.0;
	private final double PHEROMONE_MAX = 100.0;
    private final double PHEROMONE_DECREASE_RATE = 1.0;
    
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
        taken = false;
		
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
		taken = false;
        
		setupPheromone();
    }
    
    /**
     * Return whether the item is taken
     * @return: boolean: true if taken, else false
     */
    public boolean isTaken() {
        return taken;
    }
    
    /**
     * Set state of the item as taken or not taken
     * @param: boolean: true if taken, else false
     */
    public void takeItem(boolean take) {
        taken = take;
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
     * Get the attractiveness of this item
     * @return: double: the attractiveness of the item
     */
    public double getAttractiveness(){
		return attractiveness;
    }
    
    /**
     * Get the probability of taking the item
     * @return: double: the probability of taking the item
     */
    public double getProbability(){
		return (pheromone / PHEROMONE_MAX);
    }
    
    /**
	 * Set up the pheromone value for the item as well as the attractiveness
	 */
	public void setupPheromone(){
		pheromone = PHEROMONE_MIN;
		attractiveness = profitToWeightRatio();
	}
	
	/**
	 * Set the pheromone value
	 * @param: double: The amount to set the pheromone value
     */
	public void setPheromone(double pheromone) {
		this.pheromone = pheromone;
	}
	
	/**
     * Increase the pheromone value of this item
	 * @param: double: The amount to increase the pheromone by
     */
    public void increasePheromone(double amount){
        pheromone += amount;
		
		if (pheromone > PHEROMONE_MAX){
			pheromone = PHEROMONE_MAX;
		}
    }
    
    /**
     * Decrease the pheromone value of this item
     */
    public void decreasePheromone(){
        pheromone -= PHEROMONE_DECREASE_RATE;
		
		if (pheromone < PHEROMONE_MIN){
			pheromone = PHEROMONE_MIN;
		}
    }
    
}