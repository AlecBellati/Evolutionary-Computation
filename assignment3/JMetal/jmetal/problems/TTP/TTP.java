
package jmetal.problems.TTP;

import jmetal.core.Solution;
import jmetal.core.TTPSolution;
import jmetal.core.Problem;
import jmetal.encodings.solutionType.IntSolutionType;
import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

/**
 * Class representing problem ZDT1
 */
public class TTP extends Problem {
    
    private int numberOfNodes;
    private long capacityOfKnapsack;
    private double minSpeed;
    private double maxSpeed;
    private double rentingRatio;
    private int[][] items;
    private double[][] nodes;
    
    /**
     * Constructor.
     * Creates a default instance of problem ZDT1 (30 decision variables)
     * @param: String: Name of TTP File
     * @param: int: Number of Cities
     * @param: long: Capacity of the knapsack
     * @param: double: minimum speed of the thief
     * @param: double: maximum speed of the thief
     * @param: double: renting ratio of the knpasack
     * @param: int[][]: 2D-array of items and item information
     */
    public TTP(String problemName, int numberOfNodes, long capacityOfKnapsack, double minSpeed, double maxSpeed, double rentingRatio, int[][] items, double[][] nodes) throws ClassNotFoundException {
        
        this(problemName, numberOfNodes, capacityOfKnapsack, minSpeed, maxSpeed, rentingRatio, items, nodes, 30); // 30 variables by default
    }
    
    /**
     * Creates a new instance of problem ZDT1.
     * @param: String: Name of TTP File
     * @param: int: Number of Cities
     * @param: long: Capacity of the knapsack
     * @param: double: minimum speed of the thief
     * @param: double: maximum speed of the thief
     * @param: double: renting ratio of the knpasack
     * @param: int[][]: 2D-array of items and item information
     * @param: Integer: Number of variables to keep track of (default is 30 from previous constructor)
     */
    public TTP(String problemName, int numberOfNodes, long capacityOfKnapsack, double minSpeed, double maxSpeed, double rentingRatio, int[][] items, double[][] nodes, Integer numberOfVariables) {
        
        //get required variables such that
        this.numberOfNodes = numberOfNodes;
        this.capacityOfKnapsack = capacityOfKnapsack;
        this.minSpeed = minSpeed;
        this.maxSpeed = maxSpeed;
        this.rentingRatio = rentingRatio;
        this.nodes = nodes;
        this.items = items;
        
        numberOfVariables_  = numberOfVariables;
        numberOfObjectives_ =  1;
        numberOfConstraints_=  0;
        problemName_        = "TTP_"+problemName;
        
        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];
        
        // Establishes upper and lower limits for the variables
        for (int var = 0; var < numberOfVariables_; var++)
        {
            lowerLimit_[var] = 0.0;
            upperLimit_[var] = 1.0;
        }
        
        
        solutionType_ = new IntSolutionType(this);
    }
    
    /*####################################*
     *# EVALUATE COPIED FROM TTPINSTANCE #*
     *####################################*/
    
    /**
     * Translated code of the original "TTP1Objective.m".
     *
     * Important note: in contrast to the MATLAB code, city numbers start from 0
     * and item numbers start from 0.
     *
     * @param distances         a n by n matrix that shows the distances between the cities (there are n cities)
     * @param weights           the weight of each item (1 by m)
     * @param values            the profit of each item (1 by m)
     * @param av                a m by n matrix showing if the ith item is available in the jth city.
     * @param tour              a 1 by n+1 array showing the tour (a complete tour)
     * @param z                 a 1 by m array, showing which item from which city (if z(i)==j, it means item i from city j)  -->>>> the other way around:
     * @param weightofKnapsack  maximum weight of the knapsack
     * @param vmax              maximum velocity
     * @param vmin              minimum velocity
     * @param rentRate          the rent rate of the knapsack
     * @return TTP object:
     *          "fp" final profit gained form the picked items,
     *          "ft" the time takes to finish the tour (including changes of the speed),
     *          "ob" objective value,
     *          "wend" weight of the knapsack at the end of the tour
     */
    public void evaluate(Solution _solution) {
        TTPSolution solution = (TTPSolution) _solution;
        
        boolean debugPrint = !true;
        int[] tour = solution.tspTour;
        int[] z = solution.packingPlan;
        long weightofKnapsack = this.capacityOfKnapsack;
        double rentRate = this.rentingRatio;
        double vmin = this.minSpeed;
        double vmax = this.maxSpeed;
        solution.ftraw = 0;
        
        // correctness check: does the tour start and end in the same city
        if(tour[0]!=tour[tour.length-1]) {
            System.out.println("ERROR: The last city must be the same as the first city");
            solution.reset();
            return;
        }
        
        double wc=0;
        solution.ft=0;
        solution.fp=0;
        
        /* the following is used for a different interpretation of "packingPlan"
         *
         */
        int itemsPerCity = solution.packingPlan.length / (solution.tspTour.length-2);
        if (debugPrint) System.out.println("itemsPerCity="+itemsPerCity+" solution.tspTour.length="+solution.tspTour.length);
        
        for (int i=0; i<tour.length-1; i++) {
            
            // important: nothing to be picked at the first city!
            if (debugPrint) System.out.print("\ni="+i+" checking packing: ");
            
            int currentCityTEMP = tour[i]; // what's the current city? --> but the items start at city 2 in the TTP file, so I have to take another 1 off!
            
            int currentCity = currentCityTEMP-1;
            
            if (i>0) if (debugPrint) System.out.print("city "+currentCityTEMP+" cityIndexForItem[][] "+currentCity+" (this.numberOfNodes="+this.numberOfNodes+"): ");
            
            if (i>0){
                for (int itemNumber=0; itemNumber<itemsPerCity; itemNumber++) {
                    int indexOfPackingPlan = (i-1)*itemsPerCity+itemNumber;
                    if (debugPrint) System.out.print("indexOfPackingPlan="+indexOfPackingPlan+" ");
                    
                    // what is the next item's index in items-array?
                    int itemIndex = currentCity+itemNumber*(this.numberOfNodes-1);//* (this.numberOfNodes-1);
                    if (debugPrint) System.out.print("itemIndex="+itemIndex+" ");
                    
                    if (z[indexOfPackingPlan]==1) {
                        int currentWC = this.items[itemIndex][2];
                        wc=wc+currentWC;
                        
                        int currentFP=this.items[itemIndex][1];
                        solution.fp=solution.fp+currentFP;
                        
                        if (debugPrint) System.out.print("[fp="+currentFP+",wc="+currentWC+"] ");
                    }
                }
            }
            if (debugPrint) System.out.println();
            
            int h= (i+1)%(tour.length-1); //h: next tour city index
            if (debugPrint) System.out.println("  i="+i+" h="+h + " tour[i]="+tour[i]+" tour[h]="+tour[h]);
            
            long distance = (long)Math.ceil(distances(tour[i],tour[h]));
            
            // compute the raw distance
            solution.ftraw += distance;
            
            // compute the adjusted (effective) distance
            solution.ft=solution.ft+(distance / (1-wc*(vmax-vmin)/weightofKnapsack));
            //(distances[tour[i]][tour[h]] / (1-wc*(vmax-vmin)/weightofKnapsack));
            
            if (debugPrint) System.out.println("i="+i+" tour[i]="+tour[i]+" tour[h]="+tour[h]+" distance="+distance+" fp="+solution.fp + " ft=" + solution.ft);
        }
        
        solution.wendUsed = wc;
        solution.wend=weightofKnapsack-wc;
        solution.ob=solution.fp-solution.ft*rentRate;
    }
    
    /**
     * Calculates the distance between two points
     * @param - int i, int j: cities i and j to calculate distance
     * @return - double: distance between the two cities
     */
    public double distances(int i, int j) {
        double result = 0;
        result = Math.sqrt(
                           (this.nodes[i][1]-this.nodes[j][1]) *
                           (this.nodes[i][1]-this.nodes[j][1]) +
                           (this.nodes[i][2]-this.nodes[j][2]) *
                           (this.nodes[i][2]-this.nodes[j][2]));
        
        if (!true) System.out.println(" distance="+this.nodes[i][1]+ " "
                                      +this.nodes[j][1]+" "+this.nodes[i][2]+" "+this.nodes[j][2]+"->"+result);
        
        return result;
    }
}

    
    
    /**
     * Returns the value of the ZDT1 function G.
     * @param  x Solution
     * @throws JMException
     *
     private double evalG(XReal x) throws JMException {
     double g = 0.0;
     for (int i = 1; i < x.getNumberOfDecisionVariables();i++)
     g += x.getValue(i);
     double constant = (9.0 / (numberOfVariables_-1));
     g = constant * g;
     g = g + 1.0;
     return g;
     } // evalG
     
     /**
     * Returns the value of the ZDT1 function H.
     * @param f First argument of the function H.
     * @param g Second argument of the function H.
     *
     public double evalH(double f, double g) {
     double h = 0.0;
     h = 1.0 - java.lang.Math.sqrt(f/g);
     return h;
     } // evalH
     } // ZDT1
     */