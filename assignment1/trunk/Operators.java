import java.util.Random;

public class Operators{
	
	/** Used to generate random numbers - use rnd.nextInt(MAX_VALUE) */
	private Random rnd;

	/**
	*
	*
	*/
	public Operators(){
		rnd = new Random();
	}

	/**
	*
	*
	*/
	public City[][] order_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];

		return children;
	}

	/**
	*
	*
	*/
	public City[][] pmx_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];
		
		return children;
	}

	/**
	*
	*
	*/
	public City[][] cycle_crossover(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];

		return children;
	}

	/**
	*
	*
	*/
	public City[][] edge_recombination(City[][] parents){
		//this assume all solutions are of the same length!
		City[][] children = new City[parents.length][parents[0].length];
    
		return children;
	}

	/**
	* TESTING ONLY
	*/
	public static void main(String[] args){
		Operators test = new Operators();
	}
}