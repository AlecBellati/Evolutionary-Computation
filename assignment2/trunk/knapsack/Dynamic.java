/* 
 * Written by William Reid, Alec Bellati
 * Advanced Algorithms, s1, 2013
 * Assignment 1, updated 27/03/2013
 */	
    
public class Dynamic {

    /*
     * Main constructor; sets up the table and also sends off to modify the weights dependings on if
     * epsilon has been specified. (weights modified by epsilon if epsilon > 0)
     */	
    public Dynamic(int[] profit, int[] weight, int maxWeight, double epsilon){
	int[][] table;
	int maxProfit;

	if(epsilon > 0){
	    FPTAS fptas = new FPTAS();
	    int[] newProfits = fptas.adjustProfits(profit, epsilon);
	    maxProfit = maxProfit(newProfits);

	    //set table based on maxProfit
	    table = new int[profit.length][profit.length*maxProfit];
	    generateTable(newProfits, weight, table); //generate table
	    optimal(maxWeight, newProfits, table); //find optimal profits, returns string for printing
	}else{
	    maxProfit = maxProfit(profit);

	    //set table based on maxProfit
	    table = new int[profit.length][profit.length*maxProfit];
	    generateTable(profit, weight, table); //generate table
	    optimal(maxWeight, profit, table); //find optimal profits, returns string for printing
	}
    }	

    /*
     * Returns max profit for a given integer array of profits
     */
    public int maxProfit(int[] profit){
	int maxProfit = profit[0];
	for(int i = 1; i < profit.length; i++){
	    if(maxProfit < profit[i]){
		maxProfit = profit[i];
	    }
	}
	return maxProfit;
    }

    /*
     * Generates the table bases on the dynamic programming algorithm
     */
    public void generateTable(int[] profit, int[] weight, int[][] table){
	int maxProfit = maxProfit(profit);	

	//sets up the table by putting MAX value (infinity) in the first column
	for(int i = 0; i < (profit.length*maxProfit); i++){
	    table[0][i] = Integer.MAX_VALUE;
	}
	
	//adds 0 to the first position
	table[0][0] = 0;
	table[0][profit[0]] = weight[0]; //adds the FIRST weight to column one


	/*
	 * The dual loop runs through each column and row, assigning values based on the algorithms provided in lecture
	 * The two run-checks ensure no over-flow conditions from the array and the integers
	 */
	for(int i = 0; i < profit.length; i++){
	    for(int j = 0; j < (profit.length*maxProfit); j++){
		if(i+1 < profit.length){
		    if(profit[i+1] <= j && table[i][j-profit[i+1]] != Integer.MAX_VALUE){
			table[i+1][j] = Math.min(table[i][j], weight[i+1]+table[i][j-profit[i+1]]);
		    }else{
			table[i+1][j] = table[i][j]; 
		    }
		}
	    }
	}
    }

    /*
     * As the table is sorted by maximum profit, we run through the table (from the back)
     * and find the maximum value, below our maximum weight
     */
    public String optimal(int maxWeight, int[] profit, int[][] table){
	int maxProfit = maxProfit(profit);	
	
	int optimalProfit = 0;
	int size = profit.length;
	for(int i = (size * maxProfit)-1; i >= 0; i--){
	    if(table[size-1][i] <= maxWeight ){
		optimalProfit = i;
		i = 0;
	    }
	}
	
	/*
	 * We then use this index value to calculate which items are contained within the set
	 * We use i+1 as i is the index number, not the set number
	 */
	String opt = null;
	int tempProfit = optimalProfit;
	for(int i = size-1; i >= 1 ; i--){
	    if(table[i][tempProfit] < table[i-1][tempProfit]){
		if(opt == null){
		    opt = Integer.toString(i+1);
		}else{
		    opt += ", " + Integer.toString(i+1);
		}
		tempProfit = tempProfit - profit[i];
	    }
	}
	//if there is still a value in tempProfit then it means item 1 is contained in the set
	if(tempProfit > 0){
	    if(opt == null){
		opt = Integer.toString(1);
	    }else{
		opt += ", " + Integer.toString(1);
	    }
	}
	return opt + ", with optimal profit = " + Integer.toString(optimalProfit) + ", with weight = " + 
	    Integer.toString(table[size-1][optimalProfit]);
    }
				    
    public static void main(String[] args){
	int size = 20;

	generator gen = new generator();
	int[][] testArr = gen.generate(size);
	long startTime = System.currentTimeMillis();
	Dynamic test = new Dynamic(testArr[0], testArr[1], size*10000,0);

	long stopTime = System.currentTimeMillis();
	long elapsedTime = stopTime - startTime;
	System.out.println(elapsedTime);

    }						
}
