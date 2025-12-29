import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Term implements Comparable<Term> {

    // the string text of the Term
    private String query;
    // the weights associated with the text
    private long weight;

    // Initializes a term with the given query string and weight.
    public Term(String query, long weight) {
        if (query == null) {
            throw new IllegalArgumentException("The text cannot be null.");
        }
        if (weight < 0) {
            throw new IllegalArgumentException(
                    "The weight cannot be negative.");
        }
        this.query = query;
        this.weight = weight;
    }

    // private helper class that compares two terms based on the weights
    private static class WeightsCompare implements Comparator<Term> {

        // compare two terms in reverse order based on the weights
        public int compare(Term first, Term second) {
            if (first.weight > second.weight) return -1;
            else if (first.weight < second.weight) return 1;
            else return 0;
        }
    }

    // Compares the two terms in descending order by weight.
    public static Comparator<Term> byReverseWeightOrder() {
        return new WeightsCompare();
    }

    // private helper class that compares the first r characters of two terms
    private static class PrefixCompare implements Comparator<Term> {
        // the number of characters in the beginning of the string
        // that we use to compare the strings
        private int r;

        // constructor, initialize the number of characters we use
        private PrefixCompare(int r) {
            if (r < 0) {
                throw new IllegalArgumentException(
                        "The number of characters cannot be negative");
            }
            this.r = r;
        }

        // compare two terms using the first r characters
        public int compare(Term first, Term second) {
            String firstQuery = first.query;
            String secondQuery = second.query;
            for (int i = 0; i < r; i++) {
                // if one of the query has fewer than i characters
                if (firstQuery.length() <= i || secondQuery.length() <= i) {
                    return firstQuery.length() - secondQuery.length();
                }
                else if (firstQuery.charAt(i) > secondQuery.charAt(i)) {
                    return 1;
                }
                else if (firstQuery.charAt(i) < secondQuery.charAt(i)) {
                    return -1;
                }
                else {
                    continue;
                }
            }
            return 0;
        }
    }

    // Compares the two terms in lexicographic order,
    // but using only the first r characters of each query.
    public static Comparator<Term> byPrefixOrder(int r) {
        return new PrefixCompare(r);
    }

    // Compares the two terms in lexicographic order by query.
    public int compareTo(Term that) {
        String firstQuery = this.query;
        String secondQuery = that.query;
        return firstQuery.compareTo(secondQuery);
    }

    // Returns a string representation of this term in the following format:
    // the weight, followed by a tab, followed by the query.
    public String toString() {
        return weight + "\t" + query;
    }

    // unit testing (required)
    public static void main(String[] args) {
        Term first = new Term("first", 1000);
        Term second = new Term("firstRound", 50);
        Term third = new Term("third", 1000);
        Term fourth = new Term("third", 40);

        // toString test
        StdOut.println(first);

        // lexicographic comparison
        // this should return negative value since f comes earlier than t
        StdOut.println(first.compareTo(fourth));
        // this should return zero since third and fourth have the same query
        StdOut.println(third.compareTo(fourth));
        // this should return positive number since t comes later than f
        StdOut.println(third.compareTo(second));

        // reverse weight comparison
        // this should return negative value since 50 > 40
        StdOut.println(Term.byReverseWeightOrder().compare(second, fourth));
        // this should return zero since 1000 == 1000
        StdOut.println(Term.byReverseWeightOrder().compare(first, third));
        // this should return positive value since 50 < 1000
        StdOut.println(Term.byReverseWeightOrder().compare(second, third));

        // prefix order comparison
        // this should return negative value since "first" comes before "firstR"
        StdOut.println(Term.byPrefixOrder(6).compare(first, second));
        // this should return zero since "fir" is the same as "fir"
        StdOut.println(Term.byPrefixOrder(3).compare(first, second));
        // this should return positive since t comes after f
        StdOut.println(Term.byPrefixOrder(1).compare(third, second));
    }
}
