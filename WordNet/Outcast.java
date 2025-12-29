import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    // instance variable storing the wordnet
    private WordNet wordNet;

    // constructor takes a WordNet object
    public Outcast(WordNet wordnet) {
        wordNet = wordnet;
    }

    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        int farthestDistance = -1;
        String champ = "";
        for (String noun : nouns) {
            int distance = 0;
            for (String x : nouns) {
                distance += wordNet.distance(noun, x);
            }
            if (distance > farthestDistance) {
                farthestDistance = distance;
                champ = noun;
            }
        }
        return champ;
    }

    // test client (see below)
    // create WordNet from the first two command line arguments
    // calculate the outcast from each file
    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }

}
