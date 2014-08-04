import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;


public class TSPProblem {
    
    /* 2D array of floats where X is source, Y is destination and the value is cost
     * eg TSPGraph[0][1] will return the cost of the edge connecting node 0 to node 1*/
    private static double[][] TSPGraph;
    /** Alternate method for storing city information */
    private static City[] cities;
    
    private static int numVertex;
    
    public TSPProblem(String fileToLoad) {
        try {
            //open file for reading
            BufferedReader br = new BufferedReader(new FileReader(fileToLoad));
            
            //construct variables
            String line;
            int row = -1;
            int col = 0;
            
            //read the XML file and parse into TSPGraph[][]
            while( (line = br.readLine()) != null) {
                
                //get number of nodes in graph
                if(line.contains("-city problem")) {
                    String[] firstCut = line.split("-");
                    String[] getNum = firstCut[0].split(">");
                    numVertex = Integer.parseInt(getNum[1]);
                    
                    TSPGraph = new double[numVertex][numVertex];
                    cities = new City[numVertex];
                }
                
                //update for nodes
                if(line.contains("<vertex>")) {
                    row++;
                    col = 0;
                    
                    cities[row] = new City(row, numVertex);
                }
                
                //update for edges
                if(line.contains("<edge")) {
                    //get cost
                    String[] split = line.split("\"");
                    double cost = Double.parseDouble(split[1]);
                    
                    //get destNode
                    String[] destSplit = line.split(">");
                    String[] getDest = destSplit[1].split("<");
                    int node = Integer.parseInt(getDest[0]);
                    
                    //update graph
                    TSPGraph[row][node] = cost;
                    
                    //update cities
                    cities[row].add_edge(node, cost);
                    
                    //go to next city location
                    col++;
                }
            }
            br.close();
            
            //get a random solution
            Individual randomIndividual = new Individual(TSPGraph, cities);
            Population randomPopulation = new Population(randomIndividual);
            City[][] result = randomPopulation.get_solution_set(1);
            
            //print random solution
            for(int i = 0; i < result.length; i++) {
                System.out.println("***** Solution " + (i+1) + " *****");
                printSolution(result[i]);
                System.out.println("Total Cost = " + randomIndividual.get_cost(result[i]));
                System.out.println();
            }
            
            
            
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    /**
     * TESTING PURPOSES: Print the graph (best to pipe to file)
     */
    private void printGraph() {
        for(int i = 0; i < TSPGraph.length; i++) {
            System.out.print("[");
            for(int j = 0; j < TSPGraph[i].length; j++) {
                if(j+1 == TSPGraph[i].length) {
                    System.out.print(String.format("%-8f" , TSPGraph[i][j]));
                } else {
                    System.out.print(String.format("%-8f, " , TSPGraph[i][j]));
                }
            }
            System.out.println("]\n");
        }
    }
    
    /**
     * For Testing only
     * Given a City solution array, print its info.
     */
    private void printSolution(City[] city){
        for(int j = 0; j < city.length; j++){
            if(j != city.length-1){
                System.out.println(city[j].toString(city[j+1]));
            }else{ //return to start
                System.out.println(city[j].toString(city[0]));
            }
        }
    }
    
    /*****************************************
     *****************************************
     ** Main method for driver setup below. **
     *****************************************
     *****************************************/
    
    /**
     *Main method for TSPProblem handles CLI and creates a new TSPProblem instance
     */
    public static void main(String[] args) {
        
        
        /* Do options here (if there ever are any) */
        
        //Read in and load file
        int fileIdx = Arrays.asList(args).lastIndexOf("-f");
        String fileToLoad = "";
        
        if(fileIdx == -1) {
            usage();
            System.exit(1);
        } else {
            fileToLoad = args[fileIdx+1];
        }
        
        TSPProblem TSPInstance = new TSPProblem(fileToLoad);
    }
    
    /**
     * Incorrect parameter usage found - Print out usage notes for the user
     */
    private static void usage() {
        System.out.println("Usage notes for TSPProblem.java");
        System.out.println("java TSPProblem <options> -f <filename>");
        System.out.println();
        System.out.println("Options:");
        System.out.println("\t-f \t Flag followed by filepath/filename.xml of input (must be XML file)");
        System.out.println();
        System.out.println("Basic Usage: java TSPProblem -f xmlFile.xml");
        System.out.println();
    }
}