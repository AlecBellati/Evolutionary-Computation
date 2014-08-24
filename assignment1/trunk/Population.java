import java.util.Collections;
import java.util.Comparator;
import java.util.Random;
import java.util.ArrayList;

public class Population{

	/** Holds a set of solutions, characterised as a City[] */
	private ArrayList<Individual> solution_set;

	/** Number of solutions this population will hold */
	private int num_solutions;
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	* CONSTRUCTOR
	* Used to extract multiple single solutions to form a population
	* @param int size - number of solutions this population will hold
	*/
	public Population(int size){
		num_solutions = size;
		solution_set = new ArrayList<Individual>(size);
		
		rnd = new Random();
	}

	/**
	* Create a deep copy of this population array
	* @return Population - a deep copy of this object;
	*/
	public Population clone(){
		Population clone = new Population(getSize());
		for(int i = 0; i < getSize(); i++){
			clone.setSolution(i, solution_set.get(i).clone());
		}
		return clone;
	}

	/**
	*
	*
	*/
	public void add(Individual individual){
		solution_set.add(individual);
	}

	/**
	*
	*
	*/
	public void addSet(Individual[] individual){
		for(int i = 0; i < individual.length; i++){
            if(individual[i] != null) {
                solution_set.add(individual[i]);
            }
		}
	}

	/**
	*
	*
	*/
	public void addPopulation(Population population){
		ArrayList<Individual> temp_list = population.getSolutionSet();
		for(int i = 0; i < temp_list.size(); i++){
            if(temp_list.get(i) != null) {
                solution_set.add(temp_list.get(i));
            }
		}
	}

	/**
	* Gets a basic set of solutions - specified by the user
	* Should not generate any errors
	* @param double[][] TSPGraph - lookup table of city edges
	* @param City[] cities - array of cities, current solution
	*/
	public void generateRandomSolutionSet(City[] cities){
		solution_set = new ArrayList<Individual>();
		for(int i = 0; i < num_solutions; i++){
			solution_set.add(new Individual(cities, true));
		}
	}

	/**
	* Return the solution set
	* @return Individual[] - solutions of length 'num_solutions'
	*/
	public ArrayList<Individual> getSolutionSet(){
		return solution_set;
	}

	/**
	* Get the solution at index
	* @param int index - index position for solution retrieval
	* @return Individual - a specific solution, provided by the index number
	*/
	public Individual getSolution(int index){
		return solution_set.get(index);
	}

	/**
	* Get a random solution
	* @return Individual - a random solution contained in solution_set
	*/
	public Individual getRandomSolution(){
		int index = rnd.nextInt(num_solutions);
		return solution_set.get(index);
	}

	/**
	* Set the solution at the specific index position
	* @param int index - index position to place solution
	* @param Individual - solution to be inserted into the array at position index
	*/
	public void setSolution(int index, Individual individual){
		solution_set.add(index, individual);
	}

	/**
	* Return size of the solution set
	* @return int - size of this solution set
	*/
	public int getSize(){
		return solution_set.size();
	}

	/**
	* Sort the current solution set based on its each of their total costs
	*/
	public void sort(){
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(solution_set, new Comparator<Individual>() {
	        @Override
	        public int compare(Individual c1, Individual c2) {
	        	return (int)(c1.getCost() - c2.getCost());
	        }
	    });
	}

	/**
	* Return the total cost of all solutions
	* @return double - total cost of all solutions
	*/
	public double getTotalCost(){
		double cost = 0;
		for(int i = 0; i < getSize(); i++){
			cost += solution_set.get(i).getCost();
		}
		return cost;
	}
	
	/**
	* Return the average cost of this solution
	* @return double - average cost of this solution
	*/
	public double getAverageCost(){
		double cost = getTotalCost();
		
		return (cost/getSize());
	}

	/**
	* Return the best tour from this population
	* @return Individual - best solution from this population
	*/
	public Individual getBestSolution(){
		sort();
		return solution_set.get(0);
	}
	
    /**
     * Return the size of the population
     * @return int - size of current population
     */
    public int size() {
        return solution_set.size();
    }
    
    /**
     * Prints out each individual in the population
     */
    public void print() {
        for(int i = 0; i < solution_set.size(); i++) {
            System.out.print(i + " = ");
            solution_set.get(i).print();
        }
    }
}