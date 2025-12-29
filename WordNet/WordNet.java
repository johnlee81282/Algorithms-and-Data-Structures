import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RedBlackBST;
import edu.princeton.cs.algs4.StdOut;

public class WordNet {

    // symbol table that stores nouns
    private RedBlackBST<String, Queue<Integer>> rbtree;
    // ShortestCommonAncestor type object for sca() and distance()
    private ShortestCommonAncestor ancestor;
    // an array that maps the number to String (the set of words)
    private RedBlackBST<Integer, String> mapsToString;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException("call constructor with null");
        }

        rbtree = new RedBlackBST<String, Queue<Integer>>();
        mapsToString = new RedBlackBST<Integer, String>();
        int lineCount = 0;

        // create the symbol table that maps from noun to index and vice versa
        In synsetsFile = new In(synsets);
        while (!synsetsFile.isEmpty()) {
            lineCount++;
            String line = synsetsFile.readLine();
            String[] texts = line.split(",");
            String[] keys = texts[1].split(" ");
            int val = Integer.parseInt(texts[0]);
            mapsToString.put(val, texts[1]);
            for (int i = 0; i < keys.length; i++) {
                if (!rbtree.contains(keys[i])) {
                    Queue<Integer> queue = new Queue<Integer>();
                    queue.enqueue(val);
                    rbtree.put(keys[i], queue);
                }
                else {
                    Queue<Integer> queue = rbtree.get(keys[i]);
                    queue.enqueue(val);
                }
            }
        }

        // a digraph that stores the edges
        Digraph edgesTo = new Digraph(lineCount);

        // creates the digraph from hypernym file
        In hypernymsFile = new In(hypernyms);
        while (!hypernymsFile.isEmpty()) {
            String texts = hypernymsFile.readLine();
            String[] numbers = texts.split(",");
            int child = Integer.parseInt(numbers[0]);
            for (int i = 1; i < numbers.length; i++) {
                int parent = Integer.parseInt(numbers[i]);
                edgesTo.addEdge(child, parent);
            }
        }

        ancestor = new ShortestCommonAncestor(edgesTo);
    }

    // the set of all WordNet nouns
    public Iterable<String> nouns() {
        return rbtree.keys();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException("call isNoun() with null");
        }

        return rbtree.contains(word);
    }

    // a synset (second field of synsets.txt) that is a shortest common ancestor
    // of noun1 and noun2 (defined below)
    public String sca(String noun1, String noun2) {
        Queue<Integer> subsetA = rbtree.get(noun1);
        Queue<Integer> subsetB = rbtree.get(noun2);
        int commonAncestor = ancestor.ancestorSubset(subsetA, subsetB);
        return mapsToString.get(commonAncestor);
    }

    // distance between noun1 and noun2 (defined below)
    public int distance(String noun1, String noun2) {
        Queue<Integer> subsetA = rbtree.get(noun1);
        Queue<Integer> subsetB = rbtree.get(noun2);
        return ancestor.lengthSubset(subsetA, subsetB);
    }

    // unit testing
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println(wordNet.isNoun("President"));
        StdOut.println(wordNet.sca("individual", "edible_fruit"));
        StdOut.println(wordNet.distance("white_marlin", "mileage"));
        StdOut.println(wordNet.distance("Black_Plague", "black_marlin"));
    }

}
