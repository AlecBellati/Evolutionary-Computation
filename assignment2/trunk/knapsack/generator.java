/* 
 * Written by William Reid, Alec Bellati
 * Advanced Algorithms, s1, 2013
 * Assignment 1, updated 27/03/2013
 */	

import java.util.Random;

public class generator{

    private int[] profits;
    private int[] weights;

    public generator(){}

    /*
     * Randomly generates n number of profits and weights
     */
    public int[][] generate(int n){
	profits = new int[n];
	weights = new int[n];

	Random profitGenerator = new Random();
	Random weightGenerator = new Random();
	for (int i = 0; i < n; i++){
	    profits[i] = profitGenerator.nextInt(99999) +1;
	    weights[i] = weightGenerator.nextInt(99999) +1;
	}

	//returns an array of integer arrays
	return new int[][]{profits, weights};
    }

}
