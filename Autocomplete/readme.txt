Programming Assignment 3: Autocomplete


/* *****************************************************************************
 *  Describe how your firstIndexOf() method in BinarySearchDeluxe.java
 *  finds the first index of a key that is equal to the search key.
 **************************************************************************** */
First, you do a binary search until you find a key that matches the search key.
Since the middle should be the matched key, between low and middle, you keep
cutting the range into halves until middle - low == 1. The point is to make sure
that low does not match the key and middle matches the key. Therefore, we have
to check if the low does not match the key at first, and if it does, then the
low should be the first index. When making the range small, if
(middle + low) / 2 element does match with the key, make it into a new middle,
and if it does not, make it into a new low.

/* *****************************************************************************
 *  Suppose you know that the terms in some sample file are evenly
 *  distributed, i.e. the number of terms starting with any given
 *  letter are roughly the same (so the number of words starting with 'a'
 *  is very similar to the ones starting with 'b', and so on).
 *
 *  How would you modify your implementation of firstIndexOf/lastIndexOf
 *  to be more efficient *in practice* for this distribution of terms?
 *  More efficient in practice means that it would be faster for most
 *  keys, but in the worst case it could still require 1 + log n compares.
 *
 *  Note that you don't have to write any Java code, your answer can be
 *  a description of how you'd modify your binary search solution.
 **************************************************************************** */
First, we would put a numeric value in each letter alphabetically (so a=0, b=1,
c=2,...,z=25). Then, we would find the first letter of the key. After that, we
would modify the boundaries of the array so that it is only left with the
starting index of low = n*(letter value - 0.5)/26 and the ending index of
n*(letter value + 1.5)/26 (if the first letter of the key is "a" or "z", we make
the starting index 0 and ending index n, respectively). Then, using the
modified array with a much shorter length, we can perform binary search in
firstIndexOf/lastIndexOf. This would reduce the iterations about three times.



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */

