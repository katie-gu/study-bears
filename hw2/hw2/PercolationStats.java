package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;


public class PercolationStats {
    private Percolation p;
    private int row;
    private int col;
    private double percolationThreshold;
    private double[] percThres;

    public PercolationStats(int N, int T) {  // perform T independent experiments on an N-by-N grid
        if ((N <= 0) || (T <= 0)) {
            throw new IllegalArgumentException("ERROR: Incorrect arguments for N or T");
        }
        //p = new Percolation(N);
        percThres = new double[T];

        int count = 0;
        while (T > 0) {
            //System.out.println("WORKING ");
            p = new Percolation(N);
            //keep opening until percolates

            while (!p.percolates()) { // check for nulls!!
                row = (StdRandom.uniform(N)); // [0, N)
                //System.out.println("row : " + row);

                col = (StdRandom.uniform(N)); // [0, N)
                p.open(row, col);
            }


            percolationThreshold = ((double) p.numberOfOpenSites()) / (N * N);
            percThres[count] = percolationThreshold;
            count += 1;
            T -= 1;
        }

    }

    public double mean() {                    // sample mean of percolation threshold
        return StdStats.mean(percThres);

    }

    public double stddev() {                 // sample standard deviation of percolation threshold
        return StdStats.stddev(percThres);

    }

    public double confidenceLow() {          // low  endpoint of 95% confidence interval
        return mean() - ((1.96 * stddev()) / Math.sqrt(percThres.length));
    }

    public double confidenceHigh() {         // high endpoint of 95% confidence interval
        return mean() + ((1.96 * stddev()) / Math.sqrt(percThres.length));
    }


    public static void main(String[] args) {
        PercolationStats p = new PercolationStats(6, 10);
        System.out.println("Items in array: " + p.percThres.length);
        for (int i = 0; i < p.percThres.length; i++) {
            System.out.println("Item : " + p.percThres[i]);
        }
        System.out.println(p.mean());
        System.out.println(p.mean());
        System.out.println(p.stddev());
        System.out.println(p.confidenceLow());
        System.out.println(p.confidenceHigh());


        System.out.println("\n Second percStat object: ");
        PercolationStats p1 = new PercolationStats(6, 20);
        System.out.println(p1.mean());
        System.out.println(p1.mean());
        System.out.println(p1.stddev());
        System.out.println(p1.confidenceLow());
        System.out.println(p1.confidenceHigh());

    }

}


