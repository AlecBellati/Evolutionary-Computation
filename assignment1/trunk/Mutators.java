import java.util.Random;

public class Mutators{
	
	/* Class variables */
	private Random rnd;		// Random number generator
	
	/**
	 * Constructor of the Mutators.
	 * Initialise the number generator for use as a global object
	 */
	public Mutators(){
		rnd = new Random();
	}
	
	/**
	 * Take one element and insert it after another, shifting all elements along as required
	 * @param Individual - an individual candidate of the population to mutate
	 */
	public void insert(Individual individual) {
		//get city move pos and place to put city
		int move = rnd.nextInt(individual.getNumCities());
		int insert = rnd.nextInt(individual.getNumCities());
		
		if (insert > move) {
			int temp = move;
			move = insert;
			insert = temp;
		}
		
		// Temp City variable
		City temp;
		City moveCity = individual.getCityByIndex(move);
		
		// Move all other cities down from move
		while (move > insert+1) {
			// Shift elements down
			temp = individual.getCityByIndex(move-1);
			individual.setCity(move, temp);
			move--;
		}
		individual.setCity(move, moveCity);
	}
	
	/**
	 * Swaps two random 'City' objects in the supplied solution
	 * @param Individual - an individual candidate of the population to mutate
	 */
	public void swap(Individual individual) {
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());
				
		swapCities(individual, posA, posB);
	}
	
	/**
	 * Inverts a random subsection of a given individual solution
	 * @param Individual - The solution of cities to be mutated
	 */
	public void inversion(Individual individual) {
		inversionOrScramble(individual, true);
	}
	
	/**
	 * Scrambles a random subsection of a given individual solution
	 * @param Individual - The solution of cities to be mutated
	 */
	public void scramble(Individual individual) {
		inversionOrScramble(individual, false);
	}
	
	/**
	 * Used by scramble and inversion
	 * Functions are simmilar and hence their functions have been combined
	 * @param Individual - The solution of cities to be mutated
	 * @param boolean inversion - if true, run the inversion, else run scramble
	 */
	private void inversionOrScramble(Individual individual, boolean inversion) {
		// Find the subset of position to mutate
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());
		
		// Ensure that posA is less than posB
		if (posA > posB){
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		}
		
		if (posA != posB) {
			if (inversion) {
				//invert positions in the array
				inverseSubset(individual, posA, posB);
			} else {
				//scramble the positions in the array
				int subsetSize = posB - posA;
				for (int i = posA; i <= posB; i++) {
					int index = rnd.nextInt(subsetSize);
					swapCities(individual, i, posA);
				}
			}
		}
	}
	
	/**
	 * Inverses a subset specified
	 * Used by inversionOrScramble and inverOver
	 * @param Individual - The solution of cities to be mutated
	 * @param int posA - first index of subset
	 * @param int posB - last index of subset
	 */
	public void inverseSubset(Individual individual, int posA, int posB) {
		int a = posA;
		int b = posB;
		
		// Handle the area to be inversed if it is circular (wraps around end of solution)
		while (a < individual.getNumCities() && b >= 0) {
			swapCities(individual, a, b);
			a++;
			b--;
		}
		if (a == individual.getNumCities()){
			a = 0;
		} else {
			b = individual.getNumCities() - 1;
		}
		
		// Inverse the other parts of the solution
		while (a < b) {
			swapCities(individual, a, b);
			a++;
			b--;
		}
	}
	
	/**
	 * Swaps two 'City' objects in the supplied solution in the supplied spots
	 * @param Individual - an individual candidate of the population to mutate
	 * @param int - index of the first city
	 * @param int - index of the second city
	 */
	public void swapCities(Individual individual, int posA, int posB) {
		City temp = individual.getCityByIndex(posA);
		individual.setCity(posA, individual.getCityByIndex(posB));
		individual.setCity(posB, temp);
	}
}