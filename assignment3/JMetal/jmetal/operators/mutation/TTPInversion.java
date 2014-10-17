
package jmetal.operators.mutation;

import jmetal.core.Operator;
import jmetal.core.Solution;
import jmetal.core.Variable;

import jmetal.util.JMException;

import jmetal.problems.TTP.City;
import jmetal.encodings.variable.Individual;
import jmetal.encodings.solutionType.IndividualSolutionType;

import java.util.HashMap;
import java.util.Random;


public class TTPInversion extends Mutation {
    
    private Random rnd;		// Random number generator
    
    /**
     * Create an Inversion operator
     * @param: Hashmap<String, Object>: Parameters sent to operator
     */
    public TTPInversion(HashMap<String, Object> parameters) {
        super(parameters);
        
        rnd = new Random();
    }
    
    /**
     * Override method from execute
     * @param: Object: not sure yet...?
     * this gets passed a jmetal.core.Solution object
     */
    @Override
    public Object execute(Object object) throws JMException {
        //Typecast the object to a solution and perform the inversion
        Solution solution = (Solution) object;
        System.out.println("inversion");
        inversionOrScramble(solution);
        
        return solution;
    }
    
    
    
    
    /*****************************
     * INVERSION METHOD FROM TSP *
     *****************************/
    
    /**
     * Used by scramble and inversion
     * Functions are similar and hence their functions have been combined
     * @param Individual - The solution of cities to be mutated
     */
    private void inversionOrScramble(Solution solution) throws JMException{
        if(solution.getType().getClass() == IndividualSolutionType.class) {
            //Get an individual from the solution
            Variable[] indivArr = (Variable[]) solution.getDecisionVariables();
            int idx = rnd.nextInt(indivArr.length);
            Individual individual = (Individual) indivArr[idx];
            
            System.out.print("Before: ");
            individual.print();
            
            // Find the subset of position to mutate
            int posA = 0;
            int posB = 0;
        
            // Keep city[0] the same
            while (posA == 0 || posB == 0) {
                posA = rnd.nextInt(individual.getNumCities());
                posB = rnd.nextInt(individual.getNumCities());
            }
        
            // Ensure that posA is less than posB
            if (posA > posB){
                int posTemp = posA;
                posA = posB;
                posB = posTemp;
            }
        
            if (posA != posB) {
                //invert positions in the array
                inverseSubset(individual, posA, posB);
            }

            System.out.println("After: ");
            individual.print();

        } else {
            System.out.println("TTPInversion.inversionOrScramble: invalid type. " +
                                         ""+ solution.getDecisionVariables()[0].getVariableType());
            
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".inversionOrScramble()") ;
        }
    }
    
    /**
     * Inverses a subset specified
     * Used by inversionOrScramble and inverOver
     * @param Individual - The solution of cities to be mutated
     * @param int posA - first index of subset
     * @param int posB - last index of subset
     */
    public void inverseSubset(Individual individual, int posA, int posB) {
        int a = posA;
        int b = posB;
        
        // Handle the area to be inversed if it is circular (wraps around end of solution)
        while (a < individual.getNumCities() && b > 0) {
            swapCities(individual, a, b);
            a++;
            b--;
        }
        if (a == individual.getNumCities()){
            a = 1;
        } else {
            b = individual.getNumCities() - 1;
        }
        
        // Inverse the other parts of the solution
        while (a < b) {
            swapCities(individual, a, b);
            a++;
            b--;
        }
    }
    
    /**
     * Swaps two 'City' objects in the supplied solution in the supplied spots
     * @param Individual - an individual candidate of the population to mutate
     * @param int - index of the first city
     * @param int - index of the second city
     */
    public void swapCities(Individual individual, int posA, int posB) {
        City temp = individual.getCityByIndex(posA);
        individual.setCity(posA, individual.getCityByIndex(posB));
        individual.setCity(posB, temp);
    }
}