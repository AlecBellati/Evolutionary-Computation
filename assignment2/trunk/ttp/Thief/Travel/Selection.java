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

import java.util.Random;
import java.util.ArrayList;

public class Selection {
	
	/* Class variables */
	private Random rnd;													// Random number generation
	private double elitismPercentage = 0.75;		// Used for elite population selection
	
	/**
	 * Constructor of a Selection.
	 * Initialise the random number generator
	 */
	public Selection() {
		rnd = new Random();
	}
	
	/**
	 * Change the elitism percentage to adjust elitism selection.
	 * @param percent - the new elitism percentage
	 */
	public void setElitismPercentage(double percent) {
		elitismPercentage = percent;
	}
	
	/**
	 * Get the current elitism percentage value.
	 * @return elitismPercentage - the current elitism selection percentage
	 */
	public double getElitismPercentage() {
		return elitismPercentage;
	}
	
	/**
	 * Given a solution, randomly select numPopulation solutions.
	 * Find the total fitness of all solutions and the probability each solution contributes.
	 * Using this information, "spin a wheel" where each solution has the probability of
	 * being picked the same as their contribution.
	 * @param Population - solution set
	 * @param solutionSize - number of solutions to select based on the highest profits
	 * @return Population
	 */
	public Population fitnessProportional(Population solution, int solutionSize) {
		int length = solution.getSize();
		
		// Make sure the given population is not already less than or equal
		// to the given solution size
		if (length <= solutionSize){
			return solution;
		}
		
		// Create new population
		Population modifiedSolution = new Population(solutionSize);
		
		// Get the total fitness
		double totalFitness = solution.getTotalCost();
		double multiplier = length - 1;
		
		// Find the solutions for the population
		double next, total, current;
		int i, j;
		Individual newInd;
		for (i = 0; i < solutionSize; i++){
			// Get a number between 0 and 1
			next = rnd.nextDouble();
			next *= multiplier;
			
			// Cycle through until the probability is found
			total = 0;
			j = -1;
			while (j < length && total <= next){
				j++;
				
				current = solution.getSolution(j).getCost() / totalFitness;
				current = 1 - current;
				total += current;
			}
			
			// Save the solution found
			newInd = solution.getSolution(j).clone();
			modifiedSolution.setSolution(i, newInd);
		}
		
		return modifiedSolution;
	}
	
	/**
	 * Given a solution, randomly select numPopulation solutions
	 * Given this new subset, sort them by total cost and then pick the best based
	 * on the supplied integer - solutionSize
	 * @param Population - solution set
	 * @param numPopulation - number of random solutions to be used for selection
	 * @param solutionSize - number of solutions to select based on the highest profits
	 * @return Population
	 */
	public Population tournamentSelection(Population solution, int numPopulation, int solutionSize){
		int chooseFrom = 5;
		
		//can't get a larger solution set than what was supplied!!!
		if (numPopulation < solutionSize) {
			System.out.println("Supplied population size: " + solution.getSize());
			System.out.println("Requested population size: " + numPopulation + ", Requested solution size: " + solutionSize);
			System.out.println("ERROR: requested solution size is outside the bounds of the supplied data set - tournament_selection()");
			System.out.println("RETURNED: original supplied data");
		} else {
			Population reduced_solution = solution.clone();
			Population profitSolution = new Population(solutionSize);
			
			for (int i = 0; i < solutionSize; i++) {
				Population selected = new Population(chooseFrom);
				//randomly select solutions, specified by "chooseFrom" variable above
				for (int j = 0; j < chooseFrom; j++) {
					selected.setSolution(j, solution.getSolution(rnd.nextInt(solution.getSize())));
				}
				//from those solutions, pick the best one and add it to the new population
				profitSolution.setSolution(i, selected.getBestSolution().clone());
				reduced_solution.remove(selected.getBestSolution());
			}
			
			return profitSolution;
		}
		return solution;
	}
	
	
	/**
	 * Given a soluton, generate relative fitnesses for solutions
	 * And rank the population according to relative fitness
	 * @param Population - solution set
	 * @param numPopulation - number of solutions to be used for selection
	 * @return Population
	 */
	public Population elitism(Population solution, int numPopulation) {
		int length = solution.getSize();
		
		// Make sure the given population is not already less than or equal
		// to the given solution size
		if (length <= numPopulation){
			return solution;
		}
		
		// Create new population
		Population modifiedSolution = new Population(numPopulation);
		
		// Sort current population
		solution.sort();
		
		// Select elite solutions
		int noEliteSolns = (int) (numPopulation * elitismPercentage);
		for (int i = 0; i < noEliteSolns; i++) {
			modifiedSolution.setSolution(i, solution.getSolution(i));
		}
		
		// Select random solutions from the remainder
		int range = length - noEliteSolns;
		for(int i = noEliteSolns; i < numPopulation; i++){
			modifiedSolution.setSolution(i, solution.getSolution(rnd.nextInt(range) + noEliteSolns));
		}
		
		return modifiedSolution;
	}
	
	/**
	 * TESTING ONLY
	 */
	public static void main(String[] args){
		Selection test = new Selection();
	}
}