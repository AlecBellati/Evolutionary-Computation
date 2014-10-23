
package jmetal.encodings.solutionType;


import jmetal.core.SolutionType;
import jmetal.core.Problem;
import jmetal.core.Variable;
import jmetal.problems.TTP.TTP;

import jmetal.encodings.variable.Individual;


public class IndividualSolutionType extends SolutionType {

    TTP problem;
    
    /**
     *
     *
     */
    public IndividualSolutionType(Problem problem) {
        super(problem);
        
        TTP ttpProblem = (TTP) problem;
        this.problem = ttpProblem;
    }
    
    
    /**
     * Generate a new set of individuals with the cities from problem.
     */
    @Override
    public Variable[] createVariables() throws ClassNotFoundException {
        Variable[] variables = new Variable[problem_.getNumberOfVariables()];
        
        for(int i = 0; i < variables.length; i++) {
            variables[i] = new Individual(this.problem.cities, this.problem.capacityOfKnapsack, this.problem.rentingRatio, this.problem.itemsArray);
        }
        
        return variables;
    }
}