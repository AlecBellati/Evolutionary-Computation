package jmetal.problems.TTP;


import jmetal.core.Solution;
import jmetal.core.Problem;
import jmetal.core.Variable;

import jmetal.encodings.solutionType.IndividualSolutionType;
import jmetal.encodings.variable.Individual;

import jmetal.util.JMException;
import jmetal.util.wrapper.XReal;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.File;
import java.io.FileReader;

/**
 * Class representing problem ZDT1
 */
public class TTP extends Problem {
    
    public File file;
    
    public String problemName;
    public String knapsackDataType;
    public int numberOfNodes;
    public int numberOfItems;
    public long capacityOfKnapsack;
    public double minSpeed;
    public double maxSpeed;
    public double rentingRatio;
    public String edgeWeightType;
    public double[][] nodes;
    public int[][] items;
    
    public Item[] itemsArray;
    public City[] cities;
    
    /**
     * Constructor.
     * Creates a default instance of problem ZDT1 (30 decision variables)
     * @param: File: File object containing link to File to be parsed.
     */
    public TTP(File file) throws ClassNotFoundException {
        this(file, 30); // 2 variables by default
    }
    
    /**
     * Creates a new instance of problem TTP.
     * @param: File: File object containing link to File to be parsed.
     * @param: Integer: Number of variables to keep track of (default is 30 from previous constructor)
     */
    public TTP(File file, Integer numberOfVariables) {
        //parse the file into the necessary TTP data structures (in this class header) for evaluate
        parseTTP(file);

        //once parsed, setup the cities and itemsArray
        createTTPGraph();

        
        //setup jmetal variables
        numberOfVariables_  = numberOfVariables;
        numberOfObjectives_ =  3;
        numberOfConstraints_=  0;
        problemName_        = "TTP_"+file.getName();
        
        upperLimit_ = new double[numberOfVariables_];
        lowerLimit_ = new double[numberOfVariables_];
        
        // Establishes upper and lower limits for the variables
        for (int var = 0; var < numberOfVariables_; var++)
        {
            lowerLimit_[var] = 0.0;
            upperLimit_[var] = 1.0;
        }
        
        
        solutionType_ = new IndividualSolutionType(this);
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
    public void evaluate(Solution solution) {
        
        //Get an individual from the solution
        Variable[] indivArr = (Variable[]) solution.getDecisionVariables();
        Individual individual = (Individual)indivArr[0];
        
        if(individual.getCitiesByID() == null) {
            //objective 0 is the distance travelled taking into account weight
            solution.setObjective(0,Double.MAX_VALUE);
            
            //objective 1 is the amount of unused space in the knapsack at the end of the tour
            solution.setObjective(1,Double.MAX_VALUE);
            
            //objective 2 is the profit (because JMetal minimises, the profit is negativised to turn it into a minimisation problem
            solution.setObjective(2,Double.MAX_VALUE);

        }
        
        int[] tspTour = individual.getCitiesByID();
        int[] packingPlan = individual.getKnapsack().getPackingPlan(individual, numberOfItems);
        
        boolean debugPrint = !true;
        int[] tour = tspTour;
        int[] z = packingPlan;
        long weightofKnapsack = this.capacityOfKnapsack;
        double rentRate = this.rentingRatio;
        double vmin = this.minSpeed;
        double vmax = this.maxSpeed;
        long ftraw = 0;
        
        // correctness check: does the tour start and end in the same city
        if(tour[0]!=tour[tour.length-1]) {
            System.out.println("ERROR: The last city must be the same as the first city");
            return;
        }
        
        double wc=0;
        double ft=0;
        double fp=0;
        
        /* the following is used for a different interpretation of "packingPlan"
         *
         */
        int itemsPerCity = packingPlan.length / (tspTour.length-2);
        if (debugPrint) System.out.println("itemsPerCity="+itemsPerCity+" tspTour.length="+tspTour.length);
        
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
                        fp=fp+currentFP;
                        
                        if (debugPrint) System.out.print("[fp="+currentFP+",wc="+currentWC+"] ");
                    }
                }
            }
            if (debugPrint) System.out.println();
            
            int h= (i+1)%(tour.length-1); //h: next tour city index
            if (debugPrint) System.out.println("  i="+i+" h="+h + " tour[i]="+tour[i]+" tour[h]="+tour[h]);
            
            long distance = (long)Math.ceil(distances(tour[i],tour[h]));
            
            // compute the raw distance
            ftraw += distance;
            
            // compute the adjusted (effective) distance
            ft=ft+(distance / (1-wc*(vmax-vmin)/weightofKnapsack));
            //(distances[tour[i]][tour[h]] / (1-wc*(vmax-vmin)/weightofKnapsack));
            
            if (debugPrint) System.out.println("i="+i+" tour[i]="+tour[i]+" tour[h]="+tour[h]+" distance="+distance+" fp="+fp + " ft=" + ft);
        }
        
        double wendUsed = wc;
        double wend=weightofKnapsack-wc;
        double ob=fp-ft*rentRate;
        
        
        //objective 0 is the distance travelled taking into account weight
        solution.setObjective(0,ftraw);
        
        //objective 1 is the amount of unused space in the knapsack at the end of the tour
        solution.setObjective(1,wend);
        
        //objective 2 is the profit (because JMetal minimises, the profit is negativised to turn it into a minimisation problem
        solution.setObjective(2,ob*-1);
        
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
    
    
    
    /*#################################*
     * PARSER COPIED FROM TTPINSTANCE #*
     *#################################*/
    
    /**
     * Parse TTP File (Parser written by Markus)
     * @param: File: file object to parse
     */
    public void parseTTP(File file) {
        boolean debugPrint = !true;
        if (debugPrint) System.out.println(file.getAbsolutePath());
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(file));
            this.file = file;
            String line;
            while ((line = br.readLine()) != null) {
                // process the line
                
                if (line.startsWith("PROBLEM NAME")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.problemName = line;
                }
                if (line.startsWith("KNAPSACK DATA TYPE")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.knapsackDataType = line;
                }
                if (line.startsWith("DIMENSION")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.numberOfNodes=Integer.parseInt(line);
                    
                    //Create TTPGraph and cities array
                    cities = new City[numberOfNodes];
                }
                if (line.startsWith("NUMBER OF ITEMS")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.numberOfItems=Integer.parseInt(line);
                }
                if (line.startsWith("CAPACITY OF KNAPSACK")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.capacityOfKnapsack=Long.parseLong(line);
                }
                if (line.startsWith("MIN SPEED")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.minSpeed=Double.parseDouble(line);
                }
                if (line.startsWith("MAX SPEED")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.maxSpeed=Double.parseDouble(line);
                }
                if (line.startsWith("RENTING RATIO")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.rentingRatio=Double.parseDouble(line);
                }
                if (line.startsWith("EDGE_WEIGHT_TYPE")) {
                    line = line.substring(line.indexOf(":")+1);
                    line = line.replaceAll("\\s+","");
                    this.edgeWeightType = line;
                }
                if (line.startsWith("NODE_COORD_SECTION")) {
                    this.nodes = new double[this.numberOfNodes][3];
                    for (int i=0; i<this.numberOfNodes; i++) {
                        line = br.readLine();
                        String[] splittedLine = line.split("\\s+");
                        for (int j=0; j<splittedLine.length; j++) {
                            double temp = Double.parseDouble(splittedLine[j]);
                            //                            int temp = Integer.parseInt(splittedLine[j]);
                            // adjust city number by 1
                            if (j==0) temp =  temp-1;
                            this.nodes[i][j] = temp;
                        }
                    }
                }
                if (line.startsWith("ITEMS SECTION")) {
                    this.items = new int[this.numberOfItems][4];
                    for (int i=0; i<this.numberOfItems; i++) {
                        line = br.readLine();
                        String[] splittedLine = line.split("\\s+");
                        for (int j=0; j<splittedLine.length; j++) {
                            int temp = Integer.parseInt(splittedLine[j]);
                            // adjust city number by 1
                            if (j==0) temp =  temp-1;  // item numbers start here with 0 --> in TTP files with 1
                            if (j==3) temp =  temp-1;  // city numbers start here with 0 --> in TTP files with 1
                            this.items[i][j] = temp;
                        }
                    }
                }
            }
            br.close();
        } catch (IOException ex) {
            System.out.println("Something went wrong when parsing the file");
            ex.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * Create the edge graph (same as the edge graph we had in TSP
     */
    public void createTTPGraph() {
        for(int i = 0; i < numberOfNodes; i++) {
            //Correct (tick)
            cities[i] = new City(i, numberOfNodes, (numberOfItems/numberOfNodes)+1, this.nodes[i][1], this.nodes[i][2]);
        }
        
        //now add items to the cities
        itemsArray = new Item[numberOfItems];
        setupItems();
    }

    /**
     * Add the corresponding items to their corresponding cities
     * Also creates an independant items array
     */
    public void setupItems() {
        for(int i = 0; i < numberOfItems; i++) {
            itemsArray[i] = new Item(items[i][0], items[i][1], items[i][2], cities[items[i][3]].getNodeNum());
            cities[items[i][3]].addItem(new Item(items[i][0], items[i][1], items[i][2]));
        }
    }


}