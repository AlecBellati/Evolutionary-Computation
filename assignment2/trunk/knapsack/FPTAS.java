/* 
 * Written by William Reid, Alec Bellati
 * Advanced Algorithms, s1, 2013
 * Assignment 1, updated 27/03/2013
 */

public class FPTAS{

    public FPTAS(){}

    /*
     * Adjust the given profits based on the epsilon and maximum profit
     */
    public int[] adjustProfits(int[] profits, double epsilon){

	int size = profits.length;
	int[] newProfits = new int[size];

	//find maximum profit
	int maxProfit = profits[0];
	for(int i = 1; i < size; i++){
	    if(maxProfit < profits[i]){
		maxProfit = profits[i];
	    }
	}

	//K calculated here and applied to each profit using the floor function
	double K = ((epsilon*maxProfit)/((1 + epsilon)*size));
	for(int i = 0; i < size; i++){
	    newProfits[i] = (int)Math.floor((profits[i]/K));
	}

	return newProfits;
    }
}
