import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

public class PercolationStats {
    // a value in the equation for confidence interval
    private static final double VALUE = 1.96;
    // mean value
    private double meanvalue;
    // number of trials
    private int t;
    // an array of percolation thresholds
    private double[] open;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Error: Out of Bounds");
        }
        t = trials;
        double length = n * n;

        open = new double[t];
        for (int i = 0; i < t; i++) {
            Percolation percolation = new Percolation(n);
            while (!percolation.percolates()) {
                percolation.open(
                        StdRandom.uniformInt(n), StdRandom.uniformInt(n));
            }
            open[i] = percolation.numberOfOpenSites() / length;
        }
        meanvalue = StdStats.mean(open);
    }

    // sample mean of percolation threshold
    public double mean() {
        return meanvalue;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        double[] std = new double[t];
        for (int i = 0; i < t; i++) {
            std[i] = open[i] - mean();
        }

        return StdStats.stddev(std);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLow() {
        return mean() - (VALUE * stddev()) / Math.sqrt(t);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHigh() {
        return mean() + (VALUE * stddev()) / Math.sqrt(t);
    }

    // test client (see below)
    public static void main(String[] args) {
        Stopwatch stopwatch = new Stopwatch();
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats test = new PercolationStats(n, trials);
        System.out.println("mean() = " + test.mean());
        System.out.println("stddev() = " + test.stddev());
        System.out.println("confidenceLow() = " + test.confidenceLow());
        System.out.println("confidenceHigh() = " + test.confidenceHigh());
        double time = stopwatch.elapsedTime();
        System.out.println("elapsed time = " + time);

    }

}
