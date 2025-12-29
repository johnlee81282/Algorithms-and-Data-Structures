import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class WeakLearner {

    // the number of clusters / dimensions
    private final int k;
    // the dimension the learner uses
    private final int d;
    // the value the learner uses;
    private final int v;
    // the sign the learner uses
    private final int s;

    // train the weak learner
    public WeakLearner(int[][] input, double[] weights, int[] labels) {
        if (input == null || weights == null || labels == null) {
            throw new IllegalArgumentException("Argument cannot be null");
        }

        int n = input.length; // the number of inputs
        k = input[0].length;

        if (weights.length != n || labels.length != n) {
            throw new IllegalArgumentException(
                    "The length of array is incompatible");
        }

        for (int i = 0; i < n; i++) {
            if (weights[i] < 0) {
                throw new IllegalArgumentException("Weight cannot be negatie");
            }
            if (labels[i] != 0 && labels[i] != 1) {
                throw new IllegalArgumentException("Label has to be 0 or 1");
            }
        }

        double minErrorGlobal = Double.POSITIVE_INFINITY;
        int dTemp = -1;
        int vTemp = -1;
        int sTemp = -1;

        // obtain the best predictions on each dimension and get the best one
        for (int i = 0; i < k; i++) {
            int[] coordinates = new int[n]; // copy the k-th coordinates
            for (int j = 0; j < n; j++) {
                coordinates[j] = input[j][i];
            }

            // need to sort by coordinates but also keep the other info
            // we can just keep the index of coordinates
            // without changing the order
            for (int j = 0; j < n; j++) {
                coordinates[j] = coordinates[j] * n + j;
            }
            Arrays.sort(coordinates); // O(n log n)

            // obtain the original coordinates and indices
            int[] indices = new int[n];
            for (int j = 0; j < n; j++) {
                int coordinate = coordinates[j];
                int modulo = coordinate % n;
                if (modulo >= 0) {
                    indices[j] = modulo;
                    coordinates[j] = (coordinate - modulo) / n;
                }
                else {
                    // when the coordinate is negative
                    // (although this will probably not happen)
                    indices[j] = n + modulo;
                    coordinates[j] = (coordinate - indices[j]) / n;
                }
            }

            // get the errors for each prediction

            // sign = 0
            double error0 = 0; // when all the points are classified as 1
            int v0 = coordinates[0] - 1;
            for (int j = 0; j < n; j++) {
                if (labels[j] == 0) error0 += weights[j];
            }
            double minError0 = error0;
            // update for each coordinate (O(n) in total)
            int j0 = 0;
            while (j0 < n) {
                int tempV = coordinates[j0];
                if (labels[indices[j0]] == 0) { // subtract the weight
                    error0 -= weights[indices[j0]];
                }
                else { // add the weight as the prediction went from 1 to 0
                    error0 += weights[indices[j0]];
                }
                while (j0 < n - 1) {
                    if (coordinates[j0 + 1] == tempV) {
                        j0++;
                        if (labels[indices[j0]] == 0)
                            error0 -= weights[indices[j0]];
                        else error0 += weights[indices[j0]];
                    }
                    else break;
                }
                // update the minError if the error is smaller
                if (error0 < minError0) {
                    minError0 = error0;
                    v0 = tempV;
                }
                j0++;
            }

            // sign = 1
            double error1 = 0;
            int v1 = coordinates[0] - 1;
            for (int j = 0; j < n; j++) {
                if (labels[j] == 1) error1 += weights[j];
            }
            double minError1 = error1;
            int j1 = 0;
            while (j1 < n) {
                int tempV = coordinates[j1];
                if (labels[indices[j1]] == 0) error1 += weights[indices[j1]];
                else error1 -= weights[indices[j1]];
                while (j1 < n - 1) {
                    if (coordinates[j1 + 1] == tempV) {
                        j1++;
                        if (labels[indices[j1]] == 0)
                            error1 += weights[indices[j1]];
                        else error1 -= weights[indices[j1]];
                    }
                    else break;
                }
                if (error1 < minError1) {
                    minError1 = error1;
                    v1 = tempV;
                }
                j1++;
            }

            // check which one is smaller
            double minErrorLocal;
            int vLocal, sLocal;
            if (minError0 <= minError1) {
                minErrorLocal = minError0;
                vLocal = v0;
                sLocal = 0;
            }
            else {
                minErrorLocal = minError1;
                vLocal = v1;
                sLocal = 1;
            }

            // keep track of the minimum error
            if (minErrorLocal < minErrorGlobal) {
                minErrorGlobal = minErrorLocal;
                dTemp = i;
                vTemp = vLocal;
                sTemp = sLocal;
            }
        }

        this.d = dTemp;
        this.v = vTemp;
        this.s = sTemp;
    }

    // return the prediction of the learner for a new sample
    public int predict(int[] sample) {
        if (sample == null) {
            throw new IllegalArgumentException("Array cannot be null");
        }
        if (sample.length != k) {
            throw new IllegalArgumentException(
                    "The length of array is incompatible");
        }

        boolean smaller = (sample[d] <= v);
        if (smaller) {
            return s;
        }
        else {
            return 1 - s;
        }
    }

    // return the dimension the learner uses to separate the data
    public int dimensionPredictor() {
        return d;
    }

    // return the value the learner uses to separate the data
    public int valuePredictor() {
        return v;
    }

    // return the sign the learner uses to separate the data
    public int signPredictor() {
        return s;
    }

    // unit testing
    public static void main(String[] args) {
        int[][] input = { { 1, 2 }, { 2, 2 }, { 3, 3 }, { 3, 1 } };
        double[] weights = { 1, 1, 1, 1 };
        int[] labels = { 1, 0, 0, 0 };
        WeakLearner test = new WeakLearner(input, weights, labels);
        StdOut.println(test.dimensionPredictor() + " " + test.valuePredictor()
                               + " " + test.signPredictor());
    }
}
