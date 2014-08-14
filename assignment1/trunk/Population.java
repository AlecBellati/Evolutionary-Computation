import java.util.Arrays;
import java.util.Comparator;

public class Population{

	private Individual[] solution_set;

	private int num_solutions;
	
	/**
	* Constructor takes in initialised individual object
	* Used to extract multiple single solutions to form a population
	*/
	public Population(int size){
		num_solutions = size;
		solution_set = new Individual[size];
	}

	/*
	* Gets a basic set of solutions - specified by the user
	* Returns a City array of solutions;
	* Should not generate any errors
	*/
	public Individual[] generateRandomSolutionSet(double[][] TSPGraph, City[] cities){
		solution_set = new Individual[num_solutions];
		for(int i = 0; i < num_solutions; i++){
			solution_set[i] = new Individual(TSPGraph, cities, true);
		}
		return solution_set;
	}

	/**
	*
	*/
	public Individual[] getSolutionSet(){
		return solution_set;
	}

	/**
	*
	*/
	public Individual getSolution(int index){
		return solution_set[index];
	}

	/**
	*
	*/
	public void setSolution(int index, Individual individual){
		solution_set[index] = individual;
	}

	/**
	*
	*/
	public int getSize(){
		return solution_set.length;
	}

	/**
	*
	*/
	public void sort(){
		Arrays.sort(solution_set, new Comparator<Individual>() {
	        @Override
	        public int compare(Individual c1, Individual c2) {
	        	return (int)(c1.getCost() - c2.getCost());
	        }
	    });
	}

	/**
	*
	*/
	public double getAverageCost(){
		double cost = 0;
		for(int i = 0; i < solution_set.length; i++){
			cost += solution_set[i].getCost();
		}
		return (cost/solution_set.length);
	}
}