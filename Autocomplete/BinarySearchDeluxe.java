import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Comparator;

public class BinarySearchDeluxe {

    // helper function that throws an error
    private static void argumentError(Object element) {
        String msg = "An argument cannot be null.";
        if (element == null) throw new IllegalArgumentException(msg);
    }

    // Returns the index of the first key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int firstIndexOf(
            Key[] a, Key key, Comparator<Key> comparator) {
        argumentError(a);
        argumentError(key);
        argumentError(comparator);

        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int compare = comparator.compare(key, a[mid]);
            if (compare < 0) hi = mid - 1;
            else if (compare > 0) lo = mid + 1;
            else {
                // when lo is already the lower boundary
                if (comparator.compare(key, a[lo]) == 0) {
                    return lo;
                }
                // lo is always not less than the key and
                // mid is always equal to the key
                // The difference between mid and lo has to be one
                while (mid - lo > 1) {
                    int newMid = (lo + mid) >>> 1;
                    int midCompare = comparator.compare(key, a[newMid]);
                    if (midCompare == 0) {
                        mid = newMid;
                    }
                    else {
                        lo = newMid;
                    }
                }
                return mid;
            }
        }
        return -1;
    }

    // Returns the index of the last key in the sorted array a[]
    // that is equal to the search key, or -1 if no such key.
    public static <Key> int lastIndexOf(
            Key[] a, Key key, Comparator<Key> comparator) {
        argumentError(a);
        argumentError(key);
        argumentError(comparator);

        int lo = 0;
        int hi = a.length - 1;
        while (lo <= hi) {
            int mid = (lo + hi) >>> 1;
            int compare = comparator.compare(key, a[mid]);
            if (compare < 0) hi = mid - 1;
            else if (compare > 0) lo = mid + 1;
            else {
                // when hi is already the higher boundary
                if (comparator.compare(key, a[hi]) == 0) {
                    return hi;
                }
                // hi is always not bigger than the key and
                // mid is always equal to the key
                // The difference between mid and hi has to be one
                while (hi - mid > 1) {
                    int newMid = (hi + mid) >>> 1;
                    int midCompare = comparator.compare(key, a[newMid]);
                    if (midCompare == 0) {
                        mid = newMid;
                    }
                    else {
                        hi = newMid;
                    }
                }
                return mid;
            }
        }
        return -1;
    }

    // unit testing (required)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]); // the number we use in the test

        Integer[] testArray = new Integer[5 * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < 5; j++) {
                testArray[5 * i + j] = i + 1;
            }
        }

        // randomly uniform number between 0 and n + 1
        int key = StdRandom.uniformInt(n + 2);
        // StdOut.println(key);

        int firstIndex = firstIndexOf(testArray, key, Integer::compareTo);
        // StdOut.println(firstIndex);
        int lastIndex = lastIndexOf(testArray, key, Integer::compareTo);
        // StdOut.println(lastIndex);

        // brute force search of the first index and last index
        int expectedFirstIndex = -1;
        int expectedLastIndex = -1;
        for (int i = 0; i < 5 * n; i++) {
            if (testArray[i] == key) {
                if (i == 0 || testArray[i - 1] != key) {
                    expectedFirstIndex = i;
                }
                else if (i == 5 * n - 1 || testArray[i + 1] != key) {
                    expectedLastIndex = i;
                }
            }
        }

        if (firstIndex != expectedFirstIndex
                || lastIndex != expectedLastIndex) {
            StdOut.println("Error!");
        }
    }
}
