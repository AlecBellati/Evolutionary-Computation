import java.util.Random;

public class Mutators{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	* CONSTRUCTOR
	* Initialised the number generator for use as a global object
	*/
	public Mutators(){
		rnd = new Random();
	}

	/**
	* Take one element and insert it after another, shifting all elements along as required
	* @param City[] - an individual candidate of the population to mutate
    * @return City[] - the insertion mutated individual
	*/
	public void insert(Individual individual){
        
        //get city move pos and place to put city
        int move = rnd.nextInt(individual.getNumCities());
        int insert = rnd.nextInt(individual.getNumCities());
        
        //Temp City Variable
        City temp;
        City moveCity = individual.getCityByIndex(move);
        
        //move all other cities down from move
        while (true) {
            
            //shift elements down
            if(move > insert+1) {
                temp = individual.getCityByIndex(move-1);
                individual.setCity(move-1, moveCity);
                individual.setCity(move, temp);
                
                move --;
                
            //shift elements up
            } else if (move < insert) {
                temp = individual.getCityByIndex(move+1);
                individual.setCity(move+1, moveCity);
                individual.setCity(move, temp);
                
                move++;
            
            //otherwise exit loop
            } else {
                break;
            }
        }
	}	

	/**
	* Swaps two random "City" objects in the solution array
	* @param City[] - an individual candidate of the population to mutate
	* @return City[] - a solution after mutation has occured
	*/
	public void swap(Individual individual){
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());

		System.out.println(posA + " " + posB);

		City temp = individual.getCityByIndex(posA);
		individual.setCity(posA, individual.getCityByIndex(posB));
		individual.setCity(posB, temp);
	}

	/**
	* Inverts a random subsection of a given array of cities.
	* @param individual The array of cities to be mutated
	* @return City[] 
	*/
	public void inversion(Individual individual){

		// Find the subset of position to mutate
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());
		
		// Ensure that posA is less than posB
		if (posA > posB) {
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		}

		if (posA != posB) {
			// Invert the values between posA and posB (inclusive)
			int subsetSize = (int) Math.ceil((posB - posA)/2.0);
			
			for (int i = posA; i <= subsetSize; i++) {
				City cityTemp = individual.getCityByIndex(i);
				individual.setCity(i, individual.getCityByIndex(posB));
				individual.setCity(posB, cityTemp);
				posB--;
			}
		}
	}

	/**
	 * Scrambles a random subsection of a given array of cities
	 * @param individual The array of cities to be mutated
	 * @return City[] 
	 */
	public void scramble(Individual individual){
		// Find the subset of position to mutate
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());
		
		// Ensure that posA is less than posB
		if (posA > posB){
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		}
		
		if (posA != posB){
			// Scramble the positions in the array
			int subsetSize = posB - posA;
			for (int i = posA; i <= posB; i++){
				int index = rnd.nextInt(subsetSize);

				City cityTemp = individual.getCityByIndex(i);
				individual.setCity(i, individual.getCityByIndex(index + posA));
				individual.setCity(index + posA, cityTemp);
			}
		}
	}
}