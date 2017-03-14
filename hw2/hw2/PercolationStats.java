package hw2;

import edu.princeton.cs.introcs.StdRandom;
import edu.princeton.cs.introcs.StdStats;


public class PercolationStats {
    private Percolation p;
    private int row;
    private int col;
    private int percolationThreshold;
    private int[] percThres;

    public PercolationStats(int N, int T) {  // perform T independent experiments on an N-by-N grid
        if ((N <= 0) || (T <= 0)) {
            throw new IllegalArgumentException("ERROR: Incorrect arguments for N or T");
        }
        //p = new Percolation(N);
        percThres = new int[T];

        while (T >= 0) {
            p = new Percolation(N);
            //keep opening until percolates
            while (!p.percolates()) { // check for nulls!!
                row = (StdRandom.uniform(N)); // [0, N)
                col = (StdRandom.uniform(N)); // [0, N)
                p.open(row, col);
            }

            percolationThreshold = p.numberOfOpenSites() / (N * N);
            percThres[T] = percolationThreshold;
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
}                       
