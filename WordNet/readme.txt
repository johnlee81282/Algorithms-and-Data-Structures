Programming Assignment 5: WordNet


/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in synsets.txt. Why did you make this choice?
 **************************************************************************** */
We chose to use two RedBlackBSTs. One is to map indices to the nouns, and the
other is to map the nouns to indices. The one that maps the nouns to the indices
has to run in logarithmic time, so RedBlackBST or HashMap were the only options,
and we chose RedBlackBST in order to have a logarithmic lower bound. The other
does not have specific performance requirement other than taking linearlithmic
time to construct, so we could have chosen HashMap or ResizingArray, but we
chose RedBlackBST for the simplicity.

/* *****************************************************************************
 *  Describe concisely the data structure(s) you used to store the
 *  information in hypernyms.txt. Why did you make this choice?
 **************************************************************************** */
We first stored the hypernyms to the Diagram to construct the
ShortestCommonAncestor. Since we only needed to implement sca() and distance(),
ShortestCommonAncestor was enough, and it only uses O(E+V) space.

/* *****************************************************************************
 *  Describe concisely the algorithm you use in the constructor of
 *  ShortestCommonAncestor to check if the digraph is a rooted DAG.
 **************************************************************************** */
We checked if the digraph has cycles to see if it is DAG. A DAG is a rooted DAG
if and only if there is only one vertex without outward edges. Therefore, we
used a for loop and checked how many vertex does not have a single outward edge.
If there is only one vertex, then it is a rooted DAG, and if not, it is not.

/* *****************************************************************************
 *  The Digraph class you used in your implementation uses an adjacency lists
 *  representation to store a graph. Imagine that you changed this class to
 *  use an adjacency matrix instead. How would this affect the worst case
 *  running time of length() or ancestor()?
 *
 *  Express your answers as functions of the number of vertices V and
 *  the number of edges E in the digraph. Use Big Theta notation
 *  to simplify your answers.
 *
 *  Note that even though you might not use any Digraph methods in either
 *  length() or ancestor(), any method that performs a Breadth First Search
 *  would have to be modified to work with this new representation.
 **************************************************************************** */
If you use adjacency matrix, you have to visit all the indices from 0 to V - 1
and V(V-1) = O(V^2) in total to check if there are any edges overlooked while in
adjacency list, you only need to visit at most 2E edges for all the vertices.
Therefore, the running time of length() and ancestor() will be O(V^2) as
digraph.adj() will not be as efficient.

/* *****************************************************************************
 *  List any other comments here. Feel free to provide any feedback
 *  on how much you learned from doing the assignment, and whether
 *  you enjoyed doing it.
 **************************************************************************** */
We enjoyed doing this assignment.
