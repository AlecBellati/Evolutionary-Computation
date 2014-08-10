import java.util.Random;
import java.util.Arrays;
import java.util.Comparator;

public class Selection{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;
	/** For this purpose used to compute a total cost for each solution */
    private Individual individual;

	/**
	* CONSTRUCTOR
	* Initialise the random number generator
	* @param City[][]
	*/
	public Selection(Individual individual){
		rnd = new Random();
		this.individual = individual;
	}

	/**
	*
	* @param City[][]
    * @return City[][]
	*/
	public City[][] fitness_proportional(City[][] solution){
		City[][] modified_solution = null;

		return modified_solution;
	}

	/**
	* Given a solution, randomly select num_population solutions
	* Given this new subset, sort them by total cost and then pick the best based
	* on the supplied integer - solution_size
	* @param City[][] - solution set
	* @param num_population - number of random solutions to be used for selection
	* @param solution_size - number of solutions to select based on the highest profits
    * @return City[][]
	*/
	public City[][] tournament_selection(City[][] solution, int num_population, int solution_size){
		//can't get a larger solution set than what was supplied!!!
		if(num_population > solution.length || num_population < solution_size){
			System.out.println("ERROR: requested solution size is outside the bounds of the supplied data set - tournament_selection()");
			System.out.println("RETURNED: original supplied data");
		}else{
			int size = solution[0].length;
			City[][] reduced_solution = new City[num_population][size];

			//randomly select "num_population" solutions
			for(int i = 0; i < num_population; i++){
				reduced_solution[i] = solution[rnd.nextInt(solution.length)];
			}

			//sorts the reduced_solution array based on their total cost
			Arrays.sort(reduced_solution, new Comparator<City[]>() {
	            @Override
	            public int compare(City[] c1, City[] c2) {
	                return (int)(individual.get_cost(c2) - individual.get_cost(c1));
	            }
	        });

			//as the array is now sorted, select "solution_size" number of solutions, starting from 0
			City[][] profit_solution = new City[solution_size][size];
	        for(int j = 0; j < solution_size; j++){
	        	profit_solution[j] = reduced_solution[j];
	        }

			return profit_solution;
		}
		return solution;
	}


	/**
	*
	* @param City[][]
    * @return City[][]
	*/
	public City[][] elitism(City[][] solution){
		City[][] modified_solution = null;

		return modified_solution;
	}

	/**
	* TESTING ONLY
	*/
	public static void main(String[] args){
		Selection test = new Selection(null);
	}
}