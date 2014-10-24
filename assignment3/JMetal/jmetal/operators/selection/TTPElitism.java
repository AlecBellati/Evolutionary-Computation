
package jmetal.operators.selection;

import jmetal.core.Solution;
import jmetal.core.SolutionSet;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Random;


public class TTPElitism extends Selection {
    
    private Comparator comparator;
    
    private Random rnd;
    
    /**
     * Create a new TTPElitism selector
     *
     */
    public TTPElitism(HashMap<String, Object> parameters) {
        super(parameters);
            
        rnd = new Random();
    }

    /**
     * Run the selector
     * @param: Object: jmetal.core.SolutionSet
     */
    public Object execute(Object object) {
        boolean debug = !true;
        
        //get the first two solutions
        SolutionSet solutionSet = (SolutionSet)object;
        Solution solution1 = solutionSet.get(0);
        Solution solution2 = solutionSet.get(1);

        if(debug) {
            System.out.println("########## Selection ###########");
            System.out.println("Solution 1: " + solution1.toString());
            System.out.println("Solution 2: " + solution2.toString());
        }
        
        int soln = rnd.nextInt(2);
        
        if(soln == 0) {
            return solution1;
        } else {
            return solution2;
        }
    }
}