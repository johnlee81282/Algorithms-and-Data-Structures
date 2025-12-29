Programming Assignment 7: Fraud Detection

/* *****************************************************************************
 *  Describe how you implemented the Clustering constructor
 **************************************************************************** */
First, we made an EdgeWeightedGraph with m vertices and m(m-1)/2 edges. The
weight of edges is the distance between the two points. Using the graph, we
computed the minimum spanning tree using KruskalMST method. Then, we stored
all the edges of MST to Minimum Priority Queue and chose the m-k edges with the
lowest weights. With the chosen m-k edges, we created another graph, and using
CC, we stored the id of CC that corresponds to each vertex to an array.

/* *****************************************************************************
 *  Describe how you implemented the WeakLearner constructor
 **************************************************************************** */
First, we planned to obtain the prediction with minimum error on each axis.
On each axis, we stored the coordinates in 1D array of length n. Then, we sorted
the array, but in order to keep the correspondance with weights and labels,
we sorted the coordinates with coordinates[j] * n + j so that we know the
original indeces. Then, we calculated the minimum error when sign = 0. The
initial error is when the v is smaller than any coordinate, and we compared
other cases by moving up the coordinate and changing the size of errors by
weights[j] every time. Then, we did the same thing for sign = 1 and compared
which sign yields the better result. Then, we compared the local optimal error
with the global optimal error to update the global optimal error if necessary.
We also kept track of d, v, and s every time we update the global minimum error.

/* *****************************************************************************
 *  Consider the large_training.txt and large_test.txt datasets.
 *  Run the boosting algorithm with different values of k and T (iterations),
 *  and calculate the test data set accuracy and plot them below.
 *
 *  (Note: if you implemented the constructor of WeakLearner in O(kn^2) time
 *  you should use the small_training.txt and small_test.txt datasets instead,
 *  otherwise this will take too long)
 **************************************************************************** */

      k          T         test accuracy       time (seconds)
   --------------------------------------------------------------------------
      5          80         0.86875               0.202
      7          80         0.9325                0.251
      10         80         0.9675                0.292
      15         80         0.9675                0.358
      20         80         0.96375               0.412
      13         80         0.96625               0.315
      17         80         0.96875               0.383
      17         40         0.95125               0.241
      17         20         0.93125               0.171
      17         160        0.96875               0.616
      17         200        0.96875               0.752
      17         10         0.92125               0.109
      17         4          0.8125                0.076
      20         200        0.97                  0.843
/* *****************************************************************************
 *  Find the values of k and T that maximize the test data set accuracy,
 *  while running under 10 second. Write them down (as well as the accuracy)
 *  and explain:
 *   1. Your strategy to find the optimal k, T.
 *   2. Why a small value of T leads to low test accuracy.
 *   3. Why a k that is too small or too big leads to low test accuracy.
 **************************************************************************** */
k = 19, T = 2800, test data accuracy = 0.97375, time = 9.619
1. From the experiment that we did, when you keep increasing T, there is a point
where the training accuracy and test accuracy does not improve anymore. From
the previous question, we saw that k = 10 ~ 20 has an accuracy of more than 0.95
if T is big enough. Therefore, we tried to find the maximum value by testing
those k. The next maximum accuracy was k = 16, T = 3000, accuracy = 0.9725.
2. Small value of T means you are classifying n-dimentional things with T-rank
approximation. Although it could be successful with some data, most data cannot
be simplified to a few dimensions without sacrificing some information.
Therefore, small T leads to low test accuracy as it does not consider a good
amount of information.
3. When k is too small, that means an exceptional value that is likely to be
a key to identify fraud will be overlooked because those values will be reduced
to the average value and the difference becomes a lot smaller. On the other
hand, if k is too big, the WeakLearning Algorithm becomes too sensitive to all
the information. For example, it will identify a transaction as fraud when the
reality is you just bought your gracery at one place instead of going to the
adjacent stores.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
We really enjoyed this assignment.
