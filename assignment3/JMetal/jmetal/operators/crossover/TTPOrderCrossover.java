
package jmetal.operators.crossover;

import jmetal.core.Variable;

import jmetal.util.JMException;

import jmetal.problems.TTP.City;
import jmetal.problems.TTP.TTPSolution;

import jmetal.encodings.variable.Individual;
import jmetal.encodings.solutionType.IndividualSolutionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;



public class TTPOrderCrossover extends Crossover {

    private Random rnd;		// Random number generator

    /**
     * Constructor
     * @param: Hashmap<String, Object>: Parameters sent to operator
     */
    public TTPOrderCrossover(HashMap<String, Object> parameters) {
        super(parameters);
        
        rnd = new Random();
    }
    
    /**
     * Override method from execute
     * @param: Object: this gets passed a jmetal.core.Solution object
     */
    public Object execute(Object object) throws JMException {

        //Typecast the object to a solution and perform the inversion
        TTPSolution[] solution = (TTPSolution[]) object;
        orderCrossover(solution);
        
        return solution;
    }
    
    
    
    /*****************************
     * CROSSOVER METHOD FROM TSP *
     *****************************/
    
    /**
     * Performs the orderCrossover operation.
     * Determines the crossover between two parents and then interleaves
     * The parents to form the children
     * @param TTPSolution: Solution containing individuals
     */
    public void orderCrossover(TTPSolution[] solution) throws JMException {
        if(solution[0].getType().getClass() == IndividualSolutionType.class) {

            //Get two individual from the solution
            Variable[] indivArr = (Variable[]) solution[0].getDecisionVariables();
            Individual parentA = (Individual) indivArr[0];
            Variable[] indivArrB = (Variable[]) solution[1].getDecisionVariables();
            Individual parentB = (Individual) indivArrB[1];
            
            
            // Choose random part of parents to copy
            int length = parentA.getNumCities();
            int posA = 0;
            int posB = 0;
            
            // Ensure first city isn't selected
            while (posA == 0 || posB == 0) {
                posA = rnd.nextInt(length);
                posB = rnd.nextInt(length);
            }
            
            // Ensure that posA is less than  or equal to posB
            if (posA > posB){
                int posTemp = posA;
                posA = posB;
                posB = posTemp;
            }
            int subsetLength = posB - posA + 1;
            
            
            // Create children
            // Store copied values so they don't get copied again
            Individual childA = new Individual(length);
            Individual childB = new Individual(length);
            ArrayList<Integer> valuesCopiedA = new ArrayList<Integer>();
            ArrayList<Integer> valuesCopiedB = new ArrayList<Integer>();
            
            // Copy first cities over so they don't change
            childA.setCity(0, parentA.getCityByIndex(0));
            childB.setCity(0, parentB.getCityByIndex(0));
            valuesCopiedA.add(parentA.getCityByIndex(0).getNodeNum());
            valuesCopiedB.add(parentB.getCityByIndex(0).getNodeNum());
            
            // Copy subset from parentA to childA and parentB to childB
            for (int i = posA; i <= posB; i++) {
                childA.setCity(i, parentA.getCityByIndex(i));
                childB.setCity(i, parentB.getCityByIndex(i));
                valuesCopiedA.add(parentA.getCityByIndex(i).getNodeNum());
                valuesCopiedB.add(parentB.getCityByIndex(i).getNodeNum());
            }
            
            // Fill in remaining spots in Child A
            int spotsToFill = length-subsetLength-1;
            int spotsFilled = 0;
            
            int childIndex;
            int parentIndex;
            
            // Ensure counters stay in range and ignore city[0]
            if (posB + 1 == length) {
                childIndex = 1;
                parentIndex = 1;
            } else {
                childIndex = posB + 1;
                parentIndex = posB + 1;
            }
            
            while (spotsFilled < spotsToFill) {
                // If current value wasn't copied before, include it in the child
                Integer value = parentB.getCityByIndex(parentIndex).getNodeNum();
                
                if (!valuesCopiedA.contains(value)) {
                    childA.setCity(childIndex, parentB.getCityByIndex(parentIndex));
                    
                    // Increment counters
                    spotsFilled++;
                    if (childIndex == length - 1) {
                        childIndex = 1;
                    } else {
                        childIndex++;
                    }
                }
                
                if (parentIndex == length - 1) {
                    parentIndex = 1;
                } else {
                    parentIndex++;
                }
            }
            
            // Fill in remaining spots in Child B
            spotsFilled = 0;
            
            // Ensure counters stay in range and ignore city[0]
            if (posB + 1 == length) {
                childIndex = 1;
                parentIndex = 1;
            } else {
                childIndex = posB + 1;
                parentIndex = posB + 1;
            }
            
            while (spotsFilled < spotsToFill) {
                // If current value wasn't copied before, include it in the child
                Integer value = parentA.getCityByIndex(parentIndex).getNodeNum();
                
                if (!valuesCopiedB.contains(value)) {
                    childB.setCity(childIndex, parentA.getCityByIndex(parentIndex));
                    // Increment counters
                    spotsFilled++;
                    if (childIndex == length - 1) {
                        childIndex = 1;
                    } else {
                        childIndex++;
                    }
                }
                
                if (parentIndex == length - 1) {
                    parentIndex = 1;
                } else {
                    parentIndex++;
                }
            }
            
            indivArr[0] = childA;
            indivArr[1] = childB;
        } else {
            System.out.println("TTPOrderCrossover.orderCrossover: invalid type. " +
                               ""+ solution[0].getDecisionVariables()[0].getVariableType());
            
            Class cls = java.lang.String.class;
            String name = cls.getName();
            throw new JMException("Exception in " + name + ".orderCrossover()") ;

        }
    }
}