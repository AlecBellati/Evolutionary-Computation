
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
        
        Solution best = solutionSet.get(0);
        
        for(int i = 1; i < solutionSet.size(); i++) {
            Solution challenger = solutionSet.get(i);
            
            if(debug) {
                System.out.println("########## Selection ###########");
                System.out.println("Best Solution: " + best.toString());
                System.out.println("Next Solution: " + challenger.toString());
            }
            
            best = bestOfThree(best, challenger);
            
            if(debug) {
                System.out.println("Winner: " + best.toString());
            }
        }
        
        
        return best;
    }
    
    /**
     * Get the best solution out of two solutions
     * @param: Solution: current best solution
     * @param: Solution: new challenges
     * @return: Solution: best of three for this solution
     */
    public Solution bestOfThree(Solution solution1, Solution solution2) {
        //get TSP distances
        double distance1 = solution1.getObjective(0);
        double distance2 = solution2.getObjective(0);
        
        //get unused space in knapsack
        double unused1 = solution1.getObjective(1);
        double unused2 = solution2.getObjective(1);

        //get total profit
        double profit1 = solution1.getObjective(2);
        double profit2 = solution2.getObjective(2);
        
        //win tally
        int win1 = 0;
        int win2 = 0;
        
        //who wins distance?
        if(distance1 <= distance2) {
            win1++;
        } else {
            win2++;
        }
        
        //who wins unused space?
        if(unused1 <= unused2) {
            win1++;
        } else {
            win2++;
        }
        
        //who wins profit?
        if(profit1 <= profit2) {
            win1++;
        } else {
            win2++;
        }
        
        //return the winner
        if(win1 >= win2) {
            return solution1;
        } else {
            return solution2;
        }
    }
}