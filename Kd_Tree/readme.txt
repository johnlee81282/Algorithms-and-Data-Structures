Programming Assignment 4: K-d Trees

/* *****************************************************************************
 *  Describe the Node data type you used to implement the
 *  2d-tree data structure.
 **************************************************************************** */
We made a Node data type that stores the point of Point2D type, the value of
Value type, RectHV type to store the corresponding rectangle, and the children
nodes of left and right. The constructor initializes the point, value, rect to
the given value and left and right to null.

/* *****************************************************************************
 *  Describe your method for range search in a k-d tree.
 **************************************************************************** */
We created two queues, one for storing all the points inside the rectangle and
the other for storing the nodes that are yet to be searched. You start with
the root Node and go down the left and right children. If the rectangles
corresponding to the children nodes intersect with the searched rectangle,
you add the node to the node queue. Then, if the point is included in the
rectangle, you add the point to the queue. You just keep repeating this until
the node queue becomes empty.

/* *****************************************************************************
 *  Describe your method for nearest neighbor search in a k-d tree.
 **************************************************************************** */
We created a recursive function that returns the closest point in the subtree.
The input is a node (root of the subtree), the target point p, and the current
closest point closest. If the node is null, which is the base case, it returns
the closest. If the node is not null, it will look at the left subtree and right
subtree and determine which one to search for first by calculating the distances
to the corresponding rectangles. If the distance to the rectangle is bigger than
the distance between the closest and p, it will skip the search. After searching
both trees, it will return the new closest.

/* *****************************************************************************
 *  How many nearest-neighbor calculations can your PointST implementation
 *  perform per second for input1M.txt (1 million points), where the query
 *  points are random points in the unit square?
 *
 *  Fill in the table below, rounding each value to use one digit after
 *  the decimal point. Use at least 1 second of CPU time. Do not use -Xint.
 *  (Do not count the time to read the points or to build the 2d-tree.)
 *  (See the checklist for information on how to do this)
 *
 *  Repeat the same question but with your KdTreeST implementation.
 *
 **************************************************************************** */


                 # calls to         /   CPU time     =   # calls to nearest()
                 client nearest()       (seconds)        per second
                ------------------------------------------------------
PointST:        1000 / 57.012 = 17.5

KdTreeST:       1000000 / 3.552 = 281531.5

Note: more calls per second indicates better performance.

/* *****************************************************************************
 *  Suppose you wanted to add a method numberInRange(RectHV rect) to your
 *  KdTreeST, which should return the number of points that are inside rect
 *  (or on the boundary), i.e. the number of points in the iterable returned by
 *  calling range(rect).
 *
 *  Describe a pruning rule that would make this more efficient than the
 *  range() method. Also, briefly describe how you would implement it.
 *
 *  Hint: consider a range search. What can you do when the query rectangle
 *  completely contains the rectangle corresponding to a node?
 **************************************************************************** */
We would add an instance variable to the node that shows the size of the subtree
under that node. The pruning rule should be that if the rectangle of the node is
completely contained in the query rectangle, you stop searching the children
nodes and just add the size of the subtree to the answer since you know that
all the subnodes are contained in the query rectangle.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on  how helpful the class meeting was and on how much you learned
 * from doing the assignment, and whether you enjoyed doing it.
 **************************************************************************** */
We really enjoyed doing this assignment.
