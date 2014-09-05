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

//import Required TSP Classes
import TTP.Thief.Travel.City;
import TTP.Thief.Travel.Item;

/**
 *
 * @author wagner
 */
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
    
    //TTPGraph of edges
    public double[][] TTPGraph;         //2D array of node edges
    private City[] cities;              //array of created cities
    
    //Thieves
    private Alec alec;
    private Matt matt;
    private Sami sami;
    private Will will;
    
    
    /* Constructor
     * reads the instance from the file
     */
    public TTPInstance(File file) {
        //parse the file
        parseTTP(file);
        
        //create the TTPGraph
        createTTPGraph();
        
        /********************
         * CREATE YOUR THIEF*
         ********************/
        //alec = new Alec();
        matt = new Matt(cities, TTPGraph, minSpeed, maxSpeed, capacityOfKnapsack, rentingRatio);
        //sami = new Sami();
        //will = new Will();
        
    }
    
    /**
     * Create the edge graph (same as the edge graph we had in TSP
     */
    public void createTTPGraph() {
        for(int i = 0; i < numberOfNodes; i++) {
            cities[i] = new City(i, numberOfNodes, numberOfItems);
            
            for(int j = 0; j < numberOfNodes; j++) {
                if(i == j) {
                    TTPGraph[i][j] = 0;
                } else {
                    TTPGraph[i][j] = distances(i, j);
                }
                
                //add edge to City
                cities[i].addEdge(j, TTPGraph[i][j]);
            }
        }
        
        //now add items to the cities
        setupItems();
    }
    
    // used to simulate the distance matrix
    public double distances(int i, int j) {
        double result = 0;
        result = Math.sqrt(
                           (this.nodes[i][1]-this.nodes[j][1]) *
                           (this.nodes[i][1]-this.nodes[j][1]) +
                           (this.nodes[i][2]-this.nodes[j][2]) *
                           (this.nodes[i][2]-this.nodes[j][2])
                           );
        
        if (!true) System.out.println(" distance="+this.nodes[i][1]+ " "
                                      +this.nodes[j][1]+" "+this.nodes[i][2]+" "+this.nodes[j][2]+"->"+result);
        
        return result;
    }
    
    /**
     * Add the corresponding items to their corresponding cities
     */
    public void setupItems() {
        for(int i = 0; i < numberOfItems; i++) {
            cities[items[i][3]].addItem(new Item(items[i][1], items[i][2]));
        }
    }
    
    /**
     * Print the TTPGraph
     */
    public void printTTPGraph() {
        for(int i = 0; i < numberOfNodes; i++) {
            for(int j = 0; j < numberOfNodes; j++) {
                System.out.print(String.format("%3.2f ", TTPGraph[i][j]));
            }
            System.out.println();
            System.out.println();
        }
    }
    
    /**
     * Timer has expired, get the best solution from the thief and exit the program
     */
    public void getBestSolution() {
        //alec.getBestSolution();
        matt.getBestSolution();
        //sami.getBestSolution();
        //will.getBestSolution();
        System.out.println("TTPInstance: Timer expired, returning best solution");
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
                    TTPGraph = new double[numberOfNodes][numberOfNodes];
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
