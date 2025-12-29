import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class Autocomplete {

    // an array that stores the terms
    private Term[] terms;

    // Initializes the data structure from the given array of terms.
    public Autocomplete(Term[] terms) {
        error(terms, "Terms cannot be null.");

        String msg = "A term cannot be null.";

        this.terms = new Term[terms.length];
        for (int i = 0; i < terms.length; i++) {
            error(terms[i], msg);
            this.terms[i] = terms[i];
        }

        Arrays.sort(this.terms);
    }

    // helper function for the error message
    private void error(Object element, String message) {
        if (element == null) {
            throw new IllegalArgumentException(message);
        }
    }

    // helper function that produces error message when the prefix is null
    private void prefixError(String prefix) {
        error(prefix, "The prefix cannot be null.");
    }

    // Returns all terms that start with the given prefix,
    // in descending order of weight.
    public Term[] allMatches(String prefix) {
        prefixError(prefix);

        Term key = new Term(prefix, 0); // weight does not matter

        Comparator<Term> comparatorPrefix = Term.byPrefixOrder(prefix.length());
        int firstIndex = BinarySearchDeluxe.
                firstIndexOf(terms, key, comparatorPrefix);
        int lastIndex = BinarySearchDeluxe.
                lastIndexOf(terms, key, comparatorPrefix);

        if (firstIndex != -1) {
            Term[] result = new Term[lastIndex - firstIndex + 1];
            for (int i = firstIndex; i <= lastIndex; i++) {
                result[i - firstIndex] = terms[i];
            }

            Comparator<Term> comparatorWeight = Term.byReverseWeightOrder();
            Arrays.sort(result, comparatorWeight);

            return result;
        }
        else {
            return new Term[0]; // when there is no match
        }
    }

    // Returns the number of terms that start with the given prefix.
    public int numberOfMatches(String prefix) {
        prefixError(prefix);

        Term key = new Term(prefix, 0); // weight does not matter

        Comparator<Term> comparator = Term.byPrefixOrder(prefix.length());
        int firstIndex = BinarySearchDeluxe.
                firstIndexOf(terms, key, comparator);
        int lastIndex = BinarySearchDeluxe.
                lastIndexOf(terms, key, comparator);

        if (firstIndex != -1) return lastIndex - firstIndex + 1;
        else return 0; // when there is no match
    }

    // unit testing
    public static void main(String[] args) {
        // read in the terms from a file
        String filename = args[0];
        In in = new In(filename);
        int n = in.readInt();
        Term[] terms = new Term[n];
        for (int i = 0; i < n; i++) {
            long weight = in.readLong();           // read the next weight
            in.readChar();                         // scan past the tab
            String query = in.readLine();          // read the next query
            terms[i] = new Term(query, weight);    // construct the term
        }

        // read in queries from standard input and
        // print the top k matching terms
        int k = Integer.parseInt(args[1]);
        Autocomplete autocomplete = new Autocomplete(terms);
        while (StdIn.hasNextLine()) {
            String prefix = StdIn.readLine();
            Term[] results = autocomplete.allMatches(prefix);
            StdOut.printf("%d matches\n", autocomplete.numberOfMatches(prefix));
            for (int i = 0; i < Math.min(k, results.length); i++)
                StdOut.println(results[i]);
        }
    }

}
