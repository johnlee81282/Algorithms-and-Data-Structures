import edu.princeton.cs.algs4.CC;
import edu.princeton.cs.algs4.Edge;
import edu.princeton.cs.algs4.EdgeWeightedGraph;
import edu.princeton.cs.algs4.Graph;
import edu.princeton.cs.algs4.KruskalMST;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Clustering {
    // the number of locations
    private final int m;
    // the index of clusters
    private final int[] clusters;
    // the number of clusters
    private final int k;

    // run the clustering algorithm and create the clusters
    public Clustering(Point2D[] locations, int k) {
        if (locations == null) {
            throw new IllegalArgumentException("Locations cannot be null");
        }

        m = locations.length;
        this.k = k;

        for (int i = 0; i < m; i++) {
            if (locations[i] == null) {
                throw new IllegalArgumentException("Location cannot be null");
            }
        }

        if (k < 1 || k > m) {
            throw new IllegalArgumentException("k is out of range");
        }

        // initialize the digraph of locations in O(m^2)
        EdgeWeightedGraph weightedG = new EdgeWeightedGraph(m);
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                double weight = locations[i].distanceTo(locations[j]);
                weightedG.addEdge(new Edge(i, j, weight));
            }
        }

        // Compute the minimum spanning tree in O(E logE) = O(m^2 log m)
        KruskalMST mst = new KruskalMST(weightedG);

        // choose the lowest m-k edges, pq takes log time
        MinPQ<Edge> pq = new MinPQ<Edge>();
        for (Edge edge : mst.edges()) {
            pq.insert(edge);
        }
        Edge[] lowestEdges = new Edge[m - k];
        for (int i = 0; i < m - k; i++) {
            lowestEdges[i] = pq.delMin();
        }

        // create a new graph with the lowestEdges
        Graph G = new Graph(m);
        for (int i = 0; i < m - k; i++) {
            int v = lowestEdges[i].either();
            int w = lowestEdges[i].other(v);
            G.addEdge(v, w);
        }

        // store the k clusters into an array
        CC cc = new CC(G);
        clusters = new int[m];
        for (int i = 0; i < m; i++) {
            clusters[i] = cc.id(i);
        }
    }

    // return the cluster of the ith location
    public int clusterOf(int i) {
        if (i < 0 || i >= m) {
            throw new IllegalArgumentException("The index out of range");
        }

        return clusters[i];
    }

    // use the clusters to reduce the dimensions of an input
    public int[] reduceDimensions(int[] input) {
        if (input == null) {
            throw new IllegalArgumentException("input cannot be null");
        }
        if (input.length != m) {
            throw new IllegalArgumentException(
                    "The length of input does not match the number of locations");
        }

        int[] output = new int[k]; // 0s by default
        for (int i = 0; i < m; i++) {
            output[clusters[i]] += input[i];
        }
        return output;
    }

    // unit testing (required)
    public static void main(String[] args) {

        int c = Integer.parseInt(args[0]);
        int p = Integer.parseInt(args[1]);

        Point2D[] centers = new Point2D[c];
        int index = 0; // index of centers[]
        // initialize the first element
        centers[index] = new Point2D(StdRandom.uniformDouble(0, 1000),
                                     StdRandom.uniformDouble(0, 1000));
        index++;
        while (index < c) {
            double x = StdRandom.uniformDouble(0, 1000);
            double y = StdRandom.uniformDouble(0, 1000);
            Point2D random = new Point2D(x, y);
            for (int i = 0; i < index; i++) {
                if (centers[i].distanceSquaredTo(random) < 4 * 4) {
                    break;
                }
                if (i == index - 1) { // it is not close to other points
                    centers[index] = random;
                    index++;
                }
            }
        }

        Point2D[] locations = new Point2D[c * p];
        for (int i = 0; i < c; i++) {
            Point2D center = centers[i];
            for (int j = 0; j < p; j++) {
                int ind = i * p + j;
                double x = StdRandom.uniformDouble(center.x() - 1, center.x() + 1);
                double y = StdRandom.uniformDouble(center.y() - 1, center.y() + 1);
                Point2D random = new Point2D(x, y);
                while (random.distanceSquaredTo(center) > 1) {
                    x = StdRandom.uniformDouble(center.x() - 1, center.x() + 1);
                    y = StdRandom.uniformDouble(center.y() - 1, center.y() + 1);
                    random = new Point2D(x, y);
                }
                locations[ind] = random;
            }
        }

        // check if the method works as expected
        Clustering test = new Clustering(locations, c);
        outerloop:
        for (int i = 0; i < c; i++) {
            for (int j = 1; j < p; j++) {
                if (test.clusterOf(i * p + j) != test.clusterOf(i * p + j - 1)) {
                    StdOut.println("Error!");
                    break outerloop;
                }
            }
        }
    }
}
