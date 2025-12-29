Programming Assignment 1: Percolation

Answer these questions after you implement your solution.

/* *****************************************************************************
 *  Describe the data structures (i.e., instance variables) you used to
 *  implement the Percolation API.
 **************************************************************************** */
    I used several instance variables to implement the Percolation API. First, I
    created "grid" which is a boolean array that represnts the percolation grid.
    It consists of true and false elements where false means blocked and true
    means open. I also created "size" which is the length of the row (or column)
    of the grid. Furthermore, I created "top" and "bottom" where "top"
    represents the leader of the open sites connected to the open sites on the
    top row and "bottom" represents the leader of the open sites connected to
    the open sites on the bottom row. I had the index of these variables as n*n
    and n*n + 1 as they are out of the range for the percolation grid and so
    it has no impact on the indexes within the percolation grid. Moreover, I
    created "count" which counts and stores the value of the number of open
    sites. Lastly, to create "uf", I utilized WeightedQuickUnionUF which
    was pre-made from Princeton University's algs4.jar.

/* *****************************************************************************
 *  Briefly describe the algorithms you used to implement each method in
 *  the Percolation API.
 **************************************************************************** */
open(): It opens the site at index col and row. It throws an
IllegalArgumentException if the value of col and row is out of bounds within
the percolation grid. After opening the site, it looks at the sites that are
touching and puts them under the leader of the site at index col and row.
isOpen(): It returns whether the site at index col and row is open.
isFull(): It returns whether the site at index col and row is connected to
an open site in the top row.
numberOfOpenSites(): It returns "count", the value of the number of open sites
percolates(): It returns whether the system percolates.

/* *****************************************************************************
 *  First, implement Percolation using QuickFindUF.
 *  What is the largest value of n that PercolationStats can handle in
 *  less than one minute on your computer when performing T = 100 trials?
 *
 *  Fill in the table below to show the values of n that you used and the
 *  corresponding running times. Use at least 5 different values of n.
 **************************************************************************** */

 T = 100

 n          time (seconds)
 5          0.037
 20         0.056
 100        1.983
 150        8.072
 300        123.038
 200        24.246
 250        57.564
 255        63.189
 252        68.469

 approximate maximum value of n: 250

/* *****************************************************************************
 *  Describe the strategy you used for selecting the values of n.
 **************************************************************************** */
I increased the values of n in values of 50s. Then after finding the value in
50s closest to 60 seconds, I increased/decreased by 5. Then, I
increased/decreased by 1s to find the largest n under 1 minute.


/* *****************************************************************************
 *  Next, implement Percolation using WeightedQuickUnionUF.
 *  What is the largest value of n that PercolationStats can handle in
 *  less than one minute on your computer when performing T = 100 trials?
 *
 *  Fill in the table below to show the values of n that you used and the
 *  corresponding running times. Use at least 5 different values of n.
 **************************************************************************** */

 T = 100

 n          time (seconds)
100         0.134
200         0.378
1000        9.837
2000        78.1
1800        57.973

approximate maximum value of n: 1800



/* *****************************************************************************
 *  Known bugs / limitations.
 **************************************************************************** */
None




/* *****************************************************************************
 *  Describe any serious problems you encountered.
 **************************************************************************** */
None



/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
I enjoyed doing it!
