/*
 * Evolutionary Computation
 * COMP SCI 4095
 * Assignment One
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 */
 
import java.util.Arrays;

public class Dynamic {

    
	/*
     * Constructor
	 */	
    public Dynamic(){}
	
	/*
	 * Gets a solution for the knapsack problem
     */	
    public Item[] getSolution(Item[] items, int maxWeight, double epsilon){
		int[][] table;
		int maxProfit;
		int[] indices;

		if(epsilon > 0){
			FPTAS fptas = new FPTAS();
			Item[] newItems = fptas.adjustProfits(items, epsilon);
			maxProfit = maxProfit(newItems);

			//set table based on maxProfit
			table = new int[items.length][items.length * maxProfit];
			generateTable(newItems, table); //generate table
			indices = optimal(maxWeight, newItems, table); //find optimal profits, returns string for printing
		}else{
			maxProfit = maxProfit(items);

			//set table based on maxProfit
			table = new int[items.length][items.length * maxProfit];
			generateTable(items, table); //generate table
			indices = optimal(maxWeight, items, table); //find optimal profits, returns string for printing
		}
		
		//Find the items from the indices found
		Item[] results = new Item[indices.length];
		for (int i = 0; i < indices.length; i++){
			results[i] = items[indices[i]];
		}
		
		return results;
    }	

    /*
     * Finds the max profit of a given array of items
	 * @param: Item[]: An array of items
	 * @return: int: The highest profit value from the items
     */
    public int maxProfit(Item[] items){
		int maxProfit = items[0].getProfit();
		int nextProfit;
		for(int i = 1; i < items.length; i++){
			nextProfit = items[i].getProfit();
			
			if(maxProfit < nextProfit){
				maxProfit = nextProfit;
			}
		}
		return maxProfit;
    }

    /*
     * Generates the table bases on the dynamic programming algorithm
     */
    public void generateTable(Item[] items, int[][] table){
		int rowSize = table[0].length;	

		//sets up the table by putting MAX value (infinity) in the first column
		int itemSize = items.length;
		for(int i = 0; i < (rowSize); i++){
			table[0][i] = Integer.MAX_VALUE;
		}
		
		//adds 0 to the first position
		table[0][0] = 0;
		table[0][items[0].getProfit()] = items[0].getWeight(); //adds the FIRST weight to column one


		/*
		 * The dual loop runs through each column and row, assigning values based on the algorithms provided in lecture
		 * The two run-checks ensure no over-flow conditions from the array and the integers
		 */
		int nextProfit;
		for (int i = 0; i < itemSize; i++){
			for (int j = 0; j < (rowSize); j++){
				if (i+1 < itemSize){
					nextProfit = items[i + 1].getProfit();
					
					if (nextProfit <= j && table[i][j - nextProfit] != Integer.MAX_VALUE){
						table[i + 1][j] = Math.min(table[i][j], items[i + 1].getWeight() + table[i][j - nextProfit]);
					} else {
						table[i + 1][j] = table[i][j]; 
					}
				}
			}
		}
    }

    /*
     * As the table is sorted by maximum profit, we run through the table (from the back)
     * and find the maximum value, below our maximum weight
	 * @param: int: The maximum weight the knapsack can hold
	 * @param: Item[]: The items being put in the knapsack
	 * @param: int[][]: Table used for calculations
	 * @return: int[]: The index of the items taken (items may not be the originals so it needs to be done this way)
     */
    public int[] optimal(int maxWeight, Item[] items, int[][] table){
		int rowSize = table[0].length;	
		
		int optimalProfit = 0;
		int size = items.length;
		for(int i = rowSize - 1; i >= 0; i--){
			if(table[size - 1][i] <= maxWeight ){
				optimalProfit = i;
				i = 0;
			}
		}
		
		/*
		 * We then use this index value to calculate which items are contained within the set
		 * We use i+1 as i is the index number, not the set number
		 */
		int[] opt = new int[size];
		int optSize = 0;
		int tempProfit = optimalProfit;
		for(int i = size-1; i >= 1 ; i--){
			if(table[i][tempProfit] < table[i-1][tempProfit]){
				opt[optSize] = i;
				optSize++;
				
				tempProfit = tempProfit - items[i].getProfit();
			}
		}
		//if there is still a value in tempProfit then it means item 1 is contained in the set
		if(tempProfit > 0){
			opt[optSize] = 0;
			optSize++;
		}
		
		//Resize the opt array
		opt = Arrays.copyOf(opt, optSize);
		
		return opt;
    }
				    
    public static void main(String[] args){
		int size = 20;

		Dynamic dynamic = new Dynamic();
		
		generator gen = new generator();
		Item[] items = gen.generate(size);
		long startTime = System.currentTimeMillis();
		Item[] results = dynamic.getSolution(items, size*10000,0);
		
		long stopTime = System.currentTimeMillis();
		long elapsedTime = stopTime - startTime;
		System.out.println(elapsedTime);

    }						
}
