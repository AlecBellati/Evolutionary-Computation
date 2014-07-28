import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;

import java.util.Arrays;
import java.util.ArrayList;


public class TSPProblem {
    
    /* 2D array of floats where X is source, Y is destination and the value is cost
     * eg TSPGraph[0][1] will return the cost of the edge connecting node 0 to node 1*/
    private static double[][] TSPGraph;
    
    
    public static void main(String[] args) {
        
        try {
            
            /*******************************************
             * Do options here (if there ever are any) *
             *******************************************/
            
            
            //Read in and load file
            int fileIdx = Arrays.asList(args).lastIndexOf("-f");
            String fileToLoad = "";
            
            if(fileIdx == -1) {
                usage();
                System.exit(1);
            } else {
                fileToLoad = args[fileIdx+1];
            }
            
            File xmlFile = new File(fileToLoad);
            
            
            //generate the DOM with built in Java DOM parser
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            
            //setup TSPGraph
            TSPGraph = new double[getNumNodes(doc)][getNumNodes(doc)];
            
            //Fill TSPGraph with Data
            NodeList vertices = doc.getElementsByTagName("vertex");
            
            //For each <vertex> tag, get all edge data
            for (int node = 0; node < vertices.getLength(); node++) {
                
                Node nNode = vertices.item(node);
                Element eElement = (Element) nNode;
                
                //parse each <edge cost="...">{dest}</edge> tag
                for(int edge = 0; edge < eElement.getElementsByTagName("edge").getLength(); edge++ ) {
                    
                    Node thisEdge = eElement.getElementsByTagName("edge").item(edge);
                    int destNode = Integer.parseInt(thisEdge.getTextContent());
                    Element costAttr = (Element)thisEdge;
                    double cost = Double.valueOf(costAttr.getAttribute("cost"));
                    
                    
                    //add this information to the matrix
                    TSPGraph[node][destNode] = cost;
                    
                }
            }
            
        } catch (IOException ioe) {
            ioe.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        
        /*********************
         * DO TSP STUFF HERE *
         *********************/
        
        //testing purposes only
        printGraph();
    }
    
    
    /**
     * Get the number of nodes from the description in the XML document
     * @param Document: a completed, normalized DOM using the correct XML description tag
     * @return int: Number of nodes in this graph
     */
    private static int getNumNodes(Document doc) {
        String[] description = (doc.getElementsByTagName("description").item(0).getTextContent()).split("-");
        return Integer.parseInt(description[0]);
    }
    
    
    /**
     * TESTING PURPOSES: Print the graph (best to pipe to file)
     */
    private static void printGraph() {
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