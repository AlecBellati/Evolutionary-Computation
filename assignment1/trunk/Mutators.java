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
	* Take one element and insert it after another, shifting all elements along as required
	* @param City[] - an individual candidate of the population to mutate
    * @return City[] - the insertion mutated individual
	*/
	public City[] insert(City[] individual){
        
        //get city move pos and place to put city
        int move = rnd.nextInt(individual.length);
        int insert = rnd.nextInt(individual.length);
        
        //Temp City Variable
        City temp;
        City moveCity = individual[move];
        
        //move all other cities down from move
        while (true) {
            
            //shift elements down
            if(move > insert+1) {
                temp = individual[move-1];
                individual[move-1] = moveCity;
                individual[move] = temp;
                
                move --;
                
            //shift elements up
            } else if (move < insert) {
                temp = individual[move+1];
                individual[move+1] = moveCity;
                individual[move] = temp;
                
                move++;
            
            //otherwise exit loop
            } else {
                break;
            }
        }
        
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
}