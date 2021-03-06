/*
 * Evolutionary Comptuation
 * COMP SCI 4095
 * Assignment Two
 * William Reid (a1215621)
 * Alec Bellati (a1608934)
 * Sami Peachey (a1192722)
 * Matthew Hart (a1193380)
 * Adapted from Markus Wagner
 */

package TTP;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

//add Thieves
import TTP.Thief.Alec;
import TTP.Thief.Matt;
import TTP.Thief.Sami;
import TTP.Thief.Will;

//Add TTPSolution
import TTP.Thief.TTPSolution;

//import Required TSP Classes
import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Item;

public class TTPInstance {
    
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
    private Item[] itemsArray;
    
    //TTPGraph of edges
    private City[] cities;              //array of created cities
    
    //Thieves
    private Alec alec;
    private Matt matt;
    private Sami sami;
    private Will will;
    
    //TTPSolution
    private TTPSolution solution;
    
    
    /** 
    * Constructor
    * reads the instance from the file
    * @param - file: file containing the TTP graph information
    */
    public TTPInstance(File file) {
        //parse the file
        parseTTP(file);
        
        //create the TTPGraph
        createTTPGraph();
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
    
    /**
     * Print the TTPGraph
     */
    public void printTTPGraph() {
        for(int i = 0; i < numberOfNodes; i++) {
            for(int j = 0; j < numberOfNodes; j++) {
                System.out.print(String.format("%3.2f ", distances(i, j)));
            }
            System.out.println();
            System.out.println();
        }
    }

    /**
    * Used to return parameters for the algorithm "Will"
    * @param - instance: instance parameters to return
    * @return - String[]: parameters relating to a particular instance
    */
    private String[] setupRun(int instance){
        ArrayList<String[]> instances = new ArrayList<String[]>();

        //knapsack seed, TSP choice, generations, iterations, {good items, random items}
        instances.add(0, new String[]{"2", "1", "10", "-1", "-1", "-1"}); //default
        instances.add(1, new String[]{"2", "1", "10", "-1", "-1", "-1"}); //280, 279
        instances.add(2, new String[]{"2", "1", "10", "-1", "-1", "-1"}); //280, 1395
        instances.add(3, new String[]{"2", "1", "6", "-1", "-1", "-1"}); //280, 2790
        instances.add(4, new String[]{"2", "1", "1", "-1", "-1", "-1"}); //4461, 4461
        instances.add(5, new String[]{"2", "1", "10", "8", "-1", "-1"}); //4461, 22300
        instances.add(6, new String[]{"2", "1", "1", "25", "-1", "-1"}); //4461, 44610
        instances.add(7, new String[]{"4", "1", "6", "5", "7500", "2500"}); //33810, 33809
        instances.add(8, new String[]{"4", "1", "1", "12", "2000", "500"}); //33810, 169045
        instances.add(9, new String[]{"4", "1", "1", "50", "2000", "500"}); //33810, 338090

        if(instance < 0 || instance > 9){
            return instances.get(0);
        }
        return instances.get(instance);
    }

    /**
    * Formats the parameters for the particular instance for the algorithm "Will"
    * @param - instanceChoice: instance to be run, used to get the appropriate parameters
    */
    private void runWill(int instanceChoice){
        String[] instance = setupRun(instanceChoice);
        int goodItems = Integer.parseInt(instance[4]);
        int randomItems = Integer.parseInt(instance[5]);
        int[] itemChoice = null;
        if(goodItems != -1){
            if(randomItems != -1){
                itemChoice = new int[]{goodItems, randomItems};
            }else{
                itemChoice = new int[]{goodItems};
            }
        }

        will.getSolution(this, Integer.parseInt(instance[0]), Integer.parseInt(instance[1]), Integer.parseInt(instance[2]), Integer.parseInt(instance[3]), itemChoice);
    }
    
    /**
     * Entry point for program to start running
     * @param - choice: used for the "Will" algorithm to select the instance parameters
     */
    public void run(int choice) {
        /********************
         * CREATE YOUR THIEF*
         ********************/
        //alec = new Alec(cities, itemsArray, capacityOfKnapsack); alec.getSolution(this);
        //matt = new Matt(cities, itemsArray, minSpeed, maxSpeed, capacityOfKnapsack, rentingRatio); matt.getSolution();
        sami = new Sami(cities, itemsArray, minSpeed, maxSpeed, capacityOfKnapsack, rentingRatio); sami.getSolution(this);
        //will = new Will(cities, itemsArray, minSpeed, maxSpeed, capacityOfKnapsack, rentingRatio); runWill(choice);
            
        getBestSolution(false);
        System.out.println();
    }
    
    /**
     * Timer has expired, get the best solution from the thief and exit the program
     * @param - intermediate: if it is an intermediate result (2 or 5 minutes) do not write to file
     */
    public void getBestSolution(boolean intermediate) {
        if(!intermediate){
            System.out.println("TTPInstance: Getting Final TTPSolution");
        }
        String name = "";
        //solution = alec.getBestSolution(); name = "Alec";
        //solution = matt.getBestSolution(); name = "Matt";
        solution = sami.getBestSolution(); name = "Sami";
        //solution = will.getBestSolution(); name = "Will";
        
        
        //create file name (calling convention is: <ttpfile>.<algorithmname>.<systemtime>)
        String filename = problemName + "_n" + itemsArray.length + "." + name + "."+ System.currentTimeMillis() + ".ttp";
        
        //Check if solution exists
        if(solution == null) {
            System.out.println("TTPInstance: No Solution Available!");
        } else {
            if(!intermediate){
                System.out.println("TTPInstance: Solution Exists! Evalutating and Writing Solution to File");
                evaluate(solution);
                solution.writeResult(filename);
            }
            solution.println();
            System.out.println("Total profit = " + solution.getObjective());
        }
        
        if(!intermediate){
            System.out.println("TTPInstance: exiting program");
        }
    }
    
    
    
    
    /*#################################*
     *#SUPPLIED FILES FROM ASSIGNMENT #*
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
    public void evaluate(TTPSolution solution) {
        
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
     * Overloaded function that prints the short summary of the TTP Instance
     */
    public void printInstance() {
        printInstance(true);
    }
    
    /* prints the details
     * shortSummary:
     *   true   prints a short version in one line
     *   false  prints the node and item data as well
     */
    public void printInstance(boolean shortSummary) {
        if (shortSummary) {
            System.out.print("TTP Instance: ");
        } else {
            System.out.println("---- TTP Instance START ----");
        }
        
        System.out.println(this.problemName+
                           " "+this.knapsackDataType+
                           " "+this.numberOfNodes+
                           " "+this.numberOfItems+
                           " "+this.capacityOfKnapsack+
                           " "+this.minSpeed+
                           " "+this.maxSpeed+
                           " "+this.rentingRatio);
        
        if (shortSummary) {
        } else {
            for (double[] i:this.nodes) {
                System.out.println(Arrays.toString(i));
            }
            for (int[] i:this.items) {
                System.out.println(Arrays.toString(i));
            }
            System.out.println("---- TTP Instance END ----");
        }
    }
}
