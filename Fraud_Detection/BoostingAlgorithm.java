import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Stopwatch;

public class BoostingAlgorithm {

    // the number of locations
    private final int m;
    // the number of transactions
    private final int n;
    // the cluster of locations
    private final Clustering clusters;
    // the queue of WeakLearners, one will be added in iterate()
    private Queue<WeakLearner> queue;
    // the array of weights
    private double[] weights;
    // clustered input
    private final int[][] clusteredInput;
    // the correct labels for the inputs
    private final int[] labels;

    // create the clusters and initialize your data structures
    public BoostingAlgorithm(int[][] input, int[] labels, Point2D[] locations, int k) {
        if (input == null || labels == null || locations == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        n = input.length; // the number of data inputs
        m = input[0].length; // the number of locations


        if (labels.length != n || locations.length != m) {
            throw new IllegalArgumentException("Array length is incompatible");
        }

        if (k < 1 || k > m) {
            throw new IllegalArgumentException("Invalid number of clusters");
        }

        for (int i = 0; i < n; i++) {
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException("Label has to be 0 or 1");
            }
        }


        // reduce the dimension of the input information
        clusters = new Clustering(locations, k);
        clusteredInput = new int[n][k];
        for (int i = 0; i < n; i++) {
            clusteredInput[i] = clusters.reduceDimensions(input[i]);
        }

        // initialize other instance variables
        this.labels = new int[n];
        for (int i = 0; i < n; i++) {
            this.labels[i] = labels[i];
        }
        queue = new Queue<WeakLearner>();
        weights = new double[n];
        for (int i = 0; i < n; i++) {
            weights[i] = 1.0 / n;
        }
    }

    // return the current weight of the ith point
    public double weightOf(int i) {
        if (i < 0 || i >= n) {
            throw new IllegalArgumentException("Invalid index");
        }

        return weights[i];
    }

    // apply one step of the boosting algorithm
    public void iterate() {
        WeakLearner learner = new WeakLearner(clusteredInput, weights, labels);
        queue.enqueue(learner);
        double sumWeights = 0;
        for (int i = 0; i < n; i++) {
            if (learner.predict(clusteredInput[i]) != labels[i]) { // mistake
                weights[i] *= 2;
            }
            sumWeights += weights[i];
        }
        // normalize the weights again
        for (int i = 0; i < n; i++) {
            weights[i] /= sumWeights;
        }
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (sample.length != m) {
            throw new IllegalArgumentException("Array length is incompatible");
        }

        int[] clusteredSample = clusters.reduceDimensions(sample);
        int clean = 0; // the number of predictions as clean
        int fraud = 0; // the number of predictions as fraud
        int qNum = queue.size();

        for (int i = 0; i < qNum; i++) {
            WeakLearner learner = queue.dequeue();
            int predict = learner.predict(clusteredSample);
            if (predict == 0) clean++;
            else fraud++;
            queue.enqueue(learner);
        }

        if (clean >= fraud) return 0;
        else return 1;
    }

    // unit testing
    public static void main(String[] args) {
        // read in the terms from a file
        DataSet training = new DataSet(args[0]);
        DataSet testing = new DataSet(args[1]);
        int k = Integer.parseInt(args[2]);
        int trials = Integer.parseInt(args[3]);

        int[][] trainingInput = training.getInput();
        int[][] testingInput = testing.getInput();
        int[] trainingLabels = training.getLabels();
        int[] testingLabels = testing.getLabels();
        Point2D[] trainingLocations = training.getLocations();

        Stopwatch stopwatch = new Stopwatch();

        // train the model
        BoostingAlgorithm model = new BoostingAlgorithm(trainingInput, trainingLabels,
                                                        trainingLocations, k);
        for (int t = 0; t < trials; t++)
            model.iterate();

        // calculate the training data set accuracy
        double trainingAccuracy = 0;
        for (int i = 0; i < training.getN(); i++)
            if (model.predict(trainingInput[i]) == trainingLabels[i])
                trainingAccuracy += 1;
        trainingAccuracy /= training.getN();

        // calculate the test data set accuracy
        double testAccuracy = 0;
        for (int i = 0; i < testing.getN(); i++)
            if (model.predict(testingInput[i]) == testingLabels[i])
                testAccuracy += 1;
        testAccuracy /= testing.getN();

        double time = stopwatch.elapsedTime();

        StdOut.println("Training accuracy of model: " + trainingAccuracy);
        StdOut.println("Test accuracy of model: " + testAccuracy);
        StdOut.println("time: " + time);
    }
}
