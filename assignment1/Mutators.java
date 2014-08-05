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
	*
	*
	*/
	public City[] inversion(City[] individual){

		return individual;
	}

	/**
	*
	*
	*/
	public City[] scramble(City[] individual){

		return individual;
	}

	/**
	* TESTING ONLY
	*/
	public static void main(String[] args){
		Mutators test = new Mutators();
	}
}