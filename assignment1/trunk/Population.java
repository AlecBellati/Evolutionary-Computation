import java.util.Arrays;
import java.util.Comparator;

public class Population{

	/** Holds a set of solutions, characterised as a City[] */
	private Individual[] solution_set;

	/** Number of solutions this population will hold */
	private int num_solutions;
	
	/**
	* CONSTRUCTOR
	* Used to extract multiple single solutions to form a population
	* @param int size - number of solutions this population will hold
	*/
	public Population(int size){
		num_solutions = size;
		solution_set = new Individual[size];
	}

	/**
	* Gets a basic set of solutions - specified by the user
	* Should not generate any errors
	* @param double[][] TSPGraph - lookup table of city edges
	* @param City[] cities - array of cities, current solution
	*/
	public void generateRandomSolutionSet(double[][] TSPGraph, City[] cities){
		solution_set = new Individual[num_solutions];
		for(int i = 0; i < num_solutions; i++){
			solution_set[i] = new Individual(TSPGraph, cities, true);
		}
	}

	/**
	* Return the solution set
	* @return Individual[] - solutions of length 'num_solutions'
	*/
	public Individual[] getSolutionSet(){
		return solution_set;
	}

	/**
	* Get the solution at index
	* @param int index - index position for solution retrieval
	* @return Individual - a specific solution, provided by the index number
	*/
	public Individual getSolution(int index){
		return solution_set[index];
	}

	/**
	* Set the solution at the specific index position
	* @param int index - index position to place solution
	* @param Individual - solution to be inserted into the array at position index
	*/
	public void setSolution(int index, Individual individual){
		solution_set[index] = individual;
	}

	/**
	* Return size of the solution set
	* @return int - size of this solution set
	*/
	public int getSize(){
		return solution_set.length;
	}

	/**
	* Sort the current solution set based on its each of their total costs
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
	* Return the average cost of this solution
	* @return double - average cost of this solution
	*/
	public double getAverageCost(){
		double cost = 0;
		for(int i = 0; i < solution_set.length; i++){
			cost += solution_set[i].getCost();
		}
		return (cost/solution_set.length);
	}
}