import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import TTP.Optimisation.Optimisation;

import TTP.TTPInstance;
import TTP.Thief.TTPSolution;
import TTP.Utils.DeepCopy;
import TTP.Utils.Utils;

/**
 * @OriginalAuthor: Wagner
 * @Modified by:
 * a1193380 Matthew Hart
 * a1215621 William Reid
 * a1608934 Alec Bellati
 * a1192722 Sami Peachey
 */
public class DriverOld {
    
    private static double total;

    /* The current sequence of parameters is
     * args[0]  folder with TTP files
     * args[1]  pattern to identify the TTP problems that should be solved
     * args[2]  optimisation approach chosen
     * args[3]  stopping criterion: number of evaluations without improvement
     * args[4]  stopping criterion: time in milliseconds (e.g., 60000 equals 1 minute)
     */
    public static void main(String[] args) {
        total = 0;
        int runs = 20;

        if (args.length==0){
            args = new String[]{"../instances/", "a280_n1395_uncorr-similar-weights_05.ttp", "2", "10000", "600000"};
        }
        long startTime = System.currentTimeMillis();

        for(int i = 0; i < 20; i++){
            doBatch(args);
        }
        long endTime = System.currentTimeMillis();
        long duration = (endTime - startTime);

        double avgTime = duration/1000;
        double avgTimeRun = avgTime/runs;

        System.out.println("Batch run took " + avgTime + " seconds, averaging " + avgTimeRun 
            + " seconds, and a score of " + (total/runs) + " per run.\n");
    }
    
    // note: doBatch can process several files sequentially
    public static void doBatch(String[] args) {
//        String[] args = new String[]{"instances/","a2"};                      // first argument: folder with TTP and TSP files, second argument: partial filename of the instances to be solved   
//        System.out.println("parameters: "+Arrays.toString(args));
        File[] files = TTP.Utils.Utils.getFileList(args);
        
        int algorithm = Integer.parseInt(args[2]);
        int durationWithoutImprovement = Integer.parseInt(args[3]);
        int maxRuntime = Integer.parseInt(args[4]);
        
//        System.ou∂t.println("files.length="+files.length+" algorithm="+algorithm+" durationWithoutImprovement="+durationWithoutImprovement);
//        System.out.println("wend wendUsed fp ftraw ft ob computationTime");
        
        for (File f:files) {
            // read the TSP instance
            TTPInstance instance = new TTPInstance(f);
            
            long startTime = System.currentTimeMillis();
            String resultTitle = instance.file.getName() + ".NameOfTheAlgorithm." + startTime;
            
            // generate a Linkern tour (or read it if it already exists)
            int[] tour = Optimisation.linkernTour(instance);

            System.out.print(f.getName()+": ");
            
            // do the optimisation
            TTPSolution solution = Optimisation.hillClimber(instance, tour, algorithm, 
                    durationWithoutImprovement, maxRuntime);
            
            
            // print to file
            //solution.writeResult(resultTitle);
            
            // print to screen
            solution.println();
            total += solution.getObjective();
            
//            solution.printFull();
        }
    }
    
    
    public static void runSomeTests() {
        //        TTPInstance instance = new TTPInstance(new File("instances/a280_n279_bounded-strongly-corr_1.TTP"));
        TTPInstance instance = new TTPInstance(new File("instances/a280_n1395_bounded-strongly-corr_1.TTP"));
//        TTPInstance instance = new TTPInstance(new File("instances/a280_n2790_bounded-strongly-corr_10.TTP"));
//        TTPInstance instance = new TTPInstance(new File("instances/a280_n837_uncorr_9.TTP"));
//        instance.printInstance(false);
        
        int[] tour = new int[instance.numberOfNodes+1];
//        for (int i=0; i<tour.length; i++) tour[i] = i;
//        tour[instance.numberOfNodes]=0;
////        tour = permutation(tour.length);
        
        TTP.Utils.Utils.startTiming();
        tour = Optimisation.linkernTour(instance);
        TTP.Utils.Utils.stopTimingPrint();
        
        
        int[] packingPlan = new int[instance.numberOfItems];
        TTPSolution solution = new TTPSolution(tour, packingPlan);
        instance.evaluate(solution);
        System.out.print("\nLINKERN tour and no pickup: ");
        solution.printFull();
        
        packingPlan = new int[instance.numberOfItems];
        for (int i=0; i<packingPlan.length; i++) packingPlan[i] = 0;
//        for (int i=0; i<packingPlan.length; i++) packingPlan[i] = Math.random()<0.1?1:0;
        packingPlan[0]=1;
//        packingPlan[11]=1;
//        packingPlan[12]=1;
//        packingPlan[packingPlan.length-1]=1;
//        TTPSolution solution = new TTPSolution(tour, packingPlan);
//        instance.evaluate(solution);
//        solution.print();
        solution = new TTPSolution(tour, packingPlan);
        instance.evaluate(solution);
        System.out.print("\nLINKERN tour and only pickup of the first item: ");
        solution.printFull();
        
        int durationWithoutImprovement = 100;
        
        System.out.println("\nOptimiser: hillclimber (flip 1)");
        Optimisation.hillClimber(instance, tour, 1, durationWithoutImprovement, 600).printFull();
        
        System.out.println("\nOptimiser: hillclimber (flip with prob 1/n)");
        Optimisation.hillClimber(instance, tour, 2, durationWithoutImprovement, 600).printFull();
        
        
    }
    
    
}
