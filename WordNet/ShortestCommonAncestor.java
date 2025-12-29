import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class ShortestCommonAncestor {

    // an instance variable that stores the digraph
    private final Digraph digraph;


    // constructor takes a rooted DAG as argument
    public ShortestCommonAncestor(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("call constructor with null");
        digraph = new Digraph(G);

        DirectedCycle cycle = new DirectedCycle(digraph);
        if (cycle.hasCycle())
            throw new IllegalArgumentException("call constructor with non-DAG");

        // rooted DAG has only one vertex without any outward edges
        int count = 0;
        for (int i = 0; i < digraph.V(); i++) {
            if (digraph.outdegree(i) == 0) count++;
        }
        if (count != 1)
            throw new IllegalArgumentException(
                    "call constructor with non-rooted DAG");

    }


    // length of shortest ancestral path between v and w
    public int length(int v, int w) {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) {
            throw new IllegalArgumentException(
                    "the vertex out of range in length()");
        }

        Queue<Integer> subsetA = new Queue<Integer>();
        Queue<Integer> subsetB = new Queue<Integer>();
        subsetA.enqueue(v);
        subsetB.enqueue(w);
        return shortestCommonAncestorSubset(subsetA, subsetB)[0];
    }

    // a shortest common ancestor of vertices v and w
    public int ancestor(int v, int w) {
        if (v < 0 || v >= digraph.V() || w < 0 || w >= digraph.V()) {
            throw new IllegalArgumentException(
                    "the vertex out of range in ancestor()");
        }

        Queue<Integer> subsetA = new Queue<Integer>();
        Queue<Integer> subsetB = new Queue<Integer>();
        subsetA.enqueue(v);
        subsetB.enqueue(w);
        return shortestCommonAncestorSubset(subsetA, subsetB)[1];
    }

    // check if the vertex is integer and between 0 and digraph.V()
    private int convertsInt(Object i) {
        if (i == null) {
            throw new IllegalArgumentException("null vertex");
        }
        Integer j = 0;
        if (i.getClass() != j.getClass()) {
            throw new IllegalArgumentException("non-integer vertex");
        }
        else {
            int k = (int) i;
            if (k < 0 || k >= digraph.V()) {
                throw new IllegalArgumentException("invalid vertex");
            }
            return k;
        }
    }

    // helper method that calculates the length and ancestor for subsets
    private int[] shortestCommonAncestorSubset(Iterable<Integer> subsetA,
                                               Iterable<Integer> subsetB) {
        int[] result = new int[2]; // the length and the vertex
        // mark all the ancestors of v and store the length of the path
        int[] pathV = new int[digraph.V()];
        int[] pathW = new int[digraph.V()];
        boolean[] ancestorsA = new boolean[digraph.V()];
        boolean[] ancestorsB = new boolean[digraph.V()];
        Queue<Integer> ancestors = new Queue<Integer>();
        for (Object a : subsetA) {
            int v = convertsInt(a);
            ancestorsA[v] = true;
            ancestors.enqueue(v);
        }
        while (!ancestors.isEmpty()) {
            int vertex = ancestors.dequeue();
            for (int i : digraph.adj(vertex)) {
                if (ancestorsA[i]) continue;
                ancestors.enqueue(i);
                pathV[i] = pathV[vertex] + 1;
                ancestorsA[i] = true;
            }
        }

        // check the ancestors of w and keep track of the shortest path
        Queue<Integer> commonAncestors = new Queue<Integer>();
        int shortestLength = Integer.MAX_VALUE;
        int commonAncestor = -1;

        for (Object b : subsetB) {
            int w = convertsInt(b);
            ancestorsB[w] = true;
            // if w is hypernym of v
            if (ancestorsA[w]) {
                if (pathV[w] < shortestLength) {
                    shortestLength = pathV[w];
                    commonAncestor = w;
                }
            }
            commonAncestors.enqueue(w);
        }

        while (!commonAncestors.isEmpty()) {
            int vertex = commonAncestors.dequeue();
            for (int i : digraph.adj(vertex)) {
                if (ancestorsB[i]) continue;
                commonAncestors.enqueue(i);
                pathW[i] = pathW[vertex] + 1;
                ancestorsB[i] = true;
                if (ancestorsA[i]) {
                    int pathLength = pathV[i] + pathW[i];
                    if (pathLength < shortestLength) {
                        shortestLength = pathLength;
                        commonAncestor = i;
                    }
                }
            }
        }

        result[0] = shortestLength;
        result[1] = commonAncestor;
        return result;
    }

    // length of shortest ancestral path of vertex subsets A and B
    public int lengthSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException("call lengthSubset() with null");
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException(
                    "call lengthSubset() with empty subset(s)");
        return shortestCommonAncestorSubset(subsetA, subsetB)[0];

    }

    // a shortest common ancestor of vertex subsets A and B
    public int ancestorSubset(Iterable<Integer> subsetA, Iterable<Integer> subsetB) {
        if (subsetA == null || subsetB == null)
            throw new IllegalArgumentException(
                    "call ancestorSubset() with null");
        if (!subsetA.iterator().hasNext() || !subsetB.iterator().hasNext())
            throw new IllegalArgumentException(
                    "call ancestorSubset() with empty subset(s)");
        return shortestCommonAncestorSubset(subsetA, subsetB)[1];
    }

    // unit testing (required)
    // test if the lengthSubset() works as expected
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int m = Integer.parseInt(args[1]);

        // random length of the paths
        int[] pathA = new int[n];
        int[] pathB = new int[n];
        int length = 0;
        for (int i = 0; i < n; i++) {
            pathA[i] = StdRandom.uniformInt(1, m + 1);
            pathB[i] = StdRandom.uniformInt(1, m + 1);
            length += pathA[i] + pathB[i];
        }

        // add edges to the digraph
        Digraph digraph = new Digraph(length + 1);
        int current = 1;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < pathA[i] - 1; j++) {
                digraph.addEdge(current, current + 1);
                current++;
            }
            digraph.addEdge(current, 0);
            current++;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < pathB[i] - 1; j++) {
                digraph.addEdge(current, current + 1);
                current++;
            }
            digraph.addEdge(current, 0);
            current++;
        }

        // make subsetA and subsetB
        Queue<Integer> subsetA = new Queue<Integer>();
        Queue<Integer> subsetB = new Queue<Integer>();
        int subsetIndex = 1;
        for (int i = 0; i < n; i++) {
            subsetA.enqueue(subsetIndex);
            subsetIndex += pathA[i];
        }
        for (int i = 0; i < n; i++) {
            subsetB.enqueue(subsetIndex);
            subsetIndex += pathB[i];
        }

        // minimum length of subsetA and subsetB
        int minA = m + 1;
        int minB = m + 1;
        for (int i = 0; i < n; i++) {
            if (pathA[i] < minA) minA = pathA[i];
            if (pathB[i] < minB) minB = pathB[i];
        }

        // check if the length matches the expected answer
        ShortestCommonAncestor ancestor = new ShortestCommonAncestor(digraph);
        if (ancestor.lengthSubset(subsetA, subsetB) != minA + minB)
            StdOut.println("Error!");

    }

}
