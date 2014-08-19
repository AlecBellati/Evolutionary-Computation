import java.util.Random;

public class Mutators{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	* CONSTRUCTOR
	* Initialise the number generator for use as a global object
	*/
	public Mutators(){
		rnd = new Random();
	}

	/**
	* Take one element and insert it after another, shifting all elements along as required
	* @param Individual - an individual candidate of the population to mutate
	*/
	public void insert(Individual individual){
        //get city move pos and place to put city
        int move = rnd.nextInt(individual.getNumCities());
        int insert = rnd.nextInt(individual.getNumCities());

        if (insert > move){
        	int temp = move;
        	move = insert;
        	insert = temp;
        }

        //temp City variable
        City temp;
        City moveCity = individual.getCityByIndex(move);
        
        //move all other cities down from move
        while (move > insert+1) {
            //shift elements down
            temp = individual.getCityByIndex(move-1);
            individual.setCity(move-1, moveCity);
            individual.setCity(move, temp);
            move --;
        }
	}	

	/**
	* Swaps two random 'City' objects in the supplied solution
	* @param Individual - an individual candidate of the population to mutate
	*/
	public void swap(Individual individual){
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());

		//System.out.println(posA + " " + posB);

		City temp = individual.getCityByIndex(posA);
		individual.setCity(posA, individual.getCityByIndex(posB));
		individual.setCity(posB, temp);
	}

	/**
	* Inverts a random subsection of a given individual solution
	* @param Individual - The solution of cities to be mutated
	*/
	public void inversion(Individual individual){
		inversionOrScramble(individual, true);
	}

	/**
	* Scrambles a random subsection of a given individual solution
	* @param Individual - The solution of cities to be mutated
	*/
	public void scramble(Individual individual){
		inversionOrScramble(individual, false);
	}

	/**
	* Used by scramble and inversion
	* Functions are simmilar and hence their functions have been combined
	* @param Individual - The solution of cities to be mutated
	* @param boolean inversion - if true, run the inversion, else run scramble
	*/
	private void inversionOrScramble(Individual individual, boolean inversion){
		//find the subset of position to mutate
		int posA = rnd.nextInt(individual.getNumCities());
		int posB = rnd.nextInt(individual.getNumCities());
		
		//ensure that posA is less than posB
		if (posA > posB){
			int posTemp = posA;
			posA = posB;
			posB = posTemp;
		}
		
		if (posA != posB){
			if(inversion){
				//invert positions in the array
				int subsetSize = (int) Math.ceil((posB - posA)/2.0);
				for (int i = posA; i <= subsetSize; i++) {
					City cityTemp = individual.getCityByIndex(i);
					individual.setCity(i, individual.getCityByIndex(posB));
					individual.setCity(posB, cityTemp);
					posB--;
				}
			}else{
				//scramble the positions in the array
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
}