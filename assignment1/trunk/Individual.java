import java.util.ArrayList;

public class Individual{

	//TSP graph generated from XML parser
	private double[][] TSPGraph;
	//Allows for a variable number of solutions, may need to change to double[][]
	private ArrayList<double[]> solutions;

	/*
	* Takes given TSP Graph and assigns to local variable
	* Initialises solution array list
	*/
	public Individual(double[][] TSPGraph){
		this.TSPGraph = TSPGraph;
		solutions = new ArrayList<double[]>();
	}

	/*
	* Gets the basic solution set
	* Allows for a variable number solutions and returns in a double array
	* Should not generate any errors
	*/
	public double[][] get_solution_set(){
		for(int i = 0; i < TSPGraph.length; i++){
			//find basic solution in linear time and add to solution set
			//solutions will be graded from best to worst
		}

		return (double[][])solutions.toArray();
	}
}