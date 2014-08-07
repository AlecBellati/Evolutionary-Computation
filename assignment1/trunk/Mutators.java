import java.util.Random;

public class Mutators{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	*
	*
	*/
	public Mutators(){
		rnd = new Random();
	}

	/**
	*
	*
	*/
	public City[] insert(City[] individual){

		return individual;
	}	

	/**
	*
	*
	*/
	public City[] swap(City[] individual){
		int posA = rnd.nextInt(individual.length-1);
		int posB = rnd.nextInt(individual.length-1);

		City temp = individual[posA];
		individual[posA] = individual[posB];
		individual[posB] = temp;

		return individual;
	}

	/**
	* Inverts a random subsection of a given array of cities.
	*
	* @param individual The array of cities to be mutated
	*/
	public City[] inversion(City[] individual){

		// Find the subset of position to mutate
		int posA = rnd.nextInt(individual.length - 1);
		int posB = rnd.nextInt(individual.length - 1);
		
		// Ensure that posA is less than posB
		if (posA > posB) {
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		} else if (posA == posB) {
			return individual;
		}
		
		// Invert the values between posA and posB (inclusive)
		int subsetSize = (int) Math.ceil((posB - posA)/2.0);
		
		for (int i = posA; i <= subsetSize; i++) {
			City cityTemp = individual[i];
			individual[i] = individual[posB];
			individual[posB] = cityTemp;
			posB--;
		}
		
		return individual;
	}

	/**
	 * Scrambles a random subsection of a given array of cities
	 *
	 * @param individual The array of cities to be mutated
	 */
	public City[] scramble(City[] individual){
		// Find the subset of position to mutate
		int posA = rnd.nextInt(individual.length - 1);
		int posB = rnd.nextInt(individual.length - 1);
		
		// Ensure that posA is less than posB
		if (posA > posB){
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		}
		else if (posA == posB){
			return individual;
		}
		
		// Scramble the positions in the array
		int subsetSize = posB - posA;
		for (int i = posA; i <= posB; i++){
			int index = rnd.nextInt(subsetSize);
			
			City cityTemp = individual[i];
			individual[i] = individual[index + posA];
			individual[index + posA] = cityTemp;
		}
		
		return individual;
	}

	/**
	* TESTING ONLY
	*/
	public static void main(String[] args){
		Mutators test = new Mutators();
	}
}